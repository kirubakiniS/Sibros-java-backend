package com.sibros.api;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class VehicleSibrosSeviceImpl implements VehicleSibrosSevice {

    @Value("${sibros.api.key}")
    private String XmasterApiKey;
    @Value("${sibros.api.secret}")
    private String XmasterApiSecret;
    public String  getDeviceDetailsData() throws IOException, InterruptedException {
        String reasonBody = getDevice();
        return reasonBody;
    }

    public String  getDeviceModelDetailsData() throws IOException, InterruptedException {

        String reasonBody = getDeviceModel();
        return reasonBody;
    }
    public String getDevice() throws IOException, InterruptedException {

        Map<String, String> headers = new HashMap<>();
        headers.put("X-Master-Api-Key", XmasterApiKey); // Replace YOUR_ACCESS_TOKEN with your actual access token
        headers.put("X-Master-Api-Secret", XmasterApiSecret);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.prod-p-ap.sibros.tech/core/v2/devices"))
                .headers(headers.entrySet().stream()
                        .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue()))
                        .flatMap(e -> Stream.of(e.getKey(), e.getValue())) // FlatMap to ensure alternating key-value pairs
                        .toArray(String[]::new))
                .GET()

                .build();

        // Create HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Send the request and retrieve response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }


    public String getDeviceModel() throws IOException, InterruptedException {

        String reasonBody = getDevice();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(reasonBody);
        // Extract deviceModelID
        String deviceModelID = jsonNode.get("results").get(0).get("deviceModelID").asText();

        Map<String, String> headers = new HashMap<>();
        headers.put("X-Master-Api-Key", XmasterApiKey);
        headers.put("X-Master-Api-Secret", XmasterApiSecret);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.prod-p-ap.sibros.tech/core/v2/device-models/"+deviceModelID+"/controllers"))
                .headers(headers.entrySet().stream()
                        .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue()))
                        .flatMap(e -> Stream.of(e.getKey(), e.getValue())) // FlatMap to ensure alternating key-value pairs
                        .toArray(String[]::new))
                .GET()

                .build();

        // Create HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Send the request and retrieve response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();

    }

    public String addCommandRequest(String value) throws IOException, InterruptedException {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Master-Api-Key", XmasterApiKey);
        headers.put("X-Master-Api-Secret", XmasterApiSecret);

        // Prepare request body
        String requestBody = "{\n" +
                "  \"commandId\": 200,\n" +
                "  \"deviceId\": \"2817f819-2fef-494a-ac3a-8adf1ccd72ec\",\n" +
                "  \"expiresBy\": \"2024-04-26T10:30:51.827053Z\",\n" +
                "  \"payload\": {\n" +
                "    \"ecuAddress\": \"" + value + "\",\n" +
                "    \"hexString\": \"19027F\"\n" +
                "  },\n" +
                "  \"responseTimeout\": null\n" +
                "}"; // Replace this with your actual request body

        // Build HTTP request with headers and body
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.prod-p-ap.sibros.tech/core/v2/command-requests"))
                .headers(headers.entrySet().stream()
                        .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue()))
                        .flatMap(e -> Stream.of(e.getKey(), e.getValue())) // FlatMap to ensure alternating key-value pairs
                        .toArray(String[]::new))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody)) // Add the request body here
                .build();

        // Create HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Send the request and retrieve response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Process the response
        return response.body();
    }

//    public String getDeviceReadDTCData() throws IOException, InterruptedException {
//        List<VehicleSibrosDtcData> dtcDataList = new ArrayList<>();
//        String responseData = "";
//        Map<String, String> keyValueMap = new HashMap<>();
//        keyValueMap.put("VDC","0601");
//        keyValueMap.put("ABS","07E0");
//        keyValueMap.put("BMS","0693");
//        keyValueMap.put("FBCM","07C1");
//        keyValueMap.put("DSC","07C2");
//        keyValueMap.put("CCM","07C3");
//        keyValueMap.put("RBCM","07C4");
//        keyValueMap.put("HCM","07C5");
//        keyValueMap.put("PCM","07C6");
//        keyValueMap.put("THC","07C7");
//
//        for (Map.Entry<String, String> entry : keyValueMap.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//
//            responseData = addCommandRequest(value);
//
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode jsonNode = objectMapper.readTree(responseData);
//            String commandRequestId = jsonNode.get("results").get("commandRequestId").asText();
//            TimeUnit.SECONDS.sleep(2);
//            // Make the second API call to get command responses
//            String urlData = "https://api.prod-p-ap.sibros.tech/core/v2/command-requests/" + commandRequestId + "/command-responses";
//            Map<String, String> headers = new HashMap<>();
//            headers.put("X-Master-Api-Key", XmasterApiKey);
//            headers.put("X-Master-Api-Secret", XmasterApiSecret);
//
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create(urlData))
//                    .headers(headers.entrySet().stream()
//                            .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue()))
//                            .flatMap(e -> Stream.of(e.getKey(), e.getValue()))
//                            .toArray(String[]::new))
//                    .GET()
//                    .build();
//
//            // Send the request and retrieve response
//            HttpClient client = HttpClient.newHttpClient();
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            if (response.body() == null || response.body().equals("{\"results\":null}")) {
//               System.out.println("Not Null");
//            }else{
//                ObjectMapper objectMapperapi = new ObjectMapper();
//                JsonNode jsonNodeapi = objectMapperapi.readTree(response.body());
//                String statusDetails = jsonNodeapi.get("results").get(0).get("status").asText();
//
//
//                if (statusDetails.equals("COMPLETED")) {
//
//
//                    ObjectMapper mapper = new ObjectMapper();
//                    JsonNode jsonNodepayload = mapper.readTree(response.body());
//
//                    // Assuming there's only one result in the results array
//                    JsonNode decodedPayloadNode = jsonNodepayload.get("results").get(0).get("payload").get("decodedPayload");
//
//                    String jsonArray = decodedPayloadNode.toString();
//                    ObjectMapper mappers = new ObjectMapper();
//                    JsonNode jsonArrayNode = mappers.readTree(jsonArray);
//
//                    for (JsonNode node : jsonArrayNode) {
//
//                        if (node.has("DTC")) {
//                            String dtcValue = node.get("DTC").toString();
//                            VehicleSibrosDtcData dtcData = new VehicleSibrosDtcData(key, dtcValue, null, "Active fault");
//                            dtcDataList.add(dtcData);
//                        }
//                    }
//
//                } else {
//
//                    VehicleSibrosDtcData dtcData = new VehicleSibrosDtcData(key, null, null,"No DTC found");
//                    dtcDataList.add(dtcData);
//
//                }
//            }
//
//
//        }
//        VehicleSibrosDtcResponse response = new VehicleSibrosDtcResponse(200, dtcDataList, "ECU Diagnostic Trouble Codes Retrieved Successfully");
//        String jsonResponse = new ObjectMapper().writeValueAsString(response);
//                return jsonResponse;
//    }



    public String getDeviceReadDTCData() throws IOException, InterruptedException {
        Map<String, List<VehicleSibrosDtcData>> dtcDataMap = new HashMap<>();
        String responseData = "";
        Map<String, String> keyValueMap = new HashMap<>();
        keyValueMap.put("VDC","0601");
        keyValueMap.put("ABS","07E0");
        keyValueMap.put("BMS","0693");
        keyValueMap.put("FBCM","07C1");
        keyValueMap.put("DSC","07C2");
        keyValueMap.put("CCM","07C3");
        keyValueMap.put("RBCM","07C4");
        keyValueMap.put("HCM","07C5");
        keyValueMap.put("PCM","07C6");
        keyValueMap.put("THC","07C7");

        for (Map.Entry<String, String> entry : keyValueMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            responseData = addCommandRequest(value);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseData);
            String commandRequestId = jsonNode.get("results").get("commandRequestId").asText();
            TimeUnit.SECONDS.sleep(2);

            // Make the second API call to get command responses
            String urlData = "https://api.prod-p-ap.sibros.tech/core/v2/command-requests/" + commandRequestId + "/command-responses";
            Map<String, String> headers = new HashMap<>();
            headers.put("X-Master-Api-Key", XmasterApiKey);
            headers.put("X-Master-Api-Secret", XmasterApiSecret);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlData))
                    .headers(headers.entrySet().stream()
                            .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue()))
                            .flatMap(e -> Stream.of(e.getKey(), e.getValue()))
                            .toArray(String[]::new))
                    .GET()
                    .build();

            // Send the request and retrieve response
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.body() == null || response.body().equals("{\"results\":null}")) {
                System.out.println("Not Null");
            } else {
                ObjectMapper objectMapperapi = new ObjectMapper();
                JsonNode jsonNodeapi = objectMapperapi.readTree(response.body());
                String statusDetails = jsonNodeapi.get("results").get(0).get("status").asText();

                List<VehicleSibrosDtcData> dtcDataList = new ArrayList<>();

                if (statusDetails.equals("COMPLETED")) {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode jsonNodepayload = mapper.readTree(response.body());

                    // Assuming there's only one result in the results array
                    JsonNode decodedPayloadNode = jsonNodepayload.get("results").get(0).get("payload").get("decodedPayload");

                    String jsonArray = decodedPayloadNode.toString();
                    ObjectMapper mappers = new ObjectMapper();
                    JsonNode jsonArrayNode = mappers.readTree(jsonArray);

                    for (JsonNode node : jsonArrayNode) {
                        if (node.has("DTC")) {
                            String dtcValue = node.get("DTC").toString();
                            VehicleSibrosDtcData dtcData = new VehicleSibrosDtcData(key, dtcValue, null, "Active fault");
                            dtcDataList.add(dtcData);
                        }
                    }
                } else{
                    VehicleSibrosDtcData dtcData = new VehicleSibrosDtcData(key, null, null, "ECU Not Detected");
                    dtcDataList.add(dtcData);
                }

                dtcDataMap.put(key, dtcDataList);
            }
        }

        // Convert dtcDataMap to the desired JSON response format
        Map<String, Object> jsonResponseMapResponse = new HashMap<>();
        Map<String, Object> jsonResponseMap = new HashMap<>();
        dtcDataMap.forEach((ecuName, dtcList) -> {
            List<Map<String, Object>> dataList = new ArrayList<>();
            dtcList.forEach(dtcData -> {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("ecuName", dtcData.getEcuName());
                dataMap.put("diagnosticTroubleCode", dtcData.getDiagnosticTroubleCode());
                if(dtcData.getDiagnosticTroubleCode()!= null){
                    dataMap.put("description", "Description Not Found");
                }else{
                    dataMap.put("description", dtcData.getDescription());
                }

                dataMap.put("dtcState", dtcData.getDtcState());
                dataList.add(dataMap);
            });
            jsonResponseMap.put(ecuName, dataList);
        });
        jsonResponseMapResponse.put("data",jsonResponseMap);
        jsonResponseMapResponse.put("message", "ECU Diagnostic Trouble Codes Retrieved Successfully");
        jsonResponseMapResponse.put("status", 200);

        return new ObjectMapper().writeValueAsString(jsonResponseMapResponse);
    }

    public String getDevicePackageData() throws IOException, InterruptedException {

        Map<String, String> headers = new HashMap<>();
        headers.put("X-Master-Api-Key", XmasterApiKey); // Replace YOUR_ACCESS_TOKEN with your actual access token
        headers.put("X-Master-Api-Secret", XmasterApiSecret);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.prod-p-ap.sibros.tech/core/v2/packages"))
                .headers(headers.entrySet().stream()
                        .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue()))
                        .flatMap(e -> Stream.of(e.getKey(), e.getValue())) // FlatMap to ensure alternating key-value pairs
                        .toArray(String[]::new))
                .GET()

                .build();

        // Create HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Send the request and retrieve response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String addNewRolloutData(String packageID) throws IOException, InterruptedException {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Master-Api-Key", XmasterApiKey);
        headers.put("X-Master-Api-Secret", XmasterApiSecret);
        System.out.println("packageID "+ packageID );

        LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));

        // Define the format for the date-time string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");

        // Format the current date-time using the defined formatter
        String formattedDateTime = currentDateTime.format(formatter);

        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        // Format the current date-time using the defined formatter
        String formattedDateTimes = currentDateTime.format(formatters);

        String rolloutName = "Natesan Ramanathan" + formattedDateTime;
        // Prepare request body
        String requestBody = "{\n" +
                "  \"approvers\": null,\n" +
                "  \"companyID\": \"468df435-c8da-4013-bc62-14f5e69b6f70\",\n" +
                "  \"desiredState\": null,\n" +
                "  \"deviceGroupIDs\": null,\n" +
                "  \"deviceModelID\": \"7cfc32bf-b09c-4454-a2d3-a6d2c7c309e8\",\n" +
                "  \"deviceIDs\": [\n" +
                "                \"2817f819-2fef-494a-ac3a-8adf1ccd72ec\"\n" +
                "            ],\n" +
                "  \"ownerID\": \"7ce452c7-0627-4385-a986-c6a89f4ddb34\",\n" +
                "  \"packageID\": \"" + packageID + "\",\n" +
                "  \"rolloutName\": \""+ rolloutName+"\",\n" +
                "  \"rolloutType\": \"SOFTWARE_UPDATE\",\n" +
                "  \"scheduledStartTime\": \"" + formattedDateTimes+"\",\n" +
                "  \"selectiveLogConfigID\": null,\n" +
                "  \"stages\": [\n" +
                "    {\n" +
                "      \"stageNumber\": 1,\n" +
                "      \"startingStrategy\": \"ALL_OR_NOTHING\",\n" +
                "      \"failureThreshold\": 10,\n" +
                "      \"percentOfRolloutTarget\": 100,\n" +
                "      \"movingWindowPercentage\": 100,\n" +
                "      \"completeThreshold\": 100\n" +
                "    }\n" +
                "  ],\n" +
                "  \"stageTargetingType\": \"PERCENT_OVERALL\",\n" +
                "  \"startTime\":\""+formattedDateTimes+"\"\n" +
                "}"; // Replace this with your actual request body

        // Build HTTP request with headers and body
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.prod-p-ap.sibros.tech/core/v2/rollouts"))
                .headers(headers.entrySet().stream()
                        .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue()))
                        .flatMap(e -> Stream.of(e.getKey(), e.getValue())) // FlatMap to ensure alternating key-value pairs
                        .toArray(String[]::new))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody)) // Add the request body here
                .build();

        // Create HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Send the request and retrieve response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Process the response
        
        String responseDetails = response.body();
        
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseDetails);
        // Extract deviceModelID
        String deviceModelID = jsonNode.get("rolloutID").asText();
        
        
        
        return deviceModelID;
        
        
        
        
    }
    public String startRollout(String packageID) throws IOException, InterruptedException {
    	
    	
    	
    	String rolloutID = addNewRolloutData(packageID);

        Map<String, String> headers = new HashMap<>();
        headers.put("X-Master-Api-Key", XmasterApiKey); // Replace YOUR_ACCESS_TOKEN with your actual access token
        headers.put("X-Master-Api-Secret", XmasterApiSecret);
        String requestBody = "{\"desiredState\": \"COMPLETED\"}";
        System.out.println("rolloutID"+rolloutID);
        System.out.println("requestBody"+requestBody);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.prod-p-ap.sibros.tech/core/v2/rollouts/"+rolloutID+"/desiredstate"))
                .headers(headers.entrySet().stream()
                        .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue()))
                        .flatMap(e -> Stream.of(e.getKey(), e.getValue())) // FlatMap to ensure alternating key-value pairs
                        .toArray(String[]::new))
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))

                .build();

        // Create HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Send the request and retrieve response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String getRolloutStatusData(String rolloutID) throws IOException, InterruptedException {

        Map<String, String> headers = new HashMap<>();
        headers.put("X-Master-Api-Key", XmasterApiKey); // Replace YOUR_ACCESS_TOKEN with your actual access token
        headers.put("X-Master-Api-Secret", XmasterApiSecret);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.prod-p-ap.sibros.tech/core/v2/rollouts/"+rolloutID+"/progress"))
                .headers(headers.entrySet().stream()
                        .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue()))
                        .flatMap(e -> Stream.of(e.getKey(), e.getValue())) // FlatMap to ensure alternating key-value pairs
                        .toArray(String[]::new))
                .GET()

                .build();

        // Create HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Send the request and retrieve response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

//    public String getPackageECUDetails(String rolloutID) throws IOException, InterruptedException {
//
//        System.out.println(rolloutID);
//        String reasonBody = getDeviceModel();
//        return reasonBody;
//
//    }
    
    
    public class VehicleSibrosDtcData {
        private String ecuName;
        private String diagnosticTroubleCode;
        private String description;
        private String dtcState;

        // Constructor, getters, and setters

        // Constructor
        public VehicleSibrosDtcData(String ecuName, String diagnosticTroubleCode, String description, String dtcState) {
            this.ecuName = ecuName;
            this.diagnosticTroubleCode = diagnosticTroubleCode;
            this.description = description;
            this.dtcState = dtcState;
        }

        // Getters and Setters
        public String getEcuName() {
            return ecuName;
        }

        public void setEcuName(String ecuName) {
            this.ecuName = ecuName;
        }

        public String getDiagnosticTroubleCode() {
            return diagnosticTroubleCode;
        }

        public void setDiagnosticTroubleCode(String diagnosticTroubleCode) {
            this.diagnosticTroubleCode = diagnosticTroubleCode;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDtcState() {
            return dtcState;
        }

        public void setDtcState(String dtcState) {
            this.dtcState = dtcState;
        }
    }
    
    
    @GetMapping("packageManifestEntries")
    public String packageManifestEntries(String packageIdDetails,String imageRegionIds) throws IOException, InterruptedException {

        Map<String, String> headers = new HashMap<>();
        headers.put("X-Master-Api-Key", XmasterApiKey); // Replace YOUR_ACCESS_TOKEN with your actual access token
        headers.put("X-Master-Api-Secret", XmasterApiSecret);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.prod-p-ap.sibros.tech/core/v2/packages/"+packageIdDetails+"/package-manifest-entries"))
                .headers(headers.entrySet().stream()
                        .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue()))
                        .flatMap(e -> Stream.of(e.getKey(), e.getValue())) // FlatMap to ensure alternating key-value pairs
                        .toArray(String[]::new))
                .GET()

                .build();

        // Create HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Send the request and retrieve response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        
        
        ObjectMapper objectMapper = new ObjectMapper();
        
        JsonNode jsonArray = objectMapper.readTree(response.body());

        String desiredImageRegionId = imageRegionIds; // Change this to your desired imageRegionId
        String objDetails = "";
        for (JsonNode obj : jsonArray) {
            String imageRegionId = obj.get("imageRegionId").asText();
            if (imageRegionId.equals(desiredImageRegionId)) {
                // Found the desired object
                objDetails = obj.toString();
                break; // Stop iterating once found
            }
        }
        
        ObjectMapper objectMappers = new ObjectMapper();
        JsonNode jsonObject = objectMappers.readTree(objDetails);

        // Get the image ID from the JsonNode object
        String imageId = jsonObject.get("imageId").asText();

        // Print the image ID
        System.out.println("Image ID: " + imageId);
        
        
        
        return imageId;
    }
    
    
    
    
    // GET ECU Details
    
    @GetMapping("getPackageImageDetails")
    public String  getPackageImageDetails(@RequestParam String packageID) throws IOException, InterruptedException {

        String reasonBody = getDeviceModel();
        
        List<Component> componentLists = new ArrayList<>();
        
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(reasonBody);

        // Code to extract "id" entities from the JSON response
        JsonNode controllersArray = jsonNode.get("controllers");
        int count = 0;
        if (controllersArray != null && controllersArray.isArray()) {
            for (JsonNode controller : controllersArray) {
                String id = controller.get("id").asText();
                String componentId = controller.get("componentID").asText();
                String abbreviation = controller.get("abbreviation").asText();
              
                
               String revisionid =  getHardwareRevisions(id);
              
               String imageRegionIdId = getImageIdDetails(revisionid);
               
               
           
               
               
               String imageRegionCurrentVersion = getCurrentVersion(count);
               count++;
               
               String imageIdPackageManifest = packageManifestEntries(packageID,imageRegionIdId);
               
               String imageObject = getImageIdDetailsObject(imageIdPackageManifest);
               
            
               
               ObjectMapper objectMapperMap = new ObjectMapper();
               JsonNode jsonNodeMap = objectMapperMap.readTree(imageObject);
               
               String imageId = jsonNodeMap.get("imageId").asText();
               String fileName = jsonNodeMap.get("file").get("fileName").asText();
               String fileSizeBytes = jsonNodeMap.get("file").get("fileSizeBytes").asText();
               String version = jsonNodeMap.get("version").asText();
               
             
               
               Component component = new Component(componentId, abbreviation, version, imageId, fileName,fileSizeBytes, imageRegionCurrentVersion);
               componentLists.add(component);
               
              
               
               
               
        }
        } 
        
        ObjectMapper objectMapperDetails = new ObjectMapper();
        String json = objectMapperDetails.writeValueAsString(componentLists);
        
        return json;
    }
    
    
    
    
    public String getHardwareRevisions(String id) throws IOException, InterruptedException {
    	String hardwareRevisionsid = "";
    	System.out.println(" revisionid passsed");
    	Map<String, String> headers = new HashMap<>();
        headers.put("X-Master-Api-Key", XmasterApiKey); // Replace YOUR_ACCESS_TOKEN with your actual access token
        headers.put("X-Master-Api-Secret", XmasterApiSecret);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.prod-p-ap.sibros.tech/core/v2/controllers/"+id+"/hardware-revisions"))
                .headers(headers.entrySet().stream()
                        .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue()))
                        .flatMap(e -> Stream.of(e.getKey(), e.getValue())) // FlatMap to ensure alternating key-value pairs
                        .toArray(String[]::new))
                .GET()

                .build();

        // Create HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Send the request and retrieve response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseDetails =  response.body();
        
        ObjectMapper objectMapperRevision = new ObjectMapper();
        JsonNode jsonNodeRevision = objectMapperRevision.readTree(responseDetails);

        // Code to extract "id" entities from the JSON response
        JsonNode hardwareRevisionsArray = jsonNodeRevision.get("hardwareRevisions");
        if (hardwareRevisionsArray != null && hardwareRevisionsArray.isArray()) {
            for (JsonNode hardwareRevisions : hardwareRevisionsArray) {
                hardwareRevisionsid = hardwareRevisions.get("id").asText();
                //System.out.println("hardwareRevisionsID: " + hardwareRevisionsid);
                
                }
            }
        
        return hardwareRevisionsid;
        
}
    
    
    public String getImageIdDetails(String id) throws IOException, InterruptedException {
    	String hardwareRevisionsid = "";
    	
    	Map<String, String> headers = new HashMap<>();
        headers.put("X-Master-Api-Key", XmasterApiKey); // Replace YOUR_ACCESS_TOKEN with your actual access token
        headers.put("X-Master-Api-Secret", XmasterApiSecret);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.prod-p-ap.sibros.tech/core/v2/hardware-revisions/"+id+"/image-regions"))
                .headers(headers.entrySet().stream()
                        .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue()))
                        .flatMap(e -> Stream.of(e.getKey(), e.getValue())) // FlatMap to ensure alternating key-value pairs
                        .toArray(String[]::new))
                .GET()

                .build();

        // Create HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Send the request and retrieve response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseDetails =  response.body();
        
        ObjectMapper objectMapperRevision = new ObjectMapper();
        JsonNode jsonNodeRevision = objectMapperRevision.readTree(responseDetails);

        // Code to extract "id" entities from the JSON response
        JsonNode hardwareRevisionsArray = jsonNodeRevision.get("imageRegions");
        if (hardwareRevisionsArray != null && hardwareRevisionsArray.isArray()) {
            for (JsonNode hardwareRevisions : hardwareRevisionsArray) {
                hardwareRevisionsid = hardwareRevisions.get("id").asText();
                //System.out.println("hardwareRevisionsID: " + hardwareRevisionsid);
                }
            }
        
        return hardwareRevisionsid;
        
}
    
    
    public String getImageIdDetailsObject(String id) throws IOException, InterruptedException {
    	
    	Map<String, String> headers = new HashMap<>();
        headers.put("X-Master-Api-Key", XmasterApiKey); // Replace YOUR_ACCESS_TOKEN with your actual access token
        headers.put("X-Master-Api-Secret", XmasterApiSecret);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.prod-p-ap.sibros.tech/core/v2/images/"+id+""))
                .headers(headers.entrySet().stream()
                        .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue()))
                        .flatMap(e -> Stream.of(e.getKey(), e.getValue())) // FlatMap to ensure alternating key-value pairs
                        .toArray(String[]::new))
                .GET()

                .build();

        // Create HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Send the request and retrieve response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseDetails =  response.body();
        
       
        
        return responseDetails;
        
}
    //@GetMapping("getImageRegionCurrentVersion")
    public String getImageRegionCurrentVersion(String id) throws IOException, InterruptedException {
    	//id = "19ca9e67-0f30-40b0-8d5b-728d083a8a2c";
    	String currentVersionDetails = "";
    	
    	Map<String, String> headers = new HashMap<>();
        headers.put("X-Master-Api-Key", XmasterApiKey); // Replace YOUR_ACCESS_TOKEN with your actual access token
        headers.put("X-Master-Api-Secret", XmasterApiSecret);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.prod-p-ap.sibros.tech/core/v2/image-regions/"+id+"/images"))
                .headers(headers.entrySet().stream()
                        .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue()))
                        .flatMap(e -> Stream.of(e.getKey(), e.getValue())) // FlatMap to ensure alternating key-value pairs
                        .toArray(String[]::new))
                .GET()

                .build();

        // Create HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Send the request and retrieve response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseDetails =  response.body();
        
        ObjectMapper objectMapperRevision = new ObjectMapper();
        JsonNode jsonNodeRevision = objectMapperRevision.readTree(responseDetails);

        // Code to extract "id" entities from the JSON response
        JsonNode hardwareRevisionsArray = jsonNodeRevision.get("images");
     
        
        JsonNode lastObject = hardwareRevisionsArray.get(hardwareRevisionsArray.size() - 1);

        // Get the version value of the last object
        currentVersionDetails = lastObject.get("version").asText();
        
      System.out.println("currentVersionDetails " + currentVersionDetails);
        
        
        
        
        return currentVersionDetails;
        
}
    
  //  @GetMapping("getPackageDeployedId")
    public String getCurrentVersion(int count) throws IOException, InterruptedException {
    	String version = "";
    	String getPackageDeployedId = getPackageDeployedId();
    	
    	if(!getPackageDeployedId.equalsIgnoreCase("")) {
    	String reasonBody = getDeviceModel();
    	
    	 ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(reasonBody);
            
            JsonNode controllersArray = jsonNode.get("controllers");
            JsonNode controller = controllersArray.get(count);
            //if (controllersArray != null && controllersArray.isArray()) {
               // for (JsonNode controller : controllersArray) {
                    String id = controller.get("id").asText();
                   
                   
                    
                   String revisionid =  getHardwareRevisions(id);
                  
                   String imageRegionIdId = getImageIdDetails(revisionid);
                   
                   
                   
                 //  String imageRegionCurrentVersion = getImageRegionCurrentVersion(imageRegionIdId);
                   
                   
                   //String imageRegionCurrentVersion = getImageRegionCurrentVersion(imageRegionIdId);
                   
                   
                   String imageIdPackageManifest = packageManifestEntries(getPackageDeployedId,imageRegionIdId);
                  
                   String imageObject = getImageIdDetailsObject(imageIdPackageManifest);
                   
                 
                  // componentLists = new ArrayList<>();
                   
                   ObjectMapper objectMapperMap = new ObjectMapper();
                   JsonNode jsonNodeMap = objectMapperMap.readTree(imageObject);
                   
                   
                    version = jsonNodeMap.get("version").asText();
                   
                  
                   
                   
                   
           // }
          //  } 
    	}
        
        return version;
        
}
    
    
    
   
    public String getPackageDeployedId() throws IOException, InterruptedException {
    	
    	String packageDeployedId = "";
    	
    	Map<String, String> headers = new HashMap<>();
        headers.put("X-Master-Api-Key", XmasterApiKey); // Replace YOUR_ACCESS_TOKEN with your actual access token
        headers.put("X-Master-Api-Secret", XmasterApiSecret);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.prod-p-ap.sibros.tech/core/v2/packages"))
                .headers(headers.entrySet().stream()
                        .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue()))
                        .flatMap(e -> Stream.of(e.getKey(), e.getValue())) // FlatMap to ensure alternating key-value pairs
                        .toArray(String[]::new))
                .GET()

                .build();

        // Create HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Send the request and retrieve response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseDetails =  response.body();
        
//        ObjectMapper objectMapperRevision = new ObjectMapper();
//        JsonNode jsonNodeRevision = objectMapperRevision.readTree(responseDetails);
//
//        // Code to extract "id" entities from the JSON response
//        JsonNode hardwareRevisionsArray = jsonNodeRevision.get("images");
//     
//        
//        JsonNode lastObject = hardwareRevisionsArray.get(hardwareRevisionsArray.size() - 1);
//
//        // Get the version value of the last object
//        currentVersionDetails = lastObject.get("version").asText();
//        
//      System.out.println("currentVersionDetails " + currentVersionDetails);
        
        ObjectMapper objectMapperRevision = new ObjectMapper();
        JsonNode jsonNodeRevision = objectMapperRevision.readTree(responseDetails);

        // Code to extract "id" entities from the JSON response
        JsonNode hardwareRevisionsArray = jsonNodeRevision.get("results");
        if (hardwareRevisionsArray != null && hardwareRevisionsArray.isArray()) {
            for (JsonNode hardwareRevisions : hardwareRevisionsArray) {
            	String packageDeployedStatus = hardwareRevisions.get("packageStatus").asText();
                //System.out.println("hardwareRevisionsID: " + hardwareRevisionsid);
            	if(packageDeployedStatus.equals("DEPLOYED")) {
            		
            		packageDeployedId = hardwareRevisions.get("packageID").asText();
            		break;
            	}else {
            		packageDeployedId = "";
            		
            	}
            	
                }
            }
        
        
        return packageDeployedId;
        
}
    
// public String getPackageDeploymentStatus(String rolloutID) throws IOException, InterruptedException {
//    	
//    	Map<String, String> headers = new HashMap<>();
//        headers.put("X-Master-Api-Key", XmasterApiKey); // Replace YOUR_ACCESS_TOKEN with your actual access token
//        headers.put("X-Master-Api-Secret", XmasterApiSecret);
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("https://api.prod-p-ap.sibros.tech/core/v2/rollouts/"+rolloutID+"/deployments"))
//                .headers(headers.entrySet().stream()
//                        .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue()))
//                        .flatMap(e -> Stream.of(e.getKey(), e.getValue())) // FlatMap to ensure alternating key-value pairs
//                        .toArray(String[]::new))
//                .GET()
//
//                .build();
//
//        // Create HttpClient
//        HttpClient client = HttpClient.newHttpClient();
//
//        // Send the request and retrieve response
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        String responseDetails =  response.body();
//        
//        
//        
//        
//       
//        
//        return responseDetails;
//        
//}
 
 public String getPackageDeploymentStatus(String packageID) throws IOException, InterruptedException {
	 
	 String rolloutID = getPackageRolloutIdLog(packageID);
 	
 	Map<String, String> headers = new HashMap<>();
     headers.put("X-Master-Api-Key", XmasterApiKey); // Replace YOUR_ACCESS_TOKEN with your actual access token
     headers.put("X-Master-Api-Secret", XmasterApiSecret);
     HttpRequest request = HttpRequest.newBuilder()
             .uri(URI.create("https://api.prod-p-ap.sibros.tech/core/v2/rollouts/"+rolloutID+"/deployments"))
             .headers(headers.entrySet().stream()
                     .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue()))
                     .flatMap(e -> Stream.of(e.getKey(), e.getValue())) // FlatMap to ensure alternating key-value pairs
                     .toArray(String[]::new))
             .GET()

             .build();

     // Create HttpClient
     HttpClient client = HttpClient.newHttpClient();

     // Send the request and retrieve response
     HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
     String responseDetails =  response.body();
     
     List<Deployment> componentLists = new ArrayList<>();
     
     ObjectMapper objectMapper = new ObjectMapper();
     JsonNode jsonNode = objectMapper.readTree(responseDetails);
     
     JsonNode controllersArray = jsonNode.get("results");
   
     if (controllersArray != null && controllersArray.isArray()) {
         for (JsonNode controller : controllersArray) {
             String deploymentID = controller.get("deploymentID").asText();
             String deviceID = controller.get("deviceID").asText();
             String packageIDs = controller.get("packageID").asText();
             String deploymentType = controller.get("deploymentType").asText();
             String startTime = controller.get("startTime").asText();
             String endTime = controller.get("endTime").asText();
             String deploymentStatus = controller.get("deploymentStatus").asText();
             String rolloutIDs = controller.get("rolloutID").asText();
           
             
            String deviceDetais  = getDeviceID(deviceID);
            
   
         
         
            
            ObjectMapper objectMapperMap = new ObjectMapper();
            JsonNode jsonNodeMap = objectMapperMap.readTree(deviceDetais);
            
            String vinNumber = jsonNodeMap.get("deviceSerialNumber").asText();
           // String fileName = jsonNodeMap.get("file").get("fileName").asText();
           // String fileSizeBytes = jsonNodeMap.get("file").get("fileSizeBytes").asText();
           // String version = jsonNodeMap.get("version").asText();
            
          
            
            Deployment component = new Deployment(deploymentID, deviceID, packageIDs, deploymentType, startTime,endTime, vinNumber,deploymentStatus,rolloutIDs);
            componentLists.add(component);
            
           
            
            
            
     }
     } 
     
     ObjectMapper objectMapperDetails = new ObjectMapper();
     String json = objectMapperDetails.writeValueAsString(componentLists);
     
     return json;
     
}
 
 
 
 public String getDeviceID(String deviceID) throws IOException, InterruptedException {

     Map<String, String> headers = new HashMap<>();
     headers.put("X-Master-Api-Key", XmasterApiKey); // Replace YOUR_ACCESS_TOKEN with your actual access token
     headers.put("X-Master-Api-Secret", XmasterApiSecret);
     HttpRequest request = HttpRequest.newBuilder()
             .uri(URI.create("https://api.prod-p-ap.sibros.tech/core/v2/devices/"+deviceID+""))
             .headers(headers.entrySet().stream()
                     .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue()))
                     .flatMap(e -> Stream.of(e.getKey(), e.getValue())) // FlatMap to ensure alternating key-value pairs
                     .toArray(String[]::new))
             .GET()

             .build();

     // Create HttpClient
     HttpClient client = HttpClient.newHttpClient();

     // Send the request and retrieve response
     HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
     return response.body();
 }
 
 public String getPackageRolloutIdLog(String packageID) throws IOException, InterruptedException {

     Map<String, String> headers = new HashMap<>();
     headers.put("X-Master-Api-Key", XmasterApiKey); // Replace YOUR_ACCESS_TOKEN with your actual access token
     headers.put("X-Master-Api-Secret", XmasterApiSecret);
     HttpRequest request = HttpRequest.newBuilder()
             .uri(URI.create("https://api.prod-p-ap.sibros.tech/core/v2/packages/"+packageID+"/rollouts"))
             .headers(headers.entrySet().stream()
                     .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue()))
                     .flatMap(e -> Stream.of(e.getKey(), e.getValue())) // FlatMap to ensure alternating key-value pairs
                     .toArray(String[]::new))
             .GET()

             .build();

     // Create HttpClient
     HttpClient client = HttpClient.newHttpClient();

     // Send the request and retrieve response
     HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
     
     String responseDetails = response.body();
     
     
     ObjectMapper objectMapperRevision = new ObjectMapper();
     JsonNode jsonNodeRevision = objectMapperRevision.readTree(responseDetails);

     // Code to extract "id" entities from the JSON response
     JsonNode hardwareRevisionsArray = jsonNodeRevision.get("results");
  
     
     JsonNode lastObject = hardwareRevisionsArray.get(0);
     
     String rolloutID = lastObject.get("rolloutID").asText();
     
     
     return rolloutID ;
 }
    
    
    public class Component {
        private String componentID;
        private String abbreviation;
        private String version;
        private String imageId;
        private String fileName;
        private String imageSize;
        private String currentVersion;
        

        public Component(String componentID, String abbreviation, String version, String imageId, String fileName, String imageSize, String currentVersion) {
            this.componentID = componentID;
            this.abbreviation = abbreviation;
            this.version = version;
            this.imageId = imageId;
            this.fileName = fileName;
            this.imageSize = imageSize;
            this.currentVersion = currentVersion;
        }

        // Getters
        public String getComponentID() {
            return componentID;
        }

        public String getAbbreviation() {
            return abbreviation;
        }

        public String getVersion() {
            return version;
        }

        public String getImageId() {
            return imageId;
        }

        public String getFileName() {
            return fileName;
        }
        public String getImageSize() {
            return imageSize;
        }
        public String getCurrentVersion() {
            return currentVersion;
        }
        
    }
    
    
    
    public class Deployment {
        private String deploymentID;
        private String deviceID;
        private String packageID;
        private String deploymentType;
        private String startTime;
        private String endTime;
        private String vinNumber;
        private String deploymentStatus;
        private String rolloutID;

        public Deployment(String deploymentID, String deviceID, String packageID, String deploymentType, String startTime, String endTime, String vinNumber, String deploymentStatus, String rolloutID) {
            this.deploymentID = deploymentID;
            this.deviceID = deviceID;
            this.packageID = packageID;
            this.deploymentType = deploymentType;
            this.startTime = startTime;
            this.endTime = endTime;
            this.vinNumber = vinNumber;
            this.deploymentStatus = deploymentStatus;
            this.rolloutID = rolloutID;
        }

        // Getters
        public String getDeploymentID() {
            return deploymentID;
        }

        public String getDeviceID() {
            return deviceID;
        }

        public String getPackageID() {
            return packageID;
        }

        public String getDeploymentType() {
            return deploymentType;
        }

        public String getStartTime() {
            return startTime;
        }
        public String getEndTime() {
            return endTime;
        }
        public String getVinNumber() {
            return vinNumber;
        }
        public String getDeploymentStatus() {
            return deploymentStatus;
        }
        public String getRolloutID() {
            return rolloutID;
        }


        
    }
}

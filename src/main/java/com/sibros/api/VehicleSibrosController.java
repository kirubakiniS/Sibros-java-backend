package com.sibros.api;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
@RequestMapping
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class VehicleSibrosController
{
    @Autowired
    VehicleSibrosSevice VehicleSibrosSevice ;
    @GetMapping(value = "/get-vehicle-device-details")
    public String getDeviceDetails() throws IOException, InterruptedException {
        return VehicleSibrosSevice.getDeviceDetailsData();
    }

    @GetMapping(value = "/get-vehicle-device-model-details")
    public String getDeviceModelDetails() throws IOException, InterruptedException {
        return VehicleSibrosSevice.getDeviceModelDetailsData();
    }

    @GetMapping(value = "/get-vehicle-read-DTC")
    public String getDeviceReadDTC() throws IOException, InterruptedException {
        return  VehicleSibrosSevice.getDeviceReadDTCData();
    }

    @GetMapping(value = "/get-vehicle-package")
    public String getDevicePackage() throws IOException, InterruptedException {
        return  VehicleSibrosSevice.getDevicePackageData();
    }

    @GetMapping(value = "/add-new-rollout")
    public String addNewRollout(@RequestParam String packageID) throws IOException, InterruptedException {
        return  VehicleSibrosSevice.addNewRolloutData(packageID);
    }

    @GetMapping(value = "/start-rollout")
    public String startRollout(@RequestParam String packageID) throws IOException, InterruptedException {
        return  VehicleSibrosSevice.startRollout(packageID);
    }

    @GetMapping(value = "/get-rollout-status")
    public String getDevicePackage(@RequestParam String rolloutID) throws IOException, InterruptedException {
        return  VehicleSibrosSevice.getRolloutStatusData(rolloutID);
    }

    @GetMapping(value = "/getPackageImageDetails")
    public String getPackageDetails(@RequestParam String packageID) throws IOException, InterruptedException {
        return  VehicleSibrosSevice.getPackageImageDetails(packageID);
    }
    
    @GetMapping(value = "/getPackageDeploymentStatus")
    public String getPackageDeploymentStatus(@RequestParam String packageID) throws IOException, InterruptedException {
        return  VehicleSibrosSevice.getPackageDeploymentStatus(packageID);
    }
    
    @GetMapping(value = "/getPackageRolloutDeploymentLog")
    public String getPackageRolloutIdLog(@RequestParam String packageID, String componentID) throws IOException, InterruptedException {
        return  VehicleSibrosSevice.getPackageRolloutDeploymentLog(packageID,componentID);
    }
    
    
    @GetMapping(value = "/get-vehicle-offlinedevice-details")
    public String getOfflineDeviceDetails() throws IOException, InterruptedException {
        return VehicleSibrosSevice.getOfflineDeviceDetailsData();
    }
    
    @GetMapping(value = "/getPackageRolloutDeploymentLogs")
    public static String decryptFlashFile() {
        // Directory path
        String directoryPath = "C:/Users/2301-00023/Downloads/Sibros_BB_30042024";

        // File name
        String fileName = "swfl_0000bd43_001_003_254.bin";

        // Create a File object representing the file within the directory
        File file = new File(directoryPath, fileName);

        // Check if the file exists
        if (!file.exists()) {
            System.out.println("File not found: " + file.getAbsolutePath());
            return "NO Files";
        }
        StringBuilder decryptedString = new StringBuilder();
        // Attempt to read the file
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
            	
            	;
                // Convert text to byte array
                byte[] bytes = line.getBytes();

                // Convert each byte to hexadecimal and print
                for (byte b : bytes) {
                	String hexValue = String.format("%02X", b);
                   // System.out.print(String.format("%02X ", b));
                    decryptedString.append(hexValue);
                }
                
                
               // System.out.println("completed"+decryptedString.toString()); // Print newline
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		return decryptedString.toString();
    }
    
    @GetMapping(value = "/getPackageRolloutDeploymentLogss")
    public static void decryptFlashFileDownload(String saveFilePath,String fileName ) {
        // Directory path
    	String hexFile = "";
    	if (fileName.endsWith(".bin")) {
    		hexFile = fileName.substring(0, fileName.length() - 4).concat(".hex");
        }
        String inputFile = saveFilePath;
        String outputFile = "C:/Users/2301-00023/Downloads/Sibros_BB_30042024/output/"+hexFile+"";

        try (InputStream inputStream = new FileInputStream(inputFile);
                OutputStream outputStream = new FileOutputStream(outputFile)) {

               int byteRead;
               while ((byteRead = inputStream.read()) != -1) {
                   String hexByte = Integer.toHexString(byteRead & 0xFF); // Convert byte to hexadecimal
                   if (hexByte.length() == 1) {
                       // Pad single digit hexadecimal with leading zero
                       hexByte = "0" + hexByte;
                   }
                   outputStream.write(hexByte.getBytes());
               }
           } catch (IOException e) {
               e.printStackTrace();
           }
		
    }
    
    @GetMapping(value = "/get-download-files")
    public static String downloadFile(String downloadurl) throws IOException {
    	
    	// String fileURLs ="https://storage.googleapis.com/sibros-prod-ap-packages/468df435-c8da-4013-bc62-14f5e69b6f70/f4ae8887-9d71-4f3a-8dee-4ca626c4b7c5?X-Goog-Algorithm=GOOG4-RSA-SHA256&X-Goog-Credential=olympus-user%40sibros-prod-ap.iam.gserviceaccount.com%2F20240606%2Fauto%2Fstorage%2Fgoog4_request&X-Goog-Date=20240606T063603Z&X-Goog-Expires=3599&X-Goog-Signature=60b06e00ef6be54c407218f037dd9eff8c816bf6f1aee892b70d4959eba0402347327018657e440476c46bcc4775744cc8b3731d4fcb5c9db497281a7ef1afd4e9c0fbfbffc3d4f94521b6198853be9eb5ff6e1254b05eebfd6c8a43c9bdcb24a30d904d8e04b6851c8cf614c711548e6007b5119617138f213df48d6058233eed099545598f3ded7446ae4c3408bcdff9fba133ec0dd76b3ef8ed5fb3a24bdaf22650c83f1b1be50317d9d145b5f686020e0d0d9817a09e75d794a3935584793e6ee64896ec30c43c6ce3f30cef13cf6bb4be03d51260fe84600d456e6a05b97a90a99452e87545acd855ef6b3f8fd285db37c888902d0c6a29376ce18b6742&X-Goog-SignedHeaders=host&response-content-disposition=attachment%3B+filename%2A%3Dutf-8%27%27swfl_0000bd43_001_002_001.bin";
         String saveDir = "D:\\sibros_flashing_bin";
        // String fileURL = "https://storage.googleapis.com/sibros-prod-ap-packages/468df435-c8da-4013-bc62-14f5e69b6f70/202a6a05-8fd0-47fe-9221-75c9b05f3686?X-Goog-Algorithm=GOOG4-RSA-SHA256\\u0026X-Goog-Credential=olympus-user%40sibros-prod-ap.iam.gserviceaccount.com%2F20240606%2Fauto%2Fstorage%2Fgoog4_request\\u0026X-Goog-Date=20240606T064018Z\\u0026X-Goog-Expires=3599\\u0026X-Goog-Signature=2a1fddc712f36a9c84ad1381d76af3ea701c71816f787ff63037c80b36b3b39ab73e72b0992ac96a80e2c766301f336fad5ed38f2186b8408a4360728e57dcbc43a87fca1d6d72454e2132a3868d27e9d718dd24813c93696b5fd7a6298a0f4f42f1299d3671b31433958646c99dec6c6c244c823f23bbd8a2e97aa77ba5bd65551e8d62f37471b4cb6d606a9f29ae1ab26189f66528ce98b43901bc0a2885a3734eb83d48e3bdbc700cff5cff480d718813c7e4e07c29063177b211faa802e86f0ef609b40c5317317aa8dba11286967099b069f8ac2f54bc42dfaa198a2de24a946b25e153285a9d9341fd34883c5469ce3ab2e09f564a26c9ea6b0f9856c1\\u0026X-Goog-SignedHeaders=host\\u0026response-content-disposition=attachment%3B+filename%2A%3Dutf-8%27%27swfl_0000bd43_001_003_254.bin";
         
         String fileURL = downloadurl;
     
         String fileURLs = fileURL.replace("\\u0026", "&");
         
         //String fileURL = "https://storage.googleapis.com/sibros-prod-ap-packages/468df435-c8da-4013-bc62-14f5e69b6f70/f4ae8887-9d71-4f3a-8dee-4ca626c4b7c5?X-Goog-Algorithm=GOOG4-RSA-SHA256&X-Goog-Credential=olympus-user%40sibros-prod-ap.iam.gserviceaccount.com%2F20240606%2Fauto%2Fstorage%2Fgoog4_request&X-Goog-Date=20240606T052108Z&X-Goog-Expires=3599&X-Goog-Signature=1b7afa578309fbdcfeababebe72c48f950969dd4d159419a34ba74a9e3034d098228aa530054520885c1d20f8a39f03aa7f90c2a4bde7cdeb6126e7ed3e7ed1ef7efc842ce3295a01c6849eafce8acc59553f22ff39bbe92aa8dd95e0239a2cc9f48faa719081f9b03a6153d88009276d3d324f4e459315d3a1cd9d12093c79c43c42ec1e67cde7bc1d59e8037b0244c314cbadf536f12f5d72aef85d20689112629fdfbbc24f7092300f341dd540ddc882f401b4fb9d81effeff3d62bdae737e285a082c7fb522bc695c950133b567fe811d894c3a0f93f556988c8a260c15d67d0e33c63fbf4ece4e4a7080578d1d25676c681cdfbf92d73d2593f8b9a488c&X-Goog-SignedHeaders=host&response-content-disposition=attachment%3B+filename%2A%3Dutf-8%27%27swfl_0000bd43_001_002_001.bin";
         String fileName = extractFilename(fileURLs);
         System.out.println("fileName:------->" + fileName);
        // String fileName = "swfl_0000bd43_001_002_001.bin";
         URL url = new URL(fileURLs);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();

        // Check if the request was successful
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Open input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();
            String saveFilePath = saveDir + File.separator + fileName;

            // Open an output stream to save the downloaded file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

            int bytesRead = -1;
            byte[] buffer = new byte[4096];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();
            
           // System.out.println("File downloaded to: " + saveFilePath);
            decryptFlashFileDownload(saveFilePath,fileName);
            httpConn.disconnect();
            return "File downloaded to: " + saveFilePath;
            
            
        } else {
        	httpConn.disconnect();
        	return "No file to download. Server replied with HTTP code: " + responseCode;
        	
        }
        
        
    }
    
    
    public static String extractFilename(String url) throws UnsupportedEncodingException {
    	
    	
        String decodedURL = URLDecoder.decode(url, "UTF-8");
        String regex = "filename\\*?=['\"]?utf-8''([^&]+)['\"]?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(decodedURL);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
    
    
    




}

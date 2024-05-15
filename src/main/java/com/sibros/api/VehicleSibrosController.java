package com.sibros.api;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;


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
    




}

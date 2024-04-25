package com.sibros.api;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;


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
    
//    @GetMapping(value = "/getPackageRolloutIdLog")
//    public String getPackageRolloutIdLog(@RequestParam String packageID) throws IOException, InterruptedException {
//        return  VehicleSibrosSevice.getPackageRolloutIdLog(packageID);
//    }




}

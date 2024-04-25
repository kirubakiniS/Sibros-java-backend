package com.sibros.api;

import java.io.IOException;


public interface VehicleSibrosSevice {

    String getDeviceDetailsData() throws IOException, InterruptedException;
    String getDeviceModelDetailsData() throws IOException, InterruptedException;
    String getDeviceReadDTCData() throws IOException, InterruptedException;
    String getDevicePackageData() throws IOException, InterruptedException;
    String addNewRolloutData(String packageID) throws IOException, InterruptedException;
    String startRollout(String packageID) throws IOException, InterruptedException;
    String getRolloutStatusData(String rolloutID) throws IOException, InterruptedException;
    String getPackageImageDetails(String packageID) throws IOException, InterruptedException;
    String getPackageDeploymentStatus(String packageID) throws IOException, InterruptedException;
   // String getPackageRolloutIdLog(String packageID) throws IOException, InterruptedException;



}

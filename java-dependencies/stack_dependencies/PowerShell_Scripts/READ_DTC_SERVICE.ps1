# PowerShell Script for DTC Reading
# Version: 1.4
# Path: D:\DTD-KGM\DTD-KGM\java-dependencies\stack_dependencies\Dll_Files

#Add proper error handling for DTC reading
try {
    $dtcService = New-Object BluPlus.UDS.Services.Services.ReadDTCInformationService

    # Request format - adjust based on your protocol documentation
    $requestData = [byte[]](0x02, 0xFF)  # Example: Read all powertrain DTCs

    # Initialize response variable properly
    $responseData = [ref] ([byte[]]::new(0))

    # Execute the service
    $serviceResult = $dtcService.Execute($requestData, $responseData)

    Write-Host "Service execution status: $serviceResult"

    if ($serviceResult) {
        # Debug output - show the raw response type
        Write-Host "Response type: $($responseData.Value.GetType().FullName)"

        # Handle different response types
        if ($responseData.Value -is [byte[]]) {
            Write-Host "Response: $($responseData.Value)"
            $rawBytes = $responseData.Value
            $hexString = [System.BitConverter]::ToString($rawBytes)
            Write-Host "Raw DTC data (hex): $hexString"

            # Parse the DTCs
            $dtcCodes = Parse-DTCBytes -bytes $rawBytes
            Write-Host "Detected DTCs: $($dtcCodes -join ', ')"
        }
        elseif ($responseData.Value -is [string]) {
            Write-Host "String response received: $($responseData.Value)"
            # Handle string response if that's what your system returns
        }
        else {
            Write-Warning "Unexpected response type: $($responseData.Value.GetType().FullName)"
            Write-Host "Raw response value: $($responseData.Value)"
        }
    }
    else {
        Write-Warning "DTC read service returned false status"
    }
}
catch {
    Write-Error "DTC read failed: $_"
    Write-Host "Error details:"
    Write-Host "Exception type: $($_.Exception.GetType().FullName)"
    Write-Host "Message: $($_.Exception.Message)"
    Write-Host "Stack trace: $($_.Exception.StackTrace)"
}


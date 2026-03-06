try {
    $hexPathFile = $arg[0]
    $FlashingService = New-Object BluPlus.UDS.Services.FlashJob.FlashJobService($hexPathFile)

    # Execute the service
    $serviceResult = $FlashingService.Run()

    Write-Host "Service execution status: $serviceResult"
}
catch {
    Write-Error "Flash read failed: $_"
    Write-Host "Error details:"
    Write-Host "Exception type: $($_.Exception.GetType().FullName)"
    Write-Host "Message: $($_.Exception.Message)"
    Write-Host "Stack trace: $($_.Exception.StackTrace)"
}


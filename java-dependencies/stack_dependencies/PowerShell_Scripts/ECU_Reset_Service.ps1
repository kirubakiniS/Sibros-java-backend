# Change to the directory containing the DLL files
cd ".\java-dependencies\stack_dependencies\Dll_Files\"

# Unblock all files in the directory (to allow loading DLLs that were downloaded)
dir | Unblock-File

# Load the Diagnostic_Services assembly
$loaded = [System.Reflection.Assembly]::LoadFrom((Get-Location).Path + "\BluPlus.UDS.Sercices.dll")

Start-Sleep -Seconds 1

    # Create an instance of the ECUResetService class
    $instance = New-Object BluPlus.UDS.Services.Services.DiagnosticSessionControlService

    # Get the IUDSService interface from the instance
    $interface = $instance.GetType().GetInterface("BluPlus.UDS.Services.Interfaces.IUDSService")

    # Get the RunService method from the interface
    $method = $interface.GetMethod("Execute")

    # Invoke the RunService method with parameter 0x01
$method.Invoke($instance, @([byte[]](0x01), $null))
# ECU_Reset_Service.ps1

# Set working directory
#Set-Location "D:\DTD-KGM\DTD-KGM\java-dependencies\stack_dependencies\Dll_Files\"
#
## DLL path
#$dllPath = Join-Path (Get-Location).Path "Diagnostic_Services.dll"
#
## Check if DLL exists
#if (!(Test-Path $dllPath)) {
#    Write-Error "❌ DLL not found at path: $dllPath"
#    exit 1
#}
#
## Unblock DLL if downloaded externally
#Unblock-File -Path $dllPath
#
## Load the DLL
#try {
#    $assembly = [System.Reflection.Assembly]::LoadFrom($dllPath)
#    Write-Host "✅ DLL Loaded successfully: $($assembly.FullName)"
#} catch {
#    Write-Error "❌ Failed to load DLL: $_"
#    exit 1
#}
#
## Attempt to retrieve types, handle LoaderExceptions
#try {
#    $types = $assembly.GetTypes()
#    $ecuResetTypes = $types | Where-Object { $_.FullName -like "*ECUResetService*" }
#
#    if ($ecuResetTypes.Count -eq 0) {
#        Write-Error "❌ Type 'ECUResetService' not found in the assembly. Available types:"
#        $types | ForEach-Object { Write-Host " - $($_.FullName)" }
#        exit 1
#    }
#} catch [System.Reflection.ReflectionTypeLoadException] {
#    Write-Error "⚠️ One or more types failed to load. LoaderExceptions:"
#    foreach ($ex in $_.Exception.LoaderExceptions) {
#        Write-Host "LoaderException: $($ex.Message)"
#    }
#    exit 1
#} catch {
#    Write-Error "❌ Unexpected error while retrieving types: $_"
#    exit 1
#}
#
## Try to create instance
#try {
#    $instance = New-Object Diagnostic_Services.ECUResetService
#    Write-Host "✅ Created instance of ECUResetService."
#} catch {
#    Write-Error "❌ Failed to create ECUResetService instance: $_"
#    exit 1
#}
#
## Try to get interface and invoke method
#try {
#    $interface = $instance.GetType().GetInterface("Diagnostic_Services.Interfaces.IUDSService")
#    if (-not $interface) {
#        Write-Error "❌ Interface 'Diagnostic_Services.Interfaces.IUDSService' not found on the instance."
#        exit 1
#    }
#
#    $method = $interface.GetMethod("RunService")
#    if (-not $method) {
#        Write-Error "❌ Method 'RunService' not found on interface."
#        exit 1
#    }
#
#    $result = $method.Invoke($instance, @([byte]0x01))
#    Write-Host "✅ RunService Output: $result"
#} catch {
#    Write-Error "❌ Error during method invocation: $_"
#    exit 1
#}
#

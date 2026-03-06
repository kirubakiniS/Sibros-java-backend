Set-Location "D:\DTD-KGM\DTD-KGM\java-dependencies\stack_dependencies\Dll_Files"

# Load all required DLLs
$requiredDlls = @(
    "BluPlus.DServerWrapper.dll",
    "BluPlus.UDS.Services.dll",
    "Microsoft.Extensions.Configuration.dll",
    "Microsoft.Extensions.Configuration.Json.dll",
    "Microsoft.Extensions.Configuration.FileExtensions.dll"
)

foreach ($dll in $requiredDlls) {
    $dllPath = Join-Path (Get-Location) $dll
    if (Test-Path $dllPath) {
        try {
            [System.Reflection.Assembly]::LoadFrom($dllPath) | Out-Null
            Write-Host "✅ Loaded: $dll"
        } catch {
            Write-Warning "❌ Failed to load $dll - $_"
        }
    } else {
        Write-Warning "❌ Missing DLL: $dll"
    }
}

# Instantiate ReadDataByIdentifierService
try {
    $service = New-Object BluPlus.UDS.Services.Services.ReadDataByIdentifierService
    Write-Host "`n✅ Service instance created"
} catch {
    Write-Error "`n❌ Failed to create service instance: $_"
    exit 1
}

# Prepare the request data
$requestBytes = [byte[]](0x01)  # Replace with actual request bytes if needed
$responseRef = [ref]$null

# Call Execute
try {
    $success = $service.Execute($requestBytes, $responseRef)
    if ($success) {
        Write-Host "`n✅ Execute successful"
        Write-Host "📦 Response string: $($responseRef.Value)"
    } else {
        Write-Warning "`n❌ Execute returned false"
    }
} catch {
    Write-Error "`n❌ Error calling Execute: $_"
}

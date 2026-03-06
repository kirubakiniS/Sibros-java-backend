# PowerShell Script for VCI Initialization and VIN Reading
# Version: 1.4
# Path: D:\DTD-KGM\DTD-KGM\java-dependencies\stack_dependencies\Dll_Files

# Add required directories to PATH
$env:PATH += ";C:\Program Files (x86)\BlueBinaries\D-PDUSetup"

# Set working directory
Set-Location "D:\DTD-KGM\DTD-KGM\java-dependencies\stack_dependencies\Dll_Files"
[System.IO.Directory]::SetCurrentDirectory((Get-Location).Path)

# Load required assemblies
try {
    Add-Type -Path ".\BluPlus.DServerWrapper.dll"
    Add-Type -Path ".\BluPlus.UDS.Services.dll"
    Write-Host "✅ DLLs loaded successfully" -ForegroundColor Green
}
catch {
    Write-Error "❌ Failed to load DLLs: $($_.Exception.Message)"
    exit 1
}

# Initialize VCI
try {
    $vciInstance = New-Object BluPlus.UDS.Services.Common.VCI_Initialization
    $initResult = $vciInstance.Initialize_VCI()

    if ($initResult) {
        Write-Host "✅ VCI Initialization Success: $initResult" -ForegroundColor Green
    }
    else {
        Write-Warning "⚠️ VCI Initialization returned false"
        exit 1
    }
}
catch {
    Write-Error "💥 DLL Crash/Error: $($_.Exception.Message)"
    Write-Error "🧾 StackTrace: $($_.Exception.StackTrace)"
    Write-Error "❌ VCI Initialization Failed: $_"
    exit 1
}

# Add delay before VIN read
$delaySeconds = 2
Write-Host "⏳ Waiting $delaySeconds seconds before VIN read..." -ForegroundColor Yellow
Start-Sleep -Seconds $delaySeconds

# Read VIN using ReadDataByIdentifierService
try {
    $vinService = New-Object BluPlus.UDS.Services.Services.ReadDataByIdentifierService
    $requestData = [byte[]](0xF1, 0x8C)  # Standard UDS request for VIN

    $responseData = [ref] ([byte[]]::new(0))  # Initialize as byte array ref
    $vinResult = $vinService.Execute($requestData, $responseData)

    # Convert response to ASCII string based on its actual type
    if ($responseData.Value -is [byte[]]) {
        Write-Host "🧬 VIN Byte Data: $($responseData.Value -join ' ')" -ForegroundColor Cyan
        $asciiString = [System.Text.Encoding]::ASCII.GetString($responseData.Value)
        $asciiString = ($asciiString -replace '\0', '').Trim()
    }
    elseif ($responseData.Value -is [string]) {
        Write-Warning "⚠️ VIN returned as string instead of byte[]"
        $asciiString = ($responseData.Value -replace '\0', '').Trim()
    }
    else {
        Write-Error "❌ VIN Read Failed: Unexpected response type: $($responseData.Value.GetType().FullName)"
        exit 1
    }

    # Log result
    Write-Host "✅ VIN Read Successful" -ForegroundColor Green
    Write-Host "📋 VIN Data: $asciiString"
    Write-Host "Read VINSuccess: $vinResult"
}
catch {
    Write-Error "❌ VIN Read Failed: $($_.Exception.Message)"
    Write-Error "🧾 StackTrace: $($_.Exception.StackTrace)"
    exit 1
}

# Final structured summary
Write-Host "`n📦 Summary Output:"
Write-Host "{ `"VCI_Initialized`": true, `"VIN_Read_Success`": $vinResult, `"VIN`": `"$asciiString`" }"

# Script completed
Write-Host "`n✨ Script completed successfully" -ForegroundColor Magenta
exit 0

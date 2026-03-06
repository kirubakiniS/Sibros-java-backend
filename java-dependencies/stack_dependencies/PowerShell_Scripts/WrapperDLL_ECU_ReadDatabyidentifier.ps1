param(
    [string]$VciName,
    [string]$EcuName,
    [string]$DidName
)

# UTF-8 encoding
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$OutputEncoding = [System.Text.Encoding]::UTF8

# Move to DLL folder
Set-Location "$PSScriptRoot\..\Dll_Files"

Get-ChildItem | Unblock-File | Out-Null

$assemblyPath = Join-Path $PWD "WrapperAPI.dll"
[void][System.Reflection.Assembly]::LoadFrom($assemblyPath)

try {

    # 🔥 Suppress DLL Console.WriteLine output
    $originalOut = [Console]::Out
    $stringWriter = New-Object System.IO.StringWriter
    [Console]::SetOut($stringWriter)

    [void][D_PDU.detectedVCIInfo]::SimStackmode(1)
    [void][D_PDU.detectedVCIInfo]::Simulationload()
    [void][D_PDU.detectedVCIInfo]::VCI_Init($VciName)

    # Restore console
    [Console]::SetOut($originalOut)

    Start-Sleep -Seconds 1

    # 🔥 Call ReadDatabyidentifier
    $didValue = [D_PDU.detectedVCIInfo]::ReadDatabyidentifier($EcuName, $DidName)

    # Convert result to string
    $result = [string]$didValue

    # Output as JSON
    @{
        ecuName = $EcuName
        didName = $DidName
        value   = $result
    } | ConvertTo-Json -Compress
}
catch {
    "{}"
    exit 1
}
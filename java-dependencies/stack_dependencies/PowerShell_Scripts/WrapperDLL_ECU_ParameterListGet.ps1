param(
    [Parameter(Mandatory=$true)][string]$VciName,
    [Parameter(Mandatory=$true)][string]$EcuName
)

# Ensure UTF-8 output
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$OutputEncoding = [System.Text.Encoding]::UTF8

Set-Location "$PSScriptRoot\..\Dll_Files"

Get-ChildItem | Unblock-File | Out-Null

$assemblyPath = Join-Path $PWD "WrapperAPI.dll"
[void][System.Reflection.Assembly]::LoadFrom($assemblyPath)

$originalOut = [Console]::Out
$stringWriter = New-Object System.IO.StringWriter

try {

    # Suppress DLL console prints
    [Console]::SetOut($stringWriter)

    [void][D_PDU.detectedVCIInfo]::SimStackmode(1)
    [void][D_PDU.detectedVCIInfo]::Simulationload()
    [void][D_PDU.detectedVCIInfo]::VCI_Init($VciName)

    Start-Sleep -Seconds 1

    $ecuParameterList = [D_PDU.detectedVCIInfo]::GetparameterList($EcuName)

    # Convert to pure string array
    $data = foreach ($p in $ecuParameterList) { [string]$p }

    # Restore console output BEFORE printing JSON
    [Console]::SetOut($originalOut)

    # Output JSON to stdout
    Write-Output ($data | ConvertTo-Json -Compress)

}
catch {

    # Always return valid JSON
    Write-Output "[]"

    # Send real error to stderr (Java logs)
    Write-Error ("WrapperDLL_ECU_ParameterListGet failed. VciName={0}, EcuName={1}, Error={2}" -f $VciName, $EcuName, $_.Exception.Message)

    exit 1
}
finally {

    # Restore console output
    [Console]::SetOut($originalOut)
}
param(
    [string]$VciName,
    [string]$EcuName
)

# UTF-8 to avoid parsing issues
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$OutputEncoding = [System.Text.Encoding]::UTF8

Set-Location "$PSScriptRoot\..\Dll_Files"

Get-ChildItem | Unblock-File | Out-Null

$assemblyPath = Join-Path $PWD "WrapperAPI.dll"
[void][System.Reflection.Assembly]::LoadFrom($assemblyPath)

try {

    # 🔥 Redirect console output to suppress DLL internal Console.WriteLine
    $originalOut = [Console]::Out
    $stringWriter = New-Object System.IO.StringWriter
    [Console]::SetOut($stringWriter)

    [void][D_PDU.detectedVCIInfo]::SimStackmode(1)
    [void][D_PDU.detectedVCIInfo]::Simulationload()
    [void][D_PDU.detectedVCIInfo]::VCI_Init($VciName)

    # 🔥 Restore console output
    [Console]::SetOut($originalOut)

    Start-Sleep -Seconds 1

    $ecuParameterList = [D_PDU.detectedVCIInfo]::GetIOparameter($EcuName)

    $data = @()
    foreach ($p in $ecuParameterList) {
        $data += [string]$p
    }

    # ✅ ONLY JSON OUTPUT
    $data | ConvertTo-Json -Compress
}
catch {
    "[]"
    exit 1
}
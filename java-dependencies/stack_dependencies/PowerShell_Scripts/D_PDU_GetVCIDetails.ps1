cd .\java-dependencies\stack_dependencies\Dll_Files\
dir | unblock-file
$loaded = [System.Reflection.Assembly]::LoadFrom((Get-Location).Path+"\WrapperAPI.dll")
$output = [D_PDU.detectedVCIInfo]::getDevice_Detail()
foreach ($key in $output.Keys) {Write-Host "$key=$($output[$key]),"}
[D_PDU.detectedVCIInfo]::get_FirmwareVersion() + ',' -replace ': ','='
[D_PDU.detectedVCIInfo]::device_Partnumber() + ',' -replace ': ','='
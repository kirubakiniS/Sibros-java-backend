cd .\java-dependencies\stack_dependencies\Dll_Files\
dir | unblock-file
$loaded = [System.Reflection.Assembly]::LoadFrom((Get-Location).Path+"\WrapperAPI.dll")
$StaticMethodCall = $args[0]
$readData = [D_PDU.detectedVCIInfo]::$StaticMethodCall()
$iterateCount = 0
$readVciNames = @()
foreach ($item in $readData){
$iterateCount += 1
$readVciNames += $item.Key
}
if ($iterateCount -gt 1) {
$readVciNameList = $readVciNames | ConvertTo-JSON
} else {
$readVciNameList = $readVciNames |  convertTo-JSON
$readVciNameList = "[`n" + $readVciNameList + "`n]"
}
$readVciNameList
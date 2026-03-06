cd .\java-dependencies\stack_dependencies\Dll_Files\
dir | unblock-file
$loaded = [System.Reflection.Assembly]::LoadFrom((Get-Location).Path+"\WrapperAPI.dll")
[D_PDU.detectedVCIInfo]::SimStackmode(0)
[D_PDU.detectedVCIInfo]::Simulationload()
$ecuStatus = [D_PDU.detectedVCIInfo]::TesterPresent($args[1])
if ($ecuStatus = "Positive"){
$StaticMethodCall = $args[0]
$readData = [D_PDU.detectedVCIInfo]::$StaticMethodCall($args[1])
$readDtcMaps = @()
foreach ($item in $readData){
$isDtcFound = $item.Key
$classObject = $item.Value
$readDtcClass = @{
"ecuName" = $classObject.ecuName
"diagnosticTroubleCode" = $classObject.diagnosticTroubleCode
"description" = $classObject.description
"dtcState" = $classObject.dtcState
}
$readDtcMap = @{
"isDtcFound" = $isDtcFound
"readDtc" = $readDtcClass
}
$readDtcMaps += $readDtcMap
}
}
if ($isDtcFound -eq "found") {
$readDtcMapList = $readDtcMaps | ConvertTo-JSON
}else {
$readDtcMapObject = $readDtcMaps | ConvertTo-JSON
$readDtcMapList = "[`n" + $readDtcMapObject + "`n]"
}
$readDtcMapList

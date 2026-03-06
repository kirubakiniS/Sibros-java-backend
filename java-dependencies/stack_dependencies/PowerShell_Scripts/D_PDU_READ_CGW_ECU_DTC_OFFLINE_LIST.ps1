cd .\java-dependencies\stack_dependencies\Dll_Files\
dir | unblock-file
$loaded = [System.Reflection.Assembly]::LoadFrom((Get-Location).Path+"\WrapperAPI.dll")
[D_PDU.detectedVCIInfo]::SimStackmode(1)
[D_PDU.detectedVCIInfo]::Simulationload()
$StaticMethodCall = $args[0]
$readDatas = [D_PDU.detectedVCIInfo]::$StaticMethodCall($args[1])
Start-Sleep -Seconds 1
$readData = [D_PDU.detectedVCIInfo]::READDTC($args[2])
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
if ($isDtcFound -eq "found") {
    $readDtcMapList = $readDtcMaps | ConvertTo-JSON
}else {
    $readDtcMapObject = $readDtcMaps | ConvertTo-JSON
    $readDtcMapList = "[`n" + $readDtcMapObject + "`n]"
}
$readDtcMapList




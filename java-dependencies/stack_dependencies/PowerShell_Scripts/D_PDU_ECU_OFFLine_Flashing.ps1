cd .\java-dependencies\stack_dependencies\Dll_Files\
dir | unblock-file
#$loaded = [System.Reflection.Assembly]::LoadFrom((Get-Location).Path+"\WrapperAPI.dll")
#[D_PDU.detectedVCIInfo]::SimStackmode(0)
#[D_PDU.detectedVCIInfo]::Simulationload()
#$vciStatus = [D_PDU.detectedVCIInfo]::device_Partnumber()
#if (!$vciStatus.contains("Error! Could not initialize the channel"))
#{
  #$ecuStatus = [D_PDU.detectedVCIInfo]::TesterPresent($args[1])
  #if ($ecuStatus = "Positive") {
  #$StaticMethodCall = $args[0]
$loaded = [System.Reflection.Assembly]::LoadFrom((Get-Location).Path+"\WrapperAPI.dll")
[D_PDU.detectedVCIInfo]::SimStackmode(1)
[D_PDU.detectedVCIInfo]::Simulationload()
$StaticMethodCall = $args[0]
[D_PDU.detectedVCIInfo]::$StaticMethodCall($args[1])
Start-Sleep -Seconds 1
$serialNo = [D_PDU.detectedVCIInfo]::READECUVERSIONNUMBER($args[2])
$flashingStatus = [D_PDU.detectedVCIInfo]::Flashing_Sequence($args[2],"D:\sibros_flashing\output.hex")
  #}else{
  #$flashingStatus = "ECU flashing failed! - ECU not detected"
  #}
#}else{
#$flashingStatus = "ECU flashing failed! - VCI not detected"
#}
$flashingStatus


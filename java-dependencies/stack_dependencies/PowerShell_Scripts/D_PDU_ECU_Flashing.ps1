cd .\java-dependencies\stack_dependencies\Dll_Files\
dir | unblock-file
$loaded = [System.Reflection.Assembly]::LoadFrom((Get-Location).Path+"\WrapperAPI.dll")
[D_PDU.detectedVCIInfo]::SimStackmode(0)
[D_PDU.detectedVCIInfo]::Simulationload()
#$vciStatus = [D_PDU.detectedVCIInfo]::device_Partnumber()
#if (!$vciStatus.contains("Error! Could not initialize the channel"))
#{
  $ecuStatus = [D_PDU.detectedVCIInfo]::TesterPresent($args[1])
  if ($ecuStatus = "Positive") {
  $StaticMethodCall = $args[0]
  $flashingStatus = [D_PDU.detectedVCIInfo]::$StaticMethodCall($args[1],"D:\DigitalMobility\Flash_Files")
  }else{
  $flashingStatus = "ECU flashing failed! - ECU not detected"
  }
#}else{
#$flashingStatus = "ECU flashing failed! - VCI not detected"
#}
$flashingStatus


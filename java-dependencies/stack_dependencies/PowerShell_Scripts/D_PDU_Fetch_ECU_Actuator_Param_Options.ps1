cd .\java-dependencies\stack_dependencies\Dll_Files\
dir | unblock-file
$loaded = [System.Reflection.Assembly]::LoadFrom((Get-Location).Path+"\WrapperAPI.dll")
[D_PDU.detectedVCIInfo]::SimStackmode(0)
[D_PDU.detectedVCIInfo]::Simulationload()
$ecuStatus = [D_PDU.detectedVCIInfo]::TesterPresent($args[1])
if ($ecuStatus = "Positive"){
$StaticMethodCall = $args[0]
$readData = [D_PDU.detectedVCIInfo]::$StaticMethodCall($args[1],$args[2])
}
$readData|ConvertTo-JSON

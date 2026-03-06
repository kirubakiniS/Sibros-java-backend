cd .\java-dependencies\stack_dependencies\Dll_Files\
dir | unblock-file
$loaded = [System.Reflection.Assembly]::LoadFrom((Get-Location).Path+"\WrapperAPI.dll")
[D_PDU.detectedVCIInfo]::SimStackmode(0)
[D_PDU.detectedVCIInfo]::Simulationload()
$StaticMethodCall = $args[0]
[D_PDU.detectedVCIInfo]::$StaticMethodCall($args[1])
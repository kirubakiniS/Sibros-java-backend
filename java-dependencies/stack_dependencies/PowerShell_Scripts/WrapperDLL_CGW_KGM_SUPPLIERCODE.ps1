cd .\java-dependencies\stack_dependencies\Dll_Files\
dir | unblock-file
$loaded = [System.Reflection.Assembly]::LoadFrom((Get-Location).Path+"\WrapperAPI.dll")
[D_PDU.detectedVCIInfo]::SimStackmode(1)
[D_PDU.detectedVCIInfo]::Simulationload()
$StaticMethodCall = $args[0]
$vciInitStatus= [D_PDU.detectedVCIInfo]::$StaticMethodCall($args[1])
Start-Sleep -Seconds 1
$ecuSerialNo = [D_PDU.detectedVCIInfo]::READSUPPLIERCODE($args[2])
$ecuSerialNo

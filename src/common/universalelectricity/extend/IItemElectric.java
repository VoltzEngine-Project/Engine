package universalelectricity.extend;

import net.minecraft.src.ItemStack;

public interface IItemElectric extends IElectricityStorage
{
    public float onReceiveElectricity(float wattHourReceive, ItemStack itemStack);
    
    public float onUseElectricity(float wattHourRequest, ItemStack itemStack);
    
    public boolean canReceiveElectricity();
    
    public boolean canProduceElectricity();
    
    public float getTransferRate();
    
    public float getVoltage();
}

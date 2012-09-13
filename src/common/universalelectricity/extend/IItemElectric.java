package universalelectricity.extend;

import net.minecraft.src.ItemStack;

public interface IItemElectric
{
    public float onReceiveElectricity(float joules, ItemStack itemStack);
    
    public float onUseElectricity(float joules, ItemStack itemStack);
    
    public boolean canReceiveElectricity();
    
    public boolean canProduceElectricity();
        
    public float getElectricityCapacity();

    public float getTransferRate();
    
    public float getVoltage();
    
    public void setWattHoursStored(ItemStack itemStack, float wattHours);
    
    public float getWattHoursStored(ItemStack itemStack);
}

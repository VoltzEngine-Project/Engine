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
    
    public void setElectricityStored(ItemStack itemStack, float joules);
    
    public float getElectricityStored(ItemStack itemStack);
}

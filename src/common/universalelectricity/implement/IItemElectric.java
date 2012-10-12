package universalelectricity.implement;

import net.minecraft.src.ItemStack;

public interface IItemElectric extends IJouleStorage, IVoltage
{
	/**
	 * Called when this item receives electricity.
	 * @param joulesReceived - Joules sent over to this item.
	 */
    public double onReceiveElectricity(double joulesReceived, ItemStack itemStack);
    
    /**
     * Called when something uses electricity from this item.
     * @param joulesRequest - The amount of joules the consumer is requesting
     * @return
     */
    public double onUseElectricity(double joulesRequest, ItemStack itemStack);
    
    public boolean canReceiveElectricity();
    
    public boolean canProduceElectricity();
    
    public double getTransferRate();   
}

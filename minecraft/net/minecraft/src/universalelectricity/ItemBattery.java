package net.minecraft.src.universalelectricity;

import java.util.List;

import net.minecraft.src.ItemStack;
import net.minecraft.src.forge.ITextureProvider;

public class ItemBattery extends UEElectricItem implements ITextureProvider
{
    public ItemBattery(int par1, int par2)
    {
        super(par1);
        this.iconIndex = par2;
        this.setItemName("Battery");
    }
    
    /**
     * Allows items to add custom lines of information to the mouseover description
     */
    @Override
    public void addInformation(ItemStack par1ItemStack, List par2List)
    {
    	super.addInformation(par1ItemStack, par2List);
    }
    
    /**
     * This function is called to get the electricity maximum capacity in this item
     * @return - The amount of electricity maximum capacity
     */
    @Override
	public double getElectricityCapacity()
    {
    	return 15000.0;
    }
    
    /**
	 * Can this item give out electricity when placed in an tile entity? Electric items like batteries
	 * should be able to produce electricity (if they are rechargable).
	 * @return - True or False.
	 */
    @Override
	public boolean canProduceElectricity()
	{
		return true;
	}
    
    /**
     * This function is called to get the maximum transfer rate this electric item can recieve per tick
     * @return - The amount of electricity maximum capacity
     */
    @Override
	public double getTransferRate()
    {
    	return 100.0;
    }

	@Override
	public String getTextureFile()
	{
		return UCItem.textureFile;
	}

	@Override
	public int getVolts()
	{
		return 20;
	}
}

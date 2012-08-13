package universalelectricity.basiccomponents;

import java.util.List;

import universalelectricity.extend.ItemElectric;

import net.minecraft.src.ItemStack;

public class ItemBattery extends ItemElectric
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
    public float getElectricityCapacity()
    {
        return 15000;
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
    public float getTransferRate()
    {
        return 100;
    }

    @Override
    public String getTextureFile()
    {
        return BCItem.textureFile;
    }

    @Override
    public float getVolts()
    {
        return 20;
    }
}

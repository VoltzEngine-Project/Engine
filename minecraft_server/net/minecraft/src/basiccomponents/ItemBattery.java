package net.minecraft.src.basiccomponents;

import net.minecraft.src.forge.ITextureProvider;
import net.minecraft.src.universalelectricity.extend.ItemElectric;

public class ItemBattery extends ItemElectric implements ITextureProvider
{
    public ItemBattery(int par1, int par2)
    {
        super(par1);
        this.iconIndex = par2;
        this.setItemName("Battery");
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

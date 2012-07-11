package basiccomponents;

import universalelectricity.extend.ItemElectric;
import forge.ITextureProvider;


/**
 * The Class ItemBattery.
 */
public class ItemBattery extends ItemElectric implements ITextureProvider
{
    
    /**
     * Instantiates a new item battery.
     *
     * @param i the i
     * @param j the j
     */
    public ItemBattery(int i, int j)
    {
        super(i);
        textureId = j;
        a("Battery");
    }

    /* (non-Javadoc)
     * @see universalelectricity.extend.ItemElectric#getElectricityCapacity()
     */
    public float getElectricityCapacity()
    {
        return 15000F;
    }

    /* (non-Javadoc)
     * @see universalelectricity.extend.ItemElectric#canProduceElectricity()
     */
    public boolean canProduceElectricity()
    {
        return true;
    }

    /* (non-Javadoc)
     * @see universalelectricity.extend.ItemElectric#getTransferRate()
     */
    public float getTransferRate()
    {
        return 100F;
    }

    /* (non-Javadoc)
     * @see net.minecraft.server.Item#getTextureFile()
     */
    public String getTextureFile()
    {
        return BCItem.textureFile;
    }

    /* (non-Javadoc)
     * @see universalelectricity.extend.ItemElectric#getVolts()
     */
    public float getVolts()
    {
        return 20F;
    }
}

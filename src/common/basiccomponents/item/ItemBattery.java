package basiccomponents.item;

import universalelectricity.prefab.ItemElectric;
import basiccomponents.BasicComponents;

public class ItemBattery extends ItemElectric
{
    public ItemBattery(int par1, int par2)
    {
        super(par1);
        this.iconIndex = par2;
        this.setItemName("Battery");
    }

    @Override
    public double getMaxJoules()
    {
        return 20000;
    }

    @Override
    public boolean canProduceElectricity()
    {
        return true;
    }

    @Override
    public String getTextureFile()
    {
        return BasicComponents.ITEM_TEXTURE_FILE;
    }

    @Override
    public double getVoltage()
    {
        return 25;
    }
}

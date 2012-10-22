package universalelectricity.basiccomponents;

import java.util.List;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import universalelectricity.electricity.ElectricInfo;
import universalelectricity.electricity.ElectricInfo.ElectricUnit;


public class ItemCopperWire extends ItemBlock
{
	public ItemCopperWire(int id)
    {
        super(id);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }

    @Override
    public String getItemNameIS(ItemStack itemstack)
    {
    	return (new StringBuilder())
                .append(super.getItemName())
                .append(".")
                .append("Copper Wire")
                .toString();
    }
    
    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
    	par3List.add("Resistance: "+ElectricInfo.getDisplay(TileEntityCopperWire.RESISTANCE, ElectricUnit.RESISTANCE));
    	par3List.add("Max Amps: "+ElectricInfo.getDisplay(TileEntityCopperWire.MAX_AMPS, ElectricUnit.AMPERE));
    }

}

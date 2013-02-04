package universalelectricity.prefab.conductor;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricInfo.ElectricUnit;
import basiccomponents.common.tileentity.TileEntityCopperWire;

public abstract class ItemBlockConductor extends ItemBlock
{
	public ItemBlockConductor(int id)
	{
		super(id);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add("Resistance: " + ElectricInfo.getDisplay(TileEntityCopperWire.RESISTANCE, ElectricUnit.RESISTANCE));
		par3List.add("Max Amperage: " + ElectricInfo.getDisplay(TileEntityCopperWire.MAX_AMPS, ElectricUnit.AMPERE));
	}
}

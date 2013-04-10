package universalelectricity.components.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.components.client.gui.GuiBatteryBox;
import universalelectricity.components.client.gui.GuiCoalGenerator;
import universalelectricity.components.client.gui.GuiElectricFurnace;
import universalelectricity.components.common.container.ContainerBatteryBox;
import universalelectricity.components.common.container.ContainerCoalGenerator;
import universalelectricity.components.common.container.ContainerElectricFurnace;
import universalelectricity.components.common.tileentity.TileEntityBatteryBox;
import universalelectricity.components.common.tileentity.TileEntityCoalGenerator;
import universalelectricity.components.common.tileentity.TileEntityElectricFurnace;
import cpw.mods.fml.common.network.IGuiHandler;

/**
 * Extend this class in your GuiHandler
 * 
 * Be sure to call super.getClientGuiElement and super.getServerGuiElement
 */
public class BCGuiHandler implements IGuiHandler
{
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity != null)
		{
			switch (ID)
			{
				case 0:
					return new GuiBatteryBox(player.inventory, ((TileEntityBatteryBox) tileEntity));
				case 1:
					return new GuiCoalGenerator(player.inventory, ((TileEntityCoalGenerator) tileEntity));
				case 2:
					return new GuiElectricFurnace(player.inventory, ((TileEntityElectricFurnace) tileEntity));
			}
		}

		return null;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity != null)
		{
			switch (ID)
			{
				case 0:
					return new ContainerBatteryBox(player.inventory, ((TileEntityBatteryBox) tileEntity));
				case 1:
					return new ContainerCoalGenerator(player.inventory, ((TileEntityCoalGenerator) tileEntity));
				case 2:
					return new ContainerElectricFurnace(player.inventory, ((TileEntityElectricFurnace) tileEntity));
			}
		}

		return null;
	}
}

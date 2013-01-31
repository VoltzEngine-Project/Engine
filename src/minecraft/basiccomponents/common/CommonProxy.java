package basiccomponents.common;

import basiccomponents.common.container.ContainerBatteryBox;
import basiccomponents.common.container.ContainerCoalGenerator;
import basiccomponents.common.container.ContainerElectricFurnace;
import basiccomponents.common.tileentity.TileEntityBatteryBox;
import basiccomponents.common.tileentity.TileEntityCoalGenerator;
import basiccomponents.common.tileentity.TileEntityCopperWire;
import basiccomponents.common.tileentity.TileEntityElectricFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy implements IGuiHandler
{
	public void preInit()
	{
	}

	public void init()
	{
		/**
		 * Registering Tile Entities
		 */
		GameRegistry.registerTileEntity(TileEntityBatteryBox.class, "UEBatteryBox");
		GameRegistry.registerTileEntity(TileEntityCoalGenerator.class, "UECoalGenerator");
		GameRegistry.registerTileEntity(TileEntityElectricFurnace.class, "UEElectricFurnace");
		GameRegistry.registerTileEntity(TileEntityCopperWire.class, "UECopperWire");
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
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

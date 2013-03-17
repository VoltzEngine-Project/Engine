package universalelectricity.components.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.components.common.container.ContainerBatteryBox;
import universalelectricity.components.common.container.ContainerCoalGenerator;
import universalelectricity.components.common.container.ContainerElectricFurnace;
import universalelectricity.components.common.tileentity.TileEntityBatteryBox;
import universalelectricity.components.common.tileentity.TileEntityCoalGenerator;
import universalelectricity.components.common.tileentity.TileEntityCopperWire;
import universalelectricity.components.common.tileentity.TileEntityElectricFurnace;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy
{
	public void preInit()
	{
	}

	public void init()
	{
		BasicComponents.registerTileEntities();
	}
}

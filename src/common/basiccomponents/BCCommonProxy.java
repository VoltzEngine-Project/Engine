package basiccomponents;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import basiccomponents.tile.TileEntityBatteryBox;
import basiccomponents.tile.TileEntityCoalGenerator;
import basiccomponents.tile.TileEntityCopperWire;
import basiccomponents.tile.TileEntityElectricFurnace;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class BCCommonProxy implements IGuiHandler
{
	public void preInit() {}
	
	public void init()
	{
		GameRegistry.registerTileEntity(TileEntityCopperWire.class, "TileEntityCopperWire");
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
			switch(ID)
			{
				case 0: return new ContainerBatteryBox(player.inventory, ((TileEntityBatteryBox)tileEntity));
				case 1: return new ContainerCoalGenerator(player.inventory, ((TileEntityCoalGenerator)tileEntity));
				case 2: return new ContainerElectricFurnace(player.inventory, ((TileEntityElectricFurnace)tileEntity));
			}
        }
		
		return null;
	}
}

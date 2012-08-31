package universalelectricity.basiccomponents;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.common.DimensionManager;
import universalelectricity.electricity.ElectricityTicker;
import universalelectricity.extend.CommonProxy;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;

public class BCCommonProxy extends CommonProxy
{
	@Override
	public void preInit()
	{ 
		TickRegistry.registerTickHandler(new ElectricityTicker(), Side.SERVER);
	}
	
	@Override
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

package universalelectricity.basiccomponents;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.client.MinecraftForgeClient;
import universalelectricity.BasicComponents;
import universalelectricity.Ticker;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.registry.TickRegistry;

public class UEClientProxy extends UECommonProxy
{
	@Override
	public void preInit()
	{
		//Preload textures
		MinecraftForgeClient.preloadTexture(BasicComponents.BLOCK_TEXTURE_FILE);
		MinecraftForgeClient.preloadTexture(BasicComponents.ITEM_TEXTURE_FILE);
		
		TickRegistry.registerTickHandler(new Ticker(), Side.CLIENT);
	}
	
	@Override
	public void init()
	{
		ClientRegistry.registerTileEntity(TileEntityCopperWire.class, "TileEntityCopperWire", new RenderCopperWire());
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		
		if (tileEntity != null)
        {
			switch(ID)
			{
				case 0: return new GUIBatteryBox(player.inventory, ((TileEntityBatteryBox)tileEntity));
				case 1: return new GUICoalGenerator(player.inventory, ((TileEntityCoalGenerator)tileEntity));
				case 2: return new GUIElectricFurnace(player.inventory, ((TileEntityElectricFurnace)tileEntity));
			}
        }
		
		return null;
	}
}

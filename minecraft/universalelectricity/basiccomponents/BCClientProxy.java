package universalelectricity.basiccomponents;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ModLoader;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.client.MinecraftForgeClient;
import universalelectricity.basiccomponents.BCCommonProxy;
import universalelectricity.basiccomponents.BCItem;
import universalelectricity.basiccomponents.BasicComponents;
import universalelectricity.basiccomponents.TileEntityBatteryBox;
import universalelectricity.basiccomponents.TileEntityCoalGenerator;
import universalelectricity.basiccomponents.TileEntityCopperWire;
import universalelectricity.basiccomponents.TileEntityElectricFurnace;

public class BCClientProxy extends BCCommonProxy
{
	@Override
	public void preInit()
	{
		//Preload textures
		MinecraftForgeClient.preloadTexture(BasicComponents.blockTextureFile);
		MinecraftForgeClient.preloadTexture(BCItem.textureFile);
	}
	
	@Override
	public void init()
	{
		ModLoader.registerTileEntity(TileEntityCopperWire.class, "TileEntityCopperWire", new RenderCopperWire());
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
	
	@Override
	public World getWorld()
	{
		return ModLoader.getMinecraftInstance().theWorld;
	}
}

package basiccomponents;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.client.MinecraftForgeClient;
import basiccomponents.gui.GuiBatteryBox;
import basiccomponents.gui.GuiElectricFurnace;
import basiccomponents.gui.GuiCoalGenerator;
import basiccomponents.tile.TileEntityBatteryBox;
import basiccomponents.tile.TileEntityCoalGenerator;
import basiccomponents.tile.TileEntityCopperWire;
import basiccomponents.tile.TileEntityElectricFurnace;
import cpw.mods.fml.client.registry.ClientRegistry;

public class BCClientProxy extends BCCommonProxy
{
	@Override
	public void preInit()
	{
		// Preload textures
		MinecraftForgeClient.preloadTexture(BasicComponents.BLOCK_TEXTURE_FILE);
		MinecraftForgeClient.preloadTexture(BasicComponents.ITEM_TEXTURE_FILE);
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
}

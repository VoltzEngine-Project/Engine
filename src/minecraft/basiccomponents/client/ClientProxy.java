package basiccomponents.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import basiccomponents.client.gui.GuiBatteryBox;
import basiccomponents.client.gui.GuiCoalGenerator;
import basiccomponents.client.gui.GuiElectricFurnace;
import basiccomponents.common.BasicComponents;
import basiccomponents.common.CommonProxy;
import basiccomponents.common.tileentity.TileEntityBatteryBox;
import basiccomponents.common.tileentity.TileEntityCoalGenerator;
import basiccomponents.common.tileentity.TileEntityCopperWire;
import basiccomponents.common.tileentity.TileEntityElectricFurnace;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
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
		super.init();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCopperWire.class, new RenderCopperWire());
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

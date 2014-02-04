package calclavia.components.creative;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import calclavia.components.BlockCCIO;
import calclavia.components.CalclaviaLoader;
import calclavia.lib.content.BlockInfo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@BlockInfo(tileEntity = { TileInfiniteEnergy.class, TileInfiniteFluid.class })
public class BlockInfinite extends BlockCCIO
{
	private static enum Types
	{
		ENERGY("infiniteEnergy", TileInfiniteEnergy.class),
		FLUID("infiniteFluid", TileInfiniteFluid.class);

		public Icon icon;
		public String name;
		public String texture;
		Class<? extends TileEntity> clazz;

		private Types(String name, Class<? extends TileEntity> clazz)
		{
			this.name = name;
			this.clazz = clazz;
			this.texture = name;
		}

		public String getTextureName()
		{
			if (texture == null || texture.isEmpty())
			{
				return name;
			}
			return texture;
		}
	}

	public BlockInfinite()
	{
		super("infiniteBlock", CalclaviaLoader.idManager.getNextBlockID());
		setCreativeTab(CreativeTabs.tabTools);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconReg)
	{
		super.registerIcons(iconReg);

		for (Types block : Types.values())
		{
			block.icon = iconReg.registerIcon(CalclaviaLoader.PREFIX + block.getTextureName());
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta)
	{
		if (meta < Types.values().length)
		{
			return Types.values()[meta].icon;
		}
		return this.blockIcon;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		if (metadata < Types.values().length)
		{
			try
			{
				return Types.values()[metadata].clazz.newInstance();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return super.createTileEntity(world, metadata);
	}

	@Override
	public void getSubBlocks(int blockID, CreativeTabs tab, List creativeTabList)
	{
		for (Types block : Types.values())
		{
			creativeTabList.add(new ItemStack(blockID, 1, block.ordinal()));
		}
	}
}

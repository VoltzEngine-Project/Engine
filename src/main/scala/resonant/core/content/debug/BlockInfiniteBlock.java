package resonant.core.content.debug;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import resonant.core.ResonantEngine;
import resonant.lib.References;
import resonant.lib.content.BlockInfo;
import resonant.lib.multiblock.IBlockActivate;
import resonant.lib.prefab.block.BlockSidedIO;
import universalelectricity.api.UniversalElectricity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@BlockInfo(tileEntity = { "resonant.core.content.debug.TileInfiniteEnergy", "resonant.core.content.debug.TileInfiniteFluid" })
public class BlockInfiniteBlock extends BlockSidedIO
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

    public BlockInfiniteBlock(int id)
    {
        super(id, UniversalElectricity.machine);
        setCreativeTab(CreativeTabs.tabTools);
        this.setBlockUnbreakable();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconReg)
    {
        super.registerIcons(iconReg);

        for (Types block : Types.values())
        {
            block.icon = iconReg.registerIcon(References.PREFIX + block.getTextureName());
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

    @Override
    public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile instanceof IBlockActivate)
        {
            if (((IBlockActivate) tile).onActivated(entityPlayer))
            {
                return true;
            }
        }
        return false;
    }
}

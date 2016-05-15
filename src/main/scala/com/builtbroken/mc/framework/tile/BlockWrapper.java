package com.builtbroken.mc.framework.tile;

import com.builtbroken.mc.framework.tile.api.ITileHost;
import com.builtbroken.mc.lib.transform.region.Cube;
import com.builtbroken.mc.lib.transform.vector.Point;
import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import com.builtbroken.mc.prefab.tile.interfaces.tile.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Wrapper class for senting Block method calls to {@link BlockProperties} object and  {@link TileEntityWrapper} object.
 *
 * @author Dark
 */
public class BlockWrapper extends BlockContainer
{
    public final BlockProperties properties;

    public BlockWrapper(BlockProperties data)
    {
        super(data.material);
        this.properties = data;

        //Kill the game if the data is wrong
        if (properties == null)
        {
            throw new IllegalArgumentException("Block properties object can not be null");
        }
        if (properties.mod == null)
        {

        }
        //Init missing data from tileData object
        this.properties.block = this;

        //Load all data from tile data object
        if (properties.bounds != null)
        {
            this.setBlockBounds((float) this.properties.bounds.min().x(), (float) this.properties.bounds.min().y(), (float) this.properties.bounds.min().z(), (float) this.properties.bounds.max().x(), (float) this.properties.bounds.max().y(), (float) this.properties.bounds.max().z());
        }

        this.opaque = isOpaqueCube();
        setBlockName(properties.mod.getPrefix() + properties.name);
        setBlockTextureName(properties.mod.getPrefix() + properties.textureName);
        setCreativeTab(properties.creativeTab == null ? CreativeTabs.tabMisc : properties.creativeTab);
        setLightOpacity(isOpaqueCube() ? 255 : 0);
        setHardness(properties.hardness);
        setResistance(properties.resistance);
        setStepSound(properties.stepSound);
    }

    @Override
    public TileEntity createTileEntity(World world, int meta)
    {
        return properties.createNewTileEntity(world, meta);
    }


    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return properties.createNewTileEntity(world, meta);
    }

    @Override
    public void fillWithRain(World world, int x, int y, int z)
    {
        ITileHost tile = getTile(world, x, y, z);
        inject(tile, world, x, y, z);
        if (tile instanceof IRainFallible)
        {
            ((IRainFallible) tile).onFillRain();
        }
        eject(tile);
    }

    @Override
    public float getExplosionResistance(Entity entity)
    {
        return properties.staticTile instanceof IBlockTile ? ((IBlockTile) properties.staticTile).getExplosionResistance(entity) : properties.resistance;
    }

    @Override
    public float getExplosionResistance(Entity entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
    {
        ITileHost tile = getTile(world, x, y, z);
        if (tile instanceof IExplosiveResistance)
        {
            inject(tile, world, x, y, z);
            float resistance = ((IExplosiveResistance) tile).getExplosionResistance(entity, new Pos(explosionX, explosionY, explosionZ));
            eject(tile);
            return resistance;
        }
        return properties.resistance;
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player)
    {

        ITileHost tile = getTile(world, x, y, z);
        if (tile instanceof IPlayerLeftClick)
        {
            inject(tile, world, x, y, z);
            ((IPlayerLeftClick) tile).onPlayerLeftClick(player);
            eject(tile);
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        ITileHost tile = getTile(world, x, y, z);
        inject(tile, world, x, y, z);
        tile.onAdded();
        eject(tile);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        ITileHost tile = getTile(world, x, y, z);
        inject(tile, world, x, y, z);
        tile.onPlaced(entityLiving, itemStack);
        eject(tile);
    }

    @Override
    public void onPostBlockPlaced(World world, int x, int y, int z, int metadata)
    {
        ITileHost tile = getTile(world, x, y, z);
        inject(tile, world, x, y, z);
        tile.onPostPlaced(metadata);
        eject(tile);
    }

    /**
     * Called upon the block being destroyed by an explosion
     */
    @Override
    public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion ex)
    {
        ITileHost tile = getTile(world, x, y, z);
        if (tile instanceof IDestroyedByExplosion)
        {
            inject(tile, world, x, y, z);
            ((IDestroyedByExplosion) tile).onDestroyedByExplosion(ex);
            eject(tile);
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int par6)
    {
        ITileHost tile = getTile(world, x, y, z);
        inject(tile, world, x, y, z);
        tile.onRemove(block, par6);
        eject(tile);
        super.breakBlock(world, x, y, z, block, par6);
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
    {
        ITileHost tile = getTile(world, x, y, z);
        inject(tile, world, x, y, z);
        boolean b = tile.removeByPlayer(player, willHarvest);
        eject(tile);
        return b;
    }

    @Override
    public int quantityDropped(int meta, int fortune, Random random)
    {
        return properties.staticTile instanceof IBlockTile ? ((IBlockTile) properties.staticTile).quantityDropped(meta, fortune) : 1;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        ITileHost tile = getTile(world, x, y, z);
        if (tile instanceof INeighborBlock)
        {
            inject(tile, world, x, y, z);
            ((INeighborBlock) tile).onNeighborChanged(block);
            eject(tile);
        }
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side)
    {
        ITileHost tile = getTile(world, x, y, z);
        if (tile instanceof IPlaceable)
        {
            inject(tile, world, x, y, z);
            boolean b = ((IPlaceable) tile).canPlaceBlockOnSide(ForgeDirection.getOrientation(side));
            eject(tile);
            return b;
        }
        return super.canPlaceBlockOnSide(world, x, y, z, side);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        ITileHost tile = getTile(world, x, y, z);
        if (tile instanceof IPlaceable)
        {
            inject(tile, world, x, y, z);
            boolean b = ((IPlaceable) tile).canPlaceBlockAt();
            eject(tile);
            return b;
        }
        return super.canPlaceBlockAt(world, x, y, z);
    }

    @Override
    public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ)
    {
        ITileHost tile = getTile(world, x, y, z);
        if (tile instanceof INeighborBlock)
        {
            inject(tile, world, x, y, z);
            ((INeighborBlock) tile).onNeighborChanged(new Pos(tileX, tileY, tileZ));
            eject(tile);
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        ITileHost tile = getTile(world, x, y, z);
        if (tile instanceof IPlayerActivate)
        {
            inject(tile, world, x, y, z);
            boolean value = ((IPlayerActivate) tile).onPlayerActivated(player, side, new Pos(hitX, hitY, hitZ));
            eject(tile);
            return value;
        }
        return false;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random par5Random)
    {
        ITileHost tile = getTile(world, x, y, z);
        inject(tile, world, x, y, z);
        tile.blockUpdate();
        eject(tile);
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random par5Random)
    {
        ITileHost tile = getTile(world, x, y, z);
        if (tile instanceof IRandomTick)
        {
            inject(tile, world, x, y, z);
            ((IRandomTick) tile).randomDisplayTick();
            eject(tile);
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        ITileHost tile = getTile(world, x, y, z);
        if (tile instanceof ITileCollide)
        {
            inject(tile, world, x, y, z);
            ((ITileCollide) tile).onCollide(entity);
            eject(tile);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity)
    {
        ITileHost tile = getTile(world, x, y, z);
        if (tile instanceof ITileCollide)
        {
            inject(tile, world, x, y, z);
            Iterable<Cube> bounds = ((ITileCollide) tile).getCollisionBoxes(new Cube(aabb).subtract(new Pos(x, y, z)), entity);
            eject(tile);
            if (bounds != null)
            {
                for (Cube cuboid : bounds)
                {
                    AxisAlignedBB bb = cuboid.toAABB().offset(x, y, z);
                    if (aabb.intersectsWith(bb))
                    {
                        list.add(bb);
                    }
                }
            }
        }
        else
        {
            super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
        }
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
        ITileHost tile = getTile(world, x, y, z);
        if (tile instanceof ITileCollide)
        {
            inject(tile, world, x, y, z);
            AxisAlignedBB value = ((ITileCollide) tile).getSelectBounds().clone().add(tile.x(), tile.y(), tile.z()).toAABB();
            eject(tile);
            return value;
        }
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        ITileHost tile = getTile(world, x, y, z);
        if (tile instanceof ITileCollide)
        {
            inject(tile, world, x, y, z);
            AxisAlignedBB value = ((ITileCollide) tile).getCollisionBounds().clone().add(tile.x(), tile.y(), tile.z()).toAABB();
            eject(tile);
            return value;
        }
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side)
    {
        ITileHost tile = getTile(access, x, y, z);
        if (tile instanceof IBlockRender)
        {
            inject(tile, access, x, y, z);
            boolean value = ((IBlockRender) properties).shouldSideBeRendered(side);
            eject(tile);
            return value;
        }
        return super.shouldSideBeRendered(access, x, y, z, side);
    }

    @Override
    public boolean isBlockSolid(IBlockAccess access, int x, int y, int z, int side)
    {
        ITileHost tile = getTile(access, x, y, z);
        inject(tile, access, x, y, z);
        boolean value = getTile(access, x, y, z).isSolid(side);
        eject(tile);
        return value;
    }

    @Override
    public int getLightValue(IBlockAccess access, int x, int y, int z)
    {
        int value = 0;
        if (access != null)
        {
            ITileHost tile = getTile(access, x, y, z);
            inject(tile, access, x, y, z);
            value = getTile(access, x, y, z).getLightValue();
            eject(tile);
        }
        return value;
    }

    @Override
    public boolean hasComparatorInputOverride()
    {
        //TODO, add support for comparators
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return properties.staticTile == null || properties.isOpaque;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return properties.renderNormalBlock;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderType()
    {
        return properties.renderNormalBlock ? 0 : properties.renderType;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(IBlockAccess access, int x, int y, int z, int side)
    {
        ITileHost tile = getTile(access, x, y, z);
        if (tile instanceof ITileTextured)
        {
            inject(tile, access, x, y, z);
            IIcon value = ((ITileTextured) getTile(access, x, y, z)).getIcon(side, access.getBlockMetadata(x, y, z));
            eject(tile);
            return value;
        }
        return Blocks.wool.getIcon(side, side);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta)
    {
        return properties.staticTile instanceof ITileTextured ? ((ITileTextured) properties.staticTile).getIcon(side, meta) : Blocks.wool.getIcon(side, side);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        properties.registerIcons(iconRegister, true);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier(IBlockAccess access, int x, int y, int z)
    {
        ITileHost tile = getTile(access, x, y, z);
        if (tile instanceof IBlockRender)
        {
            inject(tile, access, x, y, z);
            int value = ((IBlockRender) getTile(access, x, y, z)).getColorMultiplier();
            eject(tile);
            return value;
        }
        return super.colorMultiplier(access, x, y, z);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getBlockColor()
    {
        return properties.blockColor;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player)
    {
        ITileHost tile = getTile(world, x, y, z);
        if (tile instanceof ITileItem)
        {
            inject(tile, world, x, y, z);
            ItemStack value = ((ITileItem) tile).getPickBlock(target);
            eject(tile);
            return value;
        }
        return super.getPickBlock(target, world, x, y, z, player);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        ITileHost tile = getTile(world, x, y, z);
        if (tile instanceof ITileItem)
        {
            inject(tile, world, x, y, z);
            ArrayList<ItemStack> value = ((ITileItem) tile).getDrops(metadata, fortune);
            eject(tile);
            return value != null ? value : new ArrayList<ItemStack>();
        }
        return super.getDrops(world, x, y, z, metadata, fortune);
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list)
    {
        if (properties.staticTile instanceof IBlockTile)
        {
            ((IBlockTile) properties.staticTile).getSubBlocks(item, creativeTabs, list);
        }
        else
        {
            list.add(new ItemStack(item));
        }
    }

    /**
     * Redstone interaction
     */
    @Override
    public boolean canProvidePower()
    {
        return properties.canEmmitRedstone;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess access, int x, int y, int z, int side)
    {
        ITileHost tile = getTile(access, x, y, z);
        if (tile instanceof IRedstone)
        {
            inject(tile, access, x, y, z);
            int value = ((IRedstone) getTile(access, x, y, z)).getWeakRedstonePower(side);
            eject(tile);
            return value;
        }
        return 0;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess access, int x, int y, int z, int side)
    {
        ITileHost tile = getTile(access, x, y, z);
        if (tile instanceof IRedstone)
        {
            inject(tile, access, x, y, z);
            int value = ((IRedstone) getTile(access, x, y, z)).getStrongRedstonePower(side);
            eject(tile);
            return value;
        }
        return 0;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, int x, int y, int z)
    {
        ITileHost tile = getTile(access, x, y, z);
        if (tile instanceof ITileBlockBounds)
        {
            inject(tile, access, x, y, z);
            ((ITileBlockBounds) getTile(access, x, y, z)).setBlockBoundsBasedOnState();
            eject(tile);
        }
    }

    @Override
    protected void dropBlockAsItem(World world, int x, int y, int z, ItemStack itemStack)
    {
        if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops"))
        {
            InventoryUtility.dropItemStack(world, new Pos(x, y, z), itemStack);
        }
    }

    @Override
    public int getRenderBlockPass()
    {
        return properties.renderPass;
    }

    @Override
    public int tickRate(World world)
    {
        if (properties.staticTile instanceof IBlockTile)
        {
            ((IBlockTile) properties.staticTile).setWorldAccess(world);
            int t = ((IBlockTile) properties.staticTile).tickRate();
            ((IBlockTile) properties.staticTile).setWorldAccess(null);
        }
        return 20;

    }

    public static Point getClickedFace(Byte hitSide, float hitX, float hitY, float hitZ)
    {
        switch (hitSide)
        {
            case 0:
                return new Point(1 - hitX, hitZ);
            case 1:
                return new Point(hitX, hitZ);
            case 2:
                return new Point(1 - hitX, 1 - hitY);
            case 3:
                return new Point(hitX, 1 - hitY);
            case 4:
                return new Point(hitZ, 1 - hitY);
            case 5:
                return new Point(1 - hitZ, 1 - hitY);
            default:
                return new Point(0.5, 0.5);
        }
    }


    /**
     * Injects and eject(tile);s data from the TileEntity.
     */
    public void inject(ITileHost tile, IBlockAccess access, int x, int y, int z)
    {
        if (tile == properties.staticTile)
        {
            properties.staticTile.injectLocation(access, 0, 0, 0);
        }
    }

    public void eject(ITileHost tile)
    {
        if (tile == properties.staticTile)
        {
            properties.staticTile.injectLocation(null, 0, 0, 0);
        }
    }

    public ITileHost getTile(IBlockAccess world, int x, int y, int z)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof ITileHost)
        {
            return ((ITileHost) tile);
        }
        return properties.staticTile;
    }
}

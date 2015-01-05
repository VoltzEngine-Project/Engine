package com.builtbroken.mc.prefab.tile;

import com.builtbroken.mc.lib.render.block.BlockRenderHandler;
import com.builtbroken.mc.lib.transform.region.Cuboid;
import com.builtbroken.mc.lib.transform.vector.Vector2;
import com.builtbroken.mc.lib.transform.vector.Vector3;
import com.builtbroken.mc.lib.helper.inventory.InventoryUtility;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by robert on 1/4/2015.
 */
public class BlockTile extends Block
{
    public Tile staticTile = null;

    private boolean hasTile = false;

    public BlockTile(Tile tile, String prefix, CreativeTabs tab)
    {
        super(tile.material);
        setBlockName(prefix + staticTile.name);
        setBlockTextureName(prefix + staticTile.textureName);
        setCreativeTab(staticTile.creativeTab == null ? tab : staticTile.creativeTab);
        opaque = isOpaqueCube();
        this.staticTile = tile;
        setLightOpacity(isOpaqueCube() ?  255 : 0);
        setHardness(staticTile.hardness);
        setResistance(staticTile.resistance);
        setStepSound(staticTile.stepSound);
    }

    @Override
    public TileEntity createTileEntity(World var1, int meta)
    {
        return staticTile.newTile();
    }

    @Override
    public boolean hasTileEntity(int meta)
    {
        if (staticTile.newTile() != null)
        {
            hasTile = staticTile.newTile() != null;
        }
        return hasTile;
    }

    @Override
    public void fillWithRain(World world, int x, int y, int z)
    {
        inject(world, x, y, z);
        getTile(world, x, y, z).onFillRain();
        eject();
    }

    @Override
    public float getExplosionResistance(Entity entity)
    {
        return staticTile.getExplosionResistance(entity);
    }

    public float getExplosionResistance(Entity entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
    {
        inject(world, x, y, z);
        float resistance = getTile(world, x, y, z).getExplosionResistance(entity, new Vector3(explosionX, explosionY, explosionZ));
        eject();
        return resistance;
    }

    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player)
    {
        inject(world, x, y, z);
        getTile(world, x, y, z).click(player);
        eject();
    }

    public void onBlockAdded(World world, int x, int y, int z)
    {
        inject(world, x, y, z);
        getTile(world, x, y, z).onAdded();
        eject();
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        inject(world, x, y, z);
        getTile(world, x, y, z).onPlaced(entityLiving, itemStack);
        eject();
    }

    @Override
    public void onPostBlockPlaced(World world, int x, int y, int z, int metadata)
    {
        inject(world, x, y, z);
        getTile(world, x, y, z).onPostPlaced(metadata);
        eject();
    }

    /**
     * Called upon the block being destroyed by an explosion
     */
    public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion ex)
    {
        inject(world, x, y, z);
        getTile(world, x, y, z).onDestroyedByExplosion(ex);
        eject();
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int par6)
    {
        inject(world, x, y, z);
        getTile(world, x, y, z).onRemove(block, par6);
        eject();
        super.breakBlock(world, x, y, z, block, par6);
    }

    @Override
    public int quantityDropped(int meta, int fortune, Random random)
    {
        return staticTile.quantityDropped(meta, fortune);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        inject(world, x, y, z);
        getTile(world, x, y, z).onNeighborChanged(block);
        eject();
    }

    @Override
    public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ)
    {
        inject(world, x, y, z);
        getTile(world, x, y, z).onNeighborChanged(new Vector3(tileX, tileY, tileZ));
        eject();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        inject(world, x, y, z);
        boolean value = getTile(world, x, y, z).activate(player, side, new Vector3(hitX, hitY, hitZ));
        eject();
        return value;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random par5Random)
    {
        inject(world, x, y, z);
        getTile(world, x, y, z).blockUpdate();
        eject();
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random par5Random)
    {
        inject(world, x, y, z);
        getTile(world, x, y, z).randomDisplayTick();
        eject();
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        inject(world, x, y, z);
        getTile(world, x, y, z).onCollide(entity);
        eject();
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity)
    {
        inject(world, x, y, z);
        Cuboid cube = new Cuboid(aabb).subtract(new Vector3(x, y, z));
        Iterable<Cuboid> bounds = getTile(world, x, y, z).getCollisionBoxes(cube, entity);

        if (bounds != null)
        {
            for (Cuboid cuboid: bounds)
            {
                list.add((cuboid.add(new Vector3(x, y, z))).toAABB());
            }
        }
        eject();
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
        inject(world, x, y, z);
        Tile tile = getTile(world, x, y, z);
        AxisAlignedBB value = tile.getSelectBounds().clone().add(tile).toAABB();
        eject();
        return value;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        inject(world, x, y, z);
        Tile tile = getTile(world, x, y, z);
        AxisAlignedBB value = tile.getCollisionBounds().clone().add(tile).toAABB();
        eject();
        return value;
    }

    public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side)
    {
        inject(access, x, y, z);
        boolean value =staticTile.shouldSideBeRendered(side);
        eject();
        return value;
    }

    public boolean isBlockSolid(IBlockAccess access, int x, int y, int z, int side)
    {
        inject(access, x, y, z);
        boolean value = getTile(access, x, y, z).isSolid(side);
        eject();
        return value;
    }

    public int getLightValue(IBlockAccess access, int x, int y, int z)
    {
        int value = 0;
        if (access != null)
        {
            inject(access, x, y, z);
            value = getTile(access, x, y, z).getLightValue();
            eject();
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
        return staticTile==null||staticTile.isOpaque;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return staticTile.renderNormalBlock;
    }

    @SideOnly(Side.CLIENT)
    public int getRenderType()
    {
        return staticTile.renderNormalBlock ? 0 : BlockRenderHandler.ID();
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess access, int x, int y, int z, int side)
    {
        inject(access, x, y, z);
        IIcon value = getTile(access, x, y, z).getIcon(side);
        eject();
        return value;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        return staticTile.getIcon(side, meta);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        staticTile.registerIcons(iconRegister);
    }

    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess access, int x, int y, int z)
    {
        inject(access, x, y, z);
        int value = getTile(access, x, y, z).getColorMultiplier();
        eject();
        return value;
    }

    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        inject(world, x, y, z);
        ItemStack value = getTile(world, x, y, z).getPickBlock(target);
        eject();
        return value;
    }

    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        inject(world, x, y, z);
        ArrayList<ItemStack> value = getTile(world, x, y, z).getDrops(metadata, fortune);
        eject();
        return value != null ? value : new ArrayList<ItemStack>();
    }

    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list)
    {
        staticTile.getSubBlocks(item, creativeTabs, list);
    }

    /**
     * Redstone interaction
     */
    public boolean canProvidePower()
    {
        return staticTile.canEmmitRedstone;
    }

    public int isProvidingWeakPower(IBlockAccess access, int x, int y, int z, int side)
    {
        inject(access, x, y, z);
        int value = getTile(access, x, y, z).getWeakRedstonePower(side);
        eject();
        return value;
    }

    public int isProvidingStrongPower(IBlockAccess access, int x, int y, int z, int side)
    {
        inject(access, x, y, z);
        int value = getTile(access, x, y, z).getStrongRedstonePower(side);
        eject();
        return value;
    }


    public void setBlockBoundsBasedOnState(IBlockAccess access, int x, int y, int z)
    {
        inject(access, x, y, z);
        getTile(access, x, y, z).setBlockBoundsBasedOnState();
        eject();
    }


    protected void dropBlockAsItem(World world, int x, int y, int z, ItemStack itemStack)
    {
        if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops"))
        {
            InventoryUtility.dropItemStack(world, new Vector3(x, y, z), itemStack);
        }
    }

    @Override
    public int getRenderBlockPass()
    {
        return staticTile.getRenderBlockPass();
    }

    @Override
    public int tickRate(World world)
    {
        inject(world, 0, 0, 0);
        int t = staticTile.tickRate();
        eject();
        return t;

    }

    public static Vector2 getClickedFace(Byte hitSide, float hitX, float hitY, float hitZ)
    {
        switch (hitSide)
        {
            case 0:
                return new Vector2(1 - hitX, hitZ);
            case 1:
                return new Vector2(hitX, hitZ);
            case 2:
                return new Vector2(1 - hitX, 1 - hitY);
            case 3:
                return new Vector2(hitX, 1 - hitY);
            case 4:
                return new Vector2(hitZ, 1 - hitY);
            case 5:
                return new Vector2(1 - hitZ, 1 - hitY);
            default:
                return new Vector2(0.5, 0.5);
        }
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return staticTile.creativeTab;
    }


    /**
     * Injects and eject();s data from the TileEntity.
     */
    public void inject(IBlockAccess access, int x, int y, int z)
    {
        if (access instanceof World)
        {
            staticTile.setWorldObj(((World) access));
        }

        staticTile.setAccess(access);
        staticTile.xCoord = x;
        staticTile.yCoord = y;
        staticTile.zCoord = z;

        TileEntity tile = access.getTileEntity(x, y, z);

        if (tile instanceof Tile)
        {
            ((Tile) tile).setBlock(this);
        }
    }

    public void eject()
    {
        staticTile.setWorldObj(null);
        staticTile.xCoord = 0;
        staticTile.yCoord = 0;
        staticTile.zCoord = 0;
    }

    public Tile getTile(IBlockAccess world, int x, int y, int z)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof Tile)
        {
            return ((Tile) tile);
        }
        return staticTile;
    }
}

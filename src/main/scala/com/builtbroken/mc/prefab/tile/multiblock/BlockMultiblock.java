package com.builtbroken.mc.prefab.tile.multiblock;

import com.builtbroken.mc.api.tile.client.IIconCallBack;
import com.builtbroken.mc.api.tile.multiblock.IMultiTile;
import com.builtbroken.mc.lib.transform.region.Cube;
import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.prefab.tile.multiblock.types.TileMultiInv;
import com.builtbroken.mc.prefab.tile.multiblock.types.TileMultiTank;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
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

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Dark on 7/4/2015.
 */
public class BlockMultiblock extends BlockContainer
{
    public static ITileEntityProvider EU_ENERGY_TILE_PROVIDER;
    public static ITileEntityProvider RF_ENERGY_TILE_PROVIDER;
    public static ITileEntityProvider ENERGY_TILE_PROVIDER;

    public BlockMultiblock()
    {
        super(Material.rock);
        this.setBlockName("veMultiBlock");
        this.setHardness(2f);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileMulti)
        {
            Cube cube = ((TileMulti) tile).collisionBounds;
            if (cube != null)
            {
                cube.add(x, y, z);
                return cube.toAABB();
            }
        }
        return AxisAlignedBB.getBoundingBox((double) x + this.minX, (double) y + this.minY, (double) z + this.minZ, (double) x + this.maxX, (double) y + this.maxY, (double) z + this.maxZ);
    }

    @Override
    public int getRenderType()
    {
        return MultiBlockRenderHelper.INSTANCE.getRenderId();
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileMulti)
        {
            ((TileMulti) tile).updateConnections();
        }
    }

    @Override
    public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ)
    {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileMulti)
        {
            Cube cube = ((TileMulti) tile).overrideRenderBounds;
            if (cube != null)
            {
                switch (side)
                {
                    case 0:
                        return cube.min().y() > 0.0D || !isOpaqueCube();
                    case 1:
                        return cube.max().y() < 1.0D || !isOpaqueCube();
                    case 2:
                        return cube.min().z() > 0.0D || !isOpaqueCube();
                    case 3:
                        return cube.max().z() < 1.0D || !isOpaqueCube();
                    case 4:
                        return cube.min().x() > 0.0D || !isOpaqueCube();
                    case 5:
                        return cube.max().x() < 1.0D || !isOpaqueCube();
                }
            }
        }
        switch (side)
        {
            case 0:
                return minY > 0.0D || !isOpaqueCube();
            case 1:
                return maxY < 1.0D || !isOpaqueCube();
            case 2:
                return minZ > 0.0D || !isOpaqueCube();
            case 3:
                return maxZ < 1.0D || !isOpaqueCube();
            case 4:
                return minX > 0.0D || !isOpaqueCube();
            case 5:
                return maxX < 1.0D || !isOpaqueCube();
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg)
    {
        //this.blockIcon = p_149651_1_.registerIcon(this.getTextureName());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IMultiTile && ((IMultiTile) tile).getHost() instanceof IIconCallBack)
        {
            return ((IIconCallBack) ((IMultiTile) tile).getHost()).getIconForSide(world, x, y, z, side);
        }
        return Blocks.iron_bars.blockIcon;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player)
    {
        IMultiTile tile = getTile(world, x, y, z);
        if (tile != null && tile.getHost() != null)
        {
            Block block = ((TileEntity) tile.getHost()).getWorldObj().getBlock(((TileEntity) tile.getHost()).xCoord, ((TileEntity) tile.getHost()).yCoord, ((TileEntity) tile.getHost()).zCoord);
            return block.getPickBlock(target, ((TileEntity) tile.getHost()).getWorldObj(), ((TileEntity) tile.getHost()).xCoord, ((TileEntity) tile.getHost()).yCoord, ((TileEntity) tile.getHost()).zCoord, player);
        }
        return null;
    }

    @Override
    public int quantityDropped(Random p_149745_1_)
    {
        return 0;
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return null;
    }


    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        return new ArrayList();
    }

    @Override
    protected boolean canSilkHarvest()
    {
        return false;
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        IMultiTile tile = getTile(world, x, y, z);
        if (tile != null && tile.getHost() != null)
        {
            tile.getHost().onMultiTileAdded(tile);
        }
        super.onBlockAdded(world, x, y, z);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        IMultiTile tile = getTile(world, x, y, z);
        if (tile != null && tile.getHost() != null)
        {
            tile.getHost().onMultiTileBroken(tile, null, true);
        }
        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
    {
        IMultiTile tile = getTile(world, x, y, z);
        if (tile != null && tile.getHost() != null)
        {
            tile.getHost().onMultiTileBroken(tile, player, willHarvest);
        }
        return removedByPlayer(world, player, x, y, z);
    }

    @Override
    public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion ex)
    {
        IMultiTile tile = getTile(world, x, y, z);
        if (tile != null && tile.getHost() != null)
        {
            tile.getHost().onMultiTileBroken(tile, ex, true);
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xHit, float yHit, float zHit)
    {
        IMultiTile tile = getTile(world, x, y, z);
        return tile != null && tile.getHost() != null && tile.getHost().onMultiTileActivated(tile, player, side, new Pos(xHit, yHit, zHit));
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player)
    {
        IMultiTile tile = getTile(world, x, y, z);
        if (tile != null && tile.getHost() != null)
        {
            tile.getHost().onMultiTileClicked(tile, player);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        if (meta >= 0 && meta < EnumMultiblock.values().length)
        {
            switch (EnumMultiblock.values()[meta])
            {
                case TANK:
                    return new TileMultiTank();
                case INVENTORY:
                    return new TileMultiInv();
                case ENERGY_EU:
                    return EU_ENERGY_TILE_PROVIDER != null ? EU_ENERGY_TILE_PROVIDER.createNewTileEntity(world, meta) : new TileMulti();
                case ENERGY_RF:
                    return RF_ENERGY_TILE_PROVIDER != null ? RF_ENERGY_TILE_PROVIDER.createNewTileEntity(world, meta) : new TileMulti();
                case ENERGY:
                    return ENERGY_TILE_PROVIDER != null ? ENERGY_TILE_PROVIDER.createNewTileEntity(world, meta) : new TileMulti();
            }
        }
        return new TileMulti();
    }

    protected IMultiTile getTile(World world, int x, int y, int z)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IMultiTile)
            return (IMultiTile) tile;
        return null;
    }
}

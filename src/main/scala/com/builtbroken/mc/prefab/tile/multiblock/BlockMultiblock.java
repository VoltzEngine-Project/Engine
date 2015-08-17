package com.builtbroken.mc.prefab.tile.multiblock;

import com.builtbroken.mc.api.tile.multiblock.IMultiTile;
import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.prefab.tile.multiblock.types.TileMultiInv;
import com.builtbroken.mc.prefab.tile.multiblock.types.TileMultiTank;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
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
        super(Material.circuits);
        this.setHardness(2f);
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
            tile.getHost().onMultiTileBroken(tile);
        }
        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion ex)
    {
        IMultiTile tile = getTile(world, x, y, z);
        if (tile != null && tile.getHost() != null)
        {
            tile.getHost().onMultiTileBrokenByExplosion(tile, ex);
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xHit, float yHit, float zHit)
    {
        IMultiTile tile = getTile(world, x, y, z);
        if (tile != null && tile.getHost() != null)
        {
            tile.getHost().onMultiTileActivated(tile, player, side, new Pos(xHit, yHit, zHit));
        }
        return false;
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

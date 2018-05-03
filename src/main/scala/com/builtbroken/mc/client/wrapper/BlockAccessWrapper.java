package com.builtbroken.mc.client.wrapper;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/3/2018.
 */
public class BlockAccessWrapper implements IBlockAccess
{
    public int overrideMeta = -1;
    public Block overrideBlock;
    public IBlockAccess actualWorld;

    @Override
    public Block getBlock(int x, int y, int z)
    {
        return overrideBlock != null ? overrideBlock : actualWorld.getBlock(x, y, z);
    }

    @Override
    public TileEntity getTileEntity(int x, int y, int z)
    {
        return actualWorld.getTileEntity(x, y, z);
    }

    @Override
    public int getLightBrightnessForSkyBlocks(int x, int y, int z, int p_72802_4_)
    {
        return actualWorld.getLightBrightnessForSkyBlocks(x, y, z, p_72802_4_);
    }

    @Override
    public int getBlockMetadata(int x, int y, int z)
    {
        return overrideMeta >= 0 && overrideMeta < 16 ? overrideMeta : actualWorld.getBlockMetadata(x, y, z);
    }

    @Override
    public int isBlockProvidingPowerTo(int x, int y, int z, int p_72879_4_)
    {
        return actualWorld.isBlockProvidingPowerTo(x, y, z, p_72879_4_);
    }

    @Override
    public boolean isAirBlock(int x, int y, int z)
    {
        return actualWorld.isAirBlock(x, y, z);
    }

    @Override
    public BiomeGenBase getBiomeGenForCoords(int x, int z)
    {
        return actualWorld.getBiomeGenForCoords(x, z);
    }

    @Override
    public int getHeight()
    {
        return actualWorld.getHeight();
    }

    @Override
    public boolean extendedLevelsInChunkCache()
    {
        return actualWorld.extendedLevelsInChunkCache();
    }

    @Override
    public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default)
    {
        return actualWorld.isSideSolid(x, y, z, side, _default);
    }
}

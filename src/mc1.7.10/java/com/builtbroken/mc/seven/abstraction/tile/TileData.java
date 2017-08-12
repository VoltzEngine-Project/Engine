package com.builtbroken.mc.seven.abstraction.tile;

import com.builtbroken.mc.abstraction.tile.ITileData;
import com.builtbroken.mc.abstraction.tile.ITileMaterial;
import com.builtbroken.mc.abstraction.world.IWorld;
import com.builtbroken.mc.seven.abstraction.MinecraftWrapper;
import com.builtbroken.mc.seven.abstraction.world.TilePosition;
import net.minecraft.block.Block;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/12/2017.
 */
public class TileData implements ITileData
{
    private final TilePosition position;

    public TileData(TilePosition position)
    {
        this.position = position;
    }

    @Override
    public IWorld getWorld()
    {
        return position.getWorld();
    }

    @Override
    public int xCoord()
    {
        return position.xCoord();
    }

    @Override
    public int yCoord()
    {
        return position.yCoord();
    }

    @Override
    public int zCoord()
    {
        return position.zCoord();
    }

    @Override
    public ITileMaterial getMaterial()
    {
        Block block = getBlock();
        if (block != null)
        {
            return MinecraftWrapper.INSTANCE.getTileMaterial(block.getMaterial());
        }
        return null;
    }

    public Block getBlock()
    {
        return world().getBlock(xCoord(), yCoord(), zCoord());
    }

    private World world()
    {
        return position.getWorld().getWorld();
    }
}

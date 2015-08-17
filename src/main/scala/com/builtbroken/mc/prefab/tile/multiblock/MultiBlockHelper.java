package com.builtbroken.mc.prefab.tile.multiblock;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.tile.multiblock.IMultiTileHost;
import com.builtbroken.mc.core.Engine;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Map;

/**
 * Created by Dark on 8/15/2015.
 */
public class MultiBlockHelper
{
    public static void buildMultiBlock(World world, int x, int y, int z)
    {
        if (world.blockExists(x, y, z))
        {
            TileEntity tile = world.getTileEntity(x, y, z);
            if (tile instanceof IMultiTileHost)
            {
                buildMultiBlock(world, (IMultiTileHost) tile);
            }
            else
            {
                Engine.instance.logger().error("MultiBlockHelper: Tile " + tile + " is not a multi block host");
            }
        }
    }

    public static void buildMultiBlock(World world, IMultiTileHost tile)
    {
        Map<IPos3D, String> map = tile.getLayoutOfMultiBlock();
        if (map != null && !map.isEmpty())
        {
            int i = 0;
            for (Map.Entry<IPos3D, String> entry : map.entrySet())
            {
                IPos3D location = entry.getKey();
                String type = entry.getValue();
                if (location == null)
                {
                    Engine.instance.logger().error("MultiBlockHelper: location[" + i + "] is null, this is most likely in error in " + tile);
                    i++;
                    continue;
                }

                if (type == null)
                {
                    Engine.instance.logger().error("MultiBlockHelper: type[" + i + "] is null, this is most likely in error in " + tile);
                    i++;
                    continue;
                }

                if (type.isEmpty())
                {
                    Engine.instance.logger().error("MultiBlockHelper: type[" + i + "] is empty, this is most likely in error in " + tile);
                    i++;
                    continue;
                }

                EnumMultiblock enumType = EnumMultiblock.get(type);
                if (enumType != null)
                {
                    world.setBlock((int) location.x(), (int) location.y(), (int) location.z(), Engine.multiBlock, enumType.ordinal(), 2);
                }
                else
                {
                    Engine.instance.logger().error("MultiBlockHelper: type[" + i + ", " + type + "] is not a invalid multi tile type, this is most likely in error in " + tile);
                }
                i++;
            }
        }
        else
        {
            Engine.instance.logger().error("MultiBlockHelper: Tile " + tile + " returned an empty map");
        }
    }
}

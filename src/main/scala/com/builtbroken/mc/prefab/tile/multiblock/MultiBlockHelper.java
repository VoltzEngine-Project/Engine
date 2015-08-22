package com.builtbroken.mc.prefab.tile.multiblock;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.tile.multiblock.IMultiTile;
import com.builtbroken.mc.api.tile.multiblock.IMultiTileHost;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.transform.region.Cube;
import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dark on 8/15/2015.
 */
public class MultiBlockHelper
{
    /**
     * Builds a multi block structure using data from the provided tile
     *
     * @param world - world
     * @param tile  - multiblock host
     */
    public static void buildMultiBlock(World world, IMultiTileHost tile)
    {
        buildMultiBlock(world, tile, false);
    }

    /**
     * Builds a multi block structure using data from the provided tile
     *
     * @param world    - world
     * @param tile     - multiblock host
     * @param validate - if true will check if a tile already exists at location rather than placing a new one
     */
    public static void buildMultiBlock(World world, IMultiTileHost tile, boolean validate)
    {
        Map<IPos3D, String> map = tile.getLayoutOfMultiBlock();
        if (map != null && !map.isEmpty())
        {
            int i = 0;
            for (Map.Entry<IPos3D, String> entry : map.entrySet())
            {
                IPos3D location = entry.getKey();
                String type = entry.getValue();
                String dataString = null;
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

                if (type.contains("#"))
                {
                    dataString = type.substring(type.indexOf("#") + 1, type.length());
                    type = type.substring(0, type.indexOf("#"));
                }

                EnumMultiblock enumType = EnumMultiblock.get(type);
                if (enumType != null)
                {
                    TileEntity ent = world.getTileEntity((int) location.x(), (int) location.y(), (int) location.z());
                    if (!validate || ent == null || enumType.clazz != ent.getClass())
                    {
                        world.setBlock((int) location.x(), (int) location.y(), (int) location.z(), Engine.multiBlock, enumType.ordinal(), 2);
                        ent = world.getTileEntity((int) location.x(), (int) location.y(), (int) location.z());
                    }

                    if (ent instanceof IMultiTile)
                    {
                        ((IMultiTile) ent).setHost(tile);
                        setData(dataString, (IMultiTile) ent);
                    }
                } else
                {
                    Engine.instance.logger().error("MultiBlockHelper: type[" + i + ", " + type + "] is not a invalid multi tile type, this is most likely an error in " + tile);
                }
                i++;
            }
        }
    }

    public static void setData(String dataString, IMultiTile ent)
    {
        if (ent instanceof TileMulti && dataString != null && !dataString.isEmpty())
        {
            String[] data;
            if (dataString.contains("|"))
                data = dataString.split("|");
            else
                data = new String[]{dataString};
            for (String d : data)
            {
                if (d.contains("="))
                {
                    String lowerCase = d.toLowerCase();
                    String value = lowerCase.substring(lowerCase.indexOf("=") + 1, lowerCase.length());

                    if (lowerCase.startsWith("renderblock"))
                    {
                        if (value.equals("true"))
                        {
                            ((TileMulti) ent).shouldRenderBlock = true;
                        }
                    } else if (lowerCase.startsWith("bounds"))
                    {
                        if (value.contains("{") && value.contains("}") && value.contains(","))
                        {
                            String[] values = value.split(",");
                            if (values.length == 6)
                            {
                                int[] ints = new int[6];
                                boolean failed = false;
                                for (int se = 0; se < 6; se++)
                                {
                                    try
                                    {
                                        ints[se] = Integer.parseInt(values[se]);
                                    }
                                    catch (NumberFormatException e)
                                    {
                                        failed = true;
                                        break;
                                    }
                                }
                                if (!failed)
                                {
                                    if (((TileMulti) ent).worldObj.isRemote)
                                        ((TileMulti) ent).overrideRenderBounds = new Cube(ints[0], ints[1], ints[2], ints[3], ints[4], ints[5]);
                                    ((TileMulti) ent).collisionBounds = new Cube(ints[0], ints[1], ints[2], ints[3], ints[4], ints[5]);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void destroyMultiBlockStructure(IMultiTileHost host)
    {
        if (host instanceof TileEntity)
        {
            HashMap<IPos3D, String> map = host.getLayoutOfMultiBlock();
            if (map != null && !map.isEmpty())
            {
                World world = ((TileEntity) host).getWorldObj();
                int x = ((TileEntity) host).xCoord;
                int y = ((TileEntity) host).yCoord;
                int z = ((TileEntity) host).zCoord;
                for (Map.Entry<IPos3D, String> entry : map.entrySet())
                {
                    Pos pos = new Pos(x, y, z).add(entry.getKey());
                    TileEntity tile = pos.getTileEntity(world);
                    if (tile instanceof IMultiTile)
                        ((IMultiTile) tile).setHost(null);
                    pos.setBlockToAir(world);
                }
                InventoryUtility.dropBlockAsItem(world, x, y, z, true);
            }
        }
    }
}

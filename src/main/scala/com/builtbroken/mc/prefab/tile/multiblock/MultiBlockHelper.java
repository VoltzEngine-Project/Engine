package com.builtbroken.mc.prefab.tile.multiblock;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.tile.multiblock.IMultiTile;
import com.builtbroken.mc.api.tile.multiblock.IMultiTileHost;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.transform.region.Cube;
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
            } else
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
                String[] data = null;
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
                    String dataString = type.substring(type.indexOf("#") + 1, type.length());
                    type = type.substring(0, type.indexOf("#"));
                    if (dataString.contains("|"))
                        data = dataString.split("|");
                    else
                        data = new String[]{dataString};
                }

                EnumMultiblock enumType = EnumMultiblock.get(type);
                if (enumType != null)
                {
                    world.setBlock((int) location.x(), (int) location.y(), (int) location.z(), Engine.multiBlock, enumType.ordinal(), 2);
                    TileEntity ent = world.getTileEntity((int) location.x(), (int) location.y(), (int) location.z());
                    if (ent instanceof IMultiTile)
                    {
                        ((IMultiTile) ent).setHost(tile);
                        if (ent instanceof TileMulti && data != null)
                        {
                            for (String d : data)
                            {
                                System.out.println("Processing data > " + d);
                                if (d.contains("="))
                                {
                                    String lowerCase = d.toLowerCase();
                                    System.out.println("\tLowerCase: " + lowerCase);
                                    String value = lowerCase.substring(lowerCase.indexOf("=") + 1, lowerCase.length());
                                    System.out.println("\tValue: " + lowerCase);
                                    if (lowerCase.startsWith("renderblock"))
                                    {
                                        if (value.equals("true"))
                                        {
                                            ((TileMulti) ent).shouldRenderBlock = true;
                                        } else if (value.equals("false"))
                                        {
                                            ((TileMulti) ent).shouldRenderBlock = true;
                                        } else
                                        {
                                            Engine.instance.logger().error("MultiBlockHelper: type[" + i + "] couldn't parse " + value + " as true or false, this is most likely in error in " + tile);
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
                                                        Engine.instance.logger().error("MultiBlockHelper: type[" + i + "] failed to parse " + d + ", this is most likely in error in " + tile);
                                                        break;
                                                    }
                                                }
                                                if (!failed)
                                                {
                                                    if (world.isRemote)
                                                        ((TileMulti) ent).overrideRenderBounds = new Cube(ints[0], ints[1], ints[2], ints[3], ints[4], ints[5]);
                                                    ((TileMulti) ent).collisonBounds = new Cube(ints[0], ints[1], ints[2], ints[3], ints[4], ints[5]);
                                                }
                                            } else
                                            {
                                                Engine.instance.logger().error("MultiBlockHelper: type[" + i + "] invalid formate for data " + d + ", this is most likely in error in " + tile);
                                            }
                                        } else
                                        {
                                            Engine.instance.logger().error("MultiBlockHelper: type[" + i + "] invalid formate for data " + d + ", this is most likely in error in " + tile);
                                        }
                                    } else
                                    {
                                        Engine.instance.logger().error("MultiBlockHelper: type[" + i + "] unknown data " + d + ", this is most likely in error in " + tile);
                                    }
                                } else
                                {
                                    Engine.instance.logger().error("MultiBlockHelper: type[" + i + "] invalid data " + d + ", this is most likely in error in " + tile);
                                }
                            }
                        }
                    }
                } else
                {
                    Engine.instance.logger().error("MultiBlockHelper: type[" + i + ", " + type + "] is not a invalid multi tile type, this is most likely in error in " + tile);
                }
                i++;
            }
        } else
        {
            Engine.instance.logger().error("MultiBlockHelper: Tile " + tile + " returned an empty map");
        }
    }
}

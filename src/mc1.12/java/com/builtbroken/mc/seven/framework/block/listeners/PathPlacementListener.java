package com.builtbroken.mc.seven.framework.block.listeners;

import com.builtbroken.mc.framework.block.imp.ITileEventListener;
import com.builtbroken.mc.framework.block.imp.ITileEventListenerBuilder;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.lib.data.BlockStateEntry;
import com.builtbroken.mc.framework.json.loading.JsonProcessorData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 7/21/2017.
 */
public class PathPlacementListener extends AdjacentPlacementListener
{
    @JsonProcessorData(value = "pathRange", type = "int")
    protected int pathRange = 5;

    protected List<BlockStateEntry> pathBlocks = new ArrayList();
    protected List<String> pathContentIDs = new ArrayList();


    public PathPlacementListener(Block block)
    {
        super(block);
    }

    @Override
    protected boolean isPlacementValid()
    {
        List<Pos> pathLocations = new ArrayList();
        Queue<Pos> pathNextList = new LinkedList();

        Pos center = new Pos(this);
        if (canPath(center))
        {
            pathNextList.add(center);
        }
        //Fix for placement code where center is not the block we are placing
        else
        {
            for (ForgeDirection direction : supportedDirections == null ? ForgeDirection.VALID_DIRECTIONS : supportedDirections)
            {
                pathNextList.add(center.add(direction));
            }
        }

        //Loop all tiles
        while (!pathNextList.isEmpty())
        {
            //Get next tile and add to pathed list
            Pos nextPos = pathNextList.poll();
            pathLocations.add(nextPos);

            //Loop connections
            for (ForgeDirection direction : supportedDirections == null ? ForgeDirection.VALID_DIRECTIONS : supportedDirections)
            {
                Pos pos = nextPos.add(direction);
                //Only do check once per tile
                if (!pathLocations.contains(pos) && canPath(pos))
                {
                    //Check if valid, exit condition for loop
                    if (isSupportingTile(getBlockAccess(), pos))
                    {
                        return true;
                    }

                    //Add to path next list
                    pathNextList.add(pos);
                }
            }
        }

        return false;
    }

    protected boolean canPath(Pos pos)
    {
        return isInDistance(pos) && !getBlockAccess().isAirBlock(pos.xi(), pos.yi(), pos.zi()) && isPathTile(getBlockAccess(), pos);
    }

    /**
     * Checks if tile should be pathed
     *
     * @param access
     * @param pos
     * @return
     */
    protected boolean isPathTile(IBlockAccess access, Pos pos)
    {
        return pathBlocks.isEmpty() && pathContentIDs.isEmpty() || doesContainTile(access, pos, pathBlocks, pathContentIDs);
    }

    protected boolean isInDistance(Pos pos)
    {
        if (pos.xi() > pathRange + xi() || pos.xi() < xi() - pathRange)
        {
            return false;
        }
        if (pos.yi() > pathRange + yi() || pos.yi() < yi() - pathRange)
        {
            return false;
        }
        if (pos.zi() > pathRange + zi() || pos.zi() < zi() - pathRange)
        {
            return false;
        }
        return true;
    }

    @JsonProcessorData("canPath")
    public void processPathBlocks(JsonElement inputElement)
    {
        if (inputElement.isJsonArray())
        {
            //Loop through elements in array
            for (JsonElement element : inputElement.getAsJsonArray())
            {
                //Get as object
                if (element.isJsonObject())
                {
                    JsonObject object = element.getAsJsonObject();

                    if (object.has("block"))
                    {
                        String blockName = object.getAsJsonPrimitive("block").getAsString();
                        int meta = -1;
                        if (object.has("data"))
                        {
                            meta = object.getAsJsonPrimitive("data").getAsInt();
                        }

                        pathBlocks.add(new BlockStateEntry(blockName, meta));
                    }
                    else if (object.has("contentID"))
                    {
                        pathContentIDs.add(object.getAsJsonPrimitive("contentID").getAsString().toLowerCase());
                    }
                    else
                    {
                        Engine.logger().warn("AdjacentPlacementListener#process(JsonElement) >> Could not find convert '" + element + "' int a usable type for " + this);
                    }
                }
                else
                {
                    throw new IllegalArgumentException("Invalid data, block entries must look like \n {\n\t \"block\" : \"minecraft:tnt\",\n\t \"data\" : 0 \n}");
                }
            }
        }
        else
        {
            throw new IllegalArgumentException("Invalid data, blocks data must be an array");
        }
    }

    @Override
    public String toString()
    {
        if (contentUseID != null)
        {
            return "PathFinderPlacementListener[" + block + " >> " + contentUseID + "]@" + hashCode();
        }
        else if (metaCheck != -1)
        {
            return "PathFinderPlacementListener[" + block + "@" + metaCheck + "]@" + hashCode();
        }
        return "PathFinderPlacementListener[" + block + "]@" + hashCode();
    }

    public static class Builder implements ITileEventListenerBuilder
    {
        @Override
        public ITileEventListener createListener(Block block)
        {
            return new PathPlacementListener(block);
        }

        @Override
        public String getListenerKey()
        {
            return "pathFinderPlacementListener";
        }
    }
}

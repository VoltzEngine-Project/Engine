package com.builtbroken.mc.seven.framework.block.listeners;

import com.builtbroken.mc.api.abstraction.entity.IEntityData;
import com.builtbroken.mc.api.IModObject;
import com.builtbroken.mc.api.data.ActionResponse;
import com.builtbroken.mc.api.tile.node.ITileNodeHost;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.framework.block.imp.*;
import com.builtbroken.mc.framework.json.loading.JsonProcessorData;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.lib.data.BlockStateEntry;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * Simplified placement listener for checking if a placement is valid
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 7/14/2017.
 */
public class AdjacentPlacementListener extends TileListener implements IPlacementListener, IBlockListener, IChangeListener
{
    @JsonProcessorData("invert")
    protected boolean invert = false; //TODO implement

    @JsonProcessorData("doBreakCheck")
    protected boolean doBreakCheck = true;

    protected List<BlockStateEntry> blockList = new ArrayList();
    protected List<String> contentIDs = new ArrayList();
    protected ForgeDirection[] supportedDirections = null;

    public final Block block;

    public AdjacentPlacementListener(Block block)
    {
        this.block = block;
    }

    @Override
    public List<String> getListenerKeys()
    {
        List<String> list = new ArrayList();
        list.add("placement");
        list.add("change");
        return list;
    }

    @Override
    public void onBlockChanged()
    {
        if (doBreakCheck && world() != null && isValidTileAtLocation() && this.canBlockStay() == ActionResponse.CANCEL)
        {
            world().unwrap().func_147480_a(xi(), yi(), zi(), true);
        }
    }

    @Override
    public ActionResponse canPlaceAt(IEntityData entity)
    {
        if (entity.isPlayer())
        {
            //Checks if listener is valid
            ItemStack stack  = entity.getRightClickItem();
            if (stack == null || stack.getItemDamage() != metaCheck) //TODO add content ID check on item
            {
                return ActionResponse.IGNORE;
            }
            //Check placement
            if (isPlacementValid())
            {
                return ActionResponse.DO;
            }
            return ActionResponse.CANCEL;
        }
        return ActionResponse.IGNORE;
    }

    @Override
    public ActionResponse canBlockStay()
    {
        //Checks if listener is valid
        if (!isValidTileAtLocation())
        {
            return ActionResponse.IGNORE;
        }
        //Check placement
        if (isPlacementValid())
        {
            return ActionResponse.DO;
        }
        return ActionResponse.CANCEL;
    }

    /**
     * Called to check if the placement of the block will work
     * @return
     */
    protected boolean isPlacementValid()
    {
        //Loops checking for connections
        final Pos center = new Pos(this);
        IBlockAccess access = getBlockAccess();
        for (ForgeDirection direction : supportedDirections == null ? ForgeDirection.VALID_DIRECTIONS : supportedDirections)
        {
            Pos pos = center.add(direction);
            if (isSupportingTile(access, pos))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if tile at the location can support the block
     *
     * @param access
     * @param pos
     * @return
     */
    protected boolean isSupportingTile(IBlockAccess access, Pos pos)
    {
        return doesContainTile(access, pos, blockList, contentIDs);
    }

    /**
     * Checks if the two lists contain the block at the location
     *
     * @param access
     * @param pos
     * @param blockList  - list of blockIDs & meta of tiles
     * @param contentIDs - list of content IDs of tiles
     * @return true if either list contains the tile
     */
    protected boolean doesContainTile(IBlockAccess access, Pos pos, List<BlockStateEntry> blockList, List<String> contentIDs)
    {
        Block block = pos.getBlock(access);
        if (block != null)
        {
            int meta = pos.getBlockMetadata(access);

            if (block != null)
            {
                //Check block and/or meta, loop is the same time as list.contains.... O(n)
                for (BlockStateEntry entry : blockList)
                {
                    if(entry.matches(block, meta)) //TODO pass in location to recycle code
                    {
                        return true;
                    }
                }

                //Check unique content ids TODO merge content ID check into BlockStateEntry
                List<String> ids = new ArrayList();
                TileEntity tile = pos.getTileEntity(access);
                if (tile != null && !tile.isInvalid())
                {
                    if (tile instanceof ITileNodeHost && ((ITileNodeHost) tile).getTileNode() != null)
                    {
                        ids.add((((ITileNodeHost) tile).getTileNode()).modID() + ":" + (((ITileNodeHost) tile).getTileNode()).uniqueContentID());
                    }
                    if (tile instanceof IModObject)
                    {
                        ids.add(((IModObject) tile).modID() + ":" + ((IModObject) tile).uniqueContentID());
                    }
                }
                if (block instanceof IModObject)
                {
                    ids.add(((IModObject) block).modID() + ":" + ((IModObject) block).uniqueContentID());
                }

                for (String id : ids)
                {
                    if (contentIDs.contains(id.toLowerCase()))
                    {
                        return true;
                    }
                }
            }
            return false;
        }
        //Unloaded chunks count as support
        return true;
    }

    @Override
    public boolean isValidForTile()
    {
        return true;
    }

    @JsonProcessorData("sides")
    public void processSides(JsonElement inputElement)
    {
        //TODO add rotation support
        if (inputElement.isJsonArray())
        {
            ArrayList<ForgeDirection> directions = new ArrayList();
            //Loop through elements in array
            for (JsonElement element : inputElement.getAsJsonArray())
            {
                JsonPrimitive primitive = element.getAsJsonPrimitive();
                String value = primitive.getAsString();
                if (value.equalsIgnoreCase("north"))
                {
                    directions.add(ForgeDirection.NORTH);
                }
                else if (value.equalsIgnoreCase("south"))
                {
                    directions.add(ForgeDirection.SOUTH);
                }
                else if (value.equalsIgnoreCase("east"))
                {
                    directions.add(ForgeDirection.EAST);
                }
                else if (value.equalsIgnoreCase("west"))
                {
                    directions.add(ForgeDirection.WEST);
                }
                else if (value.equalsIgnoreCase("up"))
                {
                    directions.add(ForgeDirection.UP);
                }
                else if (value.equalsIgnoreCase("down"))
                {
                    directions.add(ForgeDirection.DOWN);
                }
            }

            this.supportedDirections = directions.toArray(new ForgeDirection[directions.size()]);
        }
    }

    @JsonProcessorData("blocks")
    public void processBlocks(JsonElement inputElement)
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

                        blockList.add(new BlockStateEntry(blockName, meta));
                    }
                    else if (object.has("contentID"))
                    {
                        contentIDs.add(object.getAsJsonPrimitive("contentID").getAsString().toLowerCase());
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
            return "AdjacentPlacementListener[" + block + " >> " + contentUseID + "]@" + hashCode();
        }
        else if (metaCheck != -1)
        {
            return "AdjacentPlacementListener[" + block + "@" + metaCheck + "]@" + hashCode();
        }
        return "AdjacentPlacementListener[" + block + "]@" + hashCode();
    }

    public static class Builder implements ITileEventListenerBuilder
    {
        @Override
        public ITileEventListener createListener(Block block)
        {
            return new AdjacentPlacementListener(block);
        }

        @Override
        public String getListenerKey()
        {
            return "adjacentPlacementListener";
        }
    }
}

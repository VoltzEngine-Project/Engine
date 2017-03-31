package com.builtbroken.mc.lib.world.schematic;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.jlib.type.Pair;
import com.builtbroken.mc.api.ISave;
import com.builtbroken.mc.api.edit.IWorldEdit;
import com.builtbroken.mc.lib.helper.NBTUtility;
import com.builtbroken.mc.imp.transform.vector.Location;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.lib.world.edit.BlockEdit;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * File that represents all the data loaded from a schematic data file
 *
 * @author DarkGuardsman
 */
public class SchematicMap extends Schematic implements ISave
{
    //TODO save the schematics using block names, include a reference sheet to match block names to IDs instead of saving each block as a string

    public static final String BLOCK_LIST_SAVE_NAME = "BlockList";
    public static final String BLOCK_REF_SAVE_NAME = "BlockRef";
    public static final String BLOCK_MAP_SAVE_NAME = "BlockMap";

    private static final LinkedHashMap<String, Block> BLOCK_SAVE_MAP = new LinkedHashMap<>();
    private static final LinkedHashMap<Block, String> BLOCK_SAVE_MAP_REV = new LinkedHashMap<>();
    public Pos schematicSize;
    public Pos schematicCenter;
    public LinkedHashMap<Pos, Pair<Block, Integer>> block_map = new LinkedHashMap<>();
    public boolean init = false;
    protected String name;

    /**
     * Manually saves a block by a set name, overrides vanilla block name saving
     */
    public static void registerSaveBlock(String name, Block block)
    {
        BLOCK_SAVE_MAP.put(name, block);
        BLOCK_SAVE_MAP_REV.put(block, name);
    }

    public void init()
    {
        if (this.schematicSize != null)
        {
            this.init = true;
            this.schematicCenter = new Pos(this.schematicSize.x() / 2, 0, this.schematicSize.z() / 2);
        }
    }

    public void build(Location spot, boolean doWorldCheck)
    {
        if (this.block_map != null)
        {
            List<IWorldEdit> blocksToPlace = new ArrayList();
            this.getBlocksToPlace(spot, blocksToPlace, doWorldCheck, doWorldCheck);
            for (IWorldEdit edit : blocksToPlace)
            {
                edit.place();
            }
        }
    }

    /**
     * Gets all blocks needed to build the schematic at the given location
     *
     * @param spot                 - center point of the schematic
     * @param blockMap             - map of blocks from the schematic file
     * @param checkWorld           - check if blocks are already placed
     * @param checkIfWorldIsLoaded - check if the area is loaded, make sure this is true if
     *                             checkWorld boolean is true as it effects the results if the chunk is unloaded. Setting this
     *                             true will not load the area and is designed to prevent wasting blocks when generating
     *                             buildings using actual blocks
     */
    public void getBlocksToPlace(Location spot, List<IWorldEdit> blockMap, boolean checkWorld, boolean checkIfWorldIsLoaded)
    {
        if (this.block_map != null)
        {
            for (Entry<Pos, Pair<Block, Integer>> entry : this.block_map.entrySet())
            {
                Block block = entry.getValue().left();
                int meta = entry.getValue().right();

                if (block == null || block != Blocks.sponge)
                {
                    if (meta > 15)
                    {
                        meta = 15;
                    }
                    else if (meta < 0)
                    {
                        meta = 0;
                    }
                    Pos setPos = spot.toPos().subtract(this.schematicCenter).add(entry.getKey());
                    if (checkWorld)
                    {
                        if (checkIfWorldIsLoaded)
                        {
                            Chunk chunk = spot.world.getChunkFromBlockCoords(setPos.xi(), setPos.zi());
                            if (!chunk.isChunkLoaded)
                            {
                                continue;
                            }
                        }
                        Block checkID = setPos.getBlock(spot.world);
                        int checkMeta = setPos.getBlockMetadata(spot.world);
                        if (checkID == block && checkMeta == meta)
                        {
                            continue;
                        }
                    }
                    blockMap.add(new BlockEdit(spot.world, setPos).set(block, meta));
                }
            }
        }
    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbt)
    {
        if (!init)
        {
            this.init();
        }
        NBTTagCompound blockNBT = nbt.getCompoundTag(BLOCK_LIST_SAVE_NAME);
        if (this.schematicSize != null)
        {
            nbt.setInteger("sizeX", (int) this.schematicSize.x());
            nbt.setInteger("sizeY", (int) this.schematicSize.y());
            nbt.setInteger("sizeZ", (int) this.schematicSize.z());
        }
        if (this.schematicCenter != null)
        {
            nbt.setInteger("centerX", (int) this.schematicCenter.x());
            nbt.setInteger("centerY", (int) this.schematicCenter.y());
            nbt.setInteger("centerZ", (int) this.schematicCenter.z());
        }
        int i = 0;

        for (Entry<Pos, Pair<Block, Integer>> entry : block_map.entrySet())
        {
            String output = "";
            Block block = entry.getValue().left();
            if (block != null && SchematicMap.BLOCK_SAVE_MAP_REV.containsKey(block))
            {
                output += SchematicMap.BLOCK_SAVE_MAP_REV.get(block);
            }
            else
            {
                output += entry.getValue().left();
            }
            output += ":" + entry.getValue().right();
            output += ":" + (entry.getKey().x()) + ":" + ((int) entry.getKey().y()) + ":" + ((int) entry.getKey().z());
            blockNBT.setString("Block" + i, output);
            i++;
        }
        blockNBT.setInteger("count", i);
        nbt.setTag(BLOCK_LIST_SAVE_NAME, blockNBT);
        return nbt;
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        schematicSize = new Pos(nbt.getInteger("sizeX"), nbt.getInteger("sizeY"), nbt.getInteger("sizeZ"));
        schematicCenter = new Pos(nbt.getInteger("centerX"), nbt.getInteger("centerY"), nbt.getInteger("centerZ"));
        NBTTagCompound blockDataSave = nbt.getCompoundTag(BLOCK_LIST_SAVE_NAME);

        for (int blockCount = 0; blockCount < blockDataSave.getInteger("count"); blockCount++)
        {
            String blockString = blockDataSave.getString("Block" + blockCount);
            String[] blockData = blockString.split(":");
            //int blockID = 0;
            Block block = null;
            int blockMeta = 0;
            Pos blockPostion = new Pos();
            if (blockData != null)
            {
                try
                {
                    if (blockData.length > 0)
                    {
                        if (SchematicMap.BLOCK_SAVE_MAP.containsKey(blockData[0]))
                        {
                            block = SchematicMap.BLOCK_SAVE_MAP.get(blockData[0]);
                        }
                        else
                        {
                            block = Block.getBlockById(Integer.parseInt(blockData[0])); //TODO: Fix up, wrong implementation
                        }

                    }
                    if (blockData.length > 1)
                    {
                        blockMeta = Integer.parseInt(blockData[1]);
                    }
                    int x = 0;
                    int y = 0;
                    int z = 0;
                    if (blockData.length > 2)
                    {
                        x = Integer.parseInt(blockData[2]);
                    }
                    if (blockData.length > 3)
                    {
                        y = Integer.parseInt(blockData[3]);
                    }
                    if (blockData.length > 4)
                    {
                        z = Integer.parseInt(blockData[4]);
                    }
                    blockPostion = new Pos(x, y, z);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                this.block_map.put(blockPostion, new Pair<>(block, blockMeta));
            }
        }

        if (!init)
        {
            this.init();
        }

    }

    public void getFromResourceFolder(String fileName)
    {
        try
        {
            this.load(CompressedStreamTools.readCompressed(SchematicMap.class.getResource("/assets/artillects/schematics/" + fileName + ".dat").openStream()));
            this.name = fileName;
            this.init();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void saveToBaseDirectory(String fileName)
    {
        try
        {
            NBTTagCompound nbt = new NBTTagCompound();
            this.save(nbt);
            NBTUtility.saveData(new File(NBTUtility.getBaseDirectory(), "schematics"), fileName, nbt);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public SchematicMap loadWorldSelection(World world, IPos3D pos, IPos3D pos2)
    {
        int deltaX, deltaY, deltaZ;
        Pos start = new Pos(pos.x() > pos2.x() ? pos2.x() : pos.x(), pos.y() > pos2.y() ? pos2.y() : pos.y(), pos.z() > pos2.z() ? pos2.z() : pos.z());

        SchematicMap sch = new SchematicMap();
        if (pos.x() < pos2.x())
        {
            deltaX = (int) (pos2.x() - pos.x() + 1);
        }
        else
        {
            deltaX = (int) (pos.x() - pos2.x() + 1);
        }
        if (pos.y() < pos2.y())
        {
            deltaY = (int) (pos2.y() - pos.y() + 1);
        }
        else
        {
            deltaY = (int) (pos.y() - pos2.y() + 1);
        }
        if (pos.z() < pos2.z())
        {
            deltaZ = (int) (pos2.z() - pos.z() + 1);
        }
        else
        {
            deltaZ = (int) (pos.z() - pos2.z() + 1);
        }
        sch.schematicSize = new Pos(deltaX, deltaY, deltaZ);
        for (int x = 0; x < deltaX; ++x)
        {
            for (int y = 0; y < deltaY; ++y)
            {
                for (int z = 0; z < deltaZ; ++z)
                {
                    Block block = world.getBlock((int) start.x() + x, (int) start.y() + y, (int) start.z() + z);
                    int blockMeta = world.getBlockMetadata((int) start.x() + x, (int) start.y() + y, (int) start.z() + z);
                    sch.block_map.put(new Pos(x, y, z), new Pair<>(block, blockMeta));
                }
            }
        }
        return sch;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public HashMap<Pos, Pair<Block, Integer>> getStructure(ForgeDirection dir, int size)
    {
        return this.block_map;
    }
}

package com.builtbroken.mc.lib.helper;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.transform.vector.Pos;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by robert on 8/7/2014.
 */
public class BlockUtility
{
	private static HashMap<Block, Float> BLOCK_HARDNESS = new HashMap();
    private static HashMap<Block, Float> BLOCK_RESISTANCE = new HashMap();

    /**
     * Obfuscation names for reflection
     */
    public static final String[] CHUNK_RELIGHT_BLOCK = { "relightBlock", "func_76615_h" };
    public static final String[] CHUNK_PROPOGATE_SKY_LIGHT_OCCLUSION = { "propagateSkylightOcclusion", "func_76595_e" };

    /** Gets the block's resistance without using location data required by the
     * block's getResistance method. Uses a combination of a cache and reflection
     * to get access to the data.
     *
     *
     * @param block - block, can't be null
     * @return
     */
    public static float getBlockResistance(Block block)
    {
        //Get block hardness from cache so we don't need to use reflection every time
        if (BLOCK_RESISTANCE.containsKey(block))
        {
            return BLOCK_RESISTANCE.get(block);
        }

        //Get the block using reflection as the field is private
        try
        {
            Field field = ReflectionUtility.getMCField(Block.class, "blockResistance");
            BLOCK_RESISTANCE.put(block, field.getFloat(block));
            return BLOCK_RESISTANCE.get(block);
        }
        catch (Exception e)
        {
            if(Engine.runningAsDev)
                Engine.instance.logger().catching(e);
            else
                Engine.instance.logger().info("Failed to reflect into Block.class to get block resistance");
        }
        return 0;
    }

    /** Gets the block's hardness without using location data required by the
     * block's getHardness method. Uses a combination of a cache and reflection
     * to get access to the data.
     *
     *
     * @param block - block, if null will return 0
     * @return
     */
	public static float getBlockHardness(Block block)
	{
        //Get block hardness from cache so we don't need to use reflection every time
        if (BLOCK_HARDNESS.containsKey(block))
        {
            return BLOCK_HARDNESS.get(block);
        }

        //Get the block using reflection as the field is private
        try
        {
            Field field = ReflectionUtility.getMCField(Block.class, "blockHardness");
            BLOCK_HARDNESS.put(block, field.getFloat(block));
            return BLOCK_HARDNESS.get(block);
        }
        catch (Exception e)
        {
            if(Engine.runningAsDev)
                Engine.instance.logger().catching(e);
            else
                Engine.instance.logger().info("Failed to reflect into Block.class to get block hardness");
        }
        return 0;
	}

    /** Gets the hardness value of the block represented by
     * the itemstack. Doesn't check if the item is not an ItemBlock
     * @param stack - stack
     * @return hardness value
     */
	public static float getBlockHardness(ItemStack stack)
	{
        return getBlockHardness(Block.getBlockFromItem(stack.getItem()));
	}

    /**
     * Sets a block in a sneaky way to bypass some restraints.
     */
    public static void setBlockSneaky(World world, Pos position, Block block, int metadata, TileEntity tileEntity)
    {
        if (block != null && world != null)
        {
            Chunk chunk = world.getChunkFromChunkCoords(position.xi() >> 4, position.zi() >> 4);
            Pos chunkPosition = new Pos(position.xi() & 0xF, position.yi() & 0xF, position.zi() & 0xF);

            int heightMapIndex = chunkPosition.zi() << 4 | chunkPosition.xi();

            if (position.yi() >= chunk.precipitationHeightMap[heightMapIndex] - 1)
            {
                chunk.precipitationHeightMap[heightMapIndex] = -999;
            }

            int heightMapValue = chunk.heightMap[heightMapIndex];

            world.removeTileEntity(position.xi(), position.yi(), position.zi());

            ExtendedBlockStorage extendedBlockStorage = chunk.getBlockStorageArray()[position.yi() >> 4];

            if (extendedBlockStorage == null)
            {
                extendedBlockStorage = new ExtendedBlockStorage((position.yi() >> 4) << 4, !world.provider.hasNoSky);

                chunk.getBlockStorageArray()[position.yi() >> 4] = extendedBlockStorage;
            }

            extendedBlockStorage.func_150818_a(chunkPosition.xi(), chunkPosition.yi(), chunkPosition.zi(), block);
            extendedBlockStorage.setExtBlockMetadata(chunkPosition.xi(), chunkPosition.yi(), chunkPosition.zi(), metadata);

            if (position.yi() >= heightMapValue)
            {
                chunk.generateSkylightMap();
            }
            else
            {
                //chunk.getBlockLightOpacity(chunkPosition.xi(), position.yi(), chunkPosition.zi())
                if (chunk.getBlockLightValue(chunkPosition.xi(), position.yi(), chunkPosition.zi(), 0) > 0)
                {
                    if (position.yi() >= heightMapValue)
                    {
                        relightBlock(chunk, chunkPosition.clone().add(new Pos(0, 1, 0)));
                    }
                }
                else if (position.yi() == heightMapValue - 1)
                {
                    relightBlock(chunk, chunkPosition);
                }

                propagateSkylightOcclusion(chunk, chunkPosition);
            }

            chunk.isModified = true;
            //updateAllLightTypes
            world.func_147451_t(position.xi(), position.yi(), position.zi());

            if (tileEntity != null)
            {
                world.setTileEntity(position.xi(), position.yi(), position.zi(), tileEntity);
            }

            world.markBlockForUpdate(position.xi(), position.yi(), position.zi());
        }
    }

    /**
     * Re-lights the block in a specific position.
     *
     * @param chunk
     * @param position
     */
    public static void relightBlock(Chunk chunk, Pos position)
    {
        try
        {
            Method m = ReflectionHelper.findMethod(Chunk.class, null, CHUNK_RELIGHT_BLOCK, int.class, int.class, int.class);
            m.invoke(chunk, position.xi(), position.yi(), position.zi());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Propogates skylight occlusion in a specific chunk's position.
     *
     * @param chunk
     * @param position
     */
    public static void propagateSkylightOcclusion(Chunk chunk, Pos position)
    {
        try
        {
            Method m = ReflectionHelper.findMethod(Chunk.class, null, CHUNK_PROPOGATE_SKY_LIGHT_OCCLUSION, int.class, int.class);
            m.invoke(chunk, position.xi(), position.zi());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}

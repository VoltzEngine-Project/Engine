package resonant.lib.utility;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import resonant.engine.References;
import resonant.engine.ResonantEngine;
import resonant.lib.type.Pair;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by robert on 8/7/2014.
 */
public class BlockUtility
{
	private static HashMap<Block, Float> BLOCK_HARDNESS = new HashMap();
    private static HashMap<Block, Float> BLOCK_RESISTANCE = new HashMap();

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
        catch (IllegalAccessException e)
        {
            if(ResonantEngine.runningAsDev)
                References.LOGGER.catching(e);
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
        catch (IllegalAccessException e)
        {
            if(ResonantEngine.runningAsDev)
                References.LOGGER.catching(e);
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

}

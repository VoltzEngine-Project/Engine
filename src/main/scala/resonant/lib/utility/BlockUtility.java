package resonant.lib.utility;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import resonant.lib.type.Pair;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by robert on 8/7/2014.
 */
public class BlockUtility
{
	public static HashMap<Pair<Block, Integer>, Float> BLOCK_HARDNESS = new HashMap<Pair<Block, Integer>, Float>();

	public static float getBlockHardness(Block block)
	{
		return getBlockHardness(block, 0);
	}

	public static float getBlockHardness(Block block, int meta)
	{
		try
		{
			Pair<Block, Integer> pair = new Pair<Block, Integer>(block, meta);
			if (BLOCK_HARDNESS.containsKey(pair))
			{
				return BLOCK_HARDNESS.get(pair);
			}
			if (block != null)
			{
				Field field = block.getClass().getDeclaredField("blockHardness");
				if (field == null)
				{
					field = block.getClass().getDeclaredField("field_149782_v");
				}
				BLOCK_HARDNESS.put(pair, field.getFloat(block));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 1;
	}

	public static float getBlockHardness(ItemStack stack)
	{
		if (stack != null)
		{
			return getBlockHardness(Block.getBlockFromItem(stack.getItem()), stack.getItemDamage());
		}
		return 1;
	}

}

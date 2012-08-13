package universalelectricity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.Item;

/**
 * This class is for managing and allowing different UE mods to use each other's items
 * and blocks without an API.
 * @author Calclavia
 *
 */
public class CommonManager 
{
	private static final List<Item> items = new ArrayList<Item>();
	private static final List<Block> blocks = new ArrayList<Block>();
	
	/**
	 * Gets an item by it's item name.
	 */
	public static Item getItemByName(String name)
	{		
		for(Item item : items)
		{
			if(item.getItemName() == name)
			{
				return item;
			}
		}
		return null;
	}
	
	/**
	 * Adds a common item you would like to share with other UE mods.
	 * The other mods will be able to have access to your item object.
	 */
	public static void addCommonItem(Item item)
	{
		if(!items.contains(item))
		{
			items.add(item);
		}
	}
	
	public static Block getBlockByName(String name)
	{		
		for(Block block : blocks)
		{
			if(block.getBlockName() == name)
			{
				return block;
			}
		}
		return null;
	}
	
	public static void addCommonItem(Block block)
	{
		if(!blocks.contains(block))
		{
			blocks.add(block);
		}
	}
}

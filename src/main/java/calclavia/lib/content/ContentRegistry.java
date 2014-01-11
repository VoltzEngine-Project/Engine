package calclavia.lib.content;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.Configuration;
import calclavia.lib.content.IExtraInfo.IExtraBlockInfo;
import calclavia.lib.content.IExtraInfo.IExtraItemInfo;

import com.builtbroken.common.Pair;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Handler to make registering all parts of a mod's objects that are loaded into the game by forge.
 * 
 * @author DarkGuardsman, Calclavia
 */
public class ContentRegistry
{
	public final HashMap<Block, String> blocks = new HashMap<Block, String>();
	public final HashMap<Item, String> items = new HashMap<Item, String>();

	@SidedProxy(clientSide = "calclavia.lib.content.ClientRegistryProxy", serverSide = "calclavia.lib.content.CommonRegistryProxy")
	public static CommonRegistryProxy proxy;

	private final Configuration config;
	private final String modID;

	public ContentRegistry(Configuration config, String modID)
	{
		this.config = config;
		this.modID = modID;
	}

	public Block createBlock(Class<? extends Block> blockClass)
	{
		return createBlock(blockClass, null);
	}

	public Block createTile(Class<? extends Block> blockClass, Class<? extends TileEntity> tileClass)
	{
		return createBlock(blockClass, null, tileClass);
	}

	public Block createBlock(Class<? extends Block> blockClass, Class<? extends ItemBlock> itemClass)
	{
		return createBlock(blockClass, itemClass, null);
	}

	public Block createBlock(Class<? extends Block> blockClass, Class<? extends ItemBlock> itemClass, Class<? extends TileEntity> tileClass)
	{
		return createBlock(blockClass.getSimpleName(), blockClass, itemClass, tileClass);
	}

	public Block createBlock(String name, Class<? extends Block> blockClass, Class<? extends ItemBlock> itemClass, Class<? extends TileEntity> tileClass)
	{
		return createBlock(name, blockClass, itemClass, tileClass, true);
	}

	/**
	 * Generates a block using reflection, and runs it threw config checks
	 * 
	 * @param name - name to register the block with
	 * @param modID - mod id to register the block to
	 * @param blockClass - class to generate the instance from
	 * @param canDisable - should we allow the player the option to disable the block
	 * @param itemClass - item block to register with the block
	 */
	public Block createBlock(String name, Class<? extends Block> blockClass, Class<? extends ItemBlock> itemClass, Class<? extends TileEntity> tileClass, boolean canDisable)
	{
		Block block = null;

		if (blockClass != null && (!canDisable || canDisable && config.get("Enabled List", "Enabled " + name, true).getBoolean(true)))
		{
			// TODO redesign to catch blockID conflict
			try
			{
				block = blockClass.newInstance();
			}
			catch (IllegalArgumentException e)
			{
				throw e;
			}
			catch (Exception e)
			{
				System.out.println("Warning: Block [" + name + "] failed to be created!");
				e.printStackTrace();
			}

			if (block != null)
			{
				blocks.put(block, name);
				proxy.registerBlock(block, itemClass, name, modID);
				finishCreation(block, tileClass);
			}
		}

		return block;
	}

	/**
	 * Finishes the creation of the block loading config files and tile entities
	 * 
	 * @param tileClass
	 */
	public void finishCreation(Block block, Class<? extends TileEntity> tileClass)
	{
		if (block instanceof IExtraInfo)
		{
			if (((IExtraInfo) block).hasExtraConfigs())
			{
				Configuration extraBlockConfig = new Configuration(new File(Loader.instance().getConfigDir(), "objects/blocks/" + block.getUnlocalizedName() + ".cfg"));
				extraBlockConfig.load();
				((IExtraInfo) block).loadExtraConfigs(extraBlockConfig);
				extraBlockConfig.save();
			}
			if (block instanceof IExtraBlockInfo)
			{
				((IExtraBlockInfo) block).loadOreNames();
				Set<Pair<String, Class<? extends TileEntity>>> tileListNew = new HashSet<Pair<String, Class<? extends TileEntity>>>();
				((IExtraBlockInfo) block).getTileEntities(block.blockID, tileListNew);
				for (Pair<String, Class<? extends TileEntity>> par : tileListNew)
				{
					proxy.regiserTileEntity(par.left(), par.right());
				}
			}
		}

		if (tileClass != null)
		{
			proxy.regiserTileEntity(block.getUnlocalizedName(), tileClass);
		}
	}

	/**
	 * Method to get block via name
	 * 
	 * @param blockName
	 * @return Block requested
	 */
	public Block getBlock(String blockName)
	{
		for (Entry<Block, String> entry : blocks.entrySet())
		{
			String name = entry.getKey().getUnlocalizedName().replace("tile.", "");
			if (name.equalsIgnoreCase(blockName))
			{
				return entry.getKey();
			}
		}
		return null;
	}

	/**
	 * Creates a new item using reflection as well runs it threw some check to activate any
	 * interface methods
	 * 
	 * @param name - name to register the item with
	 * @param modid - mods that the item comes from
	 * @param clazz - item class
	 * @param canDisable - can a user disable this item
	 * @return the new item
	 */
	public Item createNewItem(String name, Class<? extends Item> clazz, boolean canDisable)
	{
		Item item = null;
		if (clazz != null && (!canDisable || canDisable && config.get("Enabled_List", "Enabled_" + name, true).getBoolean(true)))
		{
			try
			{
				item = clazz.newInstance();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			if (item != null)
			{
				items.put(item, name);
				GameRegistry.registerItem(item, name, modID);
				if (item instanceof IExtraInfo)
				{
					if (((IExtraInfo) item).hasExtraConfigs())
					{
						Configuration extraBlockConfig = new Configuration(new File(Loader.instance().getConfigDir(), "objects/items/" + item.getUnlocalizedName() + ".cfg"));
						extraBlockConfig.load();
						((IExtraInfo) item).loadExtraConfigs(extraBlockConfig);
						extraBlockConfig.save();
					}
					if (item instanceof IExtraItemInfo)
					{
						((IExtraItemInfo) item).loadOreNames();
					}
				}
			}
		}
		return item;
	}
}

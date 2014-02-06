package calclavia.lib.content;

import java.util.Map.Entry;
import java.util.WeakHashMap;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.Configuration;
import calclavia.lib.utility.LanguageUtility;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;

/**
 * Handler to make registering all parts of a mod's objects that are loaded into the game by forge.
 *
 * @author DarkGuardsman, Calclavia
 */
public class ContentRegistry
{
	public final WeakHashMap<Block, String> blocks = new WeakHashMap<Block, String>();
	public final WeakHashMap<Item, String> items = new WeakHashMap<Item, String>();

	@SidedProxy(clientSide = "calclavia.lib.content.ClientRegistryProxy", serverSide = "calclavia.lib.content.CommonRegistryProxy")
	public static CommonRegistryProxy proxy;

	private final Configuration config;
	private final String modID;
	private IDManager idManager;

	private String modPrefix;
	private CreativeTabs defaultTab;

	/** Custom unique packet IDs for the mod to use. */
	private int packetID = 0;

	public ContentRegistry(Configuration config, IDManager idManager, String modID)
	{
		this.config = config;
		this.modID = modID;
		this.idManager = idManager;
	}

	public ContentRegistry setPrefix(String modPrefix)
	{
		this.modPrefix = modPrefix;
		return this;
	}

	public ContentRegistry setTab(CreativeTabs defaultTab)
	{
		this.defaultTab = defaultTab;
		return this;
	}

	public int getNextPacketID()
	{
		return ++packetID;
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
		return createBlock(LanguageUtility.decapitalizeFirst(blockClass.getSimpleName().replace("Block", "")), blockClass, itemClass, tileClass);
	}

	public Block createBlock(String name, Class<? extends Block> blockClass, Class<? extends ItemBlock> itemClass, Class<? extends TileEntity> tileClass)
	{
		return createBlock(name, blockClass, itemClass, tileClass, true);
	}

	/**
	 * Generates a block using reflection, and runs it threw config checks
	 *
	 * @param name - name to register the block with
	 * @param tileClass - the tile class to register this block to
	 * @param blockClass - class to generate the instance from
	 * @param canDisable - should we allow the player the option to disable the block
	 * @param itemClass - item block to register with the block
	 */
	public Block createBlock(String name, Class<? extends Block> blockClass, Class<? extends ItemBlock> itemClass, Class<? extends TileEntity> tileClass, boolean canDisable)
	{
		Block block = null;

		if (blockClass != null && (!canDisable || canDisable && config.get("Enabled_List", "Enabled " + name, true).getBoolean(true)))
		{
			try
			{
				int assignedID = idManager.getNextBlockID();
				block = blockClass.getConstructor(Integer.TYPE).newInstance(config.getBlock(name, assignedID).getInt(assignedID));

				if (block != null)
				{
					if (modPrefix != null)
					{
						block.setUnlocalizedName(modPrefix + name);

						if (ReflectionHelper.getPrivateValue(Block.class, block, "textureName", "field_111026_f") == null)
							block.setTextureName(modPrefix + name);
					}

					if (defaultTab != null)
						block.setCreativeTab(defaultTab);

					blocks.put(block, name);
					proxy.registerBlock(block, itemClass, name, modID);
					finishCreation(block, tileClass);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new RuntimeException("Block [" + name + "] failed to be created: " + e.getLocalizedMessage());
			}
		}

		return block;
	}

	/**
	 * Finishes the creation of the block loading config files and tile entities
	 *
	 * @param tileClass
	 * @throws ClassNotFoundException 
	 */
	public void finishCreation(Block block, Class<? extends TileEntity> tileClass) throws ClassNotFoundException
	{
		BlockInfo blockInfo = block.getClass().getAnnotation(BlockInfo.class);

		if (blockInfo != null)
		{
			for (String string : blockInfo.tileEntity())
			{
				Class clazz = Class.forName(string);
				proxy.registerTileEntity(clazz.getName(), clazz);
			}

		}
        //TODO Remove this and transfer to @BlockInfo
		if (tileClass != null)
		{
			proxy.registerTileEntity(block.getUnlocalizedName(), tileClass);
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

	public Item createItem(Class<? extends Item> clazz)
	{
		return createItem(LanguageUtility.decapitalizeFirst(clazz.getSimpleName().replace("Item", "")), clazz, true);
	}

	public Item createItem(String name, Class<? extends Item> clazz)
	{
		return createItem(name, clazz, true);
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
	public Item createItem(String name, Class<? extends Item> clazz, boolean canDisable)
	{
		Item item = null;
		if (clazz != null && (!canDisable || canDisable && config.get("Enabled_List", "Enabled_" + name, true).getBoolean(true)))
		{
			try
			{
				int assignedID = idManager.getNextItemID();
				item = clazz.getConstructor(Integer.TYPE).newInstance(config.getItem(name, assignedID).getInt(assignedID));

				if (item != null)
				{
					if (modPrefix != null)
					{
						item.setUnlocalizedName(modPrefix + name);

						if (ReflectionHelper.getPrivateValue(Item.class, item, "iconString", "field_111218_cA") == null)
							item.setTextureName(modPrefix + name);
					}

					if (defaultTab != null)
						item.setCreativeTab(defaultTab);

					items.put(item, name);
					GameRegistry.registerItem(item, name, modID);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new RuntimeException("Item [" + name + "] failed to be created: " + e.getLocalizedMessage());
			}
		}
		return item;
	}
}

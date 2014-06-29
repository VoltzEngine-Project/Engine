package resonant.content;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import resonant.content.prefab.scala.render.ISimpleItemRenderer;
import resonant.content.spatial.block.SpatialBlock;
import resonant.content.wrapper.BlockDummy;
import resonant.content.wrapper.ItemRenderHandler;
import resonant.lib.utility.LanguageUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * Handler to make registering all parts of a mod's objects that are loaded into the game by forge.
 *
 * @author DarkGuardsman, Calclavia
 */
public class ModManager
{
	@SidedProxy(clientSide = "resonant.content.ClientRegistryProxy", serverSide = "resonant.content.CommonRegistryProxy")
	public static CommonRegistryProxy proxy;

	public final WeakHashMap<Block, String> blocks = new WeakHashMap();
	public final WeakHashMap<Item, String> items = new WeakHashMap();
	private final Configuration config;
	private final String modID;

	private String modPrefix;
	private CreativeTabs defaultTab;

	/**
	 * Custom unique packet IDs for the mod to use.
	 */
	private int packetID = 0;

	public ModManager(Configuration config, String modID)
	{
		this.config = config;
		this.modID = modID;
	}

	public ModManager setPrefix(String modPrefix)
	{
		this.modPrefix = modPrefix;
		return this;
	}

	public ModManager setTab(CreativeTabs defaultTab)
	{
		this.defaultTab = defaultTab;
		return this;
	}

	public int getNextPacketID()
	{
		return ++packetID;
	}

	/**
	 * New SpatialBlocks system.
	 */
	public BlockDummy newBlock(Class<? extends SpatialBlock> spatialClass, Object... args)
	{
		try
		{
			SpatialBlock spatial;

			if (args != null && args.length > 0)
			{
				List<Class> paramTypes = new ArrayList();

				for (Object arg : args)
				{
					paramTypes.add(arg.getClass());
				}

				spatial = spatialClass.getConstructor(paramTypes.toArray(new Class[0])).newInstance();
			}
			else
			{
				spatial = spatialClass.newInstance();
			}

			final String name = spatial.name();

			BlockDummy block = new BlockDummy(modPrefix, defaultTab, spatial);
			spatial.setBlock(block);

			blocks.put(block, name);
			proxy.registerBlock(block, spatial.itemBlock(), name, modID);

			spatial.onInstantiate();

			if (spatial instanceof ISimpleItemRenderer)
			{
				ItemRenderHandler.register(new ItemStack(block).getItem(), (ISimpleItemRenderer) spatial);
			}

			if (spatial.tile() != null)
			{
				proxy.registerTileEntity(name, spatial.tile().getClass());

				if (!spatial.normalRender())
				{
					proxy.registerDummyRenderer(spatial.tile().getClass());
				}
			}

			return block;
		}
		catch (Exception e)
		{
			throw new RuntimeException("Block [" + spatialClass.getSimpleName() + "] failed to be created:", e);
		}
	}

	/**
	 * @param tileClass
	 * @throws ClassNotFoundException
	 * @Deprecated public Block createBlock(Class<? extends Block> blockClass)
	 * {
	 * return createBlock(blockClass, ItemBlockTooltip.class);
	 * }
	 * @Deprecated public Block createTile(Class<? extends Block> blockClass, Class<? extends TileEntity> tileClass)
	 * {
	 * return createBlock(blockClass, ItemBlockTooltip.class, tileClass);
	 * }
	 * @Deprecated public Block createBlock(Class<? extends Block> blockClass, Class<? extends ItemBlock> itemClass)
	 * {
	 * return createBlock(blockClass, itemClass, null);
	 * }
	 * @Deprecated public Block createBlock(Class<? extends Block> blockClass, Class<? extends ItemBlock> itemClass, Class<? extends TileEntity> tileClass)
	 * {
	 * return createBlock(LanguageUtility.decapitalizeFirst(blockClass.getSimpleName().replace("Block", "")), blockClass, itemClass, tileClass);
	 * }
	 * @Deprecated public Block createBlock(String name, Class<? extends Block> blockClass, Class<? extends ItemBlock> itemClass, Class<? extends TileEntity> tileClass)
	 * {
	 * return createBlock(name, blockClass, itemClass, tileClass, false);
	 * }
	 * <p/>
	 * /**
	 * Generates a block using reflection, and runs it threw config checks
	 * @Deprecated public Block createBlock(String name, Class<? extends Block> blockClass, Class<? extends ItemBlock> itemClass, Class<? extends TileEntity> tileClass, boolean canDisable)
	 * {
	 * Block block = null;
	 * <p/>
	 * if (blockClass != null && (!canDisable || (canDisable && config.get("Enabled_List", "Enabled " + name, true).getBoolean(true))))
	 * {
	 * try
	 * {
	 * <p/>
	 * //                int assignedID = idManager.getNextBlockID();
	 * //                int actualID = config.getBlock(name, assignedID).getInt(assignedID);
	 * block = blockClass.getConstructor().newInstance();
	 * <p/>
	 * if (block != null)
	 * {
	 * if (modPrefix != null)
	 * {
	 * block.setBlockName(modPrefix + name);
	 * <p/>
	 * if (ReflectionHelper.getPrivateValue(Block.class, block, "textureName", "field_111026_f") == null)
	 * {
	 * block.setBlockTextureName(modPrefix + name);
	 * }
	 * }
	 * <p/>
	 * if (defaultTab != null)
	 * {
	 * block.setCreativeTab(defaultTab);
	 * }
	 * <p/>
	 * blocks.put(block, name);
	 * proxy.registerBlock(block, itemClass, name, modID);
	 * finishCreation(block, tileClass);
	 * }
	 * }
	 * catch (IllegalArgumentException e)
	 * {
	 * throw e;
	 * }
	 * catch (Exception e)
	 * {
	 * throw new RuntimeException("Block [" + name + "] failed to be created:", e);
	 * }
	 * }
	 * <p/>
	 * return block;
	 * }
	 * <p/>
	 * /**
	 * Finishes the creation of the block loading config files and tile entities
	 * @Deprecated public void finishCreation(Block block, Class<? extends TileEntity> tileClass) throws ClassNotFoundException
	 * {
	 * BlockInfo blockInfo = block.getClass().getAnnotation(BlockInfo.class);
	 * <p/>
	 * if (blockInfo != null)
	 * {
	 * for (String string : blockInfo.tileEntity())
	 * {
	 * Class clazz = Class.forName(string);
	 * proxy.registerTileEntity(clazz.getName(), clazz);
	 * }
	 * <p/>
	 * }
	 * // TODO Remove this and transfer to @BlockInfo
	 * if (tileClass != null)
	 * {
	 * proxy.registerTileEntity(block.getUnlocalizedName(), tileClass);
	 * }
	 * }
	 */

	public Item newItem(Class<? extends Item> clazz, Object... args)
	{
		return newItem(LanguageUtility.decapitalizeFirst(clazz.getSimpleName().replace("Item", "")), clazz, args);
	}

	/**
	 * Creates a new item using reflection as well runs it threw some check to activate any
	 * interface methods
	 *
	 * @param name  - name to register the item with //@param modid - mods that the item comes from
	 * @param clazz - item class
	 * @return the new item
	 */
	public <C extends Item> C newItem(String name, Class<C> clazz, Object... args)
	{

		try
		{
			Item item;

			if (args != null && args.length > 0)
			{
				List<Class> paramTypes = new ArrayList();

				for (Object arg : args)
				{
					paramTypes.add(arg.getClass());
				}

				item = clazz.getConstructor(paramTypes.toArray(new Class[0])).newInstance();
			}
			else
			{
				item = clazz.getConstructor().newInstance();
			}

			if (item != null)
			{
				if (modPrefix != null)
				{
					item.setUnlocalizedName(modPrefix + name);

					if (ReflectionHelper.getPrivateValue(Item.class, item, "iconString", "field_111218_cA") == null)
					{
						item.setTextureName(modPrefix + name);
					}
				}

				if (defaultTab != null)
				{
					item.setCreativeTab(defaultTab);
				}

				items.put(item, name);
				GameRegistry.registerItem(item, name, modID);
			}

			return (C) item;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException("Item [" + name + "] failed to be created: " + e.getLocalizedMessage(), e.fillInStackTrace());
		}
	}
}

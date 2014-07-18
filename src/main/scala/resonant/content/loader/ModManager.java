package resonant.content.loader;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import resonant.content.CommonRegistryProxy;
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

	private String modPrefix;
	private CreativeTabs defaultTab;

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

			return newBlock(spatial);
		}
		catch (Exception e)
		{
			throw new RuntimeException("Block [" + spatialClass.getSimpleName() + "] failed to be created:", e);
		}
	}

	public BlockDummy newBlock(SpatialBlock spatial)
	{
		return newBlock(spatial.name(), spatial);
	}

	public BlockDummy newBlock(String name, SpatialBlock spatial)
	{
		BlockDummy block = new BlockDummy(modPrefix, defaultTab, spatial);
		spatial.setBlock(block);

		blocks.put(block, name);
		GameRegistry.registerBlock(block, spatial.itemBlock(), name);

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

	public <C extends Block> C newBlock(String name, C block)
	{
		block.setBlockName(modPrefix + name);
		block.setBlockTextureName(modPrefix + name);
		GameRegistry.registerBlock(block, name);
		return block;
	}

	public <C extends Item> C newItem(Class<C> clazz, Object... args)
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

			return (C) newItem(name, item);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException("Item [" + name + "] failed to be created: " + e.getLocalizedMessage(), e.fillInStackTrace());
		}
	}

	public Item newItem(Item item)
	{
		return newItem(LanguageUtility.decapitalizeFirst(item.getClass().getSimpleName().replace("Item", "")), item);
	}

	public Item newItem(String name, Item item)
	{
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
			GameRegistry.registerItem(item, name);
		}

		return item;
	}
}

package calclavia.components;

import java.lang.reflect.Field;
import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import calclavia.lib.Calclavia;
import calclavia.lib.ore.OreGenBase;
import calclavia.lib.ore.OreGenReplaceStone;
import calclavia.lib.ore.OreGenerator;
import calclavia.lib.recipe.RecipeUtility;
import calclavia.lib.utility.LanguageUtility;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;

/**
 * Basic Components
 * 
 * @author Calclavia
 * 
 */
@Mod(modid = CalclaviaComponents.ID, name = Calclavia.NAME, version = CalclaviaComponents.VERSION, dependencies = "required-after:UniversalElectricity")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class CalclaviaComponents
{
	public static final String ID = "CalclaviaCore";
	public static final String MAJOR_VERSION = "@MAJOR@";
	public static final String MINOR_VERSION = "@MINOR@";
	public static final String REVISION_VERSION = "@REVIS@";
	public static final String BUILD_VERSION = "@BUILD@";
	public static final String VERSION = MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION_VERSION;

	@Mod.Metadata(ID)
	public static ModMetadata metadata;

	public static final String RESOURCE_PATH = "/assets/calclavia/";
	public static final String TEXTURE_DIRECTORY = RESOURCE_PATH + "textures/";
	public static final String BLOCK_TEXTURE_DIRECTORY = TEXTURE_DIRECTORY + "blocks/";
	public static final String ITEM_TEXTURE_DIRECTORY = TEXTURE_DIRECTORY + "items/";
	public static final String MODEL_TEXTURE_DIRECTORY = TEXTURE_DIRECTORY + "models/";

	public static final String DOMAIN = "calclavia";
	public static final String PREFIX = DOMAIN + ":";

	public static final String LANGUAGE_PATH = RESOURCE_PATH + "languages/";
	public static final String[] LANGUAGES_SUPPORTED = new String[] { "en_US", "zh_CN", "es_ES", "it_IT", "nl_NL", "de_DE", "tr_TR", "ru_RU" };

	/**
	 * Auto-incrementing configuration IDs. Use this to make sure no config ID is the same.
	 */
	public static final int BLOCK_ID_PREFIX = 3970;
	public static final int ITEM_ID_PREFIX = 13970;

	/**
	 * Blocks
	 */
	public static Block blockOreCopper;
	public static final int idBlockOreCopper = BLOCK_ID_PREFIX + 0;

	public static Block blockOreTin;
	public static final int idBlockOreTin = BLOCK_ID_PREFIX + 1;

	/**
	 * Items
	 */
	public static Item itemWrench;
	public static final int idItemWrench = ITEM_ID_PREFIX + 2;

	public static Item itemMotor;
	public static final int idItemMotor = ITEM_ID_PREFIX + 3;

	public static Item itemCircuitBasic;
	public static final int idItemCircuitBasic = ITEM_ID_PREFIX + 4;

	public static Item itemCircuitAdvanced;
	public static final int idItemCircuitAdvanced = ITEM_ID_PREFIX + 5;

	public static Item itemCircuitElite;
	public static final int idItemCircuitElite = ITEM_ID_PREFIX + 6;

	public static Item itemPlateCopper;
	public static final int idItemPlateCopper = ITEM_ID_PREFIX + 7;

	public static Item itemPlateTin;
	public static final int idItemPlateTin = ITEM_ID_PREFIX + 8;

	public static Item itemPlateBronze;
	public static final int idItemPlateBronze = ITEM_ID_PREFIX + 9;

	public static Item itemPlateSteel;
	public static final int idItemPlateSteel = ITEM_ID_PREFIX + 10;

	public static Item itemPlateIron;
	public static final int idItemPlateIron = ITEM_ID_PREFIX + 11;

	public static Item itemPlateGold;
	public static final int idItemPlateGold = ITEM_ID_PREFIX + 12;

	public static Item itemIngotCopper;
	public static final int idItemIngotCopper = ITEM_ID_PREFIX + 13;

	public static Item itemIngotTin;
	public static final int idItemIngotTin = ITEM_ID_PREFIX + 14;

	public static Item itemIngotSteel;
	public static final int idItemIngotSteel = ITEM_ID_PREFIX + 15;

	public static Item itemIngotBronze;
	public static final int idItemIngotBronze = ITEM_ID_PREFIX + 16;

	public static Item itemDustSteel;
	public static final int idItemDustSteel = ITEM_ID_PREFIX + 17;

	public static Item itemDustBronze;
	public static final int idItemDustBronze = ITEM_ID_PREFIX + 18;

	public static OreGenBase generationOreCopper, generationOreTin;
	public static String CHANNEL = "calclaviacore";

	@EventHandler
	public void init(FMLInitializationEvent evt)
	{
		Calclavia.CONFIGURATION.load();

		boolean defaultDoLoad = !(Loader.isModLoaded("ThermalExpansion") || Loader.isModLoaded("IC2"));

		if (Calclavia.CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "Enable Calclavia Core Resources", defaultDoLoad).getBoolean(defaultDoLoad))
		{
			ComponentRegistry.registerAll();
			ComponentRegistry.register("itemIngotCopper");
			ComponentRegistry.register("itemIngotTin");

			ComponentRegistry.register("blockOreCopper");
			ComponentRegistry.register("blockOreTin");

			ComponentRegistry.register("itemIngotSteel");
			ComponentRegistry.register("itemDustSteel");
			ComponentRegistry.register("itemPlateSteel");

			ComponentRegistry.register("itemIngotBronze");
			ComponentRegistry.register("itemDustBronze");
			ComponentRegistry.register("itemPlateBronze");

			ComponentRegistry.register("itemPlateCopper");
			ComponentRegistry.register("itemPlateTin");
			ComponentRegistry.register("itemPlateIron");
			ComponentRegistry.register("itemPlateGold");
		}

		ComponentRegistry.register("itemCircuitBasic");
		ComponentRegistry.register("itemCircuitAdvanced");
		ComponentRegistry.register("itemCircuitElite");

		ComponentRegistry.register("itemMotor");
		ComponentRegistry.register("itemWrench");

		Calclavia.LOGGER.fine("Attempting to load " + ComponentRegistry.requests.size() + " items.");

		for (String request : ComponentRegistry.requests)
		{
			if (request.contains("block"))
			{
				requestBlock(request, 0);
			}
			else if (request.contains("item"))
			{
				requestItem(request, 0);
			}
			else
			{
				Calclavia.LOGGER.severe("Failed to load Calclavia Core item: " + request);
			}
		}

		Calclavia.CONFIGURATION.save();

		Calclavia.LOGGER.fine("Loaded: " + LanguageUtility.loadLanguages(CalclaviaComponents.LANGUAGE_PATH, CalclaviaComponents.LANGUAGES_SUPPORTED) + " Languages.");

		CalclaviaComponents.metadata.modId = ID;
		CalclaviaComponents.metadata.name = ID;
		CalclaviaComponents.metadata.description = "Calclavia Core is an FML Plugin to provide core functionalities in order to run Calclavia's mods.";
		CalclaviaComponents.metadata.url = "http://calclavia.com/calclavia-core";
		CalclaviaComponents.metadata.version = VERSION + BUILD_VERSION;
		CalclaviaComponents.metadata.authorList = Arrays.asList(new String[] { "Calclavia" });
		CalclaviaComponents.metadata.autogenerated = false;
	}

	/**
	 * Call all of this in Init stage. Use "requestItem" or "requestBlock" instead to make it so
	 * then if another mod adds in a item with the same name, it will use that mod's item instead.
	 * 
	 * Creates a specific Basic Component item/Block.
	 * 
	 * If you want correct recipes, make sure you register required items in the following order:
	 * 
	 * Ingot, Ores, Dust, Plate, Copper Wire, Circuits, Motor, Wrench
	 * 
	 * @param name - Name of the item: e.g ingotCopper, ingotSteel
	 * @param id - The specified ID of the item. Use 0 for a default value to be used.
	 * @return The Item/Block class.
	 */
	public static Item requireItem(String fieldName, int id)
	{
		try
		{
			String name = fieldName.replace("item", "");
			name = Character.toLowerCase(name.charAt(0)) + name.substring(1);

			Field field = ReflectionHelper.findField(CalclaviaComponents.class, fieldName);
			Item f = (Item) field.get(null);

			// Grabs the default ID.
			Field idField = ReflectionHelper.findField(CalclaviaComponents.class, "id" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1));
			id = id <= 0 ? (Integer) idField.get(null) : id;

			if (f == null)
			{
				if (name.contains("ingot"))
				{
					ItemIngot ingot = new ItemIngot(name, id);
					field.set(null, ingot);
					OreDictionary.registerOre(name, ingot);
				}
				else if (name.contains("plate"))
				{
					field.set(null, new ItemPlate(name, id));
					Item item = (Item) field.get(null);

					String ingotName = name.replaceAll("plate", "ingot");

					Item itemIngot = null;

					if (OreDictionary.getOres(ingotName).size() > 0)
					{
						itemIngot = OreDictionary.getOres(ingotName).get(0).getItem();
					}

					if (name.equals("plateIron"))
					{
						itemIngot = Item.ingotIron;
					}
					else if (name.equals("plateGold"))
					{
						itemIngot = Item.ingotGold;
					}

					if (itemIngot != null)
					{
						if (OreDictionary.getOres(ingotName).size() == 0)
						{
							RecipeUtility.addRecipe(new ShapedOreRecipe(item, "II", "II", 'I', itemIngot), Calclavia.CONFIGURATION, true);
						}
					}

					RecipeUtility.addRecipe(new ShapedOreRecipe(item, "II", "II", 'I', ingotName), Calclavia.CONFIGURATION, true);
				}
				else if (name.contains("dust"))
				{
					field.set(null, new ItemBase(name, id).setCreativeTab(CreativeTabs.tabMaterials));
					Item item = (Item) field.get(null);

					if (name.equals("dustBronze"))
					{
						RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), "!#!", '!', "ingotCopper", '#', "ingotTin"), Calclavia.CONFIGURATION, true);

						if (OreDictionary.getOres("ingotBronze").size() > 0)
						{
							GameRegistry.addSmelting(item.itemID, OreDictionary.getOres("ingotBronze").get(0), 0.6f);
						}
					}
					else if (name.equals("dustSteel"))
					{
						RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), " C ", "CIC", " C ", 'I', Item.ingotIron, 'C', Item.coal), Calclavia.CONFIGURATION, RecipeUtility.getRecipesByOutput(new ItemStack(item)).size() <= 0);

						if (OreDictionary.getOres("ingotSteel").size() > 0)
						{
							GameRegistry.addSmelting(item.itemID, OreDictionary.getOres("ingotSteel").get(0), 0.8f);
						}
					}
				}
				else if (name.equals("wrench"))
				{
					field.set(null, new ItemMultitool(id));
					Item item = (Item) field.get(null);

					if (OreDictionary.getOres("ingotSteel").size() > 0)
					{
						RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), " S ", " SS", "S  ", 'S', "ingotSteel"), Calclavia.CONFIGURATION, true);
					}
					else
					{
						RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), " S ", " SS", "S  ", 'S', Item.ingotIron), Calclavia.CONFIGURATION, true);
					}
				}
				else
				{
					field.set(null, new ItemBase(name, id).setCreativeTab(CreativeTabs.tabMaterials));
					Item item = (Item) field.get(null);

					if (name.equals("circuitBasic"))
					{
						if (OreDictionary.getOres("plateBronze").size() > 0)
						{
							if (OreDictionary.getOres("copperWire").size() > 0)
							{
								RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), "!#!", "#@#", "!#!", '@', "plateBronze", '#', Item.redstone, '!', "copperWire"), Calclavia.CONFIGURATION, true);
								RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), "!#!", "#@#", "!#!", '@', "plateSteel", '#', Item.redstone, '!', "copperWire"), Calclavia.CONFIGURATION, true);
							}
							else
							{
								RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), "!#!", "#@#", "!#!", '@', "plateBronze", '#', Item.redstone, '!', Item.comparator), Calclavia.CONFIGURATION, true);
								RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), "!#!", "#@#", "!#!", '@', "plateSteel", '#', Item.redstone, '!', Item.comparator), Calclavia.CONFIGURATION, true);
							}
						}
						else
						{
							RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), "!#!", "#@#", "!#!", '@', Block.blockIron, '#', Item.redstone, '!', Item.comparator), Calclavia.CONFIGURATION, true);
						}
					}
					else if (name.equals("circuitAdvanced"))
					{
						RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), "@@@", "#?#", "@@@", '@', Item.redstone, '?', Item.diamond, '#', "circuitBasic"), Calclavia.CONFIGURATION, true);
					}
					else if (name.equals("circuitElite"))
					{
						RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), "@@@", "?#?", "@@@", '@', Item.ingotGold, '?', "circuitAdvanced", '#', Block.blockLapis), Calclavia.CONFIGURATION, true);
					}
					else if (name.equals("motor"))
					{
						if (OreDictionary.getOres("copperWire").size() > 0)
						{
							RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), "@!@", "!#!", "@!@", '!', "ingotSteel", '#', Item.ingotIron, '@', "copperWire"), Calclavia.CONFIGURATION, true);
						}
						else
						{
							RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), "@!@", "!#!", "@!@", '!', "ingotSteel", '#', Item.ingotIron, '@', Item.comparator), Calclavia.CONFIGURATION, true);
						}
					}
				}

				Item item = (Item) field.get(null);
				GameRegistry.registerItem(item, name);
				OreDictionary.registerOre(name, item);

				FMLLog.info("Basic Components: Successfully requested item: " + name);
				return item;
			}

			return f;
		}
		catch (Exception e)
		{
			FMLLog.severe("Basic Components: Failed to require item: " + fieldName);
			e.printStackTrace();
		}

		return null;
	}

	public static Item requestItem(String name, int id)
	{
		if (OreDictionary.getOres(name).size() <= 0 && !(name.equals("wrench") && Loader.isModLoaded("BuildCraft|Core")))
		{
			return requireItem(name, id);
		}

		FMLLog.info("Basic Components: " + name + " already exists in Ore Dictionary, using the ore instead.");

		if (OreDictionary.getOres(name).size() > 0)
		{
			return OreDictionary.getOres(name).get(0).getItem();
		}

		return null;
	}

	public static Block requireBlock(String fieldName, int id)
	{
		try
		{
			String name = fieldName.replace("block", "");
			name = Character.toLowerCase(name.charAt(0)) + name.substring(1);

			Field field = ReflectionHelper.findField(CalclaviaComponents.class, fieldName);
			Block f = (Block) field.get(null);
			Field idField = ReflectionHelper.findField(CalclaviaComponents.class, "id" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1));
			id = id <= 0 ? (Integer) idField.get(null) : id;

			if (f == null)
			{
				if (name.contains("ore"))
				{
					field.set(null, new BlockBase(name, id));
					Block block = (Block) field.get(null);
					GameRegistry.registerBlock(block, name);
					OreDictionary.registerOre(name, block);

					String ingotName = name.replaceAll("ore", "ingot");

					if (OreDictionary.getOres(ingotName).size() > 0)
					{
						GameRegistry.addSmelting(block.blockID, OreDictionary.getOres(ingotName).get(0), 0.6f);
					}

					Field generationField = ReflectionHelper.findField(CalclaviaComponents.class, "generation" + Character.toUpperCase(name.charAt(0)) + name.substring(1));
					generationField.set(null, new OreGenReplaceStone(name, name, new ItemStack(block), 60, 22, 4).enable(Calclavia.CONFIGURATION));
					OreGenerator.addOre((OreGenReplaceStone) generationField.get(null));
				}

				Block block = (Block) field.get(null);

				FMLLog.info("Basic Components: Successfully requested block: " + name);
				return block;
			}

			return f;
		}
		catch (Exception e)
		{
			FMLLog.severe("Basic Components: Failed to require block: " + fieldName);
			e.printStackTrace();
		}

		return null;
	}

	public static Block requestBlock(String name, int id)
	{
		if (OreDictionary.getOres(name).size() <= 0)
		{
			return requireBlock(name, id);
		}

		FMLLog.info("Basic Components: " + name + " already exists in Ore Dictionary, using the ore instead.");

		if (OreDictionary.getOres(name).get(0).getItem() instanceof ItemBlock)
		{
			return Block.blocksList[((ItemBlock) OreDictionary.getOres(name).get(0).getItem()).getBlockID()];
		}

		return null;
	}
}

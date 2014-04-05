package calclavia.components;

import calclavia.api.resonantinduction.IBoilHandler;
import calclavia.components.creative.BlockCreativeBuilder;
import calclavia.components.creative.BlockInfiniteBlock;
import calclavia.components.tool.ToolMode;
import calclavia.components.tool.ToolModeGeneral;
import calclavia.components.tool.ToolModeRotation;
import calclavia.lib.Calclavia;
import calclavia.lib.compat.waila.Waila;
import calclavia.lib.config.Config;
import calclavia.lib.config.ConfigHandler;
import calclavia.lib.content.ContentRegistry;
import calclavia.lib.content.IDManager;
import calclavia.lib.flag.CommandFlag;
import calclavia.lib.flag.FlagRegistry;
import calclavia.lib.flag.ModFlag;
import calclavia.lib.grid.UECommand;
import calclavia.lib.grid.UpdateTicker;
import calclavia.lib.modproxy.ProxyHandler;
import calclavia.lib.multiblock.fake.BlockMultiBlockPart;
import calclavia.lib.multiblock.fake.TileMultiBlockPart;
import calclavia.lib.network.PacketAnnotation;
import calclavia.lib.network.PacketHandler;
import calclavia.lib.network.PacketTile;
import calclavia.lib.prefab.ProxyBase;
import calclavia.lib.prefab.item.ItemBlockMetadata;
import calclavia.lib.prefab.ore.OreGenBase;
import calclavia.lib.prefab.ore.OreGenReplaceStone;
import calclavia.lib.prefab.ore.OreGenerator;
import calclavia.lib.recipe.RecipeUtility;
import calclavia.lib.thermal.BoilEvent;
import calclavia.lib.thermal.EventThermal.EventThermalUpdate;
import calclavia.lib.thermal.ThermalGrid;
import calclavia.lib.utility.LanguageUtility;
import calclavia.lib.utility.PotionUtility;
import calclavia.lib.utility.nbt.NBTUtility;
import calclavia.lib.utility.nbt.SaveManager;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import universalelectricity.api.vector.Vector3;
import universalelectricity.api.vector.VectorWorld;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Basic Components
 *
 * @author Calclavia
 */
@Mod(modid = CalclaviaLoader.ID, name = Calclavia.NAME, version = CalclaviaLoader.VERSION, dependencies = "required-after:UniversalElectricity")
@NetworkMod(channels = CalclaviaLoader.CHANNEL, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class)
public class CalclaviaLoader
{
	public static final String ID = "CalclaviaCore";
	public static final String CHANNEL = "calclaviacore";
	public static final PacketTile PACKET_TILE = new PacketTile(CHANNEL);
	public static final PacketAnnotation PACKET_ANNOTATION = new PacketAnnotation(CHANNEL);
	public static final String MAJOR_VERSION = "@MAJOR@";
	public static final String MINOR_VERSION = "@MINOR@";
	public static final String REVISION_VERSION = "@REVIS@";
	public static final String VERSION = MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION_VERSION;
	public static final String BUILD_VERSION = "@BUILD@";
	public static final String ASSET_DIRECTORY = "/assets/calclavia/";
	public static final String LANGUAGE_PATH = ASSET_DIRECTORY + "languages/";
	public static final String TEXTURE_PATH = "textures/";
	public static final String TEXTURE_DIRECTORY = ASSET_DIRECTORY + TEXTURE_PATH;
	public static final String BLOCK_TEXTURE_DIRECTORY = TEXTURE_DIRECTORY + "blocks/";
	public static final String ITEM_TEXTURE_DIRECTORY = TEXTURE_DIRECTORY + "items/";
	public static final String MODEL_TEXTURE_DIRECTORY = TEXTURE_DIRECTORY + "models/";

	public static final String DOMAIN = "calclavia";
	public static final String PREFIX = DOMAIN + ":";
	public static final String[] LANGUAGES_SUPPORTED = new String[] { "en_US", "zh_CN", "es_ES", "it_IT", "nl_NL", "de_DE", "tr_TR", "ru_RU" };
	/**
	 * Auto-incrementing configuration IDs. Use this to make sure no config ID is the same.
	 */
	public static final IDManager idManager = new IDManager(3970, 13970);
	public static final int idBlockOreCopper = idManager.getNextBlockID();
	public static final int idBlockOreTin = idManager.getNextBlockID();
	public static final int idItemWrench = idManager.getNextItemID();
	public static final int idItemMotor = idManager.getNextItemID();
	public static final int idItemCircuitBasic = idManager.getNextItemID();
	public static final int idItemCircuitAdvanced = idManager.getNextItemID();
	public static final int idItemCircuitElite = idManager.getNextItemID();
	public static final int idItemPlateCopper = idManager.getNextItemID();
	public static final int idItemPlateTin = idManager.getNextItemID();
	public static final int idItemPlateBronze = idManager.getNextItemID();
	public static final int idItemPlateSteel = idManager.getNextItemID();
	public static final int idItemPlateIron = idManager.getNextItemID();
	public static final int idItemPlateGold = idManager.getNextItemID();
	public static final int idItemIngotCopper = idManager.getNextItemID();
	public static final int idItemIngotTin = idManager.getNextItemID();
	public static final int idItemIngotSteel = idManager.getNextItemID();
	public static final int idItemIngotBronze = idManager.getNextItemID();
	public static final int idItemDustSteel = idManager.getNextItemID();
	public static final int idItemDustBronze = idManager.getNextItemID();
	public static final ContentRegistry contentRegistry = new ContentRegistry(Calclavia.CONFIGURATION, idManager, ID).setPrefix(PREFIX).setTab(CreativeTabs.tabTools);
	@SidedProxy(clientSide = "calclavia.components.ClientProxy", serverSide = "calclavia.lib.prefab.ProxyBase")
	public static ProxyBase proxy;
	@Mod.Metadata(ID)
	public static ModMetadata metadata;
	@Instance(ID)
	public static CalclaviaLoader INSTANCE;
	/**
	 * Blocks
	 */
	public static Block blockOreCopper;
	public static Block blockOreTin;
	/**
	 * Items
	 */
	public static Item itemWrench;
	public static Item itemMotor;
	public static Item itemCircuitBasic;
	public static Item itemCircuitAdvanced;
	public static Item itemCircuitElite;
	public static Item itemPlateCopper;
	public static Item itemPlateTin;
	public static Item itemPlateBronze;
	public static Item itemPlateSteel;
	public static Item itemPlateIron;
	public static Item itemPlateGold;
	public static Item itemIngotCopper;
	public static Item itemIngotTin;
	public static Item itemIngotSteel;
	public static Item itemIngotBronze;
	public static Item itemDustSteel;
	public static Item itemDustBronze;
	public static OreGenBase generationOreCopper, generationOreTin;
	/**
	 * Calclavia Core Blocks for mods
	 */

	public static BlockMultiBlockPart blockMulti;
	public static BlockCreativeBuilder blockCreativeBuilder;
	public static Block blockInfinite;
	@Config
	public static double steamMultiplier = 1;
	private ProxyHandler modproxies;

	public CalclaviaLoader()
	{
		this.modproxies = new ProxyHandler();
	}

	/**
	 * Call all of this in Init stage. Use "requestItem" or "requestBlock" instead to make it so
	 * then if another mod adds in a item with the same name, it will use that mod's item instead.
	 * <p/>
	 * Creates a specific Basic Component item/Block.
	 * <p/>
	 * If you want correct recipes, make sure you register required items in the following order:
	 * <p/>
	 * Ingot, Ores, Dust, Plate, Copper Wire, Circuits, Motor, Wrench
	 *
	 * @param fieldName - Name of the item: e.g ingotCopper, ingotSteel
	 * @param id        - The specified ID of the item. Use 0 for a default value to be used.
	 * @return The Item/Block class.
	 */
	public static Item requireItem(String fieldName, int id)
	{
		try
		{
			String name = fieldName.replace("item", "");
			name = Character.toLowerCase(name.charAt(0)) + name.substring(1);

			Field field = ReflectionHelper.findField(CalclaviaLoader.class, fieldName);
			Item f = (Item) field.get(null);

			// Grabs the default ID.
			Field idField = ReflectionHelper.findField(CalclaviaLoader.class, "id" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1));
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
					field.set(null, new ItemScrewdriver(id));
					Item item = (Item) field.get(null);

					if (OreDictionary.getOres("ingotSteel").size() > 0)
					{
						RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), " S ", "W  ", 'S', "ingotSteel", 'W', Block.wood), Calclavia.CONFIGURATION, true);
					}
					else
					{
						RecipeUtility.addRecipe(new ShapedOreRecipe(new ItemStack(item), " S ", "W  ", 'S', Item.ingotIron, 'W', Block.wood), Calclavia.CONFIGURATION, true);
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

			Field field = ReflectionHelper.findField(CalclaviaLoader.class, fieldName);
			Block f = (Block) field.get(null);
			Field idField = ReflectionHelper.findField(CalclaviaLoader.class, "id" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1));
			id = id <= 0 ? (Integer) idField.get(null) : id;

			if (f == null)
			{
				if (name.contains("ore"))
				{
					field.set(null, new BlockCC(name, id));
					Block block = (Block) field.get(null);
					GameRegistry.registerBlock(block, name);
					OreDictionary.registerOre(name, block);

					String ingotName = name.replaceAll("ore", "ingot");

					if (OreDictionary.getOres(ingotName).size() > 0)
					{
						GameRegistry.addSmelting(block.blockID, OreDictionary.getOres(ingotName).get(0), 0.6f);
					}

					Field generationField = ReflectionHelper.findField(CalclaviaLoader.class, "generation" + Character.toUpperCase(name.charAt(0)) + name.substring(1));
					generationField.set(null, new OreGenReplaceStone(name, name, new ItemStack(block), 60, 25, 4).enable(Calclavia.CONFIGURATION));
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

	@EventHandler
	public void preInit(FMLPreInitializationEvent evt)
	{
		Calclavia.LOGGER.setParent(FMLLog.getLogger());
		NetworkRegistry.instance().registerGuiHandler(this, proxy);

		modproxies.applyModule(Waila.class, Calclavia.CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "Enable_Waila_Support", true).getBoolean(true));

		// Potion Array resized to Current potion array, +32, Allows to miss conflicting ID's
		PotionUtility.resizePotionArray();

		SaveManager.registerClass("ModFlag", ModFlag.class);
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(SaveManager.instance());

		ToolMode.REGISTRY.add(new ToolModeGeneral());
		ToolMode.REGISTRY.add(new ToolModeRotation());

		Calclavia.CONFIGURATION.load();

		blockMulti = (BlockMultiBlockPart) contentRegistry.createTile(BlockMultiBlockPart.class, TileMultiBlockPart.class).setCreativeTab(null);
		blockMulti.setPacketType(PACKET_TILE);

		if (Calclavia.CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "Enable_Calclavia_Core_Tools", true).getBoolean(true))
		{
			blockCreativeBuilder = (BlockCreativeBuilder) contentRegistry.createBlock(BlockCreativeBuilder.class);
		}

		blockInfinite = contentRegistry.createBlock(BlockInfiniteBlock.class, ItemBlockMetadata.class);

		Calclavia.CONFIGURATION.save();
		proxy.preInit();
		modproxies.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent evt)
	{
		Calclavia.CONFIGURATION.load();

		boolean defaultDoLoad = !(Loader.isModLoaded("ThermalExpansion") || Loader.isModLoaded("IC2"));

		if (Calclavia.CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "Enable_Calclavia_Core_Resources", defaultDoLoad).getBoolean(defaultDoLoad))
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

		if (Calclavia.CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "Enable_Calclavia_Core_Tools", true).getBoolean(true))
		{
			ComponentRegistry.register("itemCircuitBasic");
			ComponentRegistry.register("itemCircuitAdvanced");
			ComponentRegistry.register("itemCircuitElite");

			// ComponentRegistry.register("itemMotor");
			ComponentRegistry.register("itemWrench");
		}

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

		Calclavia.LOGGER.fine("Loaded: " + LanguageUtility.loadLanguages(CalclaviaLoader.LANGUAGE_PATH, CalclaviaLoader.LANGUAGES_SUPPORTED) + " Languages.");

		CalclaviaLoader.metadata.modId = ID;
		CalclaviaLoader.metadata.name = ID;
		CalclaviaLoader.metadata.description = "Calclavia Core is an FML Plugin to provide core functionalities in order to run Calclavia's mods.";
		CalclaviaLoader.metadata.url = "http://calclavia.com/calclavia-core";
		CalclaviaLoader.metadata.version = VERSION + BUILD_VERSION;
		CalclaviaLoader.metadata.authorList = Arrays.asList(new String[] { "Calclavia" });
		CalclaviaLoader.metadata.autogenerated = false;
		proxy.init();
		modproxies.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent evt)
	{

		// TODO: Move to UE
		if (!UpdateTicker.INSTANCE.isAlive())
		{
			UpdateTicker.INSTANCE.start();
		}
		Calclavia.CONFIGURATION.load();
		try
		{
			ConfigHandler.configure(Calclavia.CONFIGURATION, Calclavia.DOMAIN);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		Calclavia.CONFIGURATION.save();

		// Register Thermal Grid
		UpdateTicker.addNetwork(ThermalGrid.SERVER_INSTANCE);

		modproxies.postInit();
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		// Load ModFlag from world save
		Object object = SaveManager.createAndLoad(NBTUtility.loadData(FlagRegistry.DEFAULT_NAME));
		if (!(object instanceof ModFlag))
		{
			object = new ModFlag(FlagRegistry.DEFAULT_NAME);
		}
		FlagRegistry.registerModFlag(FlagRegistry.DEFAULT_NAME, (ModFlag) object);

		// Setup command
		ICommandManager commandManager = FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager();
		ServerCommandManager serverCommandManager = ((ServerCommandManager) commandManager);
		serverCommandManager.registerCommand(new CommandFlag(FlagRegistry.getModFlag(FlagRegistry.DEFAULT_NAME)));

		// TODO: Move to UE
		serverCommandManager.registerCommand(new UECommand());
	}

	@EventHandler
	public void onServerStopping(FMLServerStoppingEvent evt)
	{
		SaveManager.saveAll();
	}

	/**
	 * Default handler.
	 */
	@ForgeSubscribe
	public void boilEventHandler(BoilEvent evt)
	{
		World world = evt.world;
		Vector3 position = evt.position;
		int blockID = position.getBlockID(world);

		for (int height = 1; height <= evt.maxSpread; height++)
		{
			TileEntity tileEntity = world.getBlockTileEntity(position.intX(), position.intY() + height, position.intZ());

			if (tileEntity instanceof IBoilHandler)
			{
				IBoilHandler handler = (IBoilHandler) tileEntity;
				FluidStack fluid = evt.getRemainForSpread(height);

				if (fluid.amount > 0)
				{
					if (handler.canFill(ForgeDirection.DOWN, fluid.getFluid()))
					{
						fluid.amount -= handler.fill(ForgeDirection.DOWN, fluid, true);
					}
				}
			}
		}

		if (world.rand.nextInt(80) == 0)
		{
			world.playSoundEffect(position.x + 0.5F, position.y + 0.5F, position.z + 0.5F, "Fluid.lava", 0.5F, 2.1F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.85F);
		}

		if (world.rand.nextInt(40) == 0)
		{
			world.playSoundEffect(position.x + 0.5F, position.y + 0.5F, position.z + 0.5F, "Fluid.lavapop", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
		}

		world.spawnParticle("bubble", position.x + Math.random(), position.y + 0.5f + Math.random(), position.z + Math.random(), 0, 0, 0);

		if (world.rand.nextInt(5) == 0)
		{
			world.spawnParticle("smoke", position.x + Math.random(), position.y + 0.5f + Math.random(), position.z + Math.random(), 0, 0, 0);
		}

		if ((blockID == Block.waterMoving.blockID || blockID == Block.waterStill.blockID) && position.getBlockMetadata(world) == 0)
		{
			position.setBlock(world, 0);
		}

		evt.setResult(Result.DENY);
	}

	/**
	 * Default handler.
	 */
	@ForgeSubscribe
	public void thermalEventHandler(EventThermalUpdate evt)
	{
		VectorWorld pos = evt.position;

		synchronized (pos.world)
		{
			Block block = Block.blocksList[pos.getBlockID()];
			Material mat = pos.world.getBlockMaterial(pos.intX(), pos.intY(), pos.intZ());

			if (mat == Material.air)
			{
				evt.heatLoss = 0.15f;
			}

			if (block == Block.waterMoving || block == Block.waterStill)
			{
				if (evt.temperature >= 373)
				{
					if (FluidRegistry.getFluid("steam") != null)
					{
						// TODO: INCORRECT!
						int volume = (int) (FluidContainerRegistry.BUCKET_VOLUME * (evt.temperature / 373) * steamMultiplier);
						MinecraftForge.EVENT_BUS.post(new BoilEvent(pos.world, pos, new FluidStack(FluidRegistry.WATER, volume), new FluidStack(FluidRegistry.getFluid("steam"), volume), 2));
					}

					evt.heatLoss = 0.2f;
				}
			}

			if (block == Block.ice)
			{
				if (evt.temperature >= 273)
				{
					pos.setBlock(Block.waterMoving.blockID);
				}

				evt.heatLoss = 0.4f;
			}
		}
	}
}

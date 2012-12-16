package basiccomponents.common;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.liquids.LiquidContainerData;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.RecipeHelper;
import universalelectricity.prefab.UETab;
import universalelectricity.prefab.UpdateNotifier;
import universalelectricity.prefab.network.ConnectionHandler;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.ore.OreGenReplaceStone;
import universalelectricity.prefab.ore.OreGenerator;
import basiccomponents.common.block.BlockBCOre;
import basiccomponents.common.block.BlockBasicMachine;
import basiccomponents.common.block.BlockCopperWire;
import basiccomponents.common.block.BlockOilFlowing;
import basiccomponents.common.block.BlockOilStill;
import basiccomponents.common.item.ItemBasic;
import basiccomponents.common.item.ItemBattery;
import basiccomponents.common.item.ItemBlockBCOre;
import basiccomponents.common.item.ItemBlockBasicMachine;
import basiccomponents.common.item.ItemBlockCopperWire;
import basiccomponents.common.item.ItemCircuit;
import basiccomponents.common.item.ItemOilBucket;
import basiccomponents.common.item.ItemWrench;
import cpw.mods.fml.common.ICraftingHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = BCLoader.CHANNEL, name = BCLoader.NAME, version = UniversalElectricity.VERSION)
@NetworkMod(channels = BCLoader.CHANNEL, clientSideRequired = true, serverSideRequired = false, connectionHandler = ConnectionHandler.class, packetHandler = PacketManager.class)
public class BCLoader implements ICraftingHandler
{
	public static final String NAME = "Basic Components";
	public static final String CHANNEL = "BasicComponents";

	public static final String FILE_PATH = "/basiccomponents/";
	public static final String LANGUAGE_PATH = FILE_PATH + "language/";
	public static final String TEXTURE_PATH = FILE_PATH + "textures/";
	public static final String BLOCK_TEXTURE_FILE = TEXTURE_PATH + "blocks.png";
	public static final String ITEM_TEXTURE_FILE = TEXTURE_PATH + "items.png";

	private static final String[] LANGUAGES_SUPPORTED = new String[] { "en_US", "zh_CN", "es_ES" };

	@Instance("BasicComponents")
	public static BCLoader instance;

	@SidedProxy(clientSide = "basiccomponents.client.ClientProxy", serverSide = "basiccomponents.common.CommonProxy")
	public static CommonProxy proxy;

	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		UniversalElectricity.register(this, UniversalElectricity.MAJOR_VERSION, UniversalElectricity.MINOR_VERSION, UniversalElectricity.REVISION_VERSION, false);
		NetworkRegistry.instance().registerGuiHandler(this, this.proxy);

		/**
		 * Define the items and blocks.
		 */
		UniversalElectricity.CONFIGURATION.load();
		BasicComponents.blockBasicOre = new BlockBCOre(UniversalElectricity.CONFIGURATION.getBlock("Copper and Tin Ores", BasicComponents.BLOCK_ID_PREFIX + 0).getInt());
		BasicComponents.blockCopperWire = new BlockCopperWire(UniversalElectricity.CONFIGURATION.getBlock("Copper_Wire", BasicComponents.BLOCK_ID_PREFIX + 1).getInt());
		BasicComponents.oilMoving = new BlockOilFlowing(UniversalElectricity.CONFIGURATION.getBlock("Oil_Flowing", BasicComponents.BLOCK_ID_PREFIX + 2).getInt());
		BasicComponents.oilStill = new BlockOilStill(UniversalElectricity.CONFIGURATION.getBlock("Oil_Still", BasicComponents.BLOCK_ID_PREFIX + 3).getInt());
		BasicComponents.blockMachine = new BlockBasicMachine(UniversalElectricity.CONFIGURATION.getBlock("Basic Machine", BasicComponents.BLOCK_ID_PREFIX + 4).getInt(), 0);

		BasicComponents.itemBattery = new ItemBattery(UniversalElectricity.CONFIGURATION.getItem("Battery", BasicComponents.ITEM_ID_PREFIX + 1).getInt(), 0);
		BasicComponents.itemWrench = new ItemWrench(UniversalElectricity.CONFIGURATION.getItem("Universal Wrench", BasicComponents.ITEM_ID_PREFIX + 2).getInt(), 20);
		BasicComponents.itemCircuit = new ItemCircuit(UniversalElectricity.CONFIGURATION.getItem("Circuit", BasicComponents.ITEM_ID_PREFIX + 3).getInt(), 16);

		BasicComponents.itemCopperIngot = new ItemBasic("ingotCopper", UniversalElectricity.CONFIGURATION.getItem("Copper Ingot", BasicComponents.ITEM_ID_PREFIX + 4).getInt(), 1);
		BasicComponents.itemTinIngot = new ItemBasic("ingotTin", UniversalElectricity.CONFIGURATION.getItem("Tin Ingot", BasicComponents.ITEM_ID_PREFIX + 5).getInt(), 2);
		BasicComponents.itemBronzeIngot = new ItemBasic("ingotBronze", UniversalElectricity.CONFIGURATION.getItem("Bronze Ingot", BasicComponents.ITEM_ID_PREFIX + 6).getInt(), 7);
		BasicComponents.itemSteelIngot = new ItemBasic("ingotSteel", UniversalElectricity.CONFIGURATION.getItem("Steel Ingot", BasicComponents.ITEM_ID_PREFIX + 7).getInt(), 3);

		BasicComponents.itemBronzeDust = new ItemBasic("dustBronze", UniversalElectricity.CONFIGURATION.getItem("Bronze Dust", BasicComponents.ITEM_ID_PREFIX + 8).getInt(), 6);
		BasicComponents.itemSteelDust = new ItemBasic("dustSteel", UniversalElectricity.CONFIGURATION.getItem("Steel Dust", BasicComponents.ITEM_ID_PREFIX + 9).getInt(), 5);

		BasicComponents.itemCopperPlate = new ItemBasic("plateCopper", UniversalElectricity.CONFIGURATION.getItem("Copper Plate", BasicComponents.ITEM_ID_PREFIX + 10).getInt(), 10);
		BasicComponents.itemTinPlate = new ItemBasic("plateTin", UniversalElectricity.CONFIGURATION.getItem("Tin Plate", BasicComponents.ITEM_ID_PREFIX + 11).getInt(), 11);
		BasicComponents.itemBronzePlate = new ItemBasic("plateBronze", UniversalElectricity.CONFIGURATION.getItem("Bronze Plate", BasicComponents.ITEM_ID_PREFIX + 12).getInt(), 8);
		BasicComponents.itemSteelPlate = new ItemBasic("plateSteel", UniversalElectricity.CONFIGURATION.getItem("Steel Plate", BasicComponents.ITEM_ID_PREFIX + 13).getInt(), 9);

		BasicComponents.itemMotor = new ItemBasic("motor", UniversalElectricity.CONFIGURATION.getItem("Motor", BasicComponents.ITEM_ID_PREFIX + 14).getInt(), 12);

		BasicComponents.itemOilBucket = new ItemOilBucket(UniversalElectricity.CONFIGURATION.getItem("Oil Bucket", BasicComponents.ITEM_ID_PREFIX + 15).getInt(), 4);

		BasicComponents.coalGenerator = ((BlockBasicMachine) BasicComponents.blockMachine).getCoalGenerator();
		BasicComponents.batteryBox = ((BlockBasicMachine) BasicComponents.blockMachine).getBatteryBox();
		BasicComponents.electricFurnace = ((BlockBasicMachine) BasicComponents.blockMachine).getElectricFurnace();

		BasicComponents.copperOreGeneration = new OreGenReplaceStone("Copper Ore", "oreCopper", new ItemStack(BasicComponents.blockBasicOre, 1, 0), 0, 50, 40, 4).enable();
		BasicComponents.tinOreGeneration = new OreGenReplaceStone("Tin Ore", "oreTin", new ItemStack(BasicComponents.blockBasicOre, 1, 1), 0, 50, 36, 3).enable();

		UniversalElectricity.CONFIGURATION.save();

		/**
		 * @author Cammygames
		 */
		LiquidContainerRegistry.registerLiquid(new LiquidContainerData(new LiquidStack(BasicComponents.oilStill, LiquidContainerRegistry.BUCKET_VOLUME), new ItemStack(BasicComponents.itemOilBucket), new ItemStack(Item.bucketEmpty)));
		MinecraftForge.EVENT_BUS.register(BasicComponents.itemOilBucket);

		// Register Blocks
		GameRegistry.registerBlock(BasicComponents.blockBasicOre, ItemBlockBCOre.class);
		GameRegistry.registerBlock(BasicComponents.blockMachine, ItemBlockBasicMachine.class);
		GameRegistry.registerBlock(BasicComponents.blockCopperWire, ItemBlockCopperWire.class);
		GameRegistry.registerBlock(BasicComponents.oilMoving);
		GameRegistry.registerBlock(BasicComponents.oilStill);
		GameRegistry.registerCraftingHandler(this);

		/**
		 * Registering all Basic Component items into the Forge Ore Dictionary.
		 */
		OreDictionary.registerOre("oreCopper", new ItemStack(BasicComponents.blockBasicOre, 0));
		OreDictionary.registerOre("oreTin", new ItemStack(BasicComponents.blockBasicOre, 0));
		OreDictionary.registerOre("copperWire", BasicComponents.blockCopperWire);

		OreDictionary.registerOre("coalGenerator", BasicComponents.coalGenerator);
		OreDictionary.registerOre("batteryBox", BasicComponents.batteryBox);
		OreDictionary.registerOre("electricFurnace", BasicComponents.electricFurnace);

		OreDictionary.registerOre("battery", BasicComponents.itemBattery);
		OreDictionary.registerOre("wrench", BasicComponents.itemWrench);
		OreDictionary.registerOre("motor", BasicComponents.itemMotor);

		OreDictionary.registerOre("basicCircuit", new ItemStack(BasicComponents.itemCircuit, 1, 0));
		OreDictionary.registerOre("advancedCircuit", new ItemStack(BasicComponents.itemCircuit, 1, 1));
		OreDictionary.registerOre("eliteCircuit", new ItemStack(BasicComponents.itemCircuit, 1, 2));

		OreDictionary.registerOre("oilMoving", BasicComponents.oilMoving);
		OreDictionary.registerOre("oilStill", BasicComponents.oilStill);
		OreDictionary.registerOre("oilBucket", BasicComponents.itemOilBucket);

		OreDictionary.registerOre("ingotCopper", BasicComponents.itemCopperIngot);
		OreDictionary.registerOre("ingotTin", BasicComponents.itemTinIngot);
		OreDictionary.registerOre("ingotBronze", BasicComponents.itemBronzeIngot);
		OreDictionary.registerOre("ingotSteel", BasicComponents.itemSteelIngot);

		OreDictionary.registerOre("dustBronze", BasicComponents.itemBronzeDust);
		OreDictionary.registerOre("dustSteel", BasicComponents.itemSteelDust);

		OreDictionary.registerOre("plateCopper", BasicComponents.itemCopperPlate);
		OreDictionary.registerOre("plateTin", BasicComponents.itemTinPlate);
		OreDictionary.registerOre("plateBronze", BasicComponents.itemBronzePlate);
		OreDictionary.registerOre("plateSteel", BasicComponents.itemSteelPlate);

		UETab.setItemStack(BasicComponents.batteryBox);

		UpdateNotifier.INSTANCE.checkUpdate(NAME, UniversalElectricity.VERSION, "http://www.calclavia.com/downloads/ue/recommendedversion.txt");

		proxy.preInit();
	}

	@Init
	public void load(FMLInitializationEvent evt)
	{
		proxy.init();

		int languages = 0;

		/**
		 * Load all languages.
		 */
		for (String language : LANGUAGES_SUPPORTED)
		{
			LanguageRegistry.instance().loadLocalization(LANGUAGE_PATH + language + ".properties", language, false);

			if (LanguageRegistry.instance().getStringLocalization("children", language) != "")
			{
				try
				{
					String[] children = LanguageRegistry.instance().getStringLocalization("children", language).split(",");

					for (String child : children)
					{
						if (child != "" || child != null)
						{
							LanguageRegistry.instance().loadLocalization(LANGUAGE_PATH + language + ".properties", child, false);
							languages++;
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}

			languages++;
		}

		System.out.println("Basic Components: Loaded " + languages + " languages.");

		OreGenerator.addOre(BasicComponents.copperOreGeneration);
		OreGenerator.addOre(BasicComponents.tinOreGeneration);

		// Recipes
		// Oil Bucket
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemOilBucket), new Object[] { "CCC", "CBC", "CCC", 'B', Item.bucketWater, 'C', Item.coal }));
		// Motor
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemMotor), new Object[] { "@!@", "!#!", "@!@", '!', "ingotSteel", '#', Item.ingotIron, '@', BasicComponents.blockCopperWire }));
		// Wrench
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemWrench), new Object[] { " S ", " DS", "S  ", 'S', "ingotSteel", 'D', Item.diamond }));
		// Battery Box
		GameRegistry.addRecipe(new ShapedOreRecipe(BasicComponents.batteryBox, new Object[] { "?!?", "###", "?!?", '#', BasicComponents.blockCopperWire, '!', BasicComponents.itemSteelPlate, '?', BasicComponents.itemBattery.getUncharged() }));
		GameRegistry.addSmelting(BasicComponents.batteryBox.itemID, new ItemStack(BasicComponents.itemSteelDust, 6), 0f);
		// Coal Generator
		GameRegistry.addRecipe(new ShapedOreRecipe(BasicComponents.coalGenerator, new Object[] { "SCS", "FMF", "BBB", 'B', "ingotBronze", 'S', BasicComponents.itemSteelPlate, 'C', BasicComponents.blockCopperWire, 'M', BasicComponents.itemMotor, 'F', Block.stoneOvenIdle }));
		GameRegistry.addSmelting(BasicComponents.coalGenerator.itemID, new ItemStack(BasicComponents.itemSteelDust, 6), 0f);
		// Electric Furnace
		GameRegistry.addRecipe(new ShapedOreRecipe(BasicComponents.electricFurnace, new Object[] { "SSS", "SCS", "SMS", 'S', "ingotSteel", 'C', BasicComponents.itemCircuit, 'M', BasicComponents.itemMotor }));
		GameRegistry.addSmelting(BasicComponents.electricFurnace.itemID, new ItemStack(BasicComponents.itemSteelDust, 6), 0f);
		// Copper
		FurnaceRecipes.smelting().addSmelting(BasicComponents.blockBasicOre.blockID, 0, new ItemStack(BasicComponents.itemCopperIngot), 0.7f);
		// Copper Wire
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.blockCopperWire, 6), new Object[] { "!!!", "@@@", "!!!", '!', Item.leather, '@', "ingotCopper" }));
		// Tin
		FurnaceRecipes.smelting().addSmelting(BasicComponents.blockBasicOre.blockID, 1, new ItemStack(BasicComponents.itemTinIngot), 0.7f);
		// Battery
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemBattery), new Object[] { " T ", "TRT", "TCT", 'T', "ingotTin", 'R', Item.redstone, 'C', Item.coal }));
		// Steel
		RecipeHelper.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemSteelDust), new Object[] { " C ", "CIC", " C ", 'C', new ItemStack(Item.coal, 1, 1), 'I', Item.ingotIron }), "Steel Dust", UniversalElectricity.CONFIGURATION, true);
		RecipeHelper.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemSteelDust), new Object[] { " C ", "CIC", " C ", 'C', new ItemStack(Item.coal, 1, 0), 'I', Item.ingotIron }), "Steel Dust", UniversalElectricity.CONFIGURATION, true);
		GameRegistry.addSmelting(BasicComponents.itemSteelDust.shiftedIndex, new ItemStack(BasicComponents.itemSteelIngot), 0.8f);
		GameRegistry.addSmelting(BasicComponents.itemSteelPlate.shiftedIndex, new ItemStack(BasicComponents.itemSteelDust, 3), 0f);
		// Bronze
		RecipeHelper.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemBronzeDust), new Object[] { "!#!", '!', "ingotCopper", '#', "ingotTin" }), "Bronze Dust", UniversalElectricity.CONFIGURATION, true);
		GameRegistry.addSmelting(BasicComponents.itemBronzeDust.shiftedIndex, new ItemStack(BasicComponents.itemBronzeIngot), 0.6f);
		GameRegistry.addSmelting(BasicComponents.itemBronzePlate.shiftedIndex, new ItemStack(BasicComponents.itemBronzeDust, 3), 0f);

		// Plates
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemCopperPlate), new Object[] { "!!", "!!", '!', "ingotCopper" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemTinPlate), new Object[] { "!!", "!!", '!', "ingotTin" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemSteelPlate), new Object[] { "!!", "!!", '!', "ingotSteel" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemBronzePlate), new Object[] { "!!", "!!", '!', "ingotBronze" }));

		// Circuit
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemCircuit, 1, 0), new Object[] { "!#!", "#@#", "!#!", '@', BasicComponents.itemBronzePlate, '#', Item.redstone, '!', BasicComponents.blockCopperWire }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemCircuit, 1, 0), new Object[] { "!#!", "#@#", "!#!", '@', BasicComponents.itemSteelPlate, '#', Item.redstone, '!', BasicComponents.blockCopperWire }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemCircuit, 1, 1), new Object[] { "@@@", "#?#", "@@@", '@', Item.redstone, '?', Item.diamond, '#', BasicComponents.itemCircuit }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemCircuit, 1, 2), new Object[] { "@@@", "?#?", "@@@", '@', Item.ingotGold, '?', new ItemStack(BasicComponents.itemCircuit, 1, 1), '#', Block.blockLapis }));
	}

	@Override
	public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix)
	{
		if (item.itemID == BasicComponents.itemOilBucket.shiftedIndex)
		{
			for (int i = 0; i < craftMatrix.getSizeInventory(); i++)
			{
				if (craftMatrix.getStackInSlot(i) != null)
				{
					if (craftMatrix.getStackInSlot(i).itemID == Item.bucketWater.shiftedIndex)
					{
						craftMatrix.setInventorySlotContents(i, null);
						return;
					}
				}
			}
		}
	}

	@Override
	public void onSmelting(EntityPlayer player, ItemStack item)
	{

	}

}

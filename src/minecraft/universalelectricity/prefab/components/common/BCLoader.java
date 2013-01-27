package universalelectricity.prefab.components.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.RecipeHelper;
import universalelectricity.prefab.TranslationHelper;
import universalelectricity.prefab.UETab;
import universalelectricity.prefab.UpdateNotifier;
import universalelectricity.prefab.components.common.block.BlockBCOre;
import universalelectricity.prefab.components.common.block.BlockBasicMachine;
import universalelectricity.prefab.components.common.block.BlockCopperWire;
import universalelectricity.prefab.components.common.item.ItemBasic;
import universalelectricity.prefab.components.common.item.ItemBattery;
import universalelectricity.prefab.components.common.item.ItemBlockBCOre;
import universalelectricity.prefab.components.common.item.ItemBlockBasicMachine;
import universalelectricity.prefab.components.common.item.ItemBlockCopperWire;
import universalelectricity.prefab.components.common.item.ItemCircuit;
import universalelectricity.prefab.components.common.item.ItemWrench;
import universalelectricity.prefab.network.ConnectionHandler;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.ore.OreGenReplaceStone;
import universalelectricity.prefab.ore.OreGenerator;
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

@Mod(modid = BCLoader.CHANNEL, name = BCLoader.NAME, version = UniversalElectricity.VERSION)
@NetworkMod(channels = BCLoader.CHANNEL, clientSideRequired = true, serverSideRequired = false, connectionHandler = ConnectionHandler.class, packetHandler = PacketManager.class)
public class BCLoader
{
	public static final String NAME = "Basic Components";
	public static final String CHANNEL = "BasicComponents";

	public static final String FILE_PATH = "/basiccomponents/";
	public static final String LANGUAGE_PATH = FILE_PATH + "language/";
	public static final String TEXTURE_PATH = FILE_PATH + "textures/";
	public static final String BLOCK_TEXTURE_FILE = TEXTURE_PATH + "blocks.png";
	public static final String ITEM_TEXTURE_FILE = TEXTURE_PATH + "items.png";

	private static final String[] LANGUAGES_SUPPORTED = new String[] { "en_US", "zh_CN", "es_ES", "it_IT", "nl_NL", "de_DE" };

	@Instance("BasicComponents")
	public static BCLoader instance;

	@SidedProxy(clientSide = "basiccomponents.client.ClientProxy", serverSide = "basiccomponents.common.CommonProxy")
	public static CommonProxy proxy;

	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		UniversalElectricity.register(this, UniversalElectricity.MAJOR_VERSION, UniversalElectricity.MINOR_VERSION, UniversalElectricity.REVISION_VERSION, true);
		NetworkRegistry.instance().registerGuiHandler(this, this.proxy);

		/**
		 * Define the items and blocks.
		 */
		UniversalElectricity.CONFIGURATION.load();
		BasicComponents.blockBasicOre = new BlockBCOre(UniversalElectricity.CONFIGURATION.getBlock("Copper and Tin Ores", BasicComponents.BLOCK_ID_PREFIX + 0).getInt());
		BasicComponents.blockCopperWire = new BlockCopperWire(UniversalElectricity.CONFIGURATION.getBlock("Copper_Wire", BasicComponents.BLOCK_ID_PREFIX + 1).getInt());
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

		BasicComponents.coalGenerator = ((BlockBasicMachine) BasicComponents.blockMachine).getCoalGenerator();
		BasicComponents.batteryBox = ((BlockBasicMachine) BasicComponents.blockMachine).getBatteryBox();
		BasicComponents.electricFurnace = ((BlockBasicMachine) BasicComponents.blockMachine).getElectricFurnace();

		// Register Blocks
		GameRegistry.registerBlock(BasicComponents.blockBasicOre, ItemBlockBCOre.class, "Ore");
		GameRegistry.registerBlock(BasicComponents.blockMachine, ItemBlockBasicMachine.class, "Basic Machine");
		GameRegistry.registerBlock(BasicComponents.blockCopperWire, ItemBlockCopperWire.class, "Copper Wire");

		BasicComponents.copperOreGeneration = new OreGenReplaceStone("Copper Ore", "oreCopper", new ItemStack(BasicComponents.blockBasicOre, 1, 0), 70, 29, 4).enable(UniversalElectricity.CONFIGURATION);
		BasicComponents.tinOreGeneration = new OreGenReplaceStone("Tin Ore", "oreTin", new ItemStack(BasicComponents.blockBasicOre, 1, 1), 70, 27, 4).enable(UniversalElectricity.CONFIGURATION);

		UniversalElectricity.CONFIGURATION.save();

		/**
		 * Registering all Basic Component items into the Forge Ore Dictionary.
		 */
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

		System.out.println("Basic Components Loaded: " + TranslationHelper.loadLanguages(LANGUAGE_PATH, LANGUAGES_SUPPORTED) + " Languages.");

		OreGenerator.addOre(BasicComponents.copperOreGeneration);
		OreGenerator.addOre(BasicComponents.tinOreGeneration);

		// Recipe Registry
		// Motor
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemMotor), new Object[] { "@!@", "!#!", "@!@", '!', "ingotSteel", '#', Item.ingotIron, '@', "copperWire" }));
		// Wrench
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemWrench), new Object[] { " S ", " DS", "S  ", 'S', "ingotSteel", 'D', Item.diamond }));
		// Battery Box
		GameRegistry.addRecipe(new ShapedOreRecipe(BasicComponents.batteryBox, new Object[] { "SSS", "BBB", "SSS", 'B', BasicComponents.itemBattery.getUncharged(), 'S', "ingotSteel" }));
		// Coal Generator
		GameRegistry.addRecipe(new ShapedOreRecipe(BasicComponents.coalGenerator, new Object[] { "MMM", "MOM", "MCM", 'M', "ingotSteel", 'C', BasicComponents.itemMotor, 'O', Block.stoneOvenIdle }));
		GameRegistry.addRecipe(new ShapedOreRecipe(BasicComponents.coalGenerator, new Object[] { "MMM", "MOM", "MCM", 'M', "ingotBronze", 'C', BasicComponents.itemMotor, 'O', Block.stoneOvenIdle }));
		// Electric Furnace
		GameRegistry.addRecipe(new ShapedOreRecipe(BasicComponents.electricFurnace, new Object[] { "SSS", "SCS", "SMS", 'S', "ingotSteel", 'C', BasicComponents.itemCircuit, 'M', BasicComponents.itemMotor }));
		// Copper
		FurnaceRecipes.smelting().addSmelting(BasicComponents.blockBasicOre.blockID, 0, new ItemStack(BasicComponents.itemCopperIngot), 0.7f);
		// Copper Wire
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.blockCopperWire, 6), new Object[] { "!!!", "@@@", "!!!", '!', Block.cloth, '@', "ingotCopper" }));
		// Tin
		FurnaceRecipes.smelting().addSmelting(BasicComponents.blockBasicOre.blockID, 1, new ItemStack(BasicComponents.itemTinIngot), 0.7f);
		// Battery
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemBattery), new Object[] { " T ", "TRT", "TCT", 'T', "ingotTin", 'R', Item.redstone, 'C', Item.coal }));
		// Steel
		RecipeHelper.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemSteelDust), new Object[] { " C ", "CIC", " C ", 'C', new ItemStack(Item.coal, 1, 1), 'I', Item.ingotIron }), "Steel Dust", UniversalElectricity.CONFIGURATION, true);
		RecipeHelper.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemSteelDust), new Object[] { " C ", "CIC", " C ", 'C', new ItemStack(Item.coal, 1, 0), 'I', Item.ingotIron }), "Steel Dust", UniversalElectricity.CONFIGURATION, true);
		GameRegistry.addSmelting(BasicComponents.itemSteelDust.itemID, new ItemStack(BasicComponents.itemSteelIngot), 0.8f);
		GameRegistry.addSmelting(BasicComponents.itemSteelPlate.itemID, new ItemStack(BasicComponents.itemSteelDust, 3), 0f);
		// Bronze
		RecipeHelper.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemBronzeDust), new Object[] { "!#!", '!', "ingotCopper", '#', "ingotTin" }), "Bronze Dust", UniversalElectricity.CONFIGURATION, true);
		GameRegistry.addSmelting(BasicComponents.itemBronzeDust.itemID, new ItemStack(BasicComponents.itemBronzeIngot), 0.6f);
		GameRegistry.addSmelting(BasicComponents.itemBronzePlate.itemID, new ItemStack(BasicComponents.itemBronzeDust, 3), 0f);

		// Plates
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemCopperPlate), new Object[] { "!!", "!!", '!', "ingotCopper" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemTinPlate), new Object[] { "!!", "!!", '!', "ingotTin" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemSteelPlate), new Object[] { "!!", "!!", '!', "ingotSteel" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemBronzePlate), new Object[] { "!!", "!!", '!', "ingotBronze" }));

		// Circuit
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemCircuit, 1, 0), new Object[] { "!#!", "#@#", "!#!", '@', BasicComponents.itemBronzePlate, '#', Item.redstone, '!', "copperWire" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemCircuit, 1, 0), new Object[] { "!#!", "#@#", "!#!", '@', BasicComponents.itemSteelPlate, '#', Item.redstone, '!', "copperWire" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemCircuit, 1, 1), new Object[] { "@@@", "#?#", "@@@", '@', Item.redstone, '?', Item.diamond, '#', BasicComponents.itemCircuit }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemCircuit, 1, 2), new Object[] { "@@@", "?#?", "@@@", '@', Item.ingotGold, '?', new ItemStack(BasicComponents.itemCircuit, 1, 1), '#', Block.blockLapis }));
	}
}

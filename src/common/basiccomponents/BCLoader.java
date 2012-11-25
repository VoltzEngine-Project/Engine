package basiccomponents;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.liquids.LiquidContainerData;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import universalelectricity.core.UEConfig;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.RecipeHelper;
import universalelectricity.prefab.UETab;
import universalelectricity.prefab.network.ConnectionHandler;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.ore.OreGenReplaceStone;
import universalelectricity.prefab.ore.OreGenerator;
import basiccomponents.block.BlockBCOre;
import basiccomponents.block.BlockBasicMachine;
import basiccomponents.block.BlockCopperWire;
import basiccomponents.block.BlockOilFlowing;
import basiccomponents.block.BlockOilStill;
import basiccomponents.item.ItemBasic;
import basiccomponents.item.ItemBasicMachine;
import basiccomponents.item.ItemBattery;
import basiccomponents.item.ItemBlockCopperWire;
import basiccomponents.item.ItemBlockOre;
import basiccomponents.item.ItemCircuit;
import basiccomponents.item.ItemOilBucket;
import basiccomponents.item.ItemWrench;
import basiccomponents.tile.TileEntityBatteryBox;
import basiccomponents.tile.TileEntityCoalGenerator;
import basiccomponents.tile.TileEntityElectricFurnace;
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

@Mod(modid = "BasicComponents", name = "Basic Components", version = UniversalElectricity.VERSION)
@NetworkMod(channels = BCLoader.CHANNEL, clientSideRequired = true, serverSideRequired = false, connectionHandler = ConnectionHandler.class, packetHandler = PacketManager.class)
public class BCLoader implements ICraftingHandler
{
	public static final String CHANNEL = "BasicComponents";

	public static final String FILE_PATH = "/basiccomponents/textures/";
	public static final String BLOCK_TEXTURE_FILE = FILE_PATH + "blocks.png";
	public static final String ITEM_TEXTURE_FILE = FILE_PATH + "items.png";

	@Instance("BasicComponents")
	public static BCLoader instance;

	@SidedProxy(clientSide = "basiccomponents.BCClientProxy", serverSide = "basiccomponents.BCCommonProxy")
	public static BCCommonProxy proxy;

	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		UniversalElectricity.register(this, UniversalElectricity.MAJOR_VERSION, UniversalElectricity.MINOR_VERSION, UniversalElectricity.REVISION_VERSION, false);
		NetworkRegistry.instance().registerGuiHandler(this, this.proxy);

		/**
		 * Define the items and blocks.
		 */
		BasicComponents.blockBasicOre = new BlockBCOre(UEConfig.getBlockConfigID(UniversalElectricity.CONFIGURATION, "Copper and Tin Ores", BasicComponents.BLOCK_ID_PREFIX));
		BasicComponents.blockCopperWire = new BlockCopperWire(UEConfig.getBlockConfigID(UniversalElectricity.CONFIGURATION, "Copper_Wire", BasicComponents.BLOCK_ID_PREFIX + 1));
		BasicComponents.oilMoving = new BlockOilFlowing(UEConfig.getBlockConfigID(UniversalElectricity.CONFIGURATION, "Oil_Flowing", BasicComponents.BLOCK_ID_PREFIX + 2));
		BasicComponents.oilStill = new BlockOilStill(UEConfig.getBlockConfigID(UniversalElectricity.CONFIGURATION, "Oil_Still", BasicComponents.BLOCK_ID_PREFIX + 3));
		BasicComponents.blockMachine = new BlockBasicMachine(UEConfig.getBlockConfigID(UniversalElectricity.CONFIGURATION, "Basic Machine", BasicComponents.BLOCK_ID_PREFIX + 4), 0);

		BasicComponents.itemBattery = new ItemBattery(UEConfig.getItemConfigID(UniversalElectricity.CONFIGURATION, "Battery", BasicComponents.ITEM_ID_PREFIX + 1), 0);
		BasicComponents.itemWrench = new ItemWrench(UEConfig.getItemConfigID(UniversalElectricity.CONFIGURATION, "Universal Wrench", BasicComponents.ITEM_ID_PREFIX + 2), 20);
		BasicComponents.itemCircuit = new ItemCircuit(UEConfig.getItemConfigID(UniversalElectricity.CONFIGURATION, "Circuit", BasicComponents.ITEM_ID_PREFIX + 7), 16);

		BasicComponents.itemTinIngot = new ItemBasic("Tin Ingot", UEConfig.getItemConfigID(UniversalElectricity.CONFIGURATION, "Tin Ingot", BasicComponents.ITEM_ID_PREFIX + 4), 2);
		BasicComponents.itemCopperIngot = new ItemBasic("Copper Ingot", UEConfig.getItemConfigID(UniversalElectricity.CONFIGURATION, "Copper Ingot", BasicComponents.ITEM_ID_PREFIX + 3), 1);
		BasicComponents.itemSteelIngot = new ItemBasic("Steel Ingot", UEConfig.getItemConfigID(UniversalElectricity.CONFIGURATION, "Steel Ingot", BasicComponents.ITEM_ID_PREFIX + 5), 3);
		BasicComponents.itemBronzeIngot = new ItemBasic("Bronze Ingot", UEConfig.getItemConfigID(UniversalElectricity.CONFIGURATION, "Bronze Ingot", BasicComponents.ITEM_ID_PREFIX + 8), 7);

		BasicComponents.itemBronzeDust = new ItemBasic("Bronze Dust", UEConfig.getItemConfigID(UniversalElectricity.CONFIGURATION, "Bronze Dust", BasicComponents.ITEM_ID_PREFIX + 9), 6);
		BasicComponents.itemSteelDust = new ItemBasic("Steel Dust", UEConfig.getItemConfigID(UniversalElectricity.CONFIGURATION, "Steel Dust", BasicComponents.ITEM_ID_PREFIX + 6), 5);

		BasicComponents.itemMotor = new ItemBasic("Motor", UEConfig.getItemConfigID(UniversalElectricity.CONFIGURATION, "Motor", BasicComponents.ITEM_ID_PREFIX + 12), 12);

		BasicComponents.itemOilBucket = new ItemOilBucket("Oil Bucket", UEConfig.getItemConfigID(UniversalElectricity.CONFIGURATION, "Oil Bucket", BasicComponents.ITEM_ID_PREFIX + 13), 4);

		BasicComponents.itemCopperPlate = new ItemBasic("Copper Plate", UEConfig.getItemConfigID(UniversalElectricity.CONFIGURATION, "Copper Plate", BasicComponents.ITEM_ID_PREFIX + 14), 10);
		BasicComponents.itemSteelPlate = new ItemBasic("Steel Plate", UEConfig.getItemConfigID(UniversalElectricity.CONFIGURATION, "Steel Plate", BasicComponents.ITEM_ID_PREFIX + 10), 9);
		BasicComponents.itemBronzePlate = new ItemBasic("Bronze Plate", UEConfig.getItemConfigID(UniversalElectricity.CONFIGURATION, "Bronze Plate", BasicComponents.ITEM_ID_PREFIX + 11), 8);
		BasicComponents.itemTinPlate = new ItemBasic("Tin Plate", UEConfig.getItemConfigID(UniversalElectricity.CONFIGURATION, "Tin Plate", BasicComponents.ITEM_ID_PREFIX + 15), 11);

		BasicComponents.coalGenerator = ((BlockBasicMachine) BasicComponents.blockMachine).getCoalGenerator();
		BasicComponents.batteryBox = ((BlockBasicMachine) BasicComponents.blockMachine).getBatteryBox();
		BasicComponents.electricFurnace = ((BlockBasicMachine) BasicComponents.blockMachine).getElectricFurnace();

		BasicComponents.copperOreGeneration = new OreGenReplaceStone("Copper Ore", "oreCopper", new ItemStack(BasicComponents.blockBasicOre, 1, 0), 0, 50, 33, 4).enable();
		BasicComponents.tinOreGeneration = new OreGenReplaceStone("Tin Ore", "oreTin", new ItemStack(BasicComponents.blockBasicOre, 1, 1), 0, 50, 28, 3).enable();

		/**
		 * @author Cammygames
		 */
		LiquidContainerRegistry.registerLiquid(new LiquidContainerData(new LiquidStack(BasicComponents.oilStill, LiquidContainerRegistry.BUCKET_VOLUME), new ItemStack(BasicComponents.itemOilBucket), new ItemStack(Item.bucketEmpty)));
		MinecraftForge.EVENT_BUS.register(BasicComponents.itemOilBucket);

		// Register Blocks
		GameRegistry.registerBlock(BasicComponents.blockBasicOre, ItemBlockOre.class);
		GameRegistry.registerBlock(BasicComponents.blockMachine, ItemBasicMachine.class);
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

		proxy.preInit();
	}

	@Init
	public void load(FMLInitializationEvent evt)
	{
		proxy.init();

		/**
		 * Adding names
		 */
		LanguageRegistry.addName(new ItemStack(BasicComponents.blockBasicOre, 1, 0), "Copper Ore");
		LanguageRegistry.addName(new ItemStack(BasicComponents.blockBasicOre, 1, 1), "Tin Ore");

		LanguageRegistry.addName(BasicComponents.oilMoving, "Oil Moving");
		LanguageRegistry.addName(BasicComponents.oilStill, "Oil Still");
		LanguageRegistry.addName(BasicComponents.itemBattery, "Basic Battery");
		LanguageRegistry.addName(new ItemStack(BasicComponents.blockCopperWire, 1, 0), "Copper Wire");
		LanguageRegistry.addName(new ItemStack(BasicComponents.itemCircuit, 1, 0), "Basic Circuit");
		LanguageRegistry.addName(new ItemStack(BasicComponents.itemCircuit, 1, 1), "Advanced Circuit");
		LanguageRegistry.addName(new ItemStack(BasicComponents.itemCircuit, 1, 2), "Elite Circuit");
		LanguageRegistry.addName(BasicComponents.itemOilBucket, "Oil Bucket");
		LanguageRegistry.addName(BasicComponents.itemWrench, "Universal Wrench");

		LanguageRegistry.addName(BasicComponents.coalGenerator, "Coal Generator");
		LanguageRegistry.addName(BasicComponents.batteryBox, "Battery Box");
		LanguageRegistry.addName(BasicComponents.electricFurnace, "Electric Furnace");
		/**
		 * Registering Tile Entities
		 */
		GameRegistry.registerTileEntity(TileEntityBatteryBox.class, "UEBatteryBox");
		GameRegistry.registerTileEntity(TileEntityCoalGenerator.class, "UECoalGenerator");
		GameRegistry.registerTileEntity(TileEntityElectricFurnace.class, "UEElectricFurnace");

		OreGenerator.addOre(BasicComponents.copperOreGeneration);
		OreGenerator.addOre(BasicComponents.tinOreGeneration);

		// Recipes
		// Oil Bucket
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemOilBucket), new Object[]
		{ "CCC", "CBC", "CCC", 'B', Item.bucketWater, 'C', Item.coal }));
		// Motor
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemMotor), new Object[]
		{ "@!@", "!#!", "@!@", '!', "ingotSteel", '#', Item.ingotIron, '@', BasicComponents.blockCopperWire }));
		// Wrench
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemWrench), new Object[]
		{ " S ", " DS", "S  ", 'S', "ingotSteel", 'D', Item.diamond }));
		// Battery Box
		GameRegistry.addRecipe(new ShapedOreRecipe(BasicComponents.batteryBox, new Object[]
		{ "?!?", "###", "?!?", '#', BasicComponents.blockCopperWire, '!', BasicComponents.itemSteelPlate, '?', BasicComponents.itemBattery.getUncharged() }));
		GameRegistry.addSmelting(BasicComponents.batteryBox.itemID, new ItemStack(BasicComponents.itemSteelDust, 6), 0f);
		// Coal Generator
		GameRegistry.addRecipe(new ShapedOreRecipe(BasicComponents.coalGenerator, new Object[]
		{ "SCS", "FMF", "BBB", 'B', "ingotBronze", 'S', BasicComponents.itemSteelPlate, 'C', BasicComponents.blockCopperWire, 'M', BasicComponents.itemMotor, 'F', Block.stoneOvenIdle }));
		GameRegistry.addSmelting(BasicComponents.coalGenerator.itemID, new ItemStack(BasicComponents.itemSteelDust, 6), 0f);
		// Electric Furnace
		GameRegistry.addRecipe(new ShapedOreRecipe(BasicComponents.electricFurnace, new Object[]
		{ "SSS", "SCS", "SMS", 'S', "ingotSteel", 'C', BasicComponents.itemCircuit, 'M', BasicComponents.itemMotor }));
		GameRegistry.addSmelting(BasicComponents.electricFurnace.itemID, new ItemStack(BasicComponents.itemSteelDust, 6), 0f);
		// Copper
		FurnaceRecipes.smelting().addSmelting(BasicComponents.blockBasicOre.blockID, 0, new ItemStack(BasicComponents.itemCopperIngot), 0.7f);
		// Copper Wire
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.blockCopperWire, 6), new Object[]
		{ "!!!", "@@@", "!!!", '!', Item.leather, '@', "ingotCopper" }));
		// Tin
		FurnaceRecipes.smelting().addSmelting(BasicComponents.blockBasicOre.blockID, 1, new ItemStack(BasicComponents.itemTinIngot), 0.7f);
		// Battery
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemBattery), new Object[]
		{ " T ", "TRT", "TCT", 'T', "ingotTin", 'R', Item.redstone, 'C', Item.coal }));
		// Steel
		RecipeHelper.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemSteelDust), new Object[]
		{ " C ", "CIC", " C ", 'C', new ItemStack(Item.coal, 1, 1), 'I', Item.ingotIron }), "Steel Dust", UniversalElectricity.CONFIGURATION, true);
		RecipeHelper.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemSteelDust), new Object[]
		{ " C ", "CIC", " C ", 'C', new ItemStack(Item.coal, 1, 0), 'I', Item.ingotIron }), "Steel Dust", UniversalElectricity.CONFIGURATION, true);
		GameRegistry.addSmelting(BasicComponents.itemSteelDust.shiftedIndex, new ItemStack(BasicComponents.itemSteelIngot), 0.8f);
		GameRegistry.addSmelting(BasicComponents.itemSteelPlate.shiftedIndex, new ItemStack(BasicComponents.itemSteelDust, 3), 0f);
		// Bronze
		RecipeHelper.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemBronzeDust), new Object[]
		{ "!#!", '!', "ingotCopper", '#', "ingotTin" }), "Bronze Dust", UniversalElectricity.CONFIGURATION, true);
		GameRegistry.addSmelting(BasicComponents.itemBronzeDust.shiftedIndex, new ItemStack(BasicComponents.itemBronzeIngot), 0.6f);
		GameRegistry.addSmelting(BasicComponents.itemBronzePlate.shiftedIndex, new ItemStack(BasicComponents.itemBronzeDust, 3), 0f);

		// Plates
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemCopperPlate), new Object[]
		{ "!!", "!!", '!', "ingotCopper" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemTinPlate), new Object[]
		{ "!!", "!!", '!', "ingotTin" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemSteelPlate), new Object[]
		{ "!!", "!!", '!', "ingotSteel" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemBronzePlate), new Object[]
		{ "!!", "!!", '!', "ingotBronze" }));

		// Circuit
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemCircuit, 1, 0), new Object[]
		{ "!#!", "#@#", "!#!", '@', BasicComponents.itemBronzePlate, '#', Item.redstone, '!', BasicComponents.blockCopperWire }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemCircuit, 1, 0), new Object[]
		{ "!#!", "#@#", "!#!", '@', BasicComponents.itemSteelPlate, '#', Item.redstone, '!', BasicComponents.blockCopperWire }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemCircuit, 1, 1), new Object[]
		{ "@@@", "#?#", "@@@", '@', Item.redstone, '?', Item.diamond, '#', BasicComponents.itemCircuit }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BasicComponents.itemCircuit, 1, 2), new Object[]
		{ "@@@", "?#?", "@@@", '@', Item.ingotGold, '?', new ItemStack(BasicComponents.itemCircuit, 1, 1), '#', Block.blockLapis }));
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

package universalelectricity.basiccomponents;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import universalelectricity.OreGenData;
import universalelectricity.OreGenerator;
import universalelectricity.UniversalElectricity;
import universalelectricity.network.PacketManager;
import universalelectricity.recipe.RecipeManager;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * A static class where you can reference variables from here
 * @author Calclavia
 *
 */

@Mod(modid = "UniversalElectricity", name = "Universal Electricity", version = UniversalElectricity.VERSION, dependencies = "before:*")
@NetworkMod(channels = { "BasicComponents" }, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketManager.class)

public class BasicComponents
{
    public static final String FILE_PATH = "/basiccomponents/textures/";
    public static final String BLOCK_TEXTURE_FILE = FILE_PATH + "blocks.png";
    public static final String ITEM_TEXTURE_FILE = FILE_PATH + "items.png";
    
    @Instance
    public static BasicComponents instance;
    
    @SidedProxy(clientSide = "universalelectricity.basiccomponents.BCClientProxy", serverSide = "universalelectricity.basiccomponents.BCCommonProxy")
	public static BCCommonProxy proxy;
        
    /**
     * Here is where all the Universal Components are defined. You may reference to these variables.
     */
    public static final int BLOCK_ID_PREFIX = 3970;
    public static final Block blockOre = new BlockBCOre(UniversalElectricity.getBlockConfigID(UniversalElectricity.CONFIGURATION, "Copper and Tin Ores", BLOCK_ID_PREFIX-1));
    public static final Block blockCopperWire = new BlockCopperWire(UniversalElectricity.getBlockConfigID(UniversalElectricity.CONFIGURATION, "Copper_Wire", BLOCK_ID_PREFIX));
    public static final Block blockBatteryBox = new BlockBatteryBox(UniversalElectricity.getBlockConfigID(UniversalElectricity.CONFIGURATION, "Battery_Box", BLOCK_ID_PREFIX + 1), 0);
    public static final Block blockCoalGenerator = new BlockCoalGenerator(UniversalElectricity.getBlockConfigID(UniversalElectricity.CONFIGURATION, "Coal_Generator", BLOCK_ID_PREFIX + 2), 0);
    public static final Block blockElectricFurnace = new BlockElectricFurnace(UniversalElectricity.getBlockConfigID(UniversalElectricity.CONFIGURATION, "Electric_Furnace", BLOCK_ID_PREFIX + 3), 0);

    public static final int ITEM_ID_PREFIX = 13970;
    public static final Item itemBattery = new ItemBattery(UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Battery", ITEM_ID_PREFIX), 0);
    public static final Item itemWrench = new ItemWrench(UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Wrench", ITEM_ID_PREFIX+1), 20);
    public static final Item itemCopperIngot = new ItemBC("Copper Ingot", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Copper_Ingot", ITEM_ID_PREFIX+2), 1);
    public static final Item itemTinIngot = new ItemBC("Tin Ingot", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Tin_Ingot", ITEM_ID_PREFIX+3), 2);
    public static final Item itemSteelIngot = new ItemBC("Steel Ingot", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Steel_Ingot", ITEM_ID_PREFIX+4), 3);
    public static final Item itemSteelAlloy = new ItemBC("Steel Alloy", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Steel_Clump", ITEM_ID_PREFIX+5), 5);
    public static final Item itemCircuit = new ItemCircuit(UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Circuit", ITEM_ID_PREFIX+6), 16);
    public static final Item itemCopperWire = new ItemCopperWire(UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Item_Copper_Wire", ITEM_ID_PREFIX+7), 21);
    public static final Item itemBronzeIngot = new ItemBC("Bronze Ingot", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Bronze_Ingot", ITEM_ID_PREFIX+8), 7);
    public static final Item itemBronzeAlloy = new ItemBC("Bronze Alloy", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Bronze_Clump", ITEM_ID_PREFIX+9), 6);
    public static final Item itemSteelPlate = new ItemBC("Steel Plate", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Steel Plate", ITEM_ID_PREFIX+10), 9);
    public static final Item itemBronzePlate = new ItemBC("Bronze Plate", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Bronze Plate", ITEM_ID_PREFIX+11), 8);
    public static final Item itemMotor = new ItemBC("Motor", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Motor", ITEM_ID_PREFIX+12), 10);

    @PreInit
	public void preInit(FMLPreInitializationEvent event)
    {
		instance = this;
		
		UniversalElectricity.registerMod(this, "Basic Componenets", UniversalElectricity.VERSION);
		
		//Checks to make sure Forge is the correction version...
		if(ForgeVersion.getMajorVersion() != 4)
		{
			throw new RuntimeException("Forge version is way too old!");
		}
		if(ForgeVersion.getMinorVersion() < 0)
		{
			throw new RuntimeException("Forge version is kind of too old!");
		}
		
		NetworkRegistry.instance().registerGuiHandler(this, this.proxy);
		GameRegistry.registerWorldGenerator(new OreGenerator());
		MinecraftForge.EVENT_BUS.register(UniversalElectricity.electricityManager);
		
    	//Register Blocks
    	GameRegistry.registerBlock(blockOre, ItemBCOre.class);
		GameRegistry.registerBlock(blockCopperWire);
		GameRegistry.registerBlock(blockBatteryBox);
		GameRegistry.registerBlock(blockCoalGenerator);
		GameRegistry.registerBlock(blockElectricFurnace);

		proxy.preInit();
    }
    
    @Init
	public void load(FMLInitializationEvent evt)
    {
    	proxy.init();
    					
    	LanguageRegistry.addName(new ItemStack(blockOre, 1, 0), "Copper Ore");
		LanguageRegistry.addName(new ItemStack(blockOre, 1, 1), "Tin Ore");

		LanguageRegistry.addName(itemBattery, "Basic Battery");
		LanguageRegistry.addName(blockCopperWire, "Copper Wire");
		LanguageRegistry.addName(new ItemStack(itemCircuit, 1, 0), "Basic Circuit");
        LanguageRegistry.addName(new ItemStack(itemCircuit, 1, 1), "Advanced Circuit");
        LanguageRegistry.addName(new ItemStack(itemCircuit, 1, 2), "Elite Circuit");

        LanguageRegistry.addName(blockBatteryBox, "Battery Box");
        LanguageRegistry.addName(blockCoalGenerator, "Coal Generator");
        LanguageRegistry.addName(blockElectricFurnace, "Electric Furnace");
		
		//Register Tile Entities
		GameRegistry.registerTileEntity(TileEntityBatteryBox.class, "TileEntityBatteryBox");
		GameRegistry.registerTileEntity(TileEntityCoalGenerator.class, "TileEntityCoalGenerator");
		GameRegistry.registerTileEntity(TileEntityElectricFurnace.class, "TileEntityElectricFurnace");
		
		OreDictionary.registerOre("ingotCopper", itemCopperIngot);
		OreDictionary.registerOre("ingotTin", itemTinIngot);
		OreDictionary.registerOre("ingotBronze", itemBronzeIngot);
		OreDictionary.registerOre("ingotSteel", itemSteelIngot);
		
		OreGenerator.ORES_TO_GENERATE.add(new OreGenData("Copper Ore", "oreCopper", new ItemStack(blockOre, 1, 0), 60, 40, 5));
		OreGenerator.ORES_TO_GENERATE.add(new OreGenData("Tin Ore", "oreTin", new ItemStack(blockOre, 1, 1), 60, 33, 4));
		
		//Recipes
		//Motor
		RecipeManager.addRecipe(new ItemStack(itemMotor), new Object [] {"@!@", "!#!", "@!@", '!', itemSteelPlate, '#', itemCircuit, '@', itemCopperWire});
		//Wrench
		RecipeManager.addRecipe(new ItemStack(itemWrench), new Object [] {"! !", " ! ", " ! ", '!', "ingotSteel"});
		//Battery Box
		RecipeManager.addRecipe(blockBatteryBox, new Object [] {"!?!", "???", "!?!", '!', itemSteelPlate, '?', (((ItemBattery)itemBattery).getChargedItemStack()) });
		RecipeManager.addShapelessRecipe(new ItemStack(itemSteelPlate, 4), new Object [] {blockBatteryBox});
		//Coal Generator
		RecipeManager.addRecipe(blockCoalGenerator, new Object [] {"!@!", "$#$", "!?!", '!', itemSteelPlate, '@', itemCopperWire, '?', itemCircuit, '#', itemMotor, '$', Block.stoneOvenIdle});
		RecipeManager.addShapelessRecipe(new ItemStack(itemSteelPlate, 4), new Object [] {blockCoalGenerator});
		//Electric Furnace
		RecipeManager.addRecipe(blockElectricFurnace, new Object [] {"!!!", "!?!", "!#!", '!', "ingotSteel", '#', itemCircuit, '?', itemSteelPlate});
		RecipeManager.addShapelessRecipe(new ItemStack(itemSteelIngot, 7), new Object [] {blockElectricFurnace});
		//Copper
		RecipeManager.addSmelting(new ItemStack(blockOre, 1, 0), new ItemStack(itemCopperIngot));
		RecipeManager.addRecipe(new ItemStack(itemCopperWire, 6), new Object [] {"!!!", "@@@", "!!!", '!', Block.cloth, '@', "ingotCopper"});
		//Tin
		RecipeManager.addSmelting(new ItemStack(blockOre, 1, 1), new ItemStack(itemTinIngot));
		RecipeManager.addRecipe(new ItemStack(itemBattery), new Object [] {" ! ", "!#!", "!?!", '!', "ingotTin", '?', Item.redstone, '#', Item.coal});
		//Steel
		RecipeManager.addRecipe(new ItemStack(itemSteelAlloy), new Object [] {"!#!", '!', new ItemStack(Item.coal, 1, 1), '#', Item.ingotIron});
		RecipeManager.addSmelting(itemSteelAlloy, new ItemStack(itemSteelIngot));
		RecipeManager.addRecipe(new ItemStack(itemSteelPlate), new Object [] {"!!", "!!", '!', "ingotSteel"});
		RecipeManager.addShapelessRecipe(new ItemStack(itemSteelIngot, 3), new Object [] {itemSteelPlate});
		//Bronze
		RecipeManager.addRecipe(itemBronzeAlloy, new Object [] {"!#!", '!', "ingotCopper",  '#', "ingotTin"});
		RecipeManager.addSmelting(itemBronzeAlloy, new ItemStack(itemBronzeIngot));
		RecipeManager.addRecipe(new ItemStack(itemBronzePlate), new Object [] {"!!", "!!", '!', "ingotBronze"});
		RecipeManager.addShapelessRecipe(new ItemStack(itemBronzeIngot, 3), new Object [] {itemSteelPlate});
		//Circuit
		RecipeManager.addRecipe(new ItemStack(itemCircuit, 1, 0), new Object [] {"!#!", "?@?", "!#!", '@', itemSteelPlate, '?', Item.ingotGold, '#', Item.redstone, '!', itemCopperWire});
		RecipeManager.addRecipe(new ItemStack(itemCircuit, 1, 1), new Object [] {"@@@", "#?#", "@@@", '@', Item.redstone, '?', Item.diamond, '#', itemCircuit});
		RecipeManager.addRecipe(new ItemStack(itemCircuit, 1, 2), new Object [] {"@@@", "?#?", "@@@", '@', Item.ingotGold, '?', new ItemStack(itemCircuit, 1, 1), '#', Block.blockLapis});
    	
    }
    
    @PostInit
   	public void modsLoaded(FMLPostInitializationEvent evt) 
	{
    	RecipeManager.addRecipes();
	}
}

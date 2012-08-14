package universalelectricity.basiccomponents;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.oredict.OreDictionary;
import universalelectricity.OreGenData;
import universalelectricity.OreGenerator;
import universalelectricity.UniversalElectricity;
import universalelectricity.electricity.ElectricityManager;
import universalelectricity.network.PacketManager;
import universalelectricity.recipe.RecipeManager;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;

/**
 * A static class where you can reference variables from here
 * @author Calclavia
 *
 */

@Mod(modid = "UniversalElectricity", name = "Universal Electricity", version = UniversalElectricity.version)
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
    
    public static final Block blockOre = new BlockBCOre(UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "Copper and Tin Ores", BLOCK_ID_PREFIX-1, true));
    public static final Block blockCopperWire = new BlockCopperWire(UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "Copper_Wire", BLOCK_ID_PREFIX, true));
    public static final Block blockBatteryBox = new BlockBatteryBox(UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "Battery_Box", BLOCK_ID_PREFIX + 1, true), 0);
    public static final Block blockCoalGenerator = new BlockCoalGenerator(UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "Coal_Generator", BLOCK_ID_PREFIX + 2, true), 0);
    public static final Block blockElectricFurnace = new BlockElectricFurnace(UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "Electric_Furnace", BLOCK_ID_PREFIX + 3, true), 0);

    public static final Item itemBattery = new ItemBattery(UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "Battery", 1586, false), 0);
    public static final Item itemWrench = new ItemWrench(UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "Wrench", 1587, false), 20);
    public static final Item itemCopperIngot = new BCItem("Copper Ingot", UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "Copper_Ingot", 1588, false), 1);
    public static final Item itemTinIngot = new BCItem("Tin Ingot", UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "Tin_Ingot", 1589, false), 2);
    public static final Item itemSteelIngot = new BCItem("Steel Ingot", UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "Steel_Ingot", 1590, false), 3);
    public static final Item itemSteelAlloy = new BCItem("Steel Alloy", UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "Steel_Clump", 1591, false), 5);
    public static final Item itemCircuit = new ItemCircuit(UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "Circuit", 1592, false), 16);
    public static final Item itemCopperWire = new ItemCopperWire(UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "Item_Copper_Wire", 1593, false), 21);
    public static final Item itemBronzeIngot = new BCItem("Bronze Ingot", UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "Bronze_Ingot", 1594, false), 7);
    public static final Item itemBronzeAlloy = new BCItem("Bronze Alloy", UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "Bronze_Clump", 1595, false), 6);
    public static final Item itemSteelPlate = new BCItem("Steel Plate", UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "Steel_Plate", 1597, false), 8);
    public static final Item itemMotor = new BCItem("Motor", UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "Motor", 1598, false), 9);

    @PreInit
	public void preInit(FMLPreInitializationEvent event)
    {    	
		instance = this;
		
		UniversalElectricity.registerMod(this, "Basic Componenets", UniversalElectricity.getVersion());
		
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
    	TickRegistry.registerTickHandler(new ElectricityManager(), Side.CLIENT);
    	TickRegistry.registerTickHandler(new ElectricityManager(), Side.SERVER);

		proxy.preInit();
    }
    
    @Init
	public void load(FMLInitializationEvent evt)
    {
    	proxy.init();
    	
		//Register Blocks
    	GameRegistry.registerBlock(blockOre, ItemBCOre.class);
		GameRegistry.registerBlock(blockCopperWire);
		GameRegistry.registerBlock(blockBatteryBox);
		GameRegistry.registerBlock(blockCoalGenerator);
		GameRegistry.registerBlock(blockElectricFurnace);
				
		ModLoader.addName(new ItemStack(blockOre, 1, 0), "Copper Ore");
		ModLoader.addName(new ItemStack(blockOre, 1, 1), "Tin Ore");

		ModLoader.addName(itemBattery, "Basic Battery");
		ModLoader.addName(blockCopperWire, "Copper Wire");
		ModLoader.addName(new ItemStack(itemCircuit, 1, 0), "Basic Circuit");
        ModLoader.addName(new ItemStack(itemCircuit, 1, 1), "Advanced Circuit");
        ModLoader.addName(new ItemStack(itemCircuit, 1, 2), "Elite Circuit");

        ModLoader.addName(blockBatteryBox, "Battery Box");
        ModLoader.addName(blockCoalGenerator, "Coal Generator");
        ModLoader.addName(blockElectricFurnace, "Electric Furnace");
		
		//Register Tile Entities
		ModLoader.registerTileEntity(TileEntityBatteryBox.class, "TileEntityBatteryBox");
		ModLoader.registerTileEntity(TileEntityCoalGenerator.class, "TileEntityCoalGenerator");
		ModLoader.registerTileEntity(TileEntityElectricFurnace.class, "TileEntityElectricFurnace");
		
		OreDictionary.registerOre("ingotCopper", itemCopperIngot);
		OreDictionary.registerOre("ingotTin", itemTinIngot);
		OreDictionary.registerOre("ingotBronze", itemBronzeIngot);
		OreDictionary.registerOre("ingotSteel", itemSteelIngot);
		
		OreGenerator.ORES_TO_GENERATE.add(new OreGenData("Copper Ore", "oreCopper", new ItemStack(blockOre, 1, 0), 60, 40, 5));
		OreGenerator.ORES_TO_GENERATE.add(new OreGenData("Tin Ore", "oreTin", new ItemStack(blockOre, 1, 1), 60, 30, 3));
		
		//Recipes
		//Motor
		RecipeManager.addRecipe(new ItemStack(itemMotor), new Object [] {"@!@", "!#!", "@!@", '!', itemSteelPlate, '#', itemCircuit, '@', itemCopperWire});
		//Wrench
		RecipeManager.addRecipe(new ItemStack(itemWrench), new Object [] {"! !", " ! ", " ! ", '!', "ingotSteel"});
		//Battery Box
		RecipeManager.addRecipe(blockBatteryBox, new Object [] {"!?!", "???", "!?!", '!', itemSteelPlate, '?', (((ItemBattery)itemBattery).getChargedItemStack()) });
		//Coal Generator
		RecipeManager.addRecipe(blockCoalGenerator, new Object [] {"!@!", "$#$", "!?!", '!', itemSteelPlate, '@', itemCopperWire, '?', itemCircuit, '#', itemMotor, '$', Block.stoneOvenIdle});
		//Electric Furnace
		RecipeManager.addRecipe(blockElectricFurnace, new Object [] {"!!!", "!?!", "!#!", '!', "ingotSteel", '#', itemCircuit, '?', itemSteelPlate});
		//Copper
		RecipeManager.addSmelting(new ItemStack(blockOre, 1, 0), new ItemStack(itemCopperIngot));
		RecipeManager.addRecipe(new ItemStack(itemCopperWire, 6), new Object [] {"!!!", "@@@", "!!!", '!', Block.cloth, '@', "ingotCopper"});
		//Tin
		RecipeManager.addSmelting(new ItemStack(blockOre, 1, 1), new ItemStack(itemTinIngot));
		RecipeManager.addRecipe(new ItemStack(itemBattery), new Object [] {" ! ", "!#!", "!?!", '!', "ingotTin", '?', Item.redstone, '#', Item.coal});
		//Steel
		RecipeManager.addRecipe(new ItemStack(itemSteelAlloy), new Object [] {"!#!", '!', new ItemStack(Item.coal, 1, 1), '#', Item.ingotIron});
		RecipeManager.addSmelting(itemSteelAlloy, new ItemStack(itemSteelIngot));
		RecipeManager.addRecipe(new ItemStack(itemSteelPlate), new Object [] {" ! ", "!!!", " ! ", '!', "ingotSteel"});
		//Bronze
		RecipeManager.addRecipe(itemBronzeAlloy, new Object [] {"!#!", '!', "ingotCopper",  '#', "ingotTin"});
		RecipeManager.addSmelting(itemBronzeAlloy, new ItemStack(itemBronzeIngot));
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

package universalelectricity.basiccomponents;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import universalelectricity.OreGenBase;
import universalelectricity.OreGenReplaceStone;
import universalelectricity.OreGenerator;
import universalelectricity.UniversalElectricity;
import universalelectricity.network.PacketManager;
import universalelectricity.recipe.RecipeManager;
import buildcraft.api.liquids.LiquidData;
import buildcraft.api.liquids.LiquidManager;
import buildcraft.api.liquids.LiquidStack;
import cpw.mods.fml.common.ICraftingHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * The main class for managing Basic Component items and blocks.
 * @author Calclavia
 *
 */

@Mod(modid = "BasicComponents", name = "Basic Components", version = BasicComponents.VERSION, dependencies = "before:*")
@NetworkMod(channels = { "BasicComponents" }, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketManager.class)

public class BasicComponents implements ICraftingHandler
{
    protected static final String VERSION = "0.8.2";

    public static final String FILE_PATH = "/basiccomponents/textures/";
    public static final String BLOCK_TEXTURE_FILE = FILE_PATH + "blocks.png";
    public static final String ITEM_TEXTURE_FILE = FILE_PATH + "items.png";
    
    public static BasicComponents instance;
    
    @SidedProxy(clientSide = "universalelectricity.basiccomponents.BCClientProxy", serverSide = "universalelectricity.basiccomponents.BCCommonProxy")
	public static BCCommonProxy proxy;
    
    /**
     * Here is where all the Universal Components are defined. You may reference to these variables.
    */
    public static final int BLOCK_ID_PREFIX = 3970;
    public static final Block blockOre = new BlockBCOre(UniversalElectricity.getBlockConfigID(UniversalElectricity.CONFIGURATION, "Copper and Tin Ores", BLOCK_ID_PREFIX));
    public static final Block blockCopperWire = new BlockCopperWire(UniversalElectricity.getBlockConfigID(UniversalElectricity.CONFIGURATION, "Copper_Wire", BLOCK_ID_PREFIX + 1));
    public static final Block oilMoving = new BlockOilFlowing(UniversalElectricity.getBlockConfigID(UniversalElectricity.CONFIGURATION, "Oil_Flowing", BLOCK_ID_PREFIX + 2));
    public static final Block oilStill = new BlockOilStill(UniversalElectricity.getBlockConfigID(UniversalElectricity.CONFIGURATION, "Oil_Still", BLOCK_ID_PREFIX + 3));
    public static final BlockBasicMachine blockMachine = new BlockBasicMachine(UniversalElectricity.getBlockConfigID(UniversalElectricity.CONFIGURATION, "Basic Machine", BLOCK_ID_PREFIX + 4), 0);
    
    public static final int ITEM_ID_PREFIX = 13970;
    public static final Item itemBattery = new ItemBattery(UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Battery", ITEM_ID_PREFIX+1), 0);
    public static final Item itemWrench = new ItemWrench(UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Wrench", ITEM_ID_PREFIX+2), 20);
    public static final Item itemCopperIngot = new ItemBC("Copper Ingot", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Copper Ingot", ITEM_ID_PREFIX+3), 1);
    public static final Item itemTinIngot = new ItemBC("Tin Ingot", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Tin Ingot", ITEM_ID_PREFIX+4), 2);
    public static final Item itemSteelIngot = new ItemBC("Steel Ingot", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Steel Ingot", ITEM_ID_PREFIX+5), 3);
    public static final Item itemSteelDust = new ItemBC("Steel Dust", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Steel Dust", ITEM_ID_PREFIX+6), 5);
    public static final Item itemCircuit = new ItemCircuit(UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Circuit", ITEM_ID_PREFIX+7), 16);
    public static final Item itemBronzeIngot = new ItemBC("Bronze Ingot", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Bronze Ingot", ITEM_ID_PREFIX+8), 7);
    public static final Item itemBronzeDust = new ItemBC("Bronze Dust", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Bronze Dust", ITEM_ID_PREFIX+9), 6);
    public static final Item itemSteelPlate = new ItemBC("Steel Plate", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Steel Plate", ITEM_ID_PREFIX+10), 9);
    public static final Item itemBronzePlate = new ItemBC("Bronze Plate", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Bronze Plate", ITEM_ID_PREFIX+11), 8);
    public static final Item itemMotor = new ItemBC("Motor", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Motor", ITEM_ID_PREFIX+12), 12);
    public static final Item itemOilBucket = new ItemOilBucket("Oil Bucket", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Oil Bucket", ITEM_ID_PREFIX+13), 4);
    public static final Item itemCopperPlate = new ItemBC("Copper Plate", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Copper Plate", ITEM_ID_PREFIX+14), 10);
    public static final Item itemTinPlate = new ItemBC("Tin Plate", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Tin Plate", ITEM_ID_PREFIX+15), 11);

    
    public static final OreGenBase copperOreGeneration = new OreGenReplaceStone("Copper Ore", "oreCopper", new ItemStack(blockOre, 1, 0), 0, 60, 50, 5);
    public static final OreGenBase tinOreGeneration = new OreGenReplaceStone("Tin Ore", "oreTin", new ItemStack(blockOre, 1, 1), 0, 60, 40, 4);
    
    @PreInit
	public void preInit(FMLPreInitializationEvent event)
    {
		instance = this;
		
		UniversalElectricity.forgeLock(4, 1, 2, true);
		UniversalElectricity.registerMod(this, "Basic Componenets", VERSION);
		
		/**
		 * @author Cammygames
		 * Thanks to Cammygames for writing the Liquid Manager for UE oil.
		 */
		LiquidManager.liquids.add(new LiquidData(new LiquidStack(oilStill, LiquidManager.BUCKET_VOLUME), new LiquidStack(oilMoving, LiquidManager.BUCKET_VOLUME), new ItemStack(itemOilBucket), new ItemStack(Item.bucketEmpty)));
		MinecraftForge.EVENT_BUS.register(itemOilBucket);		
		
		NetworkRegistry.instance().registerGuiHandler(this, this.proxy);

    	//Register Blocks
    	GameRegistry.registerBlock(blockOre, ItemBCOre.class);
		GameRegistry.registerBlock(blockMachine, ItemBasicMachine.class);
		GameRegistry.registerBlock(oilMoving);
		GameRegistry.registerBlock(oilStill);
		GameRegistry.registerBlock(blockCopperWire);
		GameRegistry.registerCraftingHandler(this);
		
		OreDictionary.registerOre("ingotCopper", itemCopperIngot);
		OreDictionary.registerOre("ingotTin", itemTinIngot);
		OreDictionary.registerOre("ingotBronze", itemBronzeIngot);
		OreDictionary.registerOre("ingotSteel", itemSteelIngot);
		
		proxy.preInit();
    }
    
    @Init
	public void load(FMLInitializationEvent evt)
    {
    	proxy.init();
    					
    	LanguageRegistry.addName(new ItemStack(blockOre, 1, 0), "Copper Ore");
		LanguageRegistry.addName(new ItemStack(blockOre, 1, 1), "Tin Ore");

		LanguageRegistry.addName(oilMoving, "Oil Moving");
		LanguageRegistry.addName(oilStill, "Oil Still");
		LanguageRegistry.addName(itemBattery, "Basic Battery");
		LanguageRegistry.addName(blockCopperWire, "Copper Wire");
		LanguageRegistry.addName(new ItemStack(itemCircuit, 1, 0), "Basic Circuit");
        LanguageRegistry.addName(new ItemStack(itemCircuit, 1, 1), "Advanced Circuit");
        LanguageRegistry.addName(new ItemStack(itemCircuit, 1, 2), "Elite Circuit");
        LanguageRegistry.addName(itemOilBucket, "Oil Bucket");
        
        LanguageRegistry.addName(blockMachine.getCoalGenerator(), "Coal Generator");
        LanguageRegistry.addName(blockMachine.getBatteryBox(), "Battery Box");
		LanguageRegistry.addName(blockMachine.getElectricFurnace(), "Electric Furnace");
		
		GameRegistry.registerTileEntity(TileEntityBatteryBox.class, "TileEntityBatteryBox");
		GameRegistry.registerTileEntity(TileEntityCoalGenerator.class, "TileEntityCoalGenerator");
		GameRegistry.registerTileEntity(TileEntityElectricFurnace.class, "TileEntityElectricFurnace");		
		
		OreGenerator.addOre(copperOreGeneration);
		OreGenerator.addOre(tinOreGeneration);
		
		copperOreGeneration.enable();
		tinOreGeneration.enable();
		
		//Recipes
		//Oil Bucket
		RecipeManager.addRecipe(new ItemStack(itemOilBucket), new Object [] {"CCC", "CBC", "CCC", 'B', Item.bucketWater, 'C', Item.coal});
		//Motor
		RecipeManager.addRecipe(new ItemStack(itemMotor), new Object [] {"@!@", "!#!", "@!@", '!', "ingotSteel", '#', Item.ingotIron, '@', blockCopperWire});
		//Wrench
		RecipeManager.addRecipe(new ItemStack(itemWrench), new Object [] {" S ", " SS", "S  ", 'S', "ingotSteel"});
		//Battery Box
		RecipeManager.addRecipe(blockMachine.getBatteryBox(), new Object [] {"?!?", "#?#", "?!?", '#', blockCopperWire,'!', itemSteelPlate, '?', (((ItemBattery)itemBattery).getUnchargedItemStack()) });
		RecipeManager.addSmelting(blockMachine.getBatteryBox(), new ItemStack(itemSteelDust, 6));
		//Coal Generator
		RecipeManager.addRecipe(blockMachine.getCoalGenerator(), new Object [] {"!@!", "$#$", "???", '?', "ingotBronze", '!', itemSteelPlate, '@', blockCopperWire, '#', itemMotor, '$', Block.stoneOvenIdle});
		RecipeManager.addSmelting(blockMachine.getCoalGenerator(), new ItemStack(itemSteelDust, 6));
		//Electric Furnace
		RecipeManager.addRecipe(blockMachine.getElectricFurnace(), new Object [] {"!!!", "!?!", "!#!", '!', "ingotSteel", '?', itemCircuit, '#', itemMotor});
		RecipeManager.addSmelting(blockMachine.getElectricFurnace(), new ItemStack(itemSteelDust, 6));
		//Copper
		RecipeManager.addSmelting(new ItemStack(blockOre, 1, 0), new ItemStack(itemCopperIngot));
		//Copper Wire
		RecipeManager.addRecipe(new ItemStack(blockCopperWire, 7), new Object [] {"!!!", "@@@", "!!!", '!', Block.cloth, '@', "ingotCopper"});
		//Tin
		RecipeManager.addSmelting(new ItemStack(blockOre, 1, 1), new ItemStack(itemTinIngot));
		//Battery
		RecipeManager.addRecipe(new ItemStack(itemBattery), new Object [] {" T ", "TRT", "TCT", 'T', "ingotTin", 'R', Item.redstone, 'C', Item.coal});
		//Steel
		RecipeManager.addRecipe(new ItemStack(itemSteelDust), new Object [] {"!#!", '!', new ItemStack(Item.coal, 1, 1), '#', Item.ingotIron});
		RecipeManager.addSmelting(itemSteelDust, new ItemStack(itemSteelIngot));
		RecipeManager.addSmelting(itemSteelPlate, new ItemStack(itemSteelDust, 3));
		//Bronze
		RecipeManager.addRecipe(itemBronzeDust, new Object [] {"!#!", '!', "ingotCopper",  '#', "ingotTin"});
		RecipeManager.addSmelting(itemBronzeDust, new ItemStack(itemBronzeIngot));
		RecipeManager.addSmelting(itemBronzePlate, new ItemStack(itemBronzeDust, 3));
		
		//Plates
		RecipeManager.addRecipe(new ItemStack(itemCopperPlate), new Object [] {"!!", "!!", '!', "ingotCopper"});
		RecipeManager.addRecipe(new ItemStack(itemTinPlate), new Object [] {"!!", "!!", '!', "ingotTin"});
		RecipeManager.addRecipe(new ItemStack(itemSteelPlate), new Object [] {"!!", "!!", '!', "ingotSteel"});
		RecipeManager.addRecipe(new ItemStack(itemBronzePlate), new Object [] {"!!", "!!", '!', "ingotBronze"});

		//Circuit
		RecipeManager.addRecipe(new ItemStack(itemCircuit, 1, 0), new Object [] {"!#!", "#@#", "!#!", '@', itemSteelPlate, '#', Item.redstone, '!', blockCopperWire});
		RecipeManager.addRecipe(new ItemStack(itemCircuit, 1, 1), new Object [] {"@@@", "#?#", "@@@", '@', Item.redstone, '?', Item.diamond, '#', itemCircuit});
		RecipeManager.addRecipe(new ItemStack(itemCircuit, 1, 2), new Object [] {"@@@", "?#?", "@@@", '@', Item.ingotGold, '?', new ItemStack(itemCircuit, 1, 1), '#', Block.blockLapis});
    }

	@Override
	public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix)
	{
		if(item.itemID == itemOilBucket.shiftedIndex)
		{
			for(int i = 0; i < craftMatrix.getSizeInventory(); i++)
			{
				if(craftMatrix.getStackInSlot(i) != null)
				{
					if(craftMatrix.getStackInSlot(i).itemID == Item.bucketWater.shiftedIndex)
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

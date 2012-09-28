package universalelectricity.basiccomponents;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Save;
import net.minecraftforge.oredict.OreDictionary;
import universalelectricity.BasicComponents;
import universalelectricity.UniversalElectricity;
import universalelectricity.electricity.ElectricityManager;
import universalelectricity.implement.UEDamageSource;
import universalelectricity.network.ConnectionHandler;
import universalelectricity.network.PacketManager;
import universalelectricity.ore.OreGenReplaceStone;
import universalelectricity.ore.OreGenerator;
import universalelectricity.recipe.RecipeManager;
import buildcraft.api.liquids.LiquidData;
import buildcraft.api.liquids.LiquidManager;
import buildcraft.api.liquids.LiquidStack;
import cpw.mods.fml.common.ICraftingHandler;
import cpw.mods.fml.common.Loader;
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
 * The main class that loads up Universal Electricity. It also loads up Basic Components and everything required
 * for Universal Electricity to run properly.
 */
@Mod(modid = "UniversalElectricity", name = "Universal Electricity", version = UniversalElectricity.VERSION)
@NetworkMod(channels = "BasicComponents", clientSideRequired = true, serverSideRequired = false, connectionHandler = ConnectionHandler.class, packetHandler = PacketManager.class)

public class UELoader implements ICraftingHandler
{
    public static final String FILE_PATH = "/basiccomponents/textures/";
    public static final String BLOCK_TEXTURE_FILE = FILE_PATH + "blocks.png";
    public static final String ITEM_TEXTURE_FILE = FILE_PATH + "items.png";
    
    @Instance("UniversalElectricity")
    public static UELoader instance;
    
    @SidedProxy(clientSide = "universalelectricity.basiccomponents.BCClientProxy", serverSide = "universalelectricity.basiccomponents.BCCommonProxy")
	public static BCCommonProxy proxy;

    @PreInit
	public void preInit(FMLPreInitializationEvent event)
    {	
		UniversalElectricity.forgeLock(4, 1, 4);
   		GameRegistry.registerWorldGenerator(new OreGenerator());
		NetworkRegistry.instance().registerGuiHandler(this, this.proxy);
   		MinecraftForge.EVENT_BUS.register(this);

		/**
		 * Define the items and blocks.
		 */
		BasicComponents.blockBasicOre = new BlockBCOre(UniversalElectricity.getBlockConfigID(UniversalElectricity.CONFIGURATION, "Copper and Tin Ores", BasicComponents.BLOCK_ID_PREFIX));
	    BasicComponents.blockCopperWire = new BlockCopperWire(UniversalElectricity.getBlockConfigID(UniversalElectricity.CONFIGURATION, "Copper_Wire", BasicComponents.BLOCK_ID_PREFIX + 1));
	    BasicComponents.oilMoving = new BlockOilFlowing(UniversalElectricity.getBlockConfigID(UniversalElectricity.CONFIGURATION, "Oil_Flowing", BasicComponents.BLOCK_ID_PREFIX + 2));
	    BasicComponents.oilStill = new BlockOilStill(UniversalElectricity.getBlockConfigID(UniversalElectricity.CONFIGURATION, "Oil_Still", BasicComponents.BLOCK_ID_PREFIX + 3));
	    BasicComponents.blockMachine = new BlockBasicMachine(UniversalElectricity.getBlockConfigID(UniversalElectricity.CONFIGURATION, "Basic Machine", BasicComponents.BLOCK_ID_PREFIX + 4), 0);
	    
	    BasicComponents.itemBattery = new ItemBattery(UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Battery", BasicComponents.ITEM_ID_PREFIX+1), 0);
	    BasicComponents.itemWrench = new ItemWrench(UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Wrench", BasicComponents.ITEM_ID_PREFIX+2), 20);
	    BasicComponents.itemCopperIngot = new ItemBasic("Copper Ingot", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Copper Ingot", BasicComponents.ITEM_ID_PREFIX+3), 1);
	    BasicComponents.itemTinIngot = new ItemBasic("Tin Ingot", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Tin Ingot", BasicComponents.ITEM_ID_PREFIX+4), 2);
	    BasicComponents.itemSteelIngot = new ItemBasic("Steel Ingot", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Steel Ingot", BasicComponents.ITEM_ID_PREFIX+5), 3);
	    BasicComponents.itemSteelDust = new ItemBasic("Steel Dust", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Steel Dust", BasicComponents.ITEM_ID_PREFIX+6), 5);
	    BasicComponents.itemCircuit = new ItemCircuit(UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Circuit", BasicComponents.ITEM_ID_PREFIX+7), 16);
	    BasicComponents.itemBronzeIngot = new ItemBasic("Bronze Ingot", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Bronze Ingot", BasicComponents.ITEM_ID_PREFIX+8), 7);
	    BasicComponents.itemBronzeDust = new ItemBasic("Bronze Dust", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Bronze Dust", BasicComponents.ITEM_ID_PREFIX+9), 6);
	    BasicComponents.itemSteelPlate = new ItemBasic("Steel Plate", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Steel Plate", BasicComponents.ITEM_ID_PREFIX+10), 9);
	    BasicComponents.itemBronzePlate = new ItemBasic("Bronze Plate", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Bronze Plate", BasicComponents.ITEM_ID_PREFIX+11), 8);
	    BasicComponents.itemMotor = new ItemBasic("Motor", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Motor", BasicComponents.ITEM_ID_PREFIX+12), 12);
	    BasicComponents.itemOilBucket = new ItemOilBucket("Oil Bucket", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Oil Bucket", BasicComponents.ITEM_ID_PREFIX+13), 4);
	    BasicComponents.itemCopperPlate = new ItemBasic("Copper Plate", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Copper Plate", BasicComponents.ITEM_ID_PREFIX+14), 10);
	    BasicComponents.itemTinPlate = new ItemBasic("Tin Plate", UniversalElectricity.getItemConfigID(UniversalElectricity.CONFIGURATION, "Tin Plate", BasicComponents.ITEM_ID_PREFIX+15), 11);

	    BasicComponents.coalGenerator = ((BlockBasicMachine)BasicComponents.blockMachine).getCoalGenerator();
	    BasicComponents.batteryBox = ((BlockBasicMachine)BasicComponents.blockMachine).getBatteryBox();
	    BasicComponents.electricFurnace = ((BlockBasicMachine)BasicComponents.blockMachine).getElectricFurnace();
	    
	    BasicComponents.copperOreGeneration = new OreGenReplaceStone("Copper Ore", "oreCopper", new ItemStack(BasicComponents.blockBasicOre, 1, 0), 0, 60, 50, 6);
	    BasicComponents.tinOreGeneration = new OreGenReplaceStone("Tin Ore", "oreTin", new ItemStack(BasicComponents.blockBasicOre, 1, 1), 0, 60, 45, 5);
	    
		/**
		 * @author Cammygames
		 * Thanks to Cammygames for writing the Liquid Manager for UE oil.
		 */
		LiquidManager.liquids.add(new LiquidData(new LiquidStack(BasicComponents.oilStill, LiquidManager.BUCKET_VOLUME), new LiquidStack(BasicComponents.oilMoving, LiquidManager.BUCKET_VOLUME), new ItemStack(BasicComponents.itemOilBucket), new ItemStack(Item.bucketEmpty)));
		MinecraftForge.EVENT_BUS.register(BasicComponents.itemOilBucket);	
	    
    	//Register Blocks
    	GameRegistry.registerBlock(BasicComponents.blockBasicOre, ItemOre.class);
		GameRegistry.registerBlock(BasicComponents.blockMachine, ItemBasicMachine.class);
		GameRegistry.registerBlock(BasicComponents.blockCopperWire, ItemCopperWire.class);
		GameRegistry.registerBlock(BasicComponents.oilMoving);
		GameRegistry.registerBlock(BasicComponents.oilStill);
		GameRegistry.registerCraftingHandler(this);
		
		OreDictionary.registerOre("ingotCopper", BasicComponents.itemCopperIngot);
		OreDictionary.registerOre("ingotTin", BasicComponents.itemTinIngot);
		OreDictionary.registerOre("ingotBronze", BasicComponents.itemBronzeIngot);
		OreDictionary.registerOre("ingotSteel", BasicComponents.itemSteelIngot);
		
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
        
        LanguageRegistry.addName(BasicComponents.coalGenerator, "Coal Generator");
        LanguageRegistry.addName(BasicComponents.batteryBox, "Battery Box");
		LanguageRegistry.addName(BasicComponents.electricFurnace, "Electric Furnace");
		/**
		 * Registering Tile Entities
		 */
		GameRegistry.registerTileEntity(TileEntityBatteryBox.class, "TileEntityBatteryBox");
		GameRegistry.registerTileEntity(TileEntityCoalGenerator.class, "TileEntityCoalGenerator");
		GameRegistry.registerTileEntity(TileEntityElectricFurnace.class, "TileEntityElectricFurnace");	
		
		BasicComponents.copperOreGeneration.enable();
		BasicComponents.tinOreGeneration.enable();
		
		OreGenerator.addOre(BasicComponents.copperOreGeneration);
		OreGenerator.addOre(BasicComponents.tinOreGeneration);
		
		//Recipes
		//Oil Bucket
		RecipeManager.addRecipe(new ItemStack(BasicComponents.itemOilBucket), new Object [] {"CCC", "CBC", "CCC", 'B', Item.bucketWater, 'C', Item.coal});
		//Motor
		RecipeManager.addRecipe(new ItemStack(BasicComponents.itemMotor), new Object [] {"@!@", "!#!", "@!@", '!', "ingotSteel", '#', Item.ingotIron, '@', BasicComponents.blockCopperWire});
		//Wrench
		RecipeManager.addRecipe(new ItemStack(BasicComponents.itemWrench), new Object [] {" S ", " SS", "S  ", 'S', "ingotSteel"});
		//Battery Box
		RecipeManager.addRecipe(BasicComponents.batteryBox, new Object [] {"?!?", "#?#", "?!?", '#', BasicComponents.blockCopperWire,'!', BasicComponents.itemSteelPlate, '?', BasicComponents.itemBattery.getUnchargedItemStack()});
		RecipeManager.addSmelting(BasicComponents.batteryBox, new ItemStack(BasicComponents.itemSteelDust, 6));
		//Coal Generator
		RecipeManager.addRecipe(BasicComponents.coalGenerator, new Object [] {"!@!", "$#$", "???", '?', "ingotBronze", '!', BasicComponents.itemSteelPlate, '@', BasicComponents.blockCopperWire, '#', BasicComponents.itemMotor, '$', Block.stoneOvenIdle});
		RecipeManager.addSmelting(BasicComponents.coalGenerator, new ItemStack(BasicComponents.itemSteelDust, 6));
		//Electric Furnace
		RecipeManager.addRecipe(BasicComponents.electricFurnace, new Object [] {"!!!", "!?!", "!#!", '!', "ingotSteel", '?', BasicComponents.itemCircuit, '#', BasicComponents.itemMotor});
		RecipeManager.addSmelting(BasicComponents.electricFurnace, new ItemStack(BasicComponents.itemSteelDust, 6));
		//Copper
		RecipeManager.addSmelting(new ItemStack(BasicComponents.blockBasicOre, 1, 0), new ItemStack(BasicComponents.itemCopperIngot));
		//Copper Wire
		RecipeManager.addRecipe(new ItemStack(BasicComponents.blockCopperWire, 7), new Object [] {"!!!", "@@@", "!!!", '!', Block.cloth, '@', "ingotCopper"});
		//Tin
		RecipeManager.addSmelting(new ItemStack(BasicComponents.blockBasicOre, 1, 1), new ItemStack(BasicComponents.itemTinIngot));
		//Battery
		RecipeManager.addRecipe(new ItemStack(BasicComponents.itemBattery), new Object [] {" T ", "TRT", "TCT", 'T', "ingotTin", 'R', Item.redstone, 'C', Item.coal});
		//Steel
		RecipeManager.addRecipe(new ItemStack(BasicComponents.itemSteelDust), new Object [] {"!#!", '!', new ItemStack(Item.coal, 1, 1), '#', Item.ingotIron}, "Steel Dust", UniversalElectricity.CONFIGURATION, true);
		RecipeManager.addSmelting(BasicComponents.itemSteelDust, new ItemStack(BasicComponents.itemSteelIngot));
		RecipeManager.addSmelting(BasicComponents.itemSteelPlate, new ItemStack(BasicComponents.itemSteelDust, 3));
		//Bronze
		RecipeManager.addRecipe(new ItemStack(BasicComponents.itemBronzeDust), new Object [] {"!#!", '!', "ingotCopper",  '#', "ingotTin"}, "Bronze Dust", UniversalElectricity.CONFIGURATION, true);
		RecipeManager.addSmelting(BasicComponents.itemBronzeDust, new ItemStack(BasicComponents.itemBronzeIngot));
		RecipeManager.addSmelting(BasicComponents.itemBronzePlate, new ItemStack(BasicComponents.itemBronzeDust, 3));
		
		//Plates
		RecipeManager.addRecipe(new ItemStack(BasicComponents.itemCopperPlate), new Object [] {"!!", "!!", '!', "ingotCopper"});
		RecipeManager.addRecipe(new ItemStack(BasicComponents.itemTinPlate), new Object [] {"!!", "!!", '!', "ingotTin"});
		RecipeManager.addRecipe(new ItemStack(BasicComponents.itemSteelPlate), new Object [] {"!!", "!!", '!', "ingotSteel"});
		RecipeManager.addRecipe(new ItemStack(BasicComponents.itemBronzePlate), new Object [] {"!!", "!!", '!', "ingotBronze"});

		//Circuit
		RecipeManager.addRecipe(new ItemStack(BasicComponents.itemCircuit, 1, 0), new Object [] {"!#!", "#@#", "!#!", '@', BasicComponents.itemSteelPlate, '#', Item.redstone, '!', BasicComponents.blockCopperWire});
		RecipeManager.addRecipe(new ItemStack(BasicComponents.itemCircuit, 1, 1), new Object [] {"@@@", "#?#", "@@@", '@', Item.redstone, '?', Item.diamond, '#', BasicComponents.itemCircuit});
		RecipeManager.addRecipe(new ItemStack(BasicComponents.itemCircuit, 1, 2), new Object [] {"@@@", "?#?", "@@@", '@', Item.ingotGold, '?', new ItemStack(BasicComponents.itemCircuit, 1, 1), '#', Block.blockLapis});
    }
    
    @PostInit
   	public void modsLoaded(FMLPostInitializationEvent evt) 
	{
    	//Add all UE recipes.
    	RecipeManager.addRecipes();
    	
    	//Register all the damage source.
		UEDamageSource.registerDeathMesages();
		
		if(UniversalElectricity.BC3_RATIO <= 0 || Loader.isModLoaded("Buildcraft"))
   		{
   			System.out.println("Disabled Buildcraft electricity conversion!");
   		}
   		else
   		{
   			System.out.println("Buildcraft conversion ratio: "+UniversalElectricity.BC3_RATIO);
   		}
   		
   		if(UniversalElectricity.IC2_RATIO <= 0 || Loader.isModLoaded("IC2"))
   		{
   			System.out.println("Disabled Industrialcraft electricity conversion!");
   		}
   		else
   		{
   			System.out.println("IC2 conversion ratio: "+UniversalElectricity.IC2_RATIO);
   		}
   		
		System.out.println("Universal Electricity v"+UniversalElectricity.VERSION+" successfully loaded!");
	}
    
    @ForgeSubscribe
    public void onEntityDeath(LivingDeathEvent event)
    {
    	if(event.entity instanceof EntityPlayer)
    	{
        	ElectricityManager.instance.timedConductorRefresh();
    	}
    }

    @ForgeSubscribe
	public void onWorldLoad(Load event)
	{
    	ElectricityManager.instance.timedConductorRefresh();
	}
    
    @ForgeSubscribe
	public void onWorldSave(Save event)
	{
    	ElectricityManager.instance.timedConductorRefresh();
	}

	@Override
	public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix)
	{
		if(item.itemID == BasicComponents.itemOilBucket.shiftedIndex)
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

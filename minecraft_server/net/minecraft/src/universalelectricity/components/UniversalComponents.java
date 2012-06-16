package net.minecraft.src.universalelectricity.components;

import net.minecraft.src.Block;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.forge.IOreHandler;
import net.minecraft.src.forge.oredict.OreDictionary;
import net.minecraft.src.universalelectricity.UniversalElectricity;
import net.minecraft.src.universalelectricity.UniversalOreData;

public class UniversalComponents implements IOreHandler
{
	public static final String filePath = "/universalcomponents/";
	
	/**
	 * Here is where all the Universal Components are defined. You may reference to these variables.
	 */
	public static final int CopperOreID = UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Copper Ore Metadata", 0, true);
	public static final int TinOreID = UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Tin Ore Metadata", 1, true);
	public static int MachineRenderType;
	
	public static final Block BlockMachine = new BlockUCMachine(UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Universal Components Machine", 190, true), 0);
	public static final Block BlockCopperWire = new BlockCopperWire(UniversalElectricity.getConfigID(UniversalElectricity.configuration, "CopperWire", 191, true));

	public static final Item ItemBattery = new ItemBattery(UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Battery", 1585, false), 0);
	public static final Item ItemElectricityMeter = new ItemElectricityMeter(UniversalElectricity.getConfigID(UniversalElectricity.configuration, "ElectricityMeter", 1586, false), 19);
	public static final Item ItemWrench = new ItemWrench(UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Wrench", 1587, false), 20);
	public static final Item ItemCopperIngot = new UCItem("Copper Ingot", UniversalElectricity.getConfigID(UniversalElectricity.configuration, "CopperIngot", 1588, false), 1);
	public static final Item ItemTinIngot = new UCItem("Tin Ingot", UniversalElectricity.getConfigID(UniversalElectricity.configuration, "TinIngot", 1589, false), 2);
	public static final Item ItemSteelIngot = new UCItem("Steel Ingot", UniversalElectricity.getConfigID(UniversalElectricity.configuration, "SteelIngot", 1590, false), 3);
	public static final Item ItemSteelClump = new UCItem("Steel Clump", UniversalElectricity.getConfigID(UniversalElectricity.configuration, "SteelClump", 1591, false), 5);
	public static final Item ItemCircuit = new ItemCircuit(UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Circuit", 1592, false), 16);
	public static final Item ItemCopperWire = new ItemCopperWire(UniversalElectricity.getConfigID(UniversalElectricity.configuration, "ItemCopperWire", 1593, false), 21);
	public static final Item ItemBronzeIngot = new UCItem("Bronze Ingot", UniversalElectricity.getConfigID(UniversalElectricity.configuration, "BronzeIngot", 1594, false), 7);
	public static final Item ItemBronzeClump = new UCItem("Bronze Clump", UniversalElectricity.getConfigID(UniversalElectricity.configuration, "BronzeClump", 1595, false), 6);
	public static final Item ItemFuel = new UCItem("Fuel", UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Fuel", 1596, false), 4);
	public static final Item ItemSteelPlate = new UCItem("Steel Plate", UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Steel Plate", 1597, false), 8);
	public static final Item ItemMotor = new UCItem("Motor", UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Motor", 1598, false), 9);
	
	public static void load()
	{
		OreDictionary.registerOreHandler(new UniversalComponents());
				
		//Register Blocks
		ModLoader.registerBlock(BlockMachine, ItemBlockUCMachine.class);
		ModLoader.registerBlock(BlockCopperWire);
		
		//Add Names for Special Items
		ModLoader.addName(ItemElectricityMeter, "Electricity Meter");
		ModLoader.addName(ItemBattery, "Basic Battery");
		ModLoader.addName(BlockCopperWire, "Copper Wire");
		ModLoader.addName(new ItemStack(ItemCircuit, 1, 0), "Basic Circuit");
        ModLoader.addName(new ItemStack(ItemCircuit, 1, 1), "Advanced Circuit");
        ModLoader.addName(new ItemStack(ItemCircuit, 1, 2), "Elite Circuit");
        ModLoader.addName(new ItemStack(BlockMachine, 1, 0), "Battery Box");
        ModLoader.addName(new ItemStack(BlockMachine, 1, 1), "Coal Generator");
        ModLoader.addName(new ItemStack(BlockMachine, 1, 2), "Electric Furnace");
		
		//Register Tile Entities
		ModLoader.registerTileEntity(TileEntityCopperWire.class, "TileEntityCopperWire");
		ModLoader.registerTileEntity(TileEntityBatteryBox.class, "TileEntityBatteryBox");
		ModLoader.registerTileEntity(TileEntityCoalGenerator.class, "TileEntityCoalGenerator");
		ModLoader.registerTileEntity(TileEntityElectricFurnace.class, "TileEntityElectricFurnace");
		
		//Add Ores
		UniversalElectricity.addOre(CopperOreID, new UniversalOreData("Copper Ore", "oreCopper", ModLoader.addOverride("/terrain.png", filePath+"copper.png"), 60, 46, 8));
		UniversalElectricity.addOre(TinOreID, new UniversalOreData("Tin Ore", "oreTin", ModLoader.addOverride("/terrain.png", filePath+"tin.png"), 60, 35, 6));
		OreDictionary.registerOre("ingotCopper", ItemCopperIngot);
		OreDictionary.registerOre("ingotTin", ItemTinIngot);
		OreDictionary.registerOre("ingotBronze", ItemBronzeIngot);
		
		//Use this recipe if you want an uncharged battery
		ItemStack unchargedBattery = new ItemStack(ItemBattery);
		unchargedBattery.setItemDamage(((ItemBattery)ItemBattery).getElectricityCapacity());
		//Recipes
		//Motor
		ModLoader.addRecipe(new ItemStack(ItemMotor), new Object [] {"@!@", "!#!", "@!@", '!', ItemSteelPlate, '#', ItemCircuit, '@', ItemCopperWire});
		//Wrench
		ModLoader.addRecipe(new ItemStack(ItemWrench), new Object [] {"! !", " ! ", " ! ", '!', ItemSteelIngot});
		//Fuel
		ModLoader.addRecipe(new ItemStack(ItemFuel), new Object [] {"!!!", "!#!", "!!!", '!', Item.coal, '#', Item.bucketWater});
		//Electricity Meter
		ModLoader.addRecipe(new ItemStack(ItemElectricityMeter), new Object [] {"!?!", "!#!", "!!!", '!', ItemSteelIngot, '?', ItemCopperWire, '#', ItemCircuit});
		//Battery Box
		ModLoader.addRecipe(new ItemStack(BlockMachine, 1, 0), new Object [] {"!?!", "???", "!?!", '!', ItemSteelPlate, '?', unchargedBattery});
		//Coal Generator
		ModLoader.addRecipe(new ItemStack(BlockMachine, 1, 1), new Object [] {"!@!", "$#$", "!?!", '!', ItemSteelPlate, '@', ItemCopperWire, '?', ItemCircuit, '#', ItemMotor, '$', Block.stoneOvenIdle});
		//Electric Furnace
		ModLoader.addRecipe(new ItemStack(BlockMachine, 1, 2), new Object [] {"!!!", "!?!", "!#!", '!', ItemSteelIngot, '#', ItemCircuit, '?', ItemSteelPlate});
		//Copper Ore
		FurnaceRecipes.smelting().addSmelting(UniversalElectricity.getOre(CopperOreID).blockID, UniversalElectricity.getOreMetadata(CopperOreID), new ItemStack(ItemCopperIngot));
		//Tin Ore
		FurnaceRecipes.smelting().addSmelting(UniversalElectricity.getOre(TinOreID).blockID, UniversalElectricity.getOreMetadata(TinOreID), new ItemStack(ItemTinIngot));
		//Steel
		ModLoader.addRecipe(new ItemStack(ItemSteelClump), new Object [] {"!#!", '!', Item.coal, '#', Item.ingotIron});
		ModLoader.addSmelting(ItemSteelClump.shiftedIndex, new ItemStack(ItemSteelIngot));
		ModLoader.addRecipe(new ItemStack(ItemSteelPlate), new Object [] {" ! ", "!!!", " ! ", '!', ItemSteelIngot});
		//Bronze
		ModLoader.addSmelting(ItemBronzeClump.shiftedIndex, new ItemStack(ItemBronzeIngot));
		//Circuit
        ModLoader.addRecipe(new ItemStack(ItemCircuit, 1, 0), new Object [] {"!#!", "?@?", "!#!", '@', ItemSteelPlate, '?', Item.ingotGold, '#', Item.redstone, '!', ItemCopperWire});
        ModLoader.addRecipe(new ItemStack(ItemCircuit, 1, 1), new Object [] {"@@@", "#?#", "@@@", '@', Item.redstone, '?', Item.diamond, '#', ItemCircuit});
        ModLoader.addRecipe(new ItemStack(ItemCircuit, 1, 2), new Object [] {"@@@", "?#?", "@@@", '@', Item.ingotGold, '?', new ItemStack(ItemCircuit, 1, 1), '#', Block.blockLapis});
	}
	
	@Override
	public void registerOre(String oreClass, ItemStack ore)
	{
		if(oreClass.equals("ingotCopper"))
        {
			ModLoader.addRecipe(new ItemStack(ItemCopperWire, 6), new Object [] {"!!!", "@@@", "!!!", '!', Block.cloth, '@', ore});
        }
		else if(oreClass.equals("ingotTin"))
        {
			//Battery
			ModLoader.addRecipe(new ItemStack(ItemBattery), new Object [] {" ! ", "!#!", "!?!", '!', ore, '?', Item.redstone, '#', Item.coal});
        }
	}
}

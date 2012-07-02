package net.minecraft.src;

import net.minecraft.src.basiccomponents.BasicComponents;
import net.minecraft.src.basiccomponents.GUIBatteryBox;
import net.minecraft.src.basiccomponents.GUICoalGenerator;
import net.minecraft.src.basiccomponents.GUIElectricFurnace;
import net.minecraft.src.basiccomponents.ItemBattery;
import net.minecraft.src.basiccomponents.RenderCopperWire;
import net.minecraft.src.basiccomponents.TileEntityBatteryBox;
import net.minecraft.src.basiccomponents.TileEntityCoalGenerator;
import net.minecraft.src.basiccomponents.TileEntityCopperWire;
import net.minecraft.src.basiccomponents.TileEntityElectricFurnace;
import net.minecraft.src.basiccomponents.BCItem;
import net.minecraft.src.forge.IConnectionHandler;
import net.minecraft.src.forge.IGuiHandler;
import net.minecraft.src.forge.MinecraftForge;
import net.minecraft.src.forge.MinecraftForgeClient;
import net.minecraft.src.forge.NetworkMod;
import net.minecraft.src.forge.oredict.OreDictionary;
import net.minecraft.src.universalelectricity.UEBlockRenderer;
import net.minecraft.src.universalelectricity.UniversalElectricity;
import net.minecraft.src.universalelectricity.ore.OreData;
import net.minecraft.src.universalelectricity.ore.UEOreManager;
import net.minecraft.src.universalelectricity.recipe.UERecipeManager;

/**
 * This class is basically just a loader for Universal Components
 * @author Calclavia
 */

public class mod_BasicComponents extends NetworkMod implements IGuiHandler, IConnectionHandler
{
	@Override
	public void load()
	{
		MinecraftForge.versionDetect("Universal Electricity", 3, 3, 8);
		BasicComponents.instance = this;
		MinecraftForge.setGuiHandler(this, this);
		UniversalElectricity.registerAddon(this, this.getVersion());
	}
	
	@Override
	public void modsLoaded()
	{	
		//Preload textures
		MinecraftForgeClient.preloadTexture(BasicComponents.blockTextureFile);
		MinecraftForgeClient.preloadTexture(BCItem.textureFile);
		
		MinecraftForge.registerConnectionHandler(this);
				
		//Register Blocks
		ModLoader.registerBlock(BasicComponents.BlockCopperWire);
		ModLoader.registerBlock(BasicComponents.BlockBatteryBox);
		ModLoader.registerBlock(BasicComponents.BlockCoalGenerator);
		ModLoader.registerBlock(BasicComponents.BlockElectricFurnace);
		
		//Add Names for Special Items
		ModLoader.addName(BasicComponents.ItemElectricityMeter, "Electricity Meter");
		ModLoader.addName(BasicComponents.ItemBattery, "Basic Battery");
		ModLoader.addName(BasicComponents.BlockCopperWire, "Copper Wire");
		ModLoader.addName(new ItemStack(BasicComponents.ItemCircuit, 1, 0), "Basic Circuit");
        ModLoader.addName(new ItemStack(BasicComponents.ItemCircuit, 1, 1), "Advanced Circuit");
        ModLoader.addName(new ItemStack(BasicComponents.ItemCircuit, 1, 2), "Elite Circuit");
        ModLoader.addName(BasicComponents.BlockBatteryBox, "Battery Box");
        ModLoader.addName(BasicComponents.BlockCoalGenerator, "Coal Generator");
        ModLoader.addName(BasicComponents.BlockElectricFurnace, "Electric Furnace");
		
		//Register Tile Entities
		ModLoader.registerTileEntity(TileEntityCopperWire.class, "TileEntityCopperWire", new RenderCopperWire());
		ModLoader.registerTileEntity(TileEntityBatteryBox.class, "TileEntityBatteryBox");
		ModLoader.registerTileEntity(TileEntityCoalGenerator.class, "TileEntityCoalGenerator");
		ModLoader.registerTileEntity(TileEntityElectricFurnace.class, "TileEntityElectricFurnace");
		
		//Add Ores
		UEOreManager.addOre(BasicComponents.CopperOreID, new OreData("Copper Ore", "oreCopper", ModLoader.addOverride("/terrain.png", BasicComponents.filePath+"copper.png"), 60, 46, 8));
		UEOreManager.addOre(BasicComponents.TinOreID, new OreData("Tin Ore", "oreTin", ModLoader.addOverride("/terrain.png", BasicComponents.filePath+"tin.png"), 60, 35, 6));
		OreDictionary.registerOre("ingotCopper", BasicComponents.ItemCopperIngot);
		OreDictionary.registerOre("ingotTin", BasicComponents.ItemTinIngot);
		OreDictionary.registerOre("ingotBronze", BasicComponents.ItemBronzeIngot);
		OreDictionary.registerOre("ingotSteel", BasicComponents.ItemSteelIngot);
		
		//Recipes
		//Motor
		UERecipeManager.addRecipe(new ItemStack(BasicComponents.ItemMotor), new Object [] {"@!@", "!#!", "@!@", '!', BasicComponents.ItemSteelPlate, '#', BasicComponents.ItemCircuit, '@', BasicComponents.ItemCopperWire});
		//Wrench
		UERecipeManager.addRecipe(new ItemStack(BasicComponents.ItemWrench), new Object [] {"! !", " ! ", " ! ", '!', "ingotSteel"});
		//Electricity Meter
		UERecipeManager.addRecipe(new ItemStack(BasicComponents.ItemElectricityMeter), new Object [] {"!?!", "!#!", "!!!", '!', "ingotSteel", '?', BasicComponents.ItemCopperWire, '#', BasicComponents.ItemCircuit});
		//Battery Box
		UERecipeManager.addRecipe(BasicComponents.BlockBatteryBox, new Object [] {"!?!", "???", "!?!", '!', BasicComponents.ItemSteelPlate, '?', (((ItemBattery)BasicComponents.ItemBattery).getChargedItemStack()) });
		//Coal Generator
		UERecipeManager.addRecipe(BasicComponents.BlockCoalGenerator, new Object [] {"!@!", "$#$", "!?!", '!', BasicComponents.ItemSteelPlate, '@', BasicComponents.ItemCopperWire, '?', BasicComponents.ItemCircuit, '#', BasicComponents.ItemMotor, '$', Block.stoneOvenIdle});
		//Electric Furnace
		UERecipeManager.addRecipe(BasicComponents.BlockElectricFurnace, new Object [] {"!!!", "!?!", "!#!", '!', "ingotSteel", '#', BasicComponents.ItemCircuit, '?', BasicComponents.ItemSteelPlate});
		//Copper
		UERecipeManager.addSmelting(new ItemStack(UEOreManager.getOre(BasicComponents.CopperOreID), 1, UEOreManager.getOreMetadata(BasicComponents.CopperOreID)), new ItemStack(BasicComponents.ItemCopperIngot));
		UERecipeManager.addRecipe(new ItemStack(BasicComponents.ItemCopperWire, 6), new Object [] {"!!!", "@@@", "!!!", '!', Block.cloth, '@', "ingotCopper"});
		//Tin
		UERecipeManager.addSmelting(new ItemStack(UEOreManager.getOre(BasicComponents.TinOreID).blockID, 1, UEOreManager.getOreMetadata(BasicComponents.TinOreID)), new ItemStack(BasicComponents.ItemTinIngot));
		UERecipeManager.addRecipe(new ItemStack(BasicComponents.ItemBattery), new Object [] {" ! ", "!#!", "!?!", '!', "ingotTin", '?', Item.redstone, '#', Item.coal});
		//Steel
		UERecipeManager.addRecipe(new ItemStack(BasicComponents.ItemSteelClump), new Object [] {"!#!", '!', Item.coal, '#', Item.ingotIron});
		UERecipeManager.addRecipe(new ItemStack(BasicComponents.ItemSteelClump), new Object [] {"!#!", '!', new ItemStack(Item.coal, 1, 1), '#', Item.ingotIron});
		UERecipeManager.addSmelting(BasicComponents.ItemSteelClump, new ItemStack(BasicComponents.ItemSteelIngot));
		UERecipeManager.addRecipe(new ItemStack(BasicComponents.ItemSteelPlate), new Object [] {" ! ", "!!!", " ! ", '!', "ingotSteel"});
		//Bronze
		UERecipeManager.addRecipe(BasicComponents.ItemBronzeClump, new Object [] {"!#!", '!', "ingotCopper",  '#', "ingotTin"});
		UERecipeManager.addSmelting(BasicComponents.ItemBronzeClump, new ItemStack(BasicComponents.ItemBronzeIngot));
		//Circuit
		UERecipeManager.addRecipe(new ItemStack(BasicComponents.ItemCircuit, 1, 0), new Object [] {"!#!", "?@?", "!#!", '@', BasicComponents.ItemSteelPlate, '?', Item.ingotGold, '#', Item.redstone, '!', BasicComponents.ItemCopperWire});
		UERecipeManager.addRecipe(new ItemStack(BasicComponents.ItemCircuit, 1, 1), new Object [] {"@@@", "#?#", "@@@", '@', Item.redstone, '?', Item.diamond, '#', BasicComponents.ItemCircuit});
		UERecipeManager.addRecipe(new ItemStack(BasicComponents.ItemCircuit, 1, 2), new Object [] {"@@@", "?#?", "@@@", '@', Item.ingotGold, '?', new ItemStack(BasicComponents.ItemCircuit, 1, 1), '#', Block.blockLapis});
	}
	
	@Override
	public String getVersion()
	{
		return UniversalElectricity.getVersion();
	}
	
	public Object getGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		
		if (tileEntity != null)
        {
			switch(ID)
			{
				case 0: return new GUIBatteryBox(player.inventory, ((TileEntityBatteryBox)tileEntity));
				case 1: return new GUICoalGenerator(player.inventory, ((TileEntityCoalGenerator)tileEntity));
				case 2: return new GUIElectricFurnace(player.inventory, ((TileEntityElectricFurnace)tileEntity));
			}
        }
		
		return null;
	}

	@Override
	public void onConnect(NetworkManager network) { }

	@Override
	public void onLogin(NetworkManager network, Packet1Login login)
	{
		BasicComponents.packetManager.registerChannel(network);
	}

	@Override
	public void onDisconnect(NetworkManager network, String message, Object[] args) { }
}
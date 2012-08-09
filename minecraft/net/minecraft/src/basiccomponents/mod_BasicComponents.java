package net.minecraft.src.basiccomponents;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet1Login;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.forge.IConnectionHandler;
import net.minecraft.src.forge.IGuiHandler;
import net.minecraft.src.forge.MinecraftForge;
import net.minecraft.src.forge.MinecraftForgeClient;
import net.minecraft.src.forge.NetworkMod;
import net.minecraft.src.forge.oredict.OreDictionary;
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
		UniversalElectricity.registerMod(this, this.getVersion());
	}
	
	@Override
	public void modsLoaded()
	{	
		//Preload textures
		MinecraftForgeClient.preloadTexture(BasicComponents.blockTextureFile);
		MinecraftForgeClient.preloadTexture(BCItem.textureFile);
		
		MinecraftForge.registerConnectionHandler(this);
				
		//Register Blocks
		ModLoader.registerBlock(BasicComponents.blockCopperWire);
		ModLoader.registerBlock(BasicComponents.blockBatteryBox);
		ModLoader.registerBlock(BasicComponents.blockCoalGenerator);
		ModLoader.registerBlock(BasicComponents.blockElectricFurnace);
		
		//Add Names for Special Items
		ModLoader.addName(BasicComponents.itemBattery, "Basic Battery");
		ModLoader.addName(BasicComponents.blockCopperWire, "Copper Wire");
		ModLoader.addName(new ItemStack(BasicComponents.itemCircuit, 1, 0), "Basic Circuit");
        ModLoader.addName(new ItemStack(BasicComponents.itemCircuit, 1, 1), "Advanced Circuit");
        ModLoader.addName(new ItemStack(BasicComponents.itemCircuit, 1, 2), "Elite Circuit");
        ModLoader.addName(BasicComponents.blockBatteryBox, "Battery Box");
        ModLoader.addName(BasicComponents.blockCoalGenerator, "Coal Generator");
        ModLoader.addName(BasicComponents.blockElectricFurnace, "Electric Furnace");
		
		//Register Tile Entities
		ModLoader.registerTileEntity(TileEntityCopperWire.class, "TileEntityCopperWire", new RenderCopperWire());
		ModLoader.registerTileEntity(TileEntityBatteryBox.class, "TileEntityBatteryBox");
		ModLoader.registerTileEntity(TileEntityCoalGenerator.class, "TileEntityCoalGenerator");
		ModLoader.registerTileEntity(TileEntityElectricFurnace.class, "TileEntityElectricFurnace");
		
		//Add Ores
		UEOreManager.addOre(BasicComponents.COPPER_ORE_ID, new OreData("Copper Ore", "oreCopper", ModLoader.addOverride("/terrain.png", BasicComponents.FILE_PATH+"copper.png"), 60, 70, 6));
		UEOreManager.addOre(BasicComponents.TIN_ORE_ID, new OreData("Tin Ore", "oreTin", ModLoader.addOverride("/terrain.png", BasicComponents.FILE_PATH+"tin.png"), 60, 50, 5));

		OreDictionary.registerOre("ingotCopper", BasicComponents.itemCopperIngot);
		OreDictionary.registerOre("ingotTin", BasicComponents.itemTinIngot);
		OreDictionary.registerOre("ingotBronze", BasicComponents.itemBronzeIngot);
		OreDictionary.registerOre("ingotSteel", BasicComponents.itemSteelIngot);
		
		//Recipes
		//Motor
		UERecipeManager.addRecipe(new ItemStack(BasicComponents.itemMotor), new Object [] {"@!@", "!#!", "@!@", '!', BasicComponents.itemSteelPlate, '#', BasicComponents.itemCircuit, '@', BasicComponents.itemCopperWire});
		//Wrench
		UERecipeManager.addRecipe(new ItemStack(BasicComponents.itemWrench), new Object [] {"! !", " ! ", " ! ", '!', "ingotSteel"});
		//Battery Box
		UERecipeManager.addRecipe(BasicComponents.blockBatteryBox, new Object [] {"!?!", "???", "!?!", '!', BasicComponents.itemSteelPlate, '?', (((ItemBattery)BasicComponents.itemBattery).getChargedItemStack()) });
		//Coal Generator
		UERecipeManager.addRecipe(BasicComponents.blockCoalGenerator, new Object [] {"!@!", "$#$", "!?!", '!', BasicComponents.itemSteelPlate, '@', BasicComponents.itemCopperWire, '?', BasicComponents.itemCircuit, '#', BasicComponents.itemMotor, '$', Block.stoneOvenIdle});
		//Electric Furnace
		UERecipeManager.addRecipe(BasicComponents.blockElectricFurnace, new Object [] {"!!!", "!?!", "!#!", '!', "ingotSteel", '#', BasicComponents.itemCircuit, '?', BasicComponents.itemSteelPlate});
		//Copper
		UERecipeManager.addSmelting(new ItemStack(UEOreManager.getOre(BasicComponents.COPPER_ORE_ID), 1, UEOreManager.getOreMetadata(BasicComponents.COPPER_ORE_ID)), new ItemStack(BasicComponents.itemCopperIngot));
		UERecipeManager.addRecipe(new ItemStack(BasicComponents.itemCopperWire, 6), new Object [] {"!!!", "@@@", "!!!", '!', Block.cloth, '@', "ingotCopper"});
		//Tin
		UERecipeManager.addSmelting(new ItemStack(UEOreManager.getOre(BasicComponents.TIN_ORE_ID).blockID, 1, UEOreManager.getOreMetadata(BasicComponents.TIN_ORE_ID)), new ItemStack(BasicComponents.itemTinIngot));
		UERecipeManager.addRecipe(new ItemStack(BasicComponents.itemBattery), new Object [] {" ! ", "!#!", "!?!", '!', "ingotTin", '?', Item.redstone, '#', Item.coal});
		//Steel
		UERecipeManager.addRecipe(new ItemStack(BasicComponents.itemSteelClump), new Object [] {"!#!", '!', new ItemStack(Item.coal, 1, 1), '#', Item.ingotIron});
		UERecipeManager.addSmelting(BasicComponents.itemSteelClump, new ItemStack(BasicComponents.itemSteelIngot));
		UERecipeManager.addRecipe(new ItemStack(BasicComponents.itemSteelPlate), new Object [] {" ! ", "!!!", " ! ", '!', "ingotSteel"});
		//Bronze
		UERecipeManager.addRecipe(BasicComponents.itemBronzeClump, new Object [] {"!#!", '!', "ingotCopper",  '#', "ingotTin"});
		UERecipeManager.addSmelting(BasicComponents.itemBronzeClump, new ItemStack(BasicComponents.itemBronzeIngot));
		//Circuit
		UERecipeManager.addRecipe(new ItemStack(BasicComponents.itemCircuit, 1, 0), new Object [] {"!#!", "?@?", "!#!", '@', BasicComponents.itemSteelPlate, '?', Item.ingotGold, '#', Item.redstone, '!', BasicComponents.itemCopperWire});
		UERecipeManager.addRecipe(new ItemStack(BasicComponents.itemCircuit, 1, 1), new Object [] {"@@@", "#?#", "@@@", '@', Item.redstone, '?', Item.diamond, '#', BasicComponents.itemCircuit});
		UERecipeManager.addRecipe(new ItemStack(BasicComponents.itemCircuit, 1, 2), new Object [] {"@@@", "?#?", "@@@", '@', Item.ingotGold, '?', new ItemStack(BasicComponents.itemCircuit, 1, 1), '#', Block.blockLapis});
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
		BasicComponents.PACKET_MANAGER.registerChannel(network);
	}

	@Override
	public void onDisconnect(NetworkManager network, String message, Object[] args) { }
}
package net.minecraft.server;

import universalelectricity.UniversalElectricity;
import universalelectricity.ore.OreData;
import universalelectricity.ore.UEOreManager;
import universalelectricity.recipe.UERecipeManager;
import basiccomponents.BasicComponents;
import basiccomponents.ContainerBatteryBox;
import basiccomponents.ContainerCoalGenerator;
import basiccomponents.ContainerElectricFurnace;
import basiccomponents.ItemBattery;
import basiccomponents.TileEntityBatteryBox;
import basiccomponents.TileEntityCoalGenerator;
import basiccomponents.TileEntityElectricFurnace;
import forge.IConnectionHandler;
import forge.IGuiHandler;
import forge.MinecraftForge;
import forge.NetworkMod;
import forge.oredict.OreDictionary;


/**
 * The Class mod_BasicComponents.
 */
public class mod_BasicComponents extends NetworkMod implements IGuiHandler, IConnectionHandler
{
    
    /**
     * Instantiates a new mod_ basic components.
     */
    public mod_BasicComponents()
    {
    }

    /* (non-Javadoc)
     * @see net.minecraft.server.BaseMod#load()
     */
    public void load()
    {
        MinecraftForge.versionDetect("Universal Electricity", 3, 3, 8);
        BasicComponents.instance = this;
        MinecraftForge.setGuiHandler(this, this);
        UniversalElectricity.registerAddon(this, getVersion());
    }

    /* (non-Javadoc)
     * @see net.minecraft.server.BaseMod#modsLoaded()
     */
    public void modsLoaded()
    {
        MinecraftForge.registerConnectionHandler(this);
        ModLoader.registerBlock(BasicComponents.BlockCopperWire);
        ModLoader.registerBlock(BasicComponents.BlockBatteryBox);
        ModLoader.registerBlock(BasicComponents.BlockCoalGenerator);
        ModLoader.registerBlock(BasicComponents.BlockElectricFurnace);
        ModLoader.addName(BasicComponents.ItemBattery, "Basic Battery");
        ModLoader.addName(BasicComponents.BlockCopperWire, "Copper Wire");
        ModLoader.addName(new ItemStack(BasicComponents.ItemCircuit, 1, 0), "Basic Circuit");
        ModLoader.addName(new ItemStack(BasicComponents.ItemCircuit, 1, 1), "Advanced Circuit");
        ModLoader.addName(new ItemStack(BasicComponents.ItemCircuit, 1, 2), "Elite Circuit");
        ModLoader.addName(BasicComponents.BlockBatteryBox, "Battery Box");
        ModLoader.addName(BasicComponents.BlockCoalGenerator, "Coal Generator");
        ModLoader.addName(BasicComponents.BlockElectricFurnace, "Electric Furnace");
        ModLoader.registerTileEntity(basiccomponents.TileEntityCopperWire.class, "TileEntityCopperWire");
        ModLoader.registerTileEntity(basiccomponents.TileEntityBatteryBox.class, "TileEntityBatteryBox");
        ModLoader.registerTileEntity(basiccomponents.TileEntityCoalGenerator.class, "TileEntityCoalGenerator");
        ModLoader.registerTileEntity(basiccomponents.TileEntityElectricFurnace.class, "TileEntityElectricFurnace");
        UEOreManager.addOre(BasicComponents.CopperOreID, new OreData("Copper Ore", "oreCopper", ModLoader.addOverride("/terrain.png", "/basiccomponents/textures/copper.png"), 60, 46, 8));
        UEOreManager.addOre(BasicComponents.TinOreID, new OreData("Tin Ore", "oreTin", ModLoader.addOverride("/terrain.png", "/basiccomponents/textures/tin.png"), 60, 35, 6));
        OreDictionary.registerOre("ingotCopper", BasicComponents.ItemCopperIngot);
        OreDictionary.registerOre("ingotTin", BasicComponents.ItemTinIngot);
        OreDictionary.registerOre("ingotBronze", BasicComponents.ItemBronzeIngot);
        OreDictionary.registerOre("ingotSteel", BasicComponents.ItemSteelIngot);
        UERecipeManager.addRecipe(new ItemStack(BasicComponents.ItemMotor), new Object[]
                {
                    "@!@", "!#!", "@!@", '!', BasicComponents.ItemSteelPlate, '#', BasicComponents.ItemCircuit, '@', BasicComponents.ItemCopperWire
                });
        UERecipeManager.addRecipe(new ItemStack(BasicComponents.ItemWrench), new Object[]
                {
                    "! !", " ! ", " ! ", '!', "ingotSteel"
                });
        UERecipeManager.addRecipe(BasicComponents.BlockBatteryBox, new Object[]
                {
                    "!?!", "???", "!?!", '!', BasicComponents.ItemSteelPlate, '?', ((ItemBattery)BasicComponents.ItemBattery).getChargedItemStack()
                });
        UERecipeManager.addRecipe(BasicComponents.BlockCoalGenerator, new Object[]
                {
                    "!@!", "$#$", "!?!", '!', BasicComponents.ItemSteelPlate, '@', BasicComponents.ItemCopperWire, '?', BasicComponents.ItemCircuit, '#',
                    BasicComponents.ItemMotor, '$', Block.FURNACE
                });
        UERecipeManager.addRecipe(BasicComponents.BlockElectricFurnace, new Object[]
                {
                    "!!!", "!?!", "!#!", '!', "ingotSteel", '#', BasicComponents.ItemCircuit, '?', BasicComponents.ItemSteelPlate
                });
        UERecipeManager.addSmelting(new ItemStack(UEOreManager.getOre(BasicComponents.CopperOreID), 1, UEOreManager.getOreMetadata(BasicComponents.CopperOreID)), new ItemStack(BasicComponents.ItemCopperIngot));
        UERecipeManager.addRecipe(new ItemStack(BasicComponents.ItemCopperWire, 6), new Object[]
                {
                    "!!!", "@@@", "!!!", '!', Block.WOOL, '@', "ingotCopper"
                });
        UERecipeManager.addSmelting(new ItemStack(UEOreManager.getOre(BasicComponents.TinOreID).id, 1, UEOreManager.getOreMetadata(BasicComponents.TinOreID)), new ItemStack(BasicComponents.ItemTinIngot));
        UERecipeManager.addRecipe(new ItemStack(BasicComponents.ItemBattery), new Object[]
                {
                    " ! ", "!#!", "!?!", '!', "ingotTin", '?', Item.REDSTONE, '#', Item.COAL
                });
        UERecipeManager.addRecipe(new ItemStack(BasicComponents.ItemSteelClump), new Object[]
                {
                    "!#!", '!', new ItemStack(Item.COAL, 1, 1), '#', Item.IRON_INGOT
                });
        UERecipeManager.addSmelting(BasicComponents.ItemSteelClump, new ItemStack(BasicComponents.ItemSteelIngot));
        UERecipeManager.addRecipe(new ItemStack(BasicComponents.ItemSteelPlate), new Object[]
                {
                    " ! ", "!!!", " ! ", '!', "ingotSteel"
                });
        UERecipeManager.addRecipe(BasicComponents.ItemBronzeClump, new Object[]
                {
                    "!#!", '!', "ingotCopper", '#', "ingotTin"
                });
        UERecipeManager.addSmelting(BasicComponents.ItemBronzeClump, new ItemStack(BasicComponents.ItemBronzeIngot));
        UERecipeManager.addRecipe(new ItemStack(BasicComponents.ItemCircuit, 1, 0), new Object[]
                {
                    "!#!", "?@?", "!#!", '@', BasicComponents.ItemSteelPlate, '?', Item.GOLD_INGOT, '#', Item.REDSTONE, '!',
                    BasicComponents.ItemCopperWire
                });
        UERecipeManager.addRecipe(new ItemStack(BasicComponents.ItemCircuit, 1, 1), new Object[]
                {
                    "@@@", "#?#", "@@@", '@', Item.REDSTONE, '?', Item.DIAMOND, '#', BasicComponents.ItemCircuit
                });
        UERecipeManager.addRecipe(new ItemStack(BasicComponents.ItemCircuit, 1, 2), new Object[]
                {
                    "@@@", "?#?", "@@@", '@', Item.GOLD_INGOT, '?', new ItemStack(BasicComponents.ItemCircuit, 1, 1), '#', Block.LAPIS_BLOCK
                });
    }

    /* (non-Javadoc)
     * @see net.minecraft.server.BaseMod#getVersion()
     */
    public String getVersion()
    {
        return UniversalElectricity.getVersion();
    }

    /* (non-Javadoc)
     * @see forge.IGuiHandler#getGuiElement(int, net.minecraft.server.EntityHuman, net.minecraft.server.World, int, int, int)
     */
    public Object getGuiElement(int i, EntityHuman entityhuman, World world, int j, int k, int l)
    {
        TileEntity tileentity = world.getTileEntity(j, k, l);

        if (tileentity != null)
        {
            switch (i)
            {
                case 0:
                    return new ContainerBatteryBox(entityhuman.inventory, (TileEntityBatteryBox)tileentity);

                case 1:
                    return new ContainerCoalGenerator(entityhuman.inventory, (TileEntityCoalGenerator)tileentity);

                case 2:
                    return new ContainerElectricFurnace(entityhuman.inventory, (TileEntityElectricFurnace)tileentity);
            }
        }

        return null;
    }

    /* (non-Javadoc)
     * @see forge.IConnectionHandler#onConnect(net.minecraft.server.NetworkManager)
     */
    public void onConnect(NetworkManager networkmanager)
    {
    }

    /* (non-Javadoc)
     * @see forge.IConnectionHandler#onLogin(net.minecraft.server.NetworkManager, net.minecraft.server.Packet1Login)
     */
    public void onLogin(NetworkManager networkmanager, Packet1Login packet1login)
    {
        BasicComponents.packetManager.registerChannel(networkmanager);
    }

    /* (non-Javadoc)
     * @see forge.IConnectionHandler#onDisconnect(net.minecraft.server.NetworkManager, java.lang.String, java.lang.Object[])
     */
    public void onDisconnect(NetworkManager networkmanager, String s, Object aobj[])
    {
    }
}

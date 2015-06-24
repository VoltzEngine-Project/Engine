package com.builtbroken.mc.core;

import com.builtbroken.mc.api.recipe.MachineRecipeType;
import com.builtbroken.mc.core.commands.CommandVE;
import com.builtbroken.mc.core.commands.permissions.GroupProfileHandler;
import com.builtbroken.mc.core.content.BlockOre;
import com.builtbroken.mc.core.content.ItemBlockOre;
import com.builtbroken.mc.core.content.ItemInstaHole;
import com.builtbroken.mc.core.content.blocks.BlockHeatedStone;
import com.builtbroken.mc.core.content.parts.ItemParts;
import com.builtbroken.mc.core.content.resources.Ores;
import com.builtbroken.mc.core.content.resources.load.CastRecipeLoader;
import com.builtbroken.mc.core.content.resources.load.CrusherRecipeLoad;
import com.builtbroken.mc.core.content.resources.load.FluidSmelterRecipeLoad;
import com.builtbroken.mc.core.content.resources.load.GrinderRecipeLoad;
import com.builtbroken.mc.core.content.tool.*;
import com.builtbroken.mc.core.handler.InteractionHandler;
import com.builtbroken.mc.core.handler.SaveManager;
import com.builtbroken.mc.core.handler.SelectionHandler;
import com.builtbroken.mc.core.network.netty.PacketManager;
import com.builtbroken.mc.core.proxy.NEIProxy;
import com.builtbroken.mc.core.registry.ModManager;
import com.builtbroken.mc.lib.helper.PotionUtility;
import com.builtbroken.mc.lib.mod.AbstractMod;
import com.builtbroken.mc.lib.mod.AbstractProxy;
import com.builtbroken.mc.lib.mod.compat.Mods;
import com.builtbroken.mc.lib.mod.compat.oc.OCProxy;
import com.builtbroken.mc.lib.mod.config.ConfigHandler;
import com.builtbroken.mc.lib.mod.config.ConfigScanner;
import com.builtbroken.mc.lib.world.edit.PlacementData;
import com.builtbroken.mc.lib.world.explosive.ExplosiveRegistry;
import com.builtbroken.mc.lib.world.heat.HeatedBlockRegistry;
import com.builtbroken.mc.prefab.explosive.ExplosiveHandler;
import com.builtbroken.mc.prefab.explosive.blast.BlastBasic;
import com.builtbroken.mc.prefab.recipe.cast.MRHandlerCast;
import com.builtbroken.mc.prefab.recipe.fluid.MRHandlerFluidStack;
import com.builtbroken.mc.prefab.recipe.item.MRHandlerItemStack;
import com.builtbroken.mc.prefab.recipe.item.MRSmelterHandler;
import com.builtbroken.mc.prefab.tile.item.ItemBlockMetadata;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

import java.io.File;
import java.util.Arrays;

/**
 * Mob class for Resonant Engine that handles common loading
 *
 * @author Calclavia, DarkGuardsman
 */

@Mod(modid = References.ID, name = References.NAME, version = References.VERSION)
public class Engine extends AbstractMod
{
    public static final ModManager contentRegistry = new ModManager().setPrefix(References.PREFIX).setTab(CreativeTabs.tabTools);
    public static final boolean runningAsDev = System.getProperty("development") != null && System.getProperty("development").equalsIgnoreCase("true");

    @SidedProxy(clientSide = "com.builtbroken.mc.core.ClientProxy", serverSide = "com.builtbroken.mc.core.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Metadata(References.ID)
    public static ModMetadata metadata;

    @Instance(References.ID)
    public static Engine instance;

    public static Block ore = null;
    public static Block heatedStone;
    public static Item itemWrench;
    public static Item instaHole;
    public static Item itemSelectionTool;
    public static Item itemCraftingParts;

    //Interal trigger booleans
    private static boolean oresRequested = false;
    public static boolean heatedRockRequested = false;

    public final PacketManager packetHandler = new PacketManager(References.CHANNEL);

    //config files
    public static Configuration heatDataConfig;
    public static Configuration explosiveConfig;

    //Configs
    public static boolean enabledHeatMap = true;
    public static boolean log_registering_explosives = false;

    /** Conversion ratio of ingot to fluid volume, based on Tinkers *in theory* */
    public static int INGOT_VOLUME = 144;

    //TODO move these to compatibility handlers later
    public static double TO_RF_RATIO = 500;
    public static double TO_BC_RATIO = 50;

    public Engine()
    {
        super(References.PREFIX, "ve/VoltzEngine");
    }

    /**
     * Requests that all ores are generated
     * Must be called in pre-init
     */
    public static void requestOres()
    {
        oresRequested = true;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent evt)
    {
        super.preInit(evt);
        References.LOGGER = logger;
        ConfigScanner.instance().generateSets(evt.getAsmData());
        ConfigHandler.sync(getConfig(), References.DOMAIN);
        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(SaveManager.instance());
        MinecraftForge.EVENT_BUS.register(new InteractionHandler());
        MinecraftForge.EVENT_BUS.register(SelectionHandler.INSTANCE);
        FMLCommonHandler.instance().bus().register(SelectionHandler.INSTANCE);

        heatDataConfig = new Configuration(new File(evt.getModConfigurationDirectory(), "bbm/ve/HeatMap.cfg"));
        explosiveConfig = new Configuration(new File(evt.getModConfigurationDirectory(), "bbm/ve/Explosives.cfg"));

        //Load heat configs
        heatDataConfig.load();
        enabledHeatMap = heatDataConfig.getBoolean("EnabledHeatMap", Configuration.CATEGORY_GENERAL, true, "Heat map handles interaction of heat based energy and the world. Disable only if it causes issues or you want to reduce world file size. If disabled it can prevent machines from working.");

        //Load explosive configs
        explosiveConfig.load();
        log_registering_explosives = explosiveConfig.getBoolean("EnableRegisterLogging", Configuration.CATEGORY_GENERAL, false, "Adds debug each time a mod registers an explosive handler. Should only be enabled to figure out which mod is overriding another mod's explosive");

        MachineRecipeType.ITEM_SMELTER.setHandler(new MRSmelterHandler());
        MachineRecipeType.ITEM_GRINDER.setHandler(new MRHandlerItemStack(MachineRecipeType.ITEM_GRINDER.INTERNAL_NAME));
        MachineRecipeType.ITEM_CRUSHER.setHandler(new MRHandlerItemStack(MachineRecipeType.ITEM_CRUSHER.INTERNAL_NAME));
        MachineRecipeType.ITEM_WASHER.setHandler(new MRHandlerItemStack(MachineRecipeType.ITEM_WASHER.INTERNAL_NAME));
        MachineRecipeType.ITEM_SAWMILL.setHandler(new MRHandlerItemStack(MachineRecipeType.ITEM_SAWMILL.INTERNAL_NAME));
        MachineRecipeType.FLUID_SMELTER.setHandler(new MRHandlerFluidStack(MachineRecipeType.FLUID_SMELTER.INTERNAL_NAME));
        MachineRecipeType.FLUID_CAST.setHandler(new MRHandlerCast());

        loader.applyModule(packetHandler);
        loader.applyModule(CrusherRecipeLoad.class);
        loader.applyModule(GrinderRecipeLoad.class);
        loader.applyModule(FluidSmelterRecipeLoad.class);
        loader.applyModule(CastRecipeLoader.class);
        loader.applyModule(NEIProxy.class);
        loader.applyModule(GroupProfileHandler.GLOBAL);
        loader.applyModule(OCProxy.class, Mods.OC.isLoaded());

        PotionUtility.resizePotionArray();

        //Manually registered configs TODO setup threw sync system (once the system is tested)
        CommandVE.disableCommands = getConfig().getBoolean("DisableServerCommands", "Commands", false, "Turns off all commands built into Voltz Engine");
        CommandVE.disableButcherCommand = getConfig().getBoolean("DisableButcherCommands", "Commands", false, "Turns off butcher command");
        CommandVE.disableClearCommand = getConfig().getBoolean("DisableClearCommands", "Commands", false, "Turns off clear command");
        CommandVE.disableRemoveCommand = getConfig().getBoolean("DisableRemoverCommands", "Commands", false, "Turns off remove command");

        ToolMode.REGISTRY.add(new ToolModeGeneral());
        ToolMode.REGISTRY.add(new ToolModeRotation());

        if(runningAsDev)
            itemCraftingParts = contentRegistry.newItem(ItemParts.class);
        /**
         * Multiblock Handling
         */
        if (getConfig().get("Content", "LoadInstantHole", runningAsDev).getBoolean(runningAsDev))
            instaHole = contentRegistry.newItem("ve.instanthole", new ItemInstaHole());
        if (getConfig().get("Content", "LoadScrewDriver", true).getBoolean(true))
            itemWrench = getManager().newItem("ve.screwdriver", new ItemScrewdriver());
        if (getConfig().get("Content", "LoadSelectionTool", true).getBoolean(true))
            itemSelectionTool = getManager().newItem("ve.selectiontool", new ItemSelectionWand());
    }

    @EventHandler
    public void init(FMLInitializationEvent evt)
    {
        super.init(evt);
        Engine.metadata.modId = References.NAME;
        Engine.metadata.name = References.NAME;
        Engine.metadata.description = References.NAME + " is a content creation toolkit";
        Engine.metadata.url = "http://www.builtbroken.com/pages/voltzengine/";
        Engine.metadata.version = References.VERSION + References.BUILD_VERSION;
        Engine.metadata.authorList = Arrays.asList("DarkCow");
        Engine.metadata.autogenerated = false;

        ExplosiveRegistry.registerOrGetExplosive(References.DOMAIN, "TNT", new ExplosiveHandler("tnt", BlastBasic.class, 1));

        //Register UpdateTicker
        //FMLCommonHandler.instance().bus().register(UpdateTicker$.MODULE$.world());

        //Late registration of content
        if (oresRequested)
        {
            ore = contentRegistry.newBlock(References.ID + "StoneOre", new BlockOre("stone"), ItemBlockOre.class);
            Ores.registerSet(ore, getConfig());
        }

        if(heatedRockRequested)
        {
            heatedStone = contentRegistry.newBlock("VEHeatedRock", BlockHeatedStone.class, ItemBlockMetadata.class);
            NEIProxy.hideItem(heatedStone);
            if(enabledHeatMap)
            {
                HeatedBlockRegistry.addNewHeatingConversion(Blocks.stone, new PlacementData(heatedStone, 15), 600);
                HeatedBlockRegistry.addNewHeatingConversion(heatedStone, Blocks.lava, 1200);
            }
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent evt)
    {
        super.postInit(evt);

        //All blocks should be loaded before post init so we can init things that need to iterate over the block list
        if(enabledHeatMap)
            HeatedBlockRegistry.init(heatDataConfig);

        //Save configs as this is our last chance
        heatDataConfig.save();
        explosiveConfig.save();

        OreDictionary.registerOre("ingotGold", Items.gold_ingot);
        OreDictionary.registerOre("ingotIron", Items.iron_ingot);
        OreDictionary.registerOre("oreGold", Blocks.gold_ore);
        OreDictionary.registerOre("oreIron", Blocks.iron_ore);
        OreDictionary.registerOre("oreLapis", Blocks.lapis_ore);
        //MachineRecipes.instance.addRecipe(RecipeType.ITEM_SMELTER.name(), new FluidStack(FluidRegistry.LAVA, FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(Blocks.stone));
        //MachineRecipes.instance.addRecipe(RecipeType.ITEM_CRUSHER.name(), Blocks.cobblestone, Blocks.gravel);
        //MachineRecipes.instance.addRecipe(RecipeType.ITEM_CRUSHER.name(), Blocks.stone, Blocks.cobblestone);
        //MachineRecipes.instance.addRecipe(RecipeType.ITEM_CRUSHER.name(), Blocks.chest, new ItemStack(Blocks.planks, 7, 0));
        //MachineRecipes.instance.addRecipe(RecipeType.ITEM_GRINDER.name(), Blocks.cobblestone, Blocks.sand);
        //MachineRecipes.instance.addRecipe(RecipeType.ITEM_GRINDER.name(), Blocks.gravel, Blocks.sand);
        //MachineRecipes.instance.addRecipe(RecipeType.ITEM_GRINDER.name(), Blocks.glass, Blocks.sand);
    }

    @Override
    public AbstractProxy getProxy()
    {
        return proxy;
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        if (!CommandVE.disableCommands)
        {
            // Setup command
            ICommandManager commandManager = FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager();
            ServerCommandManager serverCommandManager = ((ServerCommandManager) commandManager);

            //Register commands
            serverCommandManager.registerCommand(CommandVE.INSTANCE);
        }
    }

    public static boolean isPlayerOpped(EntityPlayer player)
    {
        return player instanceof EntityPlayerMP && isPlayerOpped((EntityPlayerMP)player);
    }

    public static boolean isPlayerOpped(EntityPlayerMP player)
    {
        //Taken from EntityPlayerMP#canCommandSenderUseCommand(Integer, String)
        if (player.mcServer.getConfigurationManager().func_152596_g(player.getGameProfile()))
        {
            return player.mcServer.getConfigurationManager().func_152603_m().func_152683_b(player.getGameProfile()) != null;
        }
        return false;
    }

}

package com.builtbroken.mc.core;

import com.builtbroken.jlib.lang.StringHelpers;
import com.builtbroken.mc.api.recipe.MachineRecipeType;
import com.builtbroken.mc.core.commands.CommandVE;
import com.builtbroken.mc.core.commands.permissions.GroupProfileHandler;
import com.builtbroken.mc.core.content.ItemInstaHole;
import com.builtbroken.mc.core.content.blocks.BlockHeatedStone;
import com.builtbroken.mc.core.content.parts.ItemParts;
import com.builtbroken.mc.core.content.resources.*;
import com.builtbroken.mc.core.content.resources.items.Gems;
import com.builtbroken.mc.core.content.resources.items.ItemGems;
import com.builtbroken.mc.core.content.resources.items.ItemGenMaterial;
import com.builtbroken.mc.core.content.resources.items.ItemSheetMetal;
import com.builtbroken.mc.core.content.resources.load.*;
import com.builtbroken.mc.core.content.tool.ItemScrewdriver;
import com.builtbroken.mc.core.content.tool.ItemSelectionWand;
import com.builtbroken.mc.core.content.tool.ItemSheetMetalTools;
import com.builtbroken.mc.core.content.tool.ItemSimpleCraftingTool;
import com.builtbroken.mc.core.content.tool.screwdriver.ToolMode;
import com.builtbroken.mc.core.content.tool.screwdriver.ToolModeGeneral;
import com.builtbroken.mc.core.content.tool.screwdriver.ToolModeRotation;
import com.builtbroken.mc.core.handler.InteractionHandler;
import com.builtbroken.mc.core.handler.SaveManager;
import com.builtbroken.mc.core.handler.SelectionHandler;
import com.builtbroken.mc.core.handler.TileTaskTickHandler;
import com.builtbroken.mc.core.network.netty.PacketManager;
import com.builtbroken.mc.core.registry.ModManager;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.lib.helper.PotionUtility;
import com.builtbroken.mc.lib.mod.AbstractProxy;
import com.builtbroken.mc.lib.mod.compat.Mods;
import com.builtbroken.mc.lib.mod.compat.nei.NEIProxy;
import com.builtbroken.mc.lib.mod.compat.oc.OCProxy;
import com.builtbroken.mc.lib.mod.compat.rf.RFLoader;
import com.builtbroken.mc.lib.mod.compat.tinkers.TinkerProxy;
import com.builtbroken.mc.lib.mod.config.ConfigHandler;
import com.builtbroken.mc.lib.mod.config.ConfigScanner;
import com.builtbroken.mc.lib.mod.loadable.LoadableHandler;
import com.builtbroken.mc.lib.world.edit.PlacementData;
import com.builtbroken.mc.lib.world.explosive.ExplosiveRegistry;
import com.builtbroken.mc.lib.world.heat.HeatedBlockRegistry;
import com.builtbroken.mc.prefab.explosive.ExplosiveHandler;
import com.builtbroken.mc.prefab.explosive.blast.BlastBasic;
import com.builtbroken.mc.prefab.recipe.cast.MRHandlerCast;
import com.builtbroken.mc.prefab.recipe.fluid.MRHandlerFluidStack;
import com.builtbroken.mc.prefab.recipe.item.MRHandlerItemStack;
import com.builtbroken.mc.prefab.recipe.item.MRSmelterHandler;
import com.builtbroken.mc.prefab.recipe.item.RecipeTool;
import com.builtbroken.mc.prefab.recipe.item.sheetmetal.RecipeSheetMetal;
import com.builtbroken.mc.prefab.tile.item.ItemBlockMetadata;
import com.builtbroken.mc.prefab.tile.multiblock.BlockMultiblock;
import com.builtbroken.mc.prefab.tile.multiblock.EnumMultiblock;
import com.builtbroken.mc.prefab.tile.multiblock.ItemBlockMulti;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static net.minecraftforge.oredict.RecipeSorter.Category.SHAPED;

/**
 * Mod class for Voltz Engine that handles common loading
 *
 * @author Calclavia, DarkGuardsman
 */

@Mod(modid = References.ID, name = References.NAME, version = References.VERSION, acceptableRemoteVersions = "*")
public class Engine
{
    public static final ModManager contentRegistry = new ModManager().setPrefix(References.PREFIX).setTab(CreativeTabs.tabTools);
    public static final boolean runningAsDev = System.getProperty("development") != null && System.getProperty("development").equalsIgnoreCase("true");

    @SidedProxy(clientSide = "com.builtbroken.mc.core.ClientProxy", serverSide = "com.builtbroken.mc.core.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Metadata(References.ID)
    public static ModMetadata metadata;

    @Instance(References.ID)
    public static Engine instance;

    protected LoadableHandler loader;
    protected ModManager manager;
    protected static Logger logger = LogManager.getLogger(References.DOMAIN);
    private Configuration config;

    public static Block ore = null;
    public static Block gemOre = null;
    public static Block heatedStone;
    public static Block multiBlock;

    public static Item itemWrench;
    public static ItemSimpleCraftingTool itemSimpleCraftingTools;
    public static ItemSheetMetalTools itemSheetMetalTools;
    public static Item itemSheetMetal;
    public static Item instaHole;
    public static Item itemSelectionTool;
    public static Item itemCraftingParts;
    public static Item gem = null;

    //Interal trigger booleans
    private static boolean metallicOresRequested = false;
    private static boolean gemOresRequested = false;
    private static boolean sheetMetalRequested = false;
    private static boolean multiBlockRequested = false;
    public static boolean heatedRockRequested = false;
    public static boolean simpleToolsRequested = false;

    public final PacketManager packetHandler = new PacketManager(References.CHANNEL);

    //config files
    public static Configuration heatDataConfig;
    public static Configuration explosiveConfig;

    //Configs
    public static boolean enabledHeatMap = true;
    public static boolean log_registering_explosives = false;

    /**
     * Conversion ratio of ingot to fluid volume, based on Tinkers *in theory*
     */
    public static int INGOT_VOLUME = 144;

    //TODO move these to compatibility handlers later
    public static double TO_RF_RATIO = 500;
    public static double TO_BC_RATIO = 50;

    /**
     * Requests that all ores are generated
     * Must be called in pre-init
     */
    public static void requestOres()
    {
        if (!Loader.instance().isInState(LoaderState.PREINITIALIZATION))
            throw new RuntimeException("Ores can only be requested in Pre-Init phase!");
        metallicOresRequested = true;
    }

    public static void requestGemOres()
    {
        if (!Loader.instance().isInState(LoaderState.PREINITIALIZATION))
            throw new RuntimeException("Ores can only be requested in Pre-Init phase!");
        gemOresRequested = true;
    }

    /**
     * Requests that resources like ingots and dust are loaded
     */
    public static void requestResources()
    {
        if (!Loader.instance().isInState(LoaderState.PREINITIALIZATION))
            throw new RuntimeException("Resources can only be requested in Pre-Init phase");
        DefinedGenItems.DUST.requestToLoad();
        DefinedGenItems.DUST_IMPURE.requestToLoad();
        DefinedGenItems.RUBBLE.requestToLoad();
        DefinedGenItems.INGOT.requestToLoad();
        DefinedGenItems.PLATE.requestToLoad();
        DefinedGenItems.ROD.requestToLoad();
        DefinedGenItems.GEAR.requestToLoad();
    }

    public static void requestToolParts()
    {
        if (!Loader.instance().isInState(LoaderState.PREINITIALIZATION))
            throw new RuntimeException("Tool Parts can only be requested in Pre-Init phase");
        DefinedGenItems.AX_HEAD.requestToLoad();
        DefinedGenItems.SHOVEL_HEAD.requestToLoad();
        DefinedGenItems.HOE_HEAD.requestToLoad();
        DefinedGenItems.PICK_HEAD.requestToLoad();
        DefinedGenItems.SWORD_BLADE.requestToLoad();
    }


    /**
     * Requests basic multiblock code to be loaded up
     * Must be called in pre-init
     */
    public static void requestMultiBlock()
    {
        if (!Loader.instance().isInState(LoaderState.PREINITIALIZATION))
            throw new RuntimeException("Multi block content can only be requested in Pre-Init phase");
        multiBlockRequested = true;
    }

    /**
     * Requests simple tool code to be loaded up
     * Must be called in pre-init
     */
    public static void requestSimpleTools()
    {
        if (!Loader.instance().isInState(LoaderState.PREINITIALIZATION))
            throw new RuntimeException("Multi block content can only be requested in Pre-Init phase");
        simpleToolsRequested = true;
    }

    /**
     * Requests sheet metal content to be loaded up
     * Must be called in pre-init
     */
    public static void requestSheetMetalContent()
    {
        if (!Loader.instance().isInState(LoaderState.PREINITIALIZATION))
            throw new RuntimeException("Sheet metal content can only be requested in Pre-Init phase");
        sheetMetalRequested = true;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        config = new Configuration(new File(event.getModConfigurationDirectory(), "bbm/ve/VoltzEngine.cfg"));
        heatDataConfig = new Configuration(new File(event.getModConfigurationDirectory(), "bbm/ve/HeatMap.cfg"));
        explosiveConfig = new Configuration(new File(event.getModConfigurationDirectory(), "bbm/ve/Explosives.cfg"));

        loader = new LoadableHandler();
        manager = new ModManager().setPrefix(References.DOMAIN).setTab(CreativeTabs.tabAllSearch);

        config.load();
        heatDataConfig.load();
        explosiveConfig.load();

        References.LOGGER = logger;
        ConfigScanner.instance().generateSets(event.getAsmData());
        ConfigHandler.sync(getConfig(), References.DOMAIN);
        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(SaveManager.instance());
        MinecraftForge.EVENT_BUS.register(new InteractionHandler());
        MinecraftForge.EVENT_BUS.register(SelectionHandler.INSTANCE);
        FMLCommonHandler.instance().bus().register(TileTaskTickHandler.INSTANCE);
        FMLCommonHandler.instance().bus().register(SelectionHandler.INSTANCE);


        //Load heat configs
        enabledHeatMap = heatDataConfig.getBoolean("EnabledHeatMap", Configuration.CATEGORY_GENERAL, true, "Heat map handles interaction of heat based energy and the world. Disable only if it causes issues or you want to reduce world file size. If disabled it can prevent machines from working.");

        //Load explosive configs
        log_registering_explosives = explosiveConfig.getBoolean("EnableRegisterLogging", Configuration.CATEGORY_GENERAL, false, "Adds debug each time a mod registers an explosive handler. Should only be enabled to figure out which mod is overriding another mod's explosive");

        MachineRecipeType.ITEM_SMELTER.setHandler(new MRSmelterHandler());
        MachineRecipeType.ITEM_GRINDER.setHandler(new MRHandlerItemStack(MachineRecipeType.ITEM_GRINDER.INTERNAL_NAME));
        MachineRecipeType.ITEM_CRUSHER.setHandler(new MRHandlerItemStack(MachineRecipeType.ITEM_CRUSHER.INTERNAL_NAME));
        MachineRecipeType.ITEM_WASHER.setHandler(new MRHandlerItemStack(MachineRecipeType.ITEM_WASHER.INTERNAL_NAME));
        MachineRecipeType.ITEM_SAWMILL.setHandler(new MRHandlerItemStack(MachineRecipeType.ITEM_SAWMILL.INTERNAL_NAME));
        MachineRecipeType.FLUID_SMELTER.setHandler(new MRHandlerFluidStack(MachineRecipeType.FLUID_SMELTER.INTERNAL_NAME));
        MachineRecipeType.FLUID_CAST.setHandler(new MRHandlerCast());

        RecipeSorter.register(References.PREFIX + "sheetMetalTools", RecipeSheetMetal.class, SHAPED, "after:minecraft:shaped");
        RecipeSorter.register(References.PREFIX + "Tools", RecipeTool.class, SHAPED, "after:minecraft:shaped");

        loader.applyModule(getProxy());
        loader.applyModule(packetHandler);
        loader.applyModule(CrusherRecipeLoad.class);
        loader.applyModule(GrinderRecipeLoad.class);
        loader.applyModule(FluidSmelterRecipeLoad.class);
        loader.applyModule(CastRecipeLoader.class);
        loader.applyModule(GearRecipeLoader.class);
        loader.applyModule(RodRecipeLoader.class);
        loader.applyModule(PlateRecipeLoader.class);
        loader.applyModule(NEIProxy.class);
        loader.applyModule(GroupProfileHandler.GLOBAL);
        loader.applyModule(OCProxy.class, Mods.OC.isLoaded());
        loader.applyModule(TinkerProxy.class, Mods.TINKERS.isLoaded());

        //Check if RF api exists
        boolean shouldLoadRFHandler = true;
        for (String s : new String[]{"IEnergyConnection", "IEnergyContainerItem", "IEnergyHandler", "IEnergyProvider", "IEnergyReceiver", "IEnergyStorage"})
        {
            try
            {
                Class clazz = Class.forName("cofh.api.energy." + s);
                if (clazz == null)
                {
                    shouldLoadRFHandler = false;
                    break;
                }
            } catch (ClassNotFoundException e)
            {
                shouldLoadRFHandler = false;
                logger().error("Not loading RF support as we couldn't detect one of cofh's energy classes");
                break;
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        if (shouldLoadRFHandler)
        {
            loader.applyModule(RFLoader.class);
        }

        PotionUtility.resizePotionArray();

        //Manually registered configs TODO setup threw sync system (once the system is tested)
        CommandVE.disableCommands = getConfig().getBoolean("DisableServerCommands", "Commands", false, "Turns off all commands built into Voltz Engine");
        CommandVE.disableButcherCommand = getConfig().getBoolean("DisableButcherCommands", "Commands", false, "Turns off butcher command");
        CommandVE.disableClearCommand = getConfig().getBoolean("DisableClearCommands", "Commands", false, "Turns off clear command");
        CommandVE.disableRemoveCommand = getConfig().getBoolean("DisableRemoverCommands", "Commands", false, "Turns off remove command");

        ToolMode.REGISTRY.add(new ToolModeGeneral());
        ToolMode.REGISTRY.add(new ToolModeRotation());

        if (runningAsDev)
            itemCraftingParts = contentRegistry.newItem(ItemParts.class);
        /**
         * Multiblock Handling
         */
        if (getConfig().get("Content", "LoadInstantHole", runningAsDev, "This is a developer tool for checking if ores generated correctly. It creates a chunk sized hole in the ground replacing stone with air, and air with glass. Never enable or give this to normal users as it can be used for greifing.").getBoolean(runningAsDev))
            instaHole = contentRegistry.newItem("ve.instanthole", new ItemInstaHole());
        if (getConfig().get("Content", "LoadScrewDriver", true, "Basic tool for configuring, rotating, and picking up machines.").getBoolean(true))
            itemWrench = getManager().newItem("ve.screwdriver", new ItemScrewdriver());
        if (getConfig().get("Content", "LoadSelectionTool", true, "Admin tool for selecting areas on the ground for world manipulation or other tasks.").getBoolean(true))
            itemSelectionTool = getManager().newItem("ve.selectiontool", new ItemSelectionWand());

        loader.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent evt)
    {
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
        if ((getConfig().hasKey("Content", "LoadOres") || metallicOresRequested) && getConfig().getBoolean("LoadOres", "Content", metallicOresRequested, "Loads up ore blocks and generators. Ore Generation can be disable separate if you want to keep the block for legacy purposes."))
        {
            ore = contentRegistry.newBlock(References.ID + "StoneOre", new BlockOre("stone"), ItemBlockOre.class);
            MetallicOres.registerSet(ore, getConfig());
        }

        if ((getConfig().hasKey("Content", "LoadGemOres") || gemOresRequested) && getConfig().getBoolean("LoadGemOres", "Content", gemOresRequested, "Loads up Gem Ores."))
        {
            gemOre = contentRegistry.newBlock(References.ID + "GemOre", new BlockGemOre("gem"), ItemBlockGemOre.class);
            GemOres.registerSet(gemOre, getConfig());
            gem = contentRegistry.newItem(References.ID + "Gem", ItemGems.class);
            Gems.registerSet(gem, getConfig());
        }

        if (multiBlockRequested)
        {
            multiBlock = new BlockMultiblock();
            GameRegistry.registerBlock(multiBlock, ItemBlockMulti.class, "veMultiBlock");
            NEIProxy.hideItem(multiBlock);
            EnumMultiblock.register();
        }

        boolean forceLoadSheetMetal = (sheetMetalRequested || getConfig().hasKey("SheetMetalContent", "ForceLoad")) && getConfig().getBoolean("ForceLoad", "SheetMetalContent", true, "Forces the sheet metal items to load even if not requests. Content can still loaded if false as long as another mod requests the content for crafting. This config is designed to prevent items from vanishing in saves.");
        boolean forceLoadSimpleTools = (simpleToolsRequested || getConfig().hasKey("SimpleToolsContent", "ForceLoad")) && getConfig().getBoolean("ForceLoad", "SimpleToolsContent", true, "Forces the simple tools items to load even if not requests. Content can still loaded if false as long as another mod requests the content for crafting. This config is designed to prevent items from vanishing in saves.");

        if (sheetMetalRequested || forceLoadSheetMetal)
        {
            itemSheetMetalTools = getManager().newItem("veSheetMetalTools", ItemSheetMetalTools.class);
            itemSheetMetal = getManager().newItem("veSheetMetal", ItemSheetMetal.class);
        }

        if (simpleToolsRequested || forceLoadSimpleTools)
        {
            itemSimpleCraftingTools = getManager().newItem("veSimpleTools", ItemSimpleCraftingTool.class);
        }

        if (getConfig().getBoolean("LoadHeatedRocks", "Content", heatedRockRequested, "Loads up heated rocks which are used to give explosions an extra short term effect on stone."))
        {
            heatedStone = contentRegistry.newBlock("VEHeatedRock", BlockHeatedStone.class, ItemBlockMetadata.class);
            NEIProxy.hideItem(heatedStone);
            if (enabledHeatMap)
            {
                HeatedBlockRegistry.addNewHeatingConversion(Blocks.stone, new PlacementData(heatedStone, 15), 600);
                HeatedBlockRegistry.addNewHeatingConversion(heatedStone, Blocks.lava, 1200);
            }
        }

        logger.info("Starting resource generator");
        long start = System.nanoTime();
        for (DefinedGenItems genItem : DefinedGenItems.values())
        {
            if ((getConfig().hasKey("ForceLoadContent", genItem.name) || genItem.isRequested()) && getConfig().getBoolean(genItem.name, "ForceLoadContent", genItem.isRequested(), "Forces the items to load even if not requests. Content can still loaded if false as long as another mod requests the content for crafting. This config is designed to prevent items from vanishing in saves.") || genItem.isRequested())
            {
                logger.info("\tGenerating " + genItem.name);
                genItem.item = getManager().newItem("ve" + LanguageUtility.capitalizeFirst(genItem.name), new ItemGenMaterial(genItem));
                for (GenMaterial mat : GenMaterial.values())
                {
                    if (mat == GenMaterial.UNKNOWN || genItem.ignoreMaterials.contains(mat))
                    {
                        NEIProxy.hideItem(new ItemStack(genItem.item, 1, mat.ordinal()));
                    }
                    else
                    {
                        OreDictionary.registerOre(genItem.oreDict + LanguageUtility.capitalizeFirst(mat.name().toLowerCase()), genItem.stack(mat));
                    }
                }
            }
        }
        logger.info("Done... Took " + StringHelpers.formatTimeDifference(start, System.nanoTime()));

        loader.init();
        getManager().fireInit();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent evt)
    {
        //All blocks should be loaded before post init so we can init things that need to iterate over the block list
        if (enabledHeatMap)
            HeatedBlockRegistry.init(heatDataConfig);
        OreDictionary.registerOre("ingotGold", Items.gold_ingot);
        OreDictionary.registerOre("ingotIron", Items.iron_ingot);
        OreDictionary.registerOre("oreGold", Blocks.gold_ore);
        OreDictionary.registerOre("oreIron", Blocks.iron_ore);
        OreDictionary.registerOre("oreLapis", Blocks.lapis_ore);

        loader.postInit();
        getManager().firePostInit();

        if (metallicOresRequested)
        {
            for (MetallicOres ore : MetallicOres.values())
            {
                List<ItemStack> ingots = OreDictionary.getOres("ingot" + LanguageUtility.capitalizeFirst(ore.name().toLowerCase()));
                if (ingots != null && !ingots.isEmpty())
                {
                    GameRegistry.addSmelting(ore.stack(), ingots.get(0), 0.01f);
                }
            }
        }

        //Save configs as this is our last chance
        heatDataConfig.save();
        explosiveConfig.save();
        getConfig().save();
    }

    public AbstractProxy getProxy()
    {
        return proxy;
    }

    public Configuration getConfig()
    {
        return config;
    }

    public ModManager getManager()
    {
        return this.manager;
    }

    public static Logger logger()
    {
        return logger;
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
        return player instanceof EntityPlayerMP && isPlayerOpped((EntityPlayerMP) player);
    }

    public static boolean isPlayerOpped(EntityPlayerMP player)
    {
        //Taken from EntityPlayerMP#canCommandSenderUseCommand(Integer, String)
        return player.mcServer.getConfigurationManager().func_152596_g(player.getGameProfile()) && player.mcServer.getConfigurationManager().func_152603_m().func_152683_b(player.getGameProfile()) != null;
    }

    /**
     * Use to print errors to the logger. If engine instance is null it will
     * throw the error message as an exception. This is designed for JUnit
     * tests that want to handle exceptions rather than see printlns.
     *
     * @param msg   - message that describes the issue
     * @param error - error to throw
     * @throws Throwable - throws an exception if {@link Engine#instance}
     */
    public static void error(String msg, Throwable error) throws Throwable
    {
        if (instance == null)
        {
            throw error;
        }
        else
        {
            logger().error(msg, error);
        }
    }

    /**
     * Use to print errors to the logger. If engine instance is null it will
     * throw the error message as an exception. This is designed for JUnit
     * tests that want to handle exceptions rather than see printlns.
     *
     * @param msg - message that describes the issue
     * @throws Throwable - throws an exception if {@link Engine#instance}
     */
    public static void error(String msg)
    {
        if (instance == null)
        {
            throw new RuntimeException(msg);
        }
        else
        {
            logger().error(msg);
        }
    }
}

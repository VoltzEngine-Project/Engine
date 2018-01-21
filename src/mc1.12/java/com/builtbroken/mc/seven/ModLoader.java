package com.builtbroken.mc.seven;

import com.builtbroken.jlib.lang.StringHelpers;
import com.builtbroken.mc.abstraction.EngineLoader;
import com.builtbroken.mc.api.VoltzEngineAPI;
import com.builtbroken.mc.api.event.TriggerCauseRegistry;
import com.builtbroken.mc.api.process.IWorkerThread;
import com.builtbroken.mc.api.recipe.MachineRecipeType;
import com.builtbroken.mc.core.ConfigValues;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.core.commands.CommandVE;
import com.builtbroken.mc.core.content.blast.tnt.ExplosiveHandlerTNT;
import com.builtbroken.mc.core.content.debug.ItemDevData;
import com.builtbroken.mc.core.content.tool.ItemScrewdriver;
import com.builtbroken.mc.core.content.tool.screwdriver.ToolMode;
import com.builtbroken.mc.core.content.tool.screwdriver.ToolModeGeneral;
import com.builtbroken.mc.core.content.tool.screwdriver.ToolModeRotation;
import com.builtbroken.mc.core.handler.InteractionHandler;
import com.builtbroken.mc.core.handler.SaveManager;
import com.builtbroken.mc.core.handler.TileTaskTickHandler;
import com.builtbroken.mc.framework.explosive.ExplosiveRegistry;
import com.builtbroken.mc.framework.explosive.TriggerNBTBuilder;
import com.builtbroken.mc.framework.json.JsonContentLoader;
import com.builtbroken.mc.framework.json.imp.IJsonGenObject;
import com.builtbroken.mc.framework.json.override.JsonOverrideProcessor;
import com.builtbroken.mc.framework.json.processors.event.JsonMissingMapEventProcessor;
import com.builtbroken.mc.framework.json.processors.explosive.JsonProcessorExplosive;
import com.builtbroken.mc.framework.json.processors.multiblock.JsonMultiBlockLayoutProcessor;
import com.builtbroken.mc.framework.mod.AbstractProxy;
import com.builtbroken.mc.framework.recipe.fluid.MRHandlerFluidStack;
import com.builtbroken.mc.framework.recipe.item.MRHandlerItemStack;
import com.builtbroken.mc.framework.recipe.item.MRSmelterHandler;
import com.builtbroken.mc.framework.recipe.item.RecipeTool;
import com.builtbroken.mc.framework.thread.WorkerThread;
import com.builtbroken.mc.framework.thread.action.WorldActionQue;
import com.builtbroken.mc.lib.data.heat.HeatedBlockRegistry;
import com.builtbroken.mc.lib.data.mass.MassRegistry;
import com.builtbroken.mc.lib.helper.recipe.OreNames;
import com.builtbroken.mc.lib.world.map.TileMapRegistry;
import com.builtbroken.mc.lib.world.map.radar.RadarRegistry;
import com.builtbroken.mc.lib.world.map.radio.RadioRegistry;
import com.builtbroken.mc.seven.framework.block.BlockBase;
import com.builtbroken.mc.seven.framework.block.ItemBlockBase;
import com.builtbroken.mc.seven.framework.block.json.JsonBlockListenerProcessor;
import com.builtbroken.mc.seven.framework.block.json.JsonBlockProcessor;
import com.builtbroken.mc.seven.framework.block.json.JsonBlockTileProcessor;
import com.builtbroken.mc.seven.framework.block.listeners.MultiBlockListener;
import com.builtbroken.mc.seven.framework.block.listeners.RenderBoundsListener;
import com.builtbroken.mc.seven.framework.block.listeners.RotatableListener;
import com.builtbroken.mc.seven.framework.block.listeners.WrenchRotationListener;
import com.builtbroken.mc.seven.framework.block.meta.BlockMeta;
import com.builtbroken.mc.seven.framework.block.meta.ItemBlockMeta;
import com.builtbroken.mc.seven.framework.json.extra.JsonOreNameProcessor;
import com.builtbroken.mc.seven.framework.json.item.JsonItemProcessor;
import com.builtbroken.mc.seven.framework.json.recipe.crafting.JsonCraftingRecipeProcessor;
import com.builtbroken.mc.seven.framework.json.recipe.replace.JsonRecipeReplacementProcessor;
import com.builtbroken.mc.seven.framework.json.recipe.smelting.JsonFurnaceRecipeProcessor;
import com.builtbroken.mc.seven.framework.json.world.JsonWorldOreGenProcessor;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static net.minecraftforge.oredict.RecipeSorter.Category.SHAPED;

/**
 * Mod loader for the engine
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/12/2017.
 */
@Mod(modid = References.ID, name = References.NAME, version = References.VERSION, dependencies = "after:TConstruct")
@Mod.EventBusSubscriber()
public class ModLoader extends EngineLoader
{
    @SidedProxy(clientSide = "com.builtbroken.mc.seven.client.ClientProxy", serverSide = "com.builtbroken.mc.core.ServerProxy")
    public static CommonProxy proxy;


    @Mod.Metadata(References.ID)
    public static ModMetadata metadata;

    @Mod.Instance(References.ID)
    public static ModLoader instance;

    public static Configuration config;


    public static int actionProcessorThreads = 3;

    //Configs
    public static boolean enabledHeatMap = true;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        Engine.loaderInstance = this;
        proxy.onLoad();

        //Init API values
        VoltzEngineAPI.massRegistry = new MassRegistry();

        //Init run time references
        References.GLOBAL_CONFIG_FOLDER = event.getModConfigurationDirectory();
        References.ROOT_FOLDER = References.GLOBAL_CONFIG_FOLDER.getParentFile();
        References.BBM_CONFIG_FOLDER = new File(event.getModConfigurationDirectory(), "bbm");

        //Load config files
        config = References.configuration = new Configuration(new File(References.BBM_CONFIG_FOLDER, "ve/VoltzEngine.cfg"));
        References.heatDataConfig = new Configuration(new File(event.getModConfigurationDirectory(), "bbm/ve/HeatMap.cfg"));
        References.explosiveConfig = new Configuration(new File(event.getModConfigurationDirectory(), "bbm/ve/Explosives.cfg"));

        config.load();
        References.heatDataConfig.load();
        References.explosiveConfig.load();

        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(proxy);
        MinecraftForge.EVENT_BUS.register(SaveManager.instance());
        MinecraftForge.EVENT_BUS.register(new InteractionHandler());
        MinecraftForge.EVENT_BUS.register(RadarRegistry.INSTANCE);
        MinecraftForge.EVENT_BUS.register(TileMapRegistry.INSTANCE);
        MinecraftForge.EVENT_BUS.register(RadioRegistry.INSTANCE);
        MinecraftForge.EVENT_BUS.register(new WorldActionQue());
        MinecraftForge.EVENT_BUS.register(TileTaskTickHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(proxy);

        //Load heat configs
        enabledHeatMap = References.heatDataConfig.getBoolean("EnabledHeatMap", Configuration.CATEGORY_GENERAL, true, "Heat map handles interaction of heat based energy and the world. Disable only if it causes issues or you want to reduce world file size. If disabled it can prevent machines from working.");

        //Load explosive configs
        ConfigValues.log_registering_explosives = References.explosiveConfig.getBoolean("EnableRegisterLogging", Configuration.CATEGORY_GENERAL, false, "Adds debug each time a mod registers an explosive handler. Should only be enabled to figure out which mod is overriding another mod's explosive");

        MachineRecipeType.ITEM_SMELTER.setHandler(new MRSmelterHandler());
        MachineRecipeType.ITEM_GRINDER.setHandler(new MRHandlerItemStack(MachineRecipeType.ITEM_GRINDER.INTERNAL_NAME));
        MachineRecipeType.ITEM_CRUSHER.setHandler(new MRHandlerItemStack(MachineRecipeType.ITEM_CRUSHER.INTERNAL_NAME));
        MachineRecipeType.ITEM_WASHER.setHandler(new MRHandlerItemStack(MachineRecipeType.ITEM_WASHER.INTERNAL_NAME));
        MachineRecipeType.ITEM_SAWMILL.setHandler(new MRHandlerItemStack(MachineRecipeType.ITEM_SAWMILL.INTERNAL_NAME));
        MachineRecipeType.FLUID_SMELTER.setHandler(new MRHandlerFluidStack(MachineRecipeType.FLUID_SMELTER.INTERNAL_NAME));

        //RecipeSorter.register(References.PREFIX + "sheetMetalTools", RecipeSheetMetal.class, SHAPED, "after:minecraft:shaped");
        RecipeSorter.register(References.PREFIX + "Tools", RecipeTool.class, SHAPED, "after:minecraft:shaped");
        //RecipeSorter.register(References.PREFIX + "shapedLarge", RecipeShapedOreLarge.class, SHAPED, "after:minecraft:shaped");

        loader.applyModule(getProxy());
        loader.applyModule(Engine.packetHandler);

        loadHandler();

        //TODO move somewhere else
        TriggerCauseRegistry.register("entity", new TriggerNBTBuilder("entity"));
        TriggerCauseRegistry.register("impactEntity", new TriggerNBTBuilder("impactEntity"));
        TriggerCauseRegistry.register("entityImpactBlock", new TriggerNBTBuilder("entityImpactBlock"));
        TriggerCauseRegistry.register("entityImpactEntity", new TriggerNBTBuilder("entityImpactEntity"));
        TriggerCauseRegistry.register("explosion", new TriggerNBTBuilder("explosion"));
        TriggerCauseRegistry.register("fire", new TriggerNBTBuilder("fire"));
        TriggerCauseRegistry.register("redstone", new TriggerNBTBuilder("redstone"));


        //Manually registered configs
        CommandVE.disableCommands = getConfig().getBoolean("DisableServerCommands", "Commands", false, "Turns off all commands built into Voltz Engine");
        CommandVE.disableButcherCommand = getConfig().getBoolean("DisableButcherCommands", "Commands", false, "Turns off butcher command");
        CommandVE.disableClearCommand = getConfig().getBoolean("DisableClearCommands", "Commands", false, "Turns off clear command");
        CommandVE.disableRemoveCommand = getConfig().getBoolean("DisableRemoverCommands", "Commands", false, "Turns off remove command");

        //Map commands
        ConfigValues.enableExtendedMetaPacketSync = getConfig().getBoolean("EnableExtendedBlockMetaPacketSync", "Map_data", true, "While on extended meta values will be synced to the client. Can be disabled on both sides to save on bandwidth but will result in rendering issues if disabled.");

        ToolMode.REGISTRY.add(new ToolModeGeneral());
        ToolMode.REGISTRY.add(new ToolModeRotation());

        //TODO move TNT registry to JSON (both ex and items
        ExplosiveRegistry.registerOrGetExplosive(References.DOMAIN, "TNT", new ExplosiveHandlerTNT());

        final int tntValue = 8;
        ExplosiveRegistry.registerExplosiveItem(new ItemStack(Blocks.TNT), ExplosiveRegistry.get("TNT"), tntValue);
        ExplosiveRegistry.registerExplosiveItem(new ItemStack(Items.GUNPOWDER), ExplosiveRegistry.get("TNT"), tntValue / 5.0);
        //Creeper skull
        ExplosiveRegistry.registerExplosiveItem(new ItemStack(Items.SKULL, 1, 4), ExplosiveRegistry.get("TNT"), tntValue / 10.0);

        //Call loader
        loader.preInit();
        //Claim json content


        //Ore dictionary registry
        OreDictionary.registerOre(OreNames.WOOD_STICK, Items.STICK);
        OreDictionary.registerOre(OreNames.STRING, Items.STRING);
        OreDictionary.registerOre(OreNames.FLINT, Items.FLINT);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        if (config.get("Content", "LoadDevDataTool", Engine.runningAsDev, "This is a developer tool for checking data on blocks and tile").getBoolean(Engine.runningAsDev))
        {
            event.getRegistry().register(Engine.itemDevTool = new ItemDevData());
        }
        if (config.get("Content", "LoadScrewDriver", true, "Basic tool for configuring, rotating, and picking up machines.").getBoolean(true))
        {
            event.getRegistry().register(Engine.itemWrench = new ItemScrewdriver());
        }
        JsonContentLoader.INSTANCE.claimItems(instance, event);

        //Register item blocks for blocks, TODO add way to remove item block via JSON
        List<IJsonGenObject> generatedBlocks = JsonContentLoader.INSTANCE.generatedObjects.get(References.JSON_BLOCK_KEY);
        if(generatedBlocks != null)
        {
            for (IJsonGenObject object : generatedBlocks)
            {
                if (object instanceof Block && ((Block) object).getRegistryName() != null && ((Block) object).getRegistryName().getResourceDomain() == References.DOMAIN)
                {
                    if (object instanceof BlockMeta)
                    {
                        Item itemBlock = new ItemBlockMeta((Block) object).setRegistryName(((Block) object).getRegistryName());
                        event.getRegistry().register(itemBlock);
                    }
                    else if (object instanceof BlockBase)
                    {
                        Item itemBlock = new ItemBlockBase((Block) object).setRegistryName(((Block) object).getRegistryName());
                        event.getRegistry().register(itemBlock);
                    }
                    else
                    {
                        Item itemBlock = new ItemBlock((Block) object).setRegistryName(((Block) object).getRegistryName());
                        event.getRegistry().register(itemBlock);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        JsonContentLoader.INSTANCE.claimBlocks(instance, event);
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
    {
        JsonContentLoader.INSTANCE.claimRecipes(instance, event);
    }

    @Override
    public void loadJsonContentHandlers()
    {
        JsonContentLoader.INSTANCE.add(new JsonBlockProcessor());
        JsonContentLoader.INSTANCE.add(new JsonBlockTileProcessor());
        JsonContentLoader.INSTANCE.add(new JsonMultiBlockLayoutProcessor());

        //==============================================================
        JsonContentLoader.INSTANCE.add(new JsonItemProcessor());
        JsonContentLoader.INSTANCE.add(new JsonOreNameProcessor());

        //==============================================================
        //TODO load entities

        //==============================================================
        JsonContentLoader.INSTANCE.add(new JsonCraftingRecipeProcessor());
        JsonContentLoader.INSTANCE.add(new JsonFurnaceRecipeProcessor());
        JsonContentLoader.INSTANCE.add(new JsonRecipeReplacementProcessor());

        //==============================================================
        JsonContentLoader.INSTANCE.add(new JsonWorldOreGenProcessor());
        JsonContentLoader.INSTANCE.add(new JsonOverrideProcessor());
        JsonContentLoader.INSTANCE.add(new JsonMissingMapEventProcessor());
        JsonContentLoader.INSTANCE.add(new JsonProcessorExplosive());
        //TODO add machine recipes


        //==============================================================
        JsonBlockListenerProcessor.addBuilder(new RotatableListener.Builder());
        JsonBlockListenerProcessor.addBuilder(new MultiBlockListener.Builder());
        JsonBlockListenerProcessor.addBuilder(new WrenchRotationListener.Builder());
        JsonBlockListenerProcessor.addBuilder(new RenderBoundsListener.Builder());
        //JsonBlockListenerProcessor.addBuilder(new AdjacentPlacementListener.Builder());
        //JsonBlockListenerProcessor.addBuilder(new PathPlacementListener.Builder());
        getProxy().loadJsonContentHandlers();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent evt)
    {
        //TODO make sure is setup correctly
        metadata.modId = References.NAME;
        metadata.name = References.NAME;
        metadata.description = References.NAME + " is a content creation toolkit";
        metadata.url = "http://www.builtbroken.com";
        metadata.version = References.VERSION;
        metadata.authorList = Arrays.asList("DarkCow");
        metadata.autogenerated = false;


        loader.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent evt)
    {
        //All blocks should be loaded before post init so we can init things that need to iterate over the block list
        if (enabledHeatMap)
        {
            HeatedBlockRegistry.init(References.heatDataConfig);
        }

        loader.postInit();

        //Creates world change threads for ques
        actionProcessorThreads = getConfig().getInt("WorldActionThreads", "Multi-Threading", Runtime.getRuntime().availableProcessors() - 1, 0, 100, "Creates the number of threads to be used for processing changes to the world. Used by mods like ICBM to calculate explosives before removing blocks from the world. Try to keep this one less than the number of processors you have. This way minecraft is not chocked out for CPU time.");

        //Save configs as this is our last chance
        References.heatDataConfig.save();
        References.explosiveConfig.save();
        getConfig().save();
    }

    @Mod.EventHandler
    public void loadCompleteEvent(FMLLoadCompleteEvent event)
    {
        //Clean up resources to free up ram
        loader.loadComplete();

        long time = System.nanoTime();
        Engine.logger().error("Checking ore dictionary for bad values");
        //Fix ore dictionary
        String[] oreNames = OreDictionary.getOreNames();
        for (String name : oreNames)
        {
            NonNullList<ItemStack> stacks = OreDictionary.getOres(name);
            for (ItemStack stack : stacks)
            {
                if (stack == null || stack.getItem() == null)
                {
                    Engine.logger().error("\tFound bad ore dictionary value stack='" + stack + "'  ore_name='" + name + "'");
                }
            }
        }
        time = System.nanoTime() - time;
        Engine.logger().error("Done.... took: " + StringHelpers.formatNanoTime(time));
    }

    @Mod.EventHandler
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

        for (int i = 0; i < actionProcessorThreads; i++)
        {
            Thread thread = new WorkerThread("" + i);
            thread.start();
        }
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event)
    {
        //TODO save qued objects
        synchronized (VoltzEngineAPI.WORKER_THREADS)
        {
            for (IWorkerThread thread : VoltzEngineAPI.WORKER_THREADS.values())
            {
                logger().info("Killing thread " + thread);
                thread.kill();
            }
        }
    }

    @Mod.EventHandler
    public void missingItemMappingEvent(RegistryEvent.MissingMappings<Item> event)
    {
        for (RegistryEvent.MissingMappings.Mapping<Item> missingMapping : event.getAllMappings())
        {
            final String name = missingMapping.key.toString();
            String key = null;
            if (name.startsWith("VoltzEngine:"))
            {
                key = "voltzengine:" + name.split(":")[1];
                key = key.replace("VoltzEngine", "ve");
            }
            else if (JsonMissingMapEventProcessor.mappings.containsKey(name))
            {
                key = JsonMissingMapEventProcessor.mappings.get(name);
            }

            if (key != null && !key.isEmpty())
            {
                Engine.logger().info("Fixing missing mapping for '" + name + "' replacing with '" + key + "'");
                Item object = missingMapping.registry.getValue(new ResourceLocation(key));

                if (object != null)
                {
                    missingMapping.remap(object);
                }
            }
        }
    }

    @Mod.EventHandler
    public void missingBlockMappingEvent(RegistryEvent.MissingMappings<Block> event)
    {
        for (RegistryEvent.MissingMappings.Mapping<Block> missingMapping : event.getAllMappings())
        {
            final String name = missingMapping.key.toString();
            String key = null;
            if (name.startsWith("VoltzEngine:"))
            {
                key = "voltzengine:" + name.split(":")[1];
                key = key.replace("VoltzEngine", "ve");
            }
            else if (JsonMissingMapEventProcessor.mappings.containsKey(name))
            {
                key = JsonMissingMapEventProcessor.mappings.get(name);
            }

            if (key != null && !key.isEmpty())
            {
                Engine.logger().info("Fixing missing mapping for '" + name + "' replacing with '" + key + "'");
                Block object = missingMapping.registry.getValue(new ResourceLocation(key));

                if (object != Blocks.AIR && object != null)
                {
                    missingMapping.remap(object);

                }
            }
        }
    }

    public AbstractProxy getProxy()
    {
        return proxy;
    }

    public Configuration getConfig()
    {
        return config;
    }

    @Deprecated
    public static Logger logger()
    {
        return Engine.logger();
    }
}

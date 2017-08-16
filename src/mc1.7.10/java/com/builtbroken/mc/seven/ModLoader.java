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
import com.builtbroken.mc.core.asm.ProxyASMTest;
import com.builtbroken.mc.core.commands.CommandVE;
import com.builtbroken.mc.core.content.blast.tnt.ExplosiveHandlerTNT;
import com.builtbroken.mc.core.content.blocks.BlockHeatedStone;
import com.builtbroken.mc.core.content.debug.ItemDevData;
import com.builtbroken.mc.core.content.debug.ItemInstaHole;
import com.builtbroken.mc.core.content.debug.TileInfInv;
import com.builtbroken.mc.core.content.parts.ItemCircuits;
import com.builtbroken.mc.core.content.parts.ItemCraftingParts;
import com.builtbroken.mc.core.content.resources.DefinedGenItems;
import com.builtbroken.mc.core.content.resources.GenMaterial;
import com.builtbroken.mc.core.content.resources.gems.*;
import com.builtbroken.mc.core.content.resources.items.ItemGenMaterial;
import com.builtbroken.mc.core.content.resources.items.ItemSheetMetal;
import com.builtbroken.mc.core.content.resources.load.*;
import com.builtbroken.mc.core.content.resources.ore.BlockOre;
import com.builtbroken.mc.core.content.resources.ore.ItemBlockOre;
import com.builtbroken.mc.core.content.resources.ore.MetallicOres;
import com.builtbroken.mc.core.content.tool.ItemScrewdriver;
import com.builtbroken.mc.core.content.tool.ItemSheetMetalTools;
import com.builtbroken.mc.core.content.tool.ItemSimpleCraftingTool;
import com.builtbroken.mc.core.content.tool.screwdriver.ToolMode;
import com.builtbroken.mc.core.content.tool.screwdriver.ToolModeGeneral;
import com.builtbroken.mc.core.content.tool.screwdriver.ToolModeRotation;
import com.builtbroken.mc.core.content.world.DevWorldLoader;
import com.builtbroken.mc.core.handler.InteractionHandler;
import com.builtbroken.mc.core.handler.SaveManager;
import com.builtbroken.mc.core.handler.TileTaskTickHandler;
import com.builtbroken.mc.core.registry.ModManager;
import com.builtbroken.mc.framework.explosive.ExplosiveRegistry;
import com.builtbroken.mc.framework.explosive.TriggerNBTBuilder;
import com.builtbroken.mc.framework.json.JsonContentLoader;
import com.builtbroken.mc.framework.json.override.JsonOverrideProcessor;
import com.builtbroken.mc.framework.json.processors.event.JsonMissingMapEventProcessor;
import com.builtbroken.mc.framework.json.processors.explosive.JsonProcessorExplosive;
import com.builtbroken.mc.framework.json.processors.multiblock.JsonMultiBlockLayoutProcessor;
import com.builtbroken.mc.framework.mod.AbstractProxy;
import com.builtbroken.mc.framework.multiblock.BlockMultiblock;
import com.builtbroken.mc.framework.multiblock.EnumMultiblock;
import com.builtbroken.mc.framework.multiblock.ItemBlockMulti;
import com.builtbroken.mc.framework.recipe.cast.MRHandlerCast;
import com.builtbroken.mc.framework.recipe.fluid.MRHandlerFluidStack;
import com.builtbroken.mc.framework.recipe.item.MRHandlerItemStack;
import com.builtbroken.mc.framework.recipe.item.MRSmelterHandler;
import com.builtbroken.mc.framework.recipe.item.RecipeTool;
import com.builtbroken.mc.framework.recipe.item.grid.RecipeShapedOreLarge;
import com.builtbroken.mc.framework.recipe.item.sheetmetal.RecipeSheetMetal;
import com.builtbroken.mc.framework.thread.WorkerThread;
import com.builtbroken.mc.framework.thread.action.WorldActionQue;
import com.builtbroken.mc.lib.data.heat.HeatedBlockRegistry;
import com.builtbroken.mc.lib.data.mass.MassRegistry;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.lib.helper.PotionUtility;
import com.builtbroken.mc.lib.helper.recipe.OreNames;
import com.builtbroken.mc.lib.world.edit.PlacementData;
import com.builtbroken.mc.lib.world.edit.PlacementDataExtended;
import com.builtbroken.mc.lib.world.map.TileMapRegistry;
import com.builtbroken.mc.lib.world.map.radar.RadarRegistry;
import com.builtbroken.mc.lib.world.map.radio.RadioRegistry;
import com.builtbroken.mc.prefab.tile.item.ItemBlockMetadata;
import com.builtbroken.mc.seven.framework.block.json.JsonBlockListenerProcessor;
import com.builtbroken.mc.seven.framework.block.json.JsonBlockProcessor;
import com.builtbroken.mc.seven.framework.block.json.JsonBlockTileProcessor;
import com.builtbroken.mc.seven.framework.block.listeners.*;
import com.builtbroken.mc.seven.framework.json.extra.JsonOreNameProcessor;
import com.builtbroken.mc.seven.framework.json.item.JsonItemProcessor;
import com.builtbroken.mc.seven.framework.json.recipe.crafting.JsonCraftingRecipeProcessor;
import com.builtbroken.mc.seven.framework.json.recipe.replace.JsonRecipeReplacementProcessor;
import com.builtbroken.mc.seven.framework.json.recipe.smelting.JsonFurnaceRecipeProcessor;
import com.builtbroken.mc.seven.framework.json.world.JsonWorldOreGenProcessor;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraftforge.oredict.RecipeSorter.Category.SHAPED;

/**
 * Mod loader for the engine
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/12/2017.
 */
@Mod(modid = References.ID, name = References.NAME, version = References.VERSION, dependencies = "required-after:Forge;after:TConstruct")
public class ModLoader extends EngineLoader
{
    @SidedProxy(clientSide = "com.builtbroken.mc.seven.client.ClientProxy", serverSide = "com.builtbroken.mc.core.ServerProxy")
    public static CommonProxy proxy;

    @Deprecated
    public static final ModManager contentRegistry = new ModManager().setPrefix(References.PREFIX).setTab(CreativeTabs.tabTools);

    @Mod.Metadata(References.ID)
    public static ModMetadata metadata;

    @Mod.Instance(References.ID)
    public static ModLoader instance;

    public ModManager manager;
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

        manager = new ModManager().setPrefix(References.DOMAIN).setTab(CreativeTabs.tabAllSearch);

        config.load();
        References.heatDataConfig.load();
        References.explosiveConfig.load();

        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(proxy);
        MinecraftForge.EVENT_BUS.register(SaveManager.instance());
        MinecraftForge.EVENT_BUS.register(new InteractionHandler());

        MinecraftForge.EVENT_BUS.register(RadarRegistry.INSTANCE);
        FMLCommonHandler.instance().bus().register(RadarRegistry.INSTANCE);

        MinecraftForge.EVENT_BUS.register(TileMapRegistry.INSTANCE);
        FMLCommonHandler.instance().bus().register(TileMapRegistry.INSTANCE);

        MinecraftForge.EVENT_BUS.register(RadioRegistry.INSTANCE);
        FMLCommonHandler.instance().bus().register(RadioRegistry.INSTANCE);

        FMLCommonHandler.instance().bus().register(new WorldActionQue());
        FMLCommonHandler.instance().bus().register(TileTaskTickHandler.INSTANCE);

        FMLCommonHandler.instance().bus().register(proxy);

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
        MachineRecipeType.FLUID_CAST.setHandler(new MRHandlerCast());

        RecipeSorter.register(References.PREFIX + "sheetMetalTools", RecipeSheetMetal.class, SHAPED, "after:minecraft:shaped");
        RecipeSorter.register(References.PREFIX + "Tools", RecipeTool.class, SHAPED, "after:minecraft:shaped");
        RecipeSorter.register(References.PREFIX + "shapedLarge", RecipeShapedOreLarge.class, SHAPED, "after:minecraft:shaped");

        //Internal systems
        if (config.getBoolean("ASMTestingEnabled", "Internal", true, "Enables the testing of the internally used ASM code, used to ensure quality of the game. Only disable if you know the ASM is functional or there are issues with it running. Normally though if the ASM test fails then the ASM code itself was not injected. Which will result in several features of the mod not functioning correctly."))
        {
            //TODO check for Bukkit and disable
            loader.applyModule(new ProxyASMTest());
        }
        loader.applyModule(getProxy());
        loader.applyModule(Engine.packetHandler);

        loadHandler();

        //Recipes
        loader.applyModule(SmeltingRecipeLoad.class);
        loader.applyModule(CrusherRecipeLoad.class);
        loader.applyModule(GrinderRecipeLoad.class);
        loader.applyModule(FluidSmelterRecipeLoad.class);
        loader.applyModule(CastRecipeLoader.class);
        loader.applyModule(GearRecipeLoader.class);
        loader.applyModule(RodRecipeLoader.class);
        loader.applyModule(PlateRecipeLoader.class);
        loader.applyModule(NuggetRecipeLoader.class);
        loader.applyModule(WireRecipeLoader.class);
        loader.applyModule(ScrewRecipeLoader.class);

        //TODO move somewhere else
        TriggerCauseRegistry.register("entity", new TriggerNBTBuilder("entity"));
        TriggerCauseRegistry.register("impactEntity", new TriggerNBTBuilder("impactEntity"));
        TriggerCauseRegistry.register("entityImpactBlock", new TriggerNBTBuilder("entityImpactBlock"));
        TriggerCauseRegistry.register("entityImpactEntity", new TriggerNBTBuilder("entityImpactEntity"));
        TriggerCauseRegistry.register("explosion", new TriggerNBTBuilder("explosion"));
        TriggerCauseRegistry.register("fire", new TriggerNBTBuilder("fire"));
        TriggerCauseRegistry.register("redstone", new TriggerNBTBuilder("redstone"));

        //Load dev only content
        if (Engine.runningAsDev)
        {
            loader.applyModule(new DevWorldLoader());
        }

        PotionUtility.resizePotionArray();

        //Manually registered configs
        CommandVE.disableCommands = getConfig().getBoolean("DisableServerCommands", "Commands", false, "Turns off all commands built into Voltz Engine");
        CommandVE.disableButcherCommand = getConfig().getBoolean("DisableButcherCommands", "Commands", false, "Turns off butcher command");
        CommandVE.disableClearCommand = getConfig().getBoolean("DisableClearCommands", "Commands", false, "Turns off clear command");
        CommandVE.disableRemoveCommand = getConfig().getBoolean("DisableRemoverCommands", "Commands", false, "Turns off remove command");

        //Map commands
        ConfigValues.enableExtendedMetaPacketSync = getConfig().getBoolean("EnableExtendedBlockMetaPacketSync", "Map_data", true, "While on extended meta values will be synced to the client. Can be disabled on both sides to save on bandwidth but will result in rendering issues if disabled.");

        ToolMode.REGISTRY.add(new ToolModeGeneral());
        ToolMode.REGISTRY.add(new ToolModeRotation());

        /**
         * Multiblock Handling
         */
        if (getConfig().get("Content", "LoadInstantHole", Engine.runningAsDev, "This is a developer tool for checking if ores generated correctly. It creates a chunk sized hole in the ground replacing stone with air, and air with glass. Never enable or give this to normal users as it can be used for greifing.").getBoolean(Engine.runningAsDev))
        {
            Engine.instaHole = contentRegistry.newItem("ve.instanthole", new ItemInstaHole());
        }
        if (getConfig().get("Content", "LoadDevDataTool", Engine.runningAsDev, "This is a developer tool for checking data on blocks and tile").getBoolean(Engine.runningAsDev))
        {
            Engine.itemDevTool = contentRegistry.newItem("ve.devTool", new ItemDevData());
        }
        if (getConfig().get("Content", "LoadScrewDriver", true, "Basic tool for configuring, rotating, and picking up machines.").getBoolean(true))
        {
            Engine.itemWrench = getManager().newItem("ve.screwdriver", new ItemScrewdriver());
        }

        if (Engine.runningAsDev)
        {
            Engine.blockInfInventory = getManager().newBlock(TileInfInv.class);
        }

        //TODO move TNT registry to JSON (both ex and items
        ExplosiveRegistry.registerOrGetExplosive(References.DOMAIN, "TNT", new ExplosiveHandlerTNT());

        final int tntValue = 8;
        ExplosiveRegistry.registerExplosiveItem(new ItemStack(Blocks.tnt), ExplosiveRegistry.get("TNT"), tntValue);
        ExplosiveRegistry.registerExplosiveItem(new ItemStack(Items.gunpowder), ExplosiveRegistry.get("TNT"), tntValue / 5.0);
        //Creeper skull
        ExplosiveRegistry.registerExplosiveItem(new ItemStack(Items.skull, 1, 4), ExplosiveRegistry.get("TNT"), tntValue / 10.0);

        //Call loader
        loader.preInit();
        //Claim json content
        JsonContentLoader.INSTANCE.claimContent(this);

        //Ore dictionary registry
        OreDictionary.registerOre(OreNames.WOOD_STICK, Items.stick);
        OreDictionary.registerOre(OreNames.STRING, Items.string);
        OreDictionary.registerOre(OreNames.FLINT, Items.flint);
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
        JsonBlockListenerProcessor.addBuilder(new AdjacentPlacementListener.Builder());
        JsonBlockListenerProcessor.addBuilder(new PathPlacementListener.Builder());
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

        //Register UpdateTicker
        //FMLCommonHandler.instance().bus().register(UpdateTicker$.MODULE$.world());

        //Late registration of content
        if ((getConfig().hasKey("Content", "LoadOres") || Engine.metallicOresRequested) && getConfig().getBoolean("LoadOres", "Content", Engine.metallicOresRequested, "Loads up ore blocks and generators. Ore Generation can be disable separate if you want to keep the block for legacy purposes."))
        {
            Engine.ore = contentRegistry.newBlock("veStoneOre", new BlockOre("stone"), ItemBlockOre.class);
            MetallicOres.registerSet(Engine.ore, getConfig());
        }

        if ((getConfig().hasKey("Content", "LoadGemOres") || Engine.gemOresRequested) && getConfig().getBoolean("LoadGemOres", "Content", Engine.gemOresRequested, "Loads up Gem Ores."))
        {
            Engine.gemOre = contentRegistry.newBlock("veGemOre", new BlockGemOre("stone"), ItemBlockGemOre.class);
            GemOres.registerSet(Engine.gemOre, getConfig());

            for (GemTypes types : GemTypes.values())
            {
                if (types.isRequested())
                {
                    types.item = new ItemGem(types);
                    contentRegistry.newItem("Gem" + LanguageUtility.capitalizeFirst(types.name) + "Item", types.item);
                    for (Gems gem : Gems.values())
                    {
                        if (gem != Gems.UNKNOWN)
                        {
                            OreDictionary.registerOre(types.oreDict + gem.getOreName(), types.stack(gem));
                        }
                    }
                }
            }
        }

        if (Engine.multiBlockRequested)
        {
            Engine.multiBlock = new BlockMultiblock();
            GameRegistry.registerBlock(Engine.multiBlock, ItemBlockMulti.class, "veMultiBlock");
            //NEIProxy.hideItem(multiBlock);
            EnumMultiblock.register();
        }

        boolean forceLoadSheetMetal = (Engine.sheetMetalRequested || getConfig().hasKey("SheetMetalContent", "ForceLoad")) && getConfig().getBoolean("ForceLoad", "SheetMetalContent", true, "Forces the sheet metal items to load even if not requests. Content can still loaded if false as long as another mod requests the content for crafting. This config is designed to prevent items from vanishing in saves.");
        boolean forceLoadSimpleTools = (Engine.simpleToolsRequested || getConfig().hasKey("SimpleToolsContent", "ForceLoad")) && getConfig().getBoolean("ForceLoad", "SimpleToolsContent", true, "Forces the simple tools items to load even if not requests. Content can still loaded if false as long as another mod requests the content for crafting. This config is designed to prevent items from vanishing in saves.");
        boolean forceLoadCircuits = (Engine.circuitsRequested || getConfig().hasKey("Content", "LoadCircuits")) && getConfig().getBoolean("LoadCircuits", "Content", true, "Forces the circuit items to load even if not requests. Content can still loaded if false as long as another mod requests the content for crafting. This config is designed to prevent items from vanishing in saves.");
        boolean forceLoadCraftingParts = (Engine.craftingPartsRequested || getConfig().hasKey("Content", "LoadCraftingParts")) && getConfig().getBoolean("LoadCraftingParts", "Content", true, "Forces the crafting items(Motors, coils, simple electrical parts) to load even if not requests. Content can still loaded if false as long as another mod requests the content for crafting. This config is designed to prevent items from vanishing in saves.");

        if (Engine.sheetMetalRequested || forceLoadSheetMetal)
        {
            Engine.itemSheetMetalTools = getManager().newItem("veSheetMetalTools", ItemSheetMetalTools.class);
            Engine.itemSheetMetal = getManager().newItem("veSheetMetal", ItemSheetMetal.class);

            OreDictionary.registerOre("itemSheetMetal", Engine.itemSheetMetal);
            for(ItemSheetMetal.SheetMetal metal : ItemSheetMetal.SheetMetal.values())
            {
                OreDictionary.registerOre(metal.oreName, metal.stack());
            }
        }

        if (Engine.circuitsRequested || forceLoadCircuits)
        {
            Engine.itemCircuits = getManager().newItem("veCircuits", ItemCircuits.class);
        }

        if (Engine.craftingPartsRequested || forceLoadCraftingParts)
        {
            Engine.itemCraftingParts = getManager().newItem("veCraftingParts", ItemCraftingParts.class);
        }

        if (Engine.simpleToolsRequested || forceLoadSimpleTools)
        {
            Engine.itemSimpleCraftingTools = getManager().newItem("veSimpleTools", ItemSimpleCraftingTool.class);
        }

        if (getConfig().getBoolean("LoadHeatedRocks", "Content", Engine.heatedRockRequested, "Loads up heated rocks which are used to give explosions an extra short term effect on stone."))
        {
            Engine.heatedStone = contentRegistry.newBlock("VEHeatedRock", BlockHeatedStone.class, ItemBlockMetadata.class);
            //NEIProxy.hideItem(heatedStone);
            if (enabledHeatMap)
            {
                HeatedBlockRegistry.addNewHeatingConversion(Blocks.stone, new PlacementDataExtended(Engine.heatedStone, 15, Block.getIdFromBlock(Blocks.stone)), 600);
                HeatedBlockRegistry.addNewHeatingConversion(Blocks.cobblestone, new PlacementDataExtended(Engine.heatedStone, 15, Block.getIdFromBlock(Blocks.cobblestone)), 600);
            }
        }
        else
        {
            HeatedBlockRegistry.addNewHeatingConversion(Blocks.stone, new PlacementData(Blocks.cobblestone, 1), 600);
        }

        Engine.logger().info("Starting resource generator");
        long start = System.nanoTime();
        for (DefinedGenItems genItem : DefinedGenItems.values())
        {
            if ((getConfig().hasKey("ForceLoadContent", genItem.name) || genItem.isRequested()) && getConfig().getBoolean(genItem.name, "ForceLoadContent", genItem.isRequested(), "Forces the items to load even if not requests. Content can still loaded if false as long as another mod requests the content for crafting. This config is designed to prevent items from vanishing in saves.") || genItem.isRequested())
            {
                Engine.logger().info("\tGenerating " + genItem.name);
                genItem.item = getManager().newItem("ve" + LanguageUtility.capitalizeFirst(genItem.name), new ItemGenMaterial(genItem));
                for (GenMaterial mat : GenMaterial.values())
                {
                    if (mat == GenMaterial.UNKNOWN || genItem.ignoreMaterials.contains(mat))
                    {
                        //NEIProxy.hideItem(new ItemStack(genItem.item, 1, mat.ordinal())); //TODO set hidden when moved to JSON
                    }
                    else
                    {
                        OreDictionary.registerOre(genItem.oreDict + LanguageUtility.capitalizeFirst(mat.name().toLowerCase()), genItem.stack(mat));
                    }
                }
                if (genItem == DefinedGenItems.INGOT && getConfig().getBoolean("EnableCheapSteelRecipe", "Content", true, "Enables iron ingot to steel ingot smelting recipe. Only disable if another recipe for steel exists or most items will be uncraftable."))
                {
                    //TODO check to make sure doesn't conflict
                    GameRegistry.addSmelting(Items.iron_ingot, genItem.stack(GenMaterial.STEEL), 0f);
                }
            }
        }
        if (Engine.metallicOresRequested)
        {
            //Register alt ore names
            OreDictionary.registerOre("oreBauxite", MetallicOres.BAUXITE.stack());
            OreDictionary.registerOre("oreMagnesite", MetallicOres.MAGNESITE.stack());
        }
        Engine.logger().info("Done... Took " + StringHelpers.formatTimeDifference(start, System.nanoTime()));

        loader.init();
        getManager().fireInit();
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
        getManager().firePostInit();

        //TODO move to JSON
        if (Engine.metallicOresRequested)
        {
            for (MetallicOres ore : MetallicOres.values())
            {
                List<ItemStack> ingots = OreDictionary.getOres(ore.getOreName().replace("ore", "ingot"));
                if (!ingots.isEmpty())
                {
                    ItemStack ingotStack = ingots.get(0);
                    if (ingotStack == null)
                    {
                        int i = 1;
                        while (ingotStack == null && i < ingots.size())
                        {
                            ingotStack = ingots.get(i);
                        }
                    }
                    if (ingotStack != null)
                    {
                        GameRegistry.addSmelting(ore.stack(), ingotStack, 0.01f);
                    }
                }
            }
        }

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
            ArrayList<ItemStack> stacks = OreDictionary.getOres(name);
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
    public void missingMappingEvent(FMLMissingMappingsEvent event)
    {
        for (FMLMissingMappingsEvent.MissingMapping missingMapping : event.getAll())
        {
            final String name = missingMapping.name;
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
                Object object = missingMapping.type.getRegistry().getObject(key);
                if (object == Blocks.air || object == null)
                {
                    object = missingMapping.type.getRegistry().getObject(key);
                }

                if (object != Blocks.air && object != null)
                {
                    if (object instanceof Block)
                    {
                        missingMapping.remap((Block) object);
                    }
                    else if (object instanceof Item)
                    {
                        missingMapping.remap((Item) object);
                    }
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

    public ModManager getManager()
    {
        return this.manager;
    }

    @Deprecated
    public static Logger logger()
    {
        return Engine.logger();
    }
}

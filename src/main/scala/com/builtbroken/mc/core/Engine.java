package com.builtbroken.mc.core;

import com.builtbroken.mc.api.recipe.MachineRecipeType;
import com.builtbroken.mc.core.annotation.TestAnnotation;
import com.builtbroken.mc.core.content.load.CrusherRecipeLoad;
import com.builtbroken.mc.core.content.load.GrinderRecipeLoad;
import com.builtbroken.mc.core.handler.InteractionHandler;
import com.builtbroken.mc.core.content.BlockOre;
import com.builtbroken.mc.core.content.ItemBlockOre;
import com.builtbroken.mc.prefab.recipe.MRHandlerItemStack;
import com.builtbroken.mc.prefab.recipe.MRSmelterHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import com.builtbroken.mc.core.content.tool.ItemScrewdriver;
import com.builtbroken.mc.core.content.ItemInstaHole;
import com.builtbroken.mc.core.content.tool.ToolMode;
import com.builtbroken.mc.core.content.tool.ToolModeGeneral;
import com.builtbroken.mc.core.content.tool.ToolModeRotation;
import com.builtbroken.mc.core.content.resources.*;
import com.builtbroken.mc.lib.grid.UpdateTicker;
import com.builtbroken.mc.lib.grid.UpdateTicker$;
import com.builtbroken.mc.lib.mod.config.ConfigHandler;
import com.builtbroken.mc.lib.mod.config.ConfigScanner;
import com.builtbroken.mc.core.registry.ModManager;
import com.builtbroken.mc.lib.mod.loadable.LoadableHandler;
import com.builtbroken.mc.core.network.netty.PacketManager;
import com.builtbroken.mc.lib.helper.PotionUtility;
import com.builtbroken.mc.core.handler.SaveManager;

import java.util.Arrays;

/**
 * Mob class for Resonant Engine that handles common loading
 *
 * @author Calclavia, DarkGuardsman
 */

@Mod(modid = References.ID, name = References.NAME, version = References.VERSION)
public class Engine
{
	public static final ModManager contentRegistry = new ModManager().setPrefix(References.PREFIX).setTab(CreativeTabs.tabTools);
	public static final boolean runningAsDev = System.getProperty("development") != null && System.getProperty("development").equalsIgnoreCase("true");

    @SidedProxy(clientSide = "com.builtbroken.mc.core.ClientProxy", serverSide = "com.builtbroken.mc.core.CommonProxy")
	public static CommonProxy proxy;

    @Mod.Metadata(References.ID)
	public static ModMetadata metadata;

    @Instance(References.ID)
	public static Engine instance;


    public static Block ore = null;
	public static Item itemWrench;
    public static Item instaHole;

    private static boolean oresRequested = false;
	public final PacketManager packetHandler = new PacketManager(References.CHANNEL);
	private LoadableHandler loadables = new LoadableHandler();

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
		ConfigScanner.instance().generateSets(evt.getAsmData());
		ConfigHandler.sync(References.CONFIGURATION, References.DOMAIN);

		References.CONFIGURATION.load();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

		loadables.applyModule(proxy);
		loadables.applyModule(packetHandler);
        loadables.applyModule(CrusherRecipeLoad.class);
        loadables.applyModule(GrinderRecipeLoad.class);

		PotionUtility.resizePotionArray();
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(SaveManager.instance());
		MinecraftForge.EVENT_BUS.register(new InteractionHandler());

        MachineRecipeType.ITEM_SMELTER.setHandler(new MRSmelterHandler());
        MachineRecipeType.ITEM_GRINDER.setHandler(new MRHandlerItemStack(MachineRecipeType.ITEM_GRINDER));
        MachineRecipeType.ITEM_CRUSHER.setHandler(new MRHandlerItemStack(MachineRecipeType.ITEM_CRUSHER));
        MachineRecipeType.ITEM_WASHER.setHandler(new MRHandlerItemStack(MachineRecipeType.ITEM_WASHER));
        MachineRecipeType.ITEM_SAWMILL.setHandler(new MRHandlerItemStack(MachineRecipeType.ITEM_SAWMILL));

		ToolMode.REGISTRY.add(new ToolModeGeneral());
		ToolMode.REGISTRY.add(new ToolModeRotation());

		/**
		 * Multiblock Handling
		 */
        if(runningAsDev)
            instaHole = contentRegistry.newItem(new ItemInstaHole());
		if (References.CONFIGURATION.get("Content", "LoadScrewDriver", true).getBoolean(true))
		{
			itemWrench = new ItemScrewdriver();
			GameRegistry.registerItem(itemWrench, "screwdriver", References.ID);
		}

		//BlockCreativeBuilder.register(new SchematicTestRoom());
		//Finish and close all resources

		loadables.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent evt)
	{
		Engine.metadata.modId = References.NAME;
		Engine.metadata.name = References.NAME;
		Engine.metadata.description = References.NAME + " is a mod developement framework designed to assist in creation of mods. It provided basic classes for packet handling, tile creation, inventory handling, saving/loading of NBT, and general all around prefabs.";
		Engine.metadata.url = "https://github.com/Universal-Electricity/Resonant-Engine";
		Engine.metadata.version = References.VERSION + References.BUILD_VERSION;
		Engine.metadata.authorList = Arrays.asList("Calclavia", "DarkCow", "tgame14", "Maxwolf");
		Engine.metadata.autogenerated = false;

		//Register UpdateTicker
		FMLCommonHandler.instance().bus().register(UpdateTicker$.MODULE$.world());

        //Late registration of content
        if(oresRequested)
        {
            ore = contentRegistry.newBlock(References.ID +"StoneOre", new BlockOre("stone"), ItemBlockOre.class);
            Ores.registerSet(ore, References.CONFIGURATION);
        }

		loadables.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent evt)
	{
		if (!UpdateTicker.threaded().isAlive())
		{
			UpdateTicker.threaded().start();
		}

		loadables.postInit();

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

        References.CONFIGURATION.save();
	}

	@EventHandler
	public void serverStopped(FMLServerStoppedEvent event)
	{
	}

	@EventHandler
	public void onServerStopping(FMLServerStoppingEvent evt)
	{
		SaveManager.saveAll();
	}

}

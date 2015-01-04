package com.builtbroken.mod;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import com.builtbroken.api.IUpdate;
import com.builtbroken.api.recipe.MachineRecipes;
import com.builtbroken.api.tile.IBoilHandler;
import com.builtbroken.mod.content.tool.ItemScrewdriver;
import com.builtbroken.mod.content.ItemInstaHole;
import com.builtbroken.mod.content.tool.ToolMode;
import com.builtbroken.mod.content.tool.ToolModeGeneral;
import com.builtbroken.mod.content.tool.ToolModeRotation;
import com.builtbroken.lib.debug.F3Handler$;
import com.builtbroken.lib.factory.resources.*;
import com.builtbroken.lib.grid.UpdateTicker;
import com.builtbroken.lib.grid.UpdateTicker$;
import com.builtbroken.lib.grid.thermal.BoilEvent;
import com.builtbroken.lib.grid.thermal.EventThermal.EventThermalUpdate;
import com.builtbroken.lib.grid.thermal.ThermalGrid;
import com.builtbroken.lib.mod.config.ConfigHandler;
import com.builtbroken.lib.mod.config.ConfigScanner;
import com.builtbroken.lib.mod.content.ModManager;
import com.builtbroken.lib.mod.loadable.LoadableHandler;
import com.builtbroken.lib.network.netty.PacketManager;
import com.builtbroken.lib.prefab.tile.multiblock.synthetic.SyntheticMultiblock;
import com.builtbroken.lib.prefab.tile.spatial.BlockDummy;
import com.builtbroken.lib.transform.vector.Vector3;
import com.builtbroken.lib.transform.vector.VectorWorld;
import com.builtbroken.lib.utility.PlayerInteractionHandler;
import com.builtbroken.lib.utility.PotionUtility;
import com.builtbroken.lib.utility.nbt.SaveManager;

import java.util.Arrays;

/**
 * Mob class for Resonant Engine that handles common loading
 *
 * @author Calclavia, DarkGuardsman
 */

@Mod(modid = References.ID, name = References.NAME, version = References.VERSION)
public class BBL
{
	public static final ModManager contentRegistry = new ModManager().setPrefix(References.PREFIX).setTab(CreativeTabs.tabTools);
	public static final boolean runningAsDev = System.getProperty("development") != null && System.getProperty("development").equalsIgnoreCase("true");
    @SidedProxy(clientSide = "com.builtbroken.mod.ClientProxy", serverSide = "com.builtbroken.mod.CommonProxy")
	public static CommonProxy proxy;
	@Mod.Metadata(References.ID)
	public static ModMetadata metadata;
	@Instance(References.ID)
	public static BBL instance;
	public static Block ore = null;
	public static Item itemWrench;
    public static Item instaHole;
    @Deprecated
    public static ResourceFactoryHandler resourceFactory = new ResourceFactoryHandler();
    private static boolean oresRequested = false;
	private static ThermalGrid thermalGrid = new ThermalGrid();
	public final PacketManager packetHandler = new PacketManager(References.CHANNEL);
	private LoadableHandler loadables = new LoadableHandler();

	/**
	 * Requests that all ores are generated
	 * Must be called in pre-init
	 */
	public static void requestAllOres()
	{
		for (DefinedResources resource : DefinedResources.values())
		{
			requestOre(resource);
		}
	}

	/**
	 * Requests that all ores are generated
	 * Must be called in pre-init
	 *
	 * @param resource - resource to request its ore to generate, still restricted by configs
	 */
	public static void requestOre(DefinedResources resource)
	{
		oresRequested = true;
		resource.requested = true;
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

		PotionUtility.resizePotionArray();
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(SaveManager.instance());
		MinecraftForge.EVENT_BUS.register(new PlayerInteractionHandler());
		MinecraftForge.EVENT_BUS.register(F3Handler$.MODULE$);

		ToolMode.REGISTRY.add(new ToolModeGeneral());
		ToolMode.REGISTRY.add(new ToolModeRotation());

		/**
		 * Multiblock Handling
		 */
		SyntheticMultiblock.instance = new SyntheticMultiblock();
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
		BBL.metadata.modId = References.NAME;
		BBL.metadata.name = References.NAME;
		BBL.metadata.description = References.NAME + " is a mod developement framework designed to assist in creation of mods. It provided basic classes for packet handling, tile creation, inventory handling, saving/loading of NBT, and general all around prefabs.";
		BBL.metadata.url = "https://github.com/Universal-Electricity/Resonant-Engine";
		BBL.metadata.version = References.VERSION + References.BUILD_VERSION;
		BBL.metadata.authorList = Arrays.asList("Calclavia", "DarkCow", "tgame14", "Maxwolf");
		BBL.metadata.autogenerated = false;

		//Register UpdateTicker
		FMLCommonHandler.instance().bus().register(UpdateTicker$.MODULE$.world());

        //Late registration of content
        if(oresRequested)
        {
            ore = contentRegistry.newBlock("ReOres", BlockOre.class, ItemBlockOre.class);
            DefinedResources.registerSet(0, ore, References.CONFIGURATION);
        }

		loadables.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent evt)
	{
		UpdateTicker.threaded().addUpdater(BBL.thermalGrid);

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
		MachineRecipes.instance.addRecipe(RecipeType.SMELTER.name(), new FluidStack(FluidRegistry.LAVA, FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(Blocks.stone));
		MachineRecipes.instance.addRecipe(RecipeType.CRUSHER.name(), Blocks.cobblestone, Blocks.gravel);
		MachineRecipes.instance.addRecipe(RecipeType.CRUSHER.name(), Blocks.stone, Blocks.cobblestone);
		MachineRecipes.instance.addRecipe(RecipeType.CRUSHER.name(), Blocks.chest, new ItemStack(Blocks.planks, 7, 0));
		MachineRecipes.instance.addRecipe(RecipeType.GRINDER.name(), Blocks.cobblestone, Blocks.sand);
		MachineRecipes.instance.addRecipe(RecipeType.GRINDER.name(), Blocks.gravel, Blocks.sand);
		MachineRecipes.instance.addRecipe(RecipeType.GRINDER.name(), Blocks.glass, Blocks.sand);

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

	/**
	 * Default handler.
	 */
	@SubscribeEvent
	public void boilEventHandler(BoilEvent evt)
	{
		World world = evt.world;
		Vector3 position = evt.position;

		//TODO change to a raytrace to get the block above
		for (int height = 1; height <= evt.maxSpread; height++)
		{
			TileEntity tileEntity = world.getTileEntity(position.xi(), position.yi() + height, position.zi());

			if (tileEntity instanceof IBoilHandler)
			{
				IBoilHandler handler = (IBoilHandler) tileEntity;
				FluidStack fluid = evt.getRemainForSpread(height);

				if (fluid.amount > 0)
				{
					if (handler.canFill(ForgeDirection.DOWN, fluid.getFluid()))
					{
						fluid.amount -= handler.fill(ForgeDirection.DOWN, fluid, true);
					}
				}
			}
		}

		evt.setResult(Event.Result.DENY);
	}

	/**
	 * Default handler.
	 */
	@SubscribeEvent
	public void thermalEventHandler(EventThermalUpdate evt)
	{
		final VectorWorld pos = evt.position;

		synchronized (pos.world())
		{
			Block block = pos.getBlock();
			Material mat = pos.getBlock().getMaterial();

			if (mat == Material.air)
			{
				evt.heatLoss = 0.15f;
			}

			if (block == Blocks.flowing_water || block == Blocks.water)
			{
				if (evt.temperature >= 373)
				{
					if (FluidRegistry.getFluid("steam") != null)
					{
						// TODO: INCORRECT!
						int volume = (int) (FluidContainerRegistry.BUCKET_VOLUME * (evt.temperature / 373));
						MinecraftForge.EVENT_BUS.post(new BoilEvent(pos.world(), pos, new FluidStack(FluidRegistry.WATER, volume), new FluidStack(FluidRegistry.getFluid("steam"), volume), 2, evt.isReactor));
					}

					evt.heatLoss = 0.2f;
				}
			}

			if (block == Blocks.ice)
			{
				if (evt.temperature >= 273)
				{

					UpdateTicker.threaded().addUpdater(new IUpdate()
					{
						@Override
						public void update(double delta)
						{
							pos.setBlock(Blocks.flowing_water);
						}

						@Override
						public boolean canUpdate()
						{
							return true;
						}

						@Override
						public boolean continueUpdate()
						{
							return false;
						}
					});
				}

				evt.heatLoss = 0.4f;
			}
		}
	}
}

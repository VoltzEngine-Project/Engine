package resonant.engine;

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
import resonant.api.IBoilHandler;
import resonant.api.grid.IUpdate;
import resonant.api.mffs.fortron.FrequencyGridRegistry;
import resonant.api.recipe.MachineRecipes;
import resonant.content.factory.resources.DefinedResources;
import resonant.content.factory.resources.RecipeType;
import resonant.content.factory.resources.ResourceFactoryHandler;
import resonant.content.loader.ModManager;
import resonant.content.prefab.itemblock.ItemBlockMetadata;
import resonant.content.wrapper.BlockDummy;
import resonant.engine.content.BlockOre;
import resonant.engine.content.ItemBlockOre;
import resonant.engine.content.ItemScrewdriver;
import resonant.engine.content.debug.ItemInstaHole;
import resonant.engine.content.debug.TileCreativeBuilder;
import resonant.engine.content.debug.TileInfiniteFluid;
import resonant.engine.content.tool.ToolMode;
import resonant.engine.content.tool.ToolModeGeneral;
import resonant.engine.content.tool.ToolModeRotation;
import resonant.engine.grid.frequency.FrequencyGrid;
import resonant.engine.grid.thermal.BoilEvent;
import resonant.engine.grid.thermal.EventThermal.EventThermalUpdate;
import resonant.engine.grid.thermal.ThermalGrid;
import resonant.lib.config.ConfigHandler;
import resonant.lib.config.ConfigScanner;
import resonant.lib.debug.F3Handler$;
import resonant.lib.grid.UpdateTicker;
import resonant.lib.grid.UpdateTicker$;
import resonant.lib.loadable.LoadableHandler;
import resonant.lib.multiblock.synthetic.SyntheticMultiblock;
import resonant.lib.network.netty.PacketManager;
import resonant.lib.ore.OreGenerator;
import resonant.lib.transform.vector.Vector3;
import resonant.lib.transform.vector.VectorWorld;
import resonant.lib.utility.PlayerInteractionHandler;
import resonant.lib.utility.PotionUtility;
import resonant.lib.utility.nbt.SaveManager;

import java.util.Arrays;

/**
 * Mob class for Resonant Engine that handles common loading
 *
 * @author Calclavia, DarkGuardsman
 */

@Mod(modid = References.ID, name = References.NAME, version = References.VERSION)
public class ResonantEngine
{
	public static final ModManager contentRegistry = new ModManager().setPrefix(References.PREFIX).setTab(CreativeTabs.tabTools);
	public static final boolean runningAsDev = System.getProperty("development") != null && System.getProperty("development").equalsIgnoreCase("true");
    public final PacketManager packetHandler = new PacketManager(References.CHANNEL);

    @SidedProxy(clientSide = "resonant.engine.ClientProxy", serverSide = "resonant.engine.CommonProxy")
	public static CommonProxy proxy;

	@Mod.Metadata(References.ID)
	public static ModMetadata metadata;

	@Instance(References.ID)
	public static ResonantEngine instance;

	public static BlockDummy blockCreativeBuilder;
	public static Block blockInfiniteFluid;
	public static Block ore = null;

	public static Item itemWrench;
    public static Item instaHole;

    @Deprecated
    public static ResourceFactoryHandler resourceFactory = new ResourceFactoryHandler();

    private static boolean oresRequested = false;
	private static ThermalGrid thermalGrid = new ThermalGrid();

	private LoadableHandler loadables = new LoadableHandler();


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
		if (References.CONFIGURATION.get("Content", "LoadParts", true).getBoolean(true))
		{
			//TODO setup chips, motor, and basic crafting parts
		}
		if (References.CONFIGURATION.get("Creative Tools", "CreativeBuilder", true).getBoolean(true))
		{
			blockCreativeBuilder = contentRegistry.newBlock(TileCreativeBuilder.class);
		}
		if (References.CONFIGURATION.get("Creative Tools", "InfiniteSource", true).getBoolean(true))
		{
			blockInfiniteFluid = contentRegistry.newBlock(TileInfiniteFluid.class);
		}

		//BlockCreativeBuilder.register(new SchematicTestRoom());
		//Finish and close all resources

		loadables.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent evt)
	{
		ResonantEngine.metadata.modId = References.NAME;
		ResonantEngine.metadata.name = References.NAME;
		ResonantEngine.metadata.description = References.NAME + " is a mod developement framework designed to assist in creation of mods. It provided basic classes for packet handling, tile creation, inventory handling, saving/loading of NBT, and general all around prefabs.";
		ResonantEngine.metadata.url = "https://github.com/Universal-Electricity/Resonant-Engine";
		ResonantEngine.metadata.version = References.VERSION + References.BUILD_VERSION;
		ResonantEngine.metadata.authorList = Arrays.asList("Calclavia", "DarkCow", "tgame14", "Maxwolf");
		ResonantEngine.metadata.autogenerated = false;

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
		UpdateTicker.threaded().addUpdater(ResonantEngine.thermalGrid);

		if (!UpdateTicker.threaded().isAlive())
		{
			UpdateTicker.threaded().start();
		}

		loadables.postInit();

		//TODO: Find better way to do this in terms of not reinitiating grids twice.
		FrequencyGridRegistry.CLIENT_INSTANCE = new FrequencyGrid();
		FrequencyGridRegistry.SERVER_INSTANCE = new FrequencyGrid();

		OreDictionary.registerOre("ingotGold", Items.gold_ingot);
		OreDictionary.registerOre("ingotIron", Items.iron_ingot);
		OreDictionary.registerOre("oreGold", Blocks.gold_ore);
		OreDictionary.registerOre("oreIron", Blocks.iron_ore);
		OreDictionary.registerOre("oreLapis", Blocks.lapis_ore);
		MachineRecipes.INSTANCE.addRecipe(RecipeType.SMELTER.name(), new FluidStack(FluidRegistry.LAVA, FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(Blocks.stone));
		MachineRecipes.INSTANCE.addRecipe(RecipeType.CRUSHER.name(), Blocks.cobblestone, Blocks.gravel);
		MachineRecipes.INSTANCE.addRecipe(RecipeType.CRUSHER.name(), Blocks.stone, Blocks.cobblestone);
		MachineRecipes.INSTANCE.addRecipe(RecipeType.CRUSHER.name(), Blocks.chest, new ItemStack(Blocks.planks, 7, 0));
		MachineRecipes.INSTANCE.addRecipe(RecipeType.GRINDER.name(), Blocks.cobblestone, Blocks.sand);
		MachineRecipes.INSTANCE.addRecipe(RecipeType.GRINDER.name(), Blocks.gravel, Blocks.sand);
		MachineRecipes.INSTANCE.addRecipe(RecipeType.GRINDER.name(), Blocks.glass, Blocks.sand);

        References.CONFIGURATION.save();
	}

    /**
     * Requests that all ores are generated
     * Must be called in pre-init
     */
    public static void requestAllOres()
    {
        for(DefinedResources resource : DefinedResources.values())
        {
            requestOre(resource);
        }
    }

    /**
     * Requests that all ores are generated
     * Must be called in pre-init
     * @param resource - resource to request its ore to generate, still restricted by configs
     */
    public static void requestOre(DefinedResources resource)
    {
        oresRequested = true;
        resource.requested = true;
    }

	@EventHandler
	public void serverStopped(FMLServerStoppedEvent event)
	{
		/**
		 * Reinitiate FrequencyGrid
		 */
		FrequencyGridRegistry.CLIENT_INSTANCE = new FrequencyGrid();
		FrequencyGridRegistry.SERVER_INSTANCE = new FrequencyGrid();
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

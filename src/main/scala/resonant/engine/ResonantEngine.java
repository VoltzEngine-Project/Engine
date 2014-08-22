package resonant.engine;

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
import resonant.api.mffs.fortron.FrequencyGridRegistry;
import resonant.api.recipe.MachineRecipes;
import resonant.content.factory.resources.ResourceFactoryHandler;
import resonant.content.factory.resources.RecipeType;
import resonant.content.loader.ModManager;
import resonant.content.wrapper.BlockDummy;
import resonant.engine.content.ItemScrewdriver;
import resonant.engine.content.debug.TileCreativeBuilder;
import resonant.engine.content.debug.TileInfiniteEnergy;
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
import resonant.lib.loadable.LoadableHandler;
import resonant.lib.multiblock.synthetic.SyntheticMultiblock;
import resonant.lib.network.netty.PacketManager;
import resonant.lib.ore.OreGenerator;
import resonant.lib.utility.PlayerInteractionHandler;
import resonant.lib.utility.PotionUtility;
import resonant.lib.utility.nbt.SaveManager;
import universalelectricity.api.core.grid.IUpdate;
import universalelectricity.core.grid.UpdateTicker;
import universalelectricity.core.transform.vector.Vector3;
import universalelectricity.core.transform.vector.VectorWorld;

import java.util.Arrays;

/**
 * Mob class for Resonant Engine that handles common loading
 *
 * @author Calclavia, DarkGuardsman
 */

@Mod(modid = References.ID, name = References.NAME, version = References.VERSION, dependencies = "required-after:UniversalElectricity")
public class ResonantEngine
{
	public static final ModManager contentRegistry = new ModManager().setPrefix(References.PREFIX).setTab(CreativeTabs.tabTools);
	public static final boolean runningAsDev = System.getProperty("development") != null && System.getProperty("development").equalsIgnoreCase("true");

    @SidedProxy(clientSide = "resonant.engine.ClientProxy", serverSide = "resonant.engine.CommonProxy")
	public static CommonProxy proxy;

    @Mod.Metadata(References.ID)
	public static ModMetadata metadata;

    @Instance(References.ID)
	public static ResonantEngine instance;

	/** Blocks TODO move to XML data if requires no logic */
	public static BlockDummy blockCreativeBuilder;
	public static Block blockInfiniteFluid, blockInfiniteEnergy;
    public static Block blockOreCopper = null;
    public static Block blockOreTin = null;

    /** Items TODO move to XML data if requires no logic */
	public static Item itemWrench;
    public static Item itemMotor = null;
    public static Item itemCircuitBasic = null;
    public static Item itemCircuitAdvanced = null;
    public static Item itemCircuitElite = null;

    /** Ore Generator TODO move to XML data */
    public static OreGenerator generationOreCopper = null;
    public static OreGenerator generationOreTin  = null;
    public static ResourceFactoryHandler resourceFactory;


	private static ThermalGrid thermalGrid = new ThermalGrid();
	public final PacketManager packetHandler = new PacketManager(References.CHANNEL);
	private LoadableHandler loadables = new LoadableHandler();

	@EventHandler
	public void preInit(FMLPreInitializationEvent evt)
	{
		ConfigScanner.instance().generateSets(evt.getAsmData());
        resourceFactory = new ResourceFactoryHandler();
		References.CONFIGURATION.load();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

		//loadables.applyModule(Waila.class, true);
		loadables.applyModule(proxy);
		loadables.applyModule(packetHandler);

		PotionUtility.resizePotionArray();
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(SaveManager.instance());
		MinecraftForge.EVENT_BUS.register(new PlayerInteractionHandler());

		ToolMode.REGISTRY.add(new ToolModeGeneral());
		ToolMode.REGISTRY.add(new ToolModeRotation());

		/**
		 * Multiblock Handling
		 */
		SyntheticMultiblock.instance = new SyntheticMultiblock();
        //TODO setup ores
        if(References.CONFIGURATION.get("Content", "LoadScrewDriver", true).getBoolean(true))
        {
            itemWrench = new ItemScrewdriver();
            GameRegistry.registerItem(itemWrench, "screwdriver", References.ID);
        }
        if(References.CONFIGURATION.get("Content", "LoadParts", true).getBoolean(true))
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
			blockInfiniteEnergy = contentRegistry.newBlock(TileInfiniteEnergy.class);
		}

		//BlockCreativeBuilder.register(new SchematicTestRoom());
		//Finish and close all resources
		References.CONFIGURATION.save();
		loadables.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent evt)
	{
		References.CONFIGURATION.load();

		References.CONFIGURATION.save();
		//References.LOGGER.info("Loaded: " + LanguageUtility.loadLanguages(References.LANGUAGE_DIRECTORY, References.LANGUAGES) + " Languages.");

		ResonantEngine.metadata.modId = References.NAME;
		ResonantEngine.metadata.name = References.NAME;
		ResonantEngine.metadata.description = References.NAME + " is a mod developement framework designed to assist in creation of mods. It provided basic classes for packet handling, tile creation, inventory handling, saving/loading of NBT, and general all around prefabs.";
		ResonantEngine.metadata.url = "https://github.com/Universal-Electricity/Resonant-Engine";
		ResonantEngine.metadata.version = References.VERSION + References.BUILD_VERSION;
		ResonantEngine.metadata.authorList = Arrays.asList("Calclavia", "DarkCow", "tgame14", "Maxwolf");
		ResonantEngine.metadata.autogenerated = false;
		proxy.init();
		loadables.init();

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
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent evt)
	{

		ConfigHandler.sync(References.CONFIGURATION, References.DOMAIN);

		// Register Thermal Grid
		UpdateTicker.addUpdater(ResonantEngine.thermalGrid);

		loadables.postInit();

		//TODO: Find better way to do this in terms of not reinitiating grids twice.
		FrequencyGridRegistry.CLIENT_INSTANCE = new FrequencyGrid();
		FrequencyGridRegistry.SERVER_INSTANCE = new FrequencyGrid();
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
		Block block = position.getBlock(world);

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

		// Reactors will not actually remove water source blocks, however weapons will.
		//        if ((blockID == Block.waterMoving.blockID || blockID == Block.waterStill.blockID) && position.getBlockMetadata(world) == 0 && !evt.isReactor)
		//        {
		//            position.setBlock(world, 0);
		//        }

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

					UpdateTicker.addUpdater(new IUpdate()
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

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
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import resonant.api.IBoilHandler;
import resonant.content.ModManager;
import resonant.content.wrapper.BlockDummy;
import resonant.engine.content.debug.TileCreativeBuilder;
import resonant.engine.content.debug.TileInfiniteEnergy;
import resonant.engine.content.debug.TileInfiniteFluid;
import resonant.engine.content.tool.ToolMode;
import resonant.engine.content.tool.ToolModeGeneral;
import resonant.engine.content.tool.ToolModeRotation;
import resonant.engine.grid.thermal.BoilEvent;
import resonant.engine.grid.thermal.EventThermal.EventThermalUpdate;
import resonant.engine.grid.thermal.ThermalGrid;
import resonant.lib.config.ConfigHandler;
import resonant.lib.config.ConfigScanner;
import resonant.lib.modproxy.ProxyHandler;
import resonant.lib.multiblock.synthetic.SyntheticMultiblock;
import resonant.lib.network.netty.PacketManager;
import resonant.lib.prefab.ProxyBase;
import resonant.lib.utility.LanguageUtility;
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

@Mod(modid = References.NAME, name = References.NAME, version = References.VERSION, dependencies = "required-after:UniversalElectricity")
public class ResonantEngine
{
	public static final ModManager contentRegistry = new ModManager(References.CONFIGURATION, References.NAME).setPrefix(References.PREFIX).setTab(CreativeTabs.tabTools);

	@SidedProxy(clientSide = "resonant.engine.ClientProxy", serverSide = "resonant.lib.engine.ProxyBase")
	public static ProxyBase proxy;

	@Mod.Metadata(References.NAME)
	public static ModMetadata metadata;

	@Instance(References.NAME)
	public static ResonantEngine instance;

	public static final boolean runningAsDev = System.getProperty("development") != null && System.getProperty("development").equalsIgnoreCase("true");

	/**
	 * Blocks
	 */
	public static BlockDummy blockCreativeBuilder;
	public static Block blockInfiniteFluid, blockInfiniteEnergy;

	/**
	 * Items
	 */
	public static Item itemWrench;

	private static ThermalGrid thermalGrid = new ThermalGrid();
	public final PacketManager packetHandler = new PacketManager();
	private ProxyHandler modProxy = new ProxyHandler();

	@EventHandler
	public void preInit(FMLPreInitializationEvent evt)
	{
		ConfigScanner.instance().generateSets(evt.getAsmData());

		References.CONFIGURATION.load();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

		//modProxy.applyModule(Waila.class, true);
		modProxy.applyModule(this.packetHandler);

		// Potion Array resized to Current potion array, +32, Allows to miss conflicting ID's
		PotionUtility.resizePotionArray();

		//SaveManager.registerClass("ModFlag", ModFlag.class);

		/**
		 * EventHandlers
		 */
		MinecraftForge.EVENT_BUS.register(instance);
		MinecraftForge.EVENT_BUS.register(SaveManager.instance());
		MinecraftForge.EVENT_BUS.register(new PlayerInteractionHandler());

		ToolMode.REGISTRY.add(new ToolModeGeneral());
		ToolMode.REGISTRY.add(new ToolModeRotation());

		/**
		 * Multiblock Handling
		 */
		SyntheticMultiblock.instance = new SyntheticMultiblock();

		//TODO: Calclavia - Return the prefabs as we still need them. Using traits is nice and all but we still need java classes

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
		proxy.preInit();
		modProxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent evt)
	{
		References.CONFIGURATION.load();

		References.CONFIGURATION.save();
		References.LOGGER.info("Loaded: " + LanguageUtility.loadLanguages(References.LANGUAGE_DIRECTORY, References.LANGUAGES) + " Languages.");

		ResonantEngine.metadata.modId = References.NAME;
		ResonantEngine.metadata.name = References.NAME;
		ResonantEngine.metadata.description = References.NAME + " is a mod developement framework designed to assist in creation of mods. It provided basic classes for packet handling, tile creation, inventory handling, saving/loading of NBT, and general all around prefabs.";
		ResonantEngine.metadata.url = "https://github.com/Universal-Electricity/Resonant-Engine";
		ResonantEngine.metadata.version = References.VERSION + References.BUILD_VERSION;
		ResonantEngine.metadata.authorList = Arrays.asList(new String[] { "Calclavia", "DarkCow", "tgame14", "Maxwolf" });
		ResonantEngine.metadata.autogenerated = false;
		proxy.init();
		modProxy.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent evt)
	{
		ConfigHandler.configure(References.CONFIGURATION, References.DOMAIN);

		// Register Thermal Grid
		UpdateTicker.addUpdater(ResonantEngine.thermalGrid);

		proxy.postInit();
		modProxy.postInit();
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		/*
		// Load ModFlag from world save
		Object object = SaveManager.createAndLoad(NBTUtility.loadData(FlagRegistry.DEFAULT_NAME));
		if (!(object instanceof ModFlag))
		{
			object = new ModFlag(FlagRegistry.DEFAULT_NAME);
		}
		FlagRegistry.registerModFlag(FlagRegistry.DEFAULT_NAME, (ModFlag) object);

		// Setup command
		ICommandManager commandManager = FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager();
		ServerCommandManager serverCommandManager = ((ServerCommandManager) commandManager);
		serverCommandManager.registerCommand(new CommandFlag(FlagRegistry.getModFlag(FlagRegistry.DEFAULT_NAME)));
		*/
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

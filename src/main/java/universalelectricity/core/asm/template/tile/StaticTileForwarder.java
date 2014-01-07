package universalelectricity.core.asm.template.tile;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyTile;

import java.util.HashSet;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.api.CompatibilityType;
import universalelectricity.api.electricity.IVoltageInput;
import universalelectricity.api.energy.IEnergyContainer;
import universalelectricity.api.energy.IEnergyInterface;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;
import cpw.mods.fml.relauncher.ReflectionHelper;

/**
 * @author Calclavia
 * 
 */
public class StaticTileForwarder
{
	/**
	 * IC2 Functions
	 */
	private static final HashSet<IEnergyTile> loadedIC2Tiles = new HashSet<IEnergyTile>();

	public static void loadIC(IEnergyTile tile)
	{
		if (CompatibilityType.INDUSTRIALCRAFT.isLoaded() && !loadedIC2Tiles.contains(tile) && !((TileEntity) tile).worldObj.isRemote)
		{
			MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(tile));
			loadedIC2Tiles.add(tile);
		}
	}

	public static void unloadIC(IEnergyTile tile)
	{
		if (CompatibilityType.INDUSTRIALCRAFT.isLoaded() && loadedIC2Tiles.contains(tile) && !((TileEntity) tile).worldObj.isRemote)
		{
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(tile));
			loadedIC2Tiles.remove(tile);
		}
	}

	/**
	 * Adds electricity to an block. Returns the quantity of electricity that was accepted. This
	 * should always return 0 if the block cannot be externally charged.
	 * 
	 * @param from Orientation the electricity is sent in from.
	 * @param receive Maximum amount of electricity (joules) to be sent into the block.
	 * @param doReceive If false, the charge will only be simulated.
	 * @return Amount of energy that was accepted by the block.
	 */
	public static long onReceiveEnergy(IEnergyInterface handler, ForgeDirection from, long receive, boolean doReceive)
	{
		return handler.onReceiveEnergy(from, receive, doReceive);
	}

	/**
	 * Adds electricity to an block. Returns the ElectricityPack, the electricity provided. This
	 * should always return null if the block cannot be externally discharged.
	 * 
	 * @param from Orientation the electricity is requested from.
	 * @param energy Maximum amount of energy to be sent into the block.
	 * @param doReceive If false, the charge will only be simulated.
	 * @return Amount of energy that was given out by the block.
	 */
	public static long onExtractEnergy(IEnergyInterface handler, ForgeDirection from, long request, boolean doProvide)
	{
		return handler.onExtractEnergy(from, request, doProvide);
	}

	/**
	 * Gets the voltage of this TileEntity.
	 * 
	 * @return The amount of volts. E.g 120v or 240v
	 */
	public static long getVoltage(IVoltageInput handler, ForgeDirection direction)
	{
		return handler.getVoltageInput(direction);
	}

	public static long getElectricityStored(IEnergyContainer handler, ForgeDirection from)
	{
		return handler.getEnergy(from);
	}

	public static long getMaxElectricity(IEnergyContainer handler, ForgeDirection from)
	{
		return handler.getEnergyCapacity(from);
	}

	public static boolean canConnect(IEnergyInterface handler, ForgeDirection from)
	{
		return handler.canConnect(from);
	}

	public static void validateTile(Object obj)
	{
		if (obj instanceof TileEntity)
		{
			TileEntity tileEntity = (TileEntity) obj;
			if (tileEntity.isInvalid())
			{
				try
				{
					ReflectionHelper.setPrivateValue(TileEntity.class, tileEntity, false, "tileEntityInvalid", "field_70328_o");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public static void invalidateTile(Object obj)
	{
		if (obj instanceof TileEntity)
		{
			TileEntity tileEntity = (TileEntity) obj;

			if (!tileEntity.isInvalid())
			{
				try
				{
					ReflectionHelper.setPrivateValue(TileEntity.class, tileEntity, true, "tileEntityInvalid", "field_70328_o");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * BuildCraft
	 */
	public static class DummyPowerHandler extends PowerHandler
	{
		public DummyPowerHandler(IPowerReceptor receptor, Type type)
		{
			super(receptor, type);
		}

		public DummyPowerReceiver receiver = new DummyPowerReceiver();

		public class DummyPowerReceiver extends PowerReceiver
		{
			DummyPowerReceiver()
			{
				super();
			}

			@Override
			public float receiveEnergy(Type source, final float quantity, ForgeDirection from)
			{
				return ((IEnergyInterface) receptor).onReceiveEnergy(from, (long) (quantity * CompatibilityType.BUILDCRAFT.reciprocal_ratio), true) * CompatibilityType.BUILDCRAFT.ratio;
			}

			public float powerRequest()
			{
				return ((IEnergyInterface) receptor).onReceiveEnergy(ForgeDirection.UNKNOWN, Integer.MAX_VALUE, true) * CompatibilityType.BUILDCRAFT.ratio;
			}
		}

		public PowerReceiver getPowerReceiver()
		{
			return receiver;
		}
	}

	/**
	 * @param templateBCTile
	 * @param side
	 * @return
	 */
	public static PowerReceiver getPowerReceiver(IEnergyInterface handler, ForgeDirection side)
	{
		DummyPowerHandler dummy = new DummyPowerHandler((IPowerReceptor) handler, Type.MACHINE);
		return dummy.getPowerReceiver();
	}

	/**
	 * @param templateBCTile
	 * @return
	 */
	public static World getWorld(IEnergyInterface handler)
	{
		if (handler instanceof TileEntity)
		{
			return ((TileEntity) handler).worldObj;
		}

		return null;
	}
}

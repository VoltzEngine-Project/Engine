package universalelectricity.core.asm.template;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyTile;

import java.util.HashSet;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.api.Compatibility.CompatibilityType;
import universalelectricity.api.electricity.IVoltage;
import universalelectricity.api.energy.IEnergyContainer;
import universalelectricity.api.energy.IEnergyInterface;

/**
 * @author Calclavia
 * 
 */
public class StaticForwarder
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
	public static int onReceiveEnergy(IEnergyInterface handler, ForgeDirection from, int receive, boolean doReceive)
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
	public static int onExtractEnergy(IEnergyInterface handler, ForgeDirection from, int request, boolean doProvide)
	{
		return handler.onExtractEnergy(from, request, doProvide);
	}

	/**
	 * Gets the voltage of this TileEntity.
	 * 
	 * @return The amount of volts. E.g 120v or 240v
	 */
	public static int getVoltage(IVoltage handler, ForgeDirection direction)
	{
		return handler.getVoltage(direction);
	}

	public static int getElectricityStored(IEnergyContainer handler, ForgeDirection from)
	{
		return handler.getEnergy(from);
	}

	public static float getMaxElectricity(IEnergyContainer handler, ForgeDirection from)
	{
		return handler.getEnergyCapacity(from);
	}

	public static boolean canConnect(IEnergyInterface handler, ForgeDirection from)
	{
		return handler.canConnect(from);
	}
}

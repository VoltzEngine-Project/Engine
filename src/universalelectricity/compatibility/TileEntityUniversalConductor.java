package universalelectricity.compatibility;

import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergyTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.block.IConnector;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.tile.TileEntityConductor;
import buildcraft.api.power.IPowerReceptor;

/**
 * A universal conductor class.
 * 
 * Extend this class or use as a reference for your own implementation of compatible conductor
 * tiles.
 * 
 * TODO: Need working BuildCraft support!
 * 
 * @author micdoodle8
 * 
 */
public abstract class TileEntityUniversalConductor extends TileEntityConductor
{

	@Override
	public TileEntity[] getAdjacentConnections()
	{
		if (this.adjacentConnections == null)
		{
			this.adjacentConnections = new TileEntity[6];

			for (byte i = 0; i < 6; i++)
			{
				ForgeDirection side = ForgeDirection.getOrientation(i);
				TileEntity tileEntity = VectorHelper.getTileEntityFromSide(this.worldObj, new Vector3(this), side);

				if (tileEntity instanceof IConnector)
				{
					if (((IConnector) tileEntity).canConnect(side.getOpposite()))
					{
						this.adjacentConnections[i] = tileEntity;
					}
				}
				else if (Compatibility.isIndustrialCraft2Loaded() && tileEntity instanceof IEnergyTile)
				{
					if (tileEntity instanceof IEnergyAcceptor)
					{
						if (((IEnergyAcceptor) tileEntity).acceptsEnergyFrom(this, ForgeDirection.values()[(i + 2) % 6].getOpposite()))
						{
							this.adjacentConnections[i] = tileEntity;
						}
					}
					else
					{
						this.adjacentConnections[i] = tileEntity;
					}
				}
				else if (Compatibility.isBuildcraftLoaded() && tileEntity instanceof IPowerReceptor)
				{
					this.adjacentConnections[i] = tileEntity;
				}
			}
		}

		return this.adjacentConnections;
	}

	/**
	 * Takes power from nearby IC2 blocks and inject it into the network.
	 */
	@Override
	public void doWithdraw(ForgeDirection direction, TileEntity tileEntity)
	{
		super.doWithdraw(direction, tileEntity);

		if (tileEntity instanceof IEnergySource)
		{
			float injection = (float) ((IEnergySource) tileEntity).getOfferedEnergy();
			((IEnergySource) tileEntity).drawEnergy(injection);
			this.getNetwork().produce(ElectricityPack.getFromWatts(injection * Compatibility.IC2_RATIO, 120), tileEntity);
		}
	}
}

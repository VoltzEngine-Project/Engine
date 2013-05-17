package calclavia.lib;

import ic2.api.energy.tile.IEnergySink;
import universalelectricity.core.block.IConnector;
import universalelectricity.core.block.IVoltage;
import buildcraft.api.power.IPowerReceptor;

public interface IUniversalEnergyTile extends IPowerReceptor, IEnergySink, IConnector, IVoltage
{

}

package universalelectricity.core.grid;

import java.util.LinkedHashSet;
import java.util.Set;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.api.IConnector;
import universalelectricity.api.energy.IConductor;
import universalelectricity.api.vector.Vector3;
import universalelectricity.core.path.ConnectionPathfinder;
import universalelectricity.core.path.Pathfinder;

/**
 * An grid-like, world cable-based network.
 * 
 * @author Calclavia
 * 
 */
public abstract class Network<N, C, A> implements INetwork<N, C, A>
{
	/**
	 * A set of connectors (e.g conductors).
	 */
	protected final Set<C> connectorSet = new LinkedHashSet<C>();

	@Override
	public void addConnector(C connector)
	{
		this.connectorSet.add(connector);

		if (!NetworkTickHandler.INSTANCE.networks.contains(this))
		{
			NetworkTickHandler.INSTANCE.toAddNetworks.add(this);
		}
	}

	@Override
	public void removeConnector(C connector)
	{
		this.connectorSet.remove(connector);
	}

	@Override
	public Set<C> getConnectors()
	{
		return this.connectorSet;
	}

	@Override
	public String toString()
	{
		return this.getClass().getSimpleName() + "[" + this.hashCode() + "|Connectors:" + this.connectorSet.size() + "]";
	}

}

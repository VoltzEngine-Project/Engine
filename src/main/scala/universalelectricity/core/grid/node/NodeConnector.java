package universalelectricity.core.grid.node;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.api.core.grid.IConnector;
import universalelectricity.api.core.grid.INode;
import universalelectricity.api.core.grid.INodeProvider;
import universalelectricity.core.grid.Node;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * A Node that is designed to connect to other nodes, tiles, or objects.
 *
 * @author Darkguardsman, Calclavia
 */
public class NodeConnector extends Node implements IConnector
{
	protected byte connectionMap = 0x3F;

	/**
	 * Connections to other machines, Object denotes the thing that is connected to, Direction is the face of this machine
	 */
	protected final WeakHashMap<Object, ForgeDirection> connections = new WeakHashMap();

	public NodeConnector(INodeProvider parent)
	{
		super(parent);
	}

	@Override
	public Map<Object, ForgeDirection> getConnections()
	{
		return connections;
	}

	@Override
	public boolean canConnect(ForgeDirection direction, Object object)
	{
		return object != null && isValidConnection(object) && canConnect(direction);
	}

	public boolean canConnect(ForgeDirection from)
	{
		return ((connectionMap & (1 << from.ordinal())) != 0) || from == ForgeDirection.UNKNOWN;
	}

	public boolean isValidConnection(Object object)
	{
		return object != null && object.getClass().isAssignableFrom(getClass());
	}

	@Override
	public void deconstruct()
	{
		super.deconstruct();

		if (connections != null)
		{
			connections.clear();
		}
	}

	@Override
	public void reconstruct()
	{
		super.reconstruct();
		buildConnections();
	}

	/**
	 * Called during reconstruct to build the connection map. This is a general way used to search all adjacent TileEntity to see and try to connect to it.
	 */
	protected void buildConnections()
	{
		connections.clear();

		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
		{
			if (canConnect(direction))
			{
				TileEntity tile = position().add(direction).getTileEntity();
				INode node = getNodeFrom(tile, direction.getOpposite());

				if (node != null)
				{
					addConnection(node, direction);
				}
			}
		}
	}

	protected INode getNodeFrom(TileEntity tile, ForgeDirection from)
	{
		if (tile instanceof INodeProvider)
		{
			INode node = ((INodeProvider) tile).getNode(getRelativeClass(), from);

			if (node != null)
			{
				return node;
			}
		}

		return null;
	}

	/**
	 * Called to add an object to the connection map. Override this to update connection masks for client packets if needed.
	 */
	protected void addConnection(Object obj, ForgeDirection dir)
	{
		connections.put(obj, dir);
	}

	/**
	 * The class used to compare when making connections
	 */
	protected Class<? extends INode> getRelativeClass()
	{
		return getClass();
	}
}

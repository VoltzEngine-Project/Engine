package universalelectricity.core.grid.node;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.api.core.grid.IConnector;
import universalelectricity.api.core.grid.INode;
import universalelectricity.api.core.grid.INodeProvider;
import universalelectricity.core.grid.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * A Node that is designed to connect to other nodes, tiles, or objects.
 * @author Darkguardsman
 */
public class NodeConnector extends Node implements IConnector
{
    protected byte connectionMap = Byte.parseByte("111111", 2);
    protected WeakHashMap<Object, ForgeDirection> connections = new WeakHashMap<Object, ForgeDirection>();

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
        return (connectionMap & (1 << from.ordinal())) != 0;
    }

    public boolean isValidConnection(Object object)
    {
        return object != null && object.getClass().isAssignableFrom(getClass());
    }

    @Override
    public void deconstruct()
    {
        super.deconstruct();
        if(connections != null)
            connections.clear();
    }

    @Override
    public void reconstruct()
    {
        super.reconstruct();
        buildConnections();
    }

    /** Called during reconstruct to build the connection map */
    public void buildConnections()
    {
        if(connections == null)
            connections = new WeakHashMap<Object, ForgeDirection>();
        else
            connections.clear();
        
        for(ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
        {
            if(canConnect(direction.getOpposite()))
            {
                TileEntity tile = position().add(direction).getTileEntity();
                if(isValidConnection(tile))
                {
                    addConnection(tile, direction);
                }
            }
        }
    }

    /** Called to add an object to the connection map, and allows for add events during connection updating */
    protected void addConnection(Object obj, ForgeDirection dir)
    {
        connections.put(obj, dir);
    }
}

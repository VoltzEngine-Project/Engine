package resonant.api.grid;

import net.minecraftforge.common.util.ForgeDirection;

import java.util.Map;
import java.util.Set;

/**
 * Applied to anything that can connect to another object either part of a grid or tile system.
 * @author Darkguardsman
 */
public interface INodeConnector<A> extends INode
{
    /**
	 * Can the object connect to this node. Only called on connection update logic, and shouldn't be called per tick.
     *
     * @param other - The other has to be a connectable object.
     * @param from - Side of the node this connection is connecting from
     * @return True if and only if the connection is allowed
     */
    public <B extends A> boolean canConnect(B other, ForgeDirection from);

	public Set<A> connections();
}

package resonant.api.grid;

import net.minecraftforge.common.util.ForgeDirection;

import java.util.Map;

/**
 * Applied to anything that can connect to another object either part of a grid or tile system. *
 * @author Darkguardsman
 */
public interface IConnector
{
    /** Map of connections to this object from outside the object.
     *
     * List is not limited to 6 but each connection should have a direction
     * with Unknown being a custom or internal connection.
     * @return Map of connections*/
    public Map<Object, ForgeDirection> getConnections();

    /** Can the object connect to this machine. Only called on connection update logic, and shouldn't be called per tick.
     *
     * @param obj - assume this is a TileEntity, MultiPart, or INode unless custom
     *            logic is provided. Make sure to do IF checks correctly for each.
     * @param from - side of the machine this connection is connecting from
     * @return true if the connection is allowed, false for any reason the connection can not happen.
     */
    public boolean canConnect(Object obj, ForgeDirection from);
}

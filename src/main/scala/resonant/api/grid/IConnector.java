package resonant.api.grid;

import net.minecraftforge.common.util.ForgeDirection;

import java.util.Map;

/** Simple interface for checking connections to an object from a direction.
 *
 * Created on 11/12/2014.
 * @author Darkguardsman
 */
public interface IConnector
{
    /** Simple direction based connection check.
     *  Should be simple as side == side, and connection instanceof type
     *
     * @param connection - what is trying to connect, shouldn't be null
     * @param from - desired side to connect, may be null for internal connectons
     * @return true if the object can connect based on direction, and type
     */
    public boolean canConnect(Object connection, ForgeDirection from);

    /**
     * Map of connections by the sides they connected to. Is not limited to one
     * connection per side, however all connectins must return a side. Even if
     * this side ends up being ForgeDirection.Unknown
     * @return map of connections, or empty map in place of null/no connections
     */
    public Map<Object, ForgeDirection> getConnections();

}

package resonant.lib.network;

import java.util.List;

/** Applied to all classes that can send packets. For ease of use.
 * 
 * @author Calclavia */
public interface IPacketSender
{
    public List getPacketData(int type);
}

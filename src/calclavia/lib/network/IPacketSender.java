package calclavia.lib.network;

import java.util.ArrayList;

/**
 * Applied to all classes that can send packets. For ease of use.
 * @author Calclavia
 *
 */
public interface IPacketSender
{
	public ArrayList getPacketData(int type);
}

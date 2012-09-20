package universalelectricity.extend;

import net.minecraft.src.EntityPlayer;
import net.minecraftforge.common.ForgeDirection;


/**
 * The IMachine interface is an interface that must be applied to all tile entities that can input or output electricity.
 * @author Calclavia
 *
 */
public interface IMachine extends IDisableable
{
    /**
     * Called every tick on this machine.
     * 
     * @param amps - Amount of amps this electric unit is receiving.
     * @param voltage - The voltage of the electricity sent. If more than one
     * packet is being sent to you in this update, the highest voltage will
     * override.
     * @param side - The side of the block in which the electricity is coming from.
     */
    public void onReceive(double amps, double voltage, ForgeDirection side);

    /**
     * How many watts does this electrical unit need per tick?
     * Recommend for you to return the max electricity storage of this machine (if there is one).
     *
     * Set this to 0 if your electric unit can not receive electricity.
     */
    public double wattRequest();

    /**
     * Can this machine visually connect to a wire on this specific side?
     * @param side. 0-5 byte
     * @return - True if so.
     */
    public boolean canConnect(ForgeDirection side);

    /**
     * Can this unit receive electricity from this specific side
     * @param side. 0-5 byte
     * @return - True if so.
     */
    public boolean canReceiveFromSide(ForgeDirection side);

    /**
     * Gets the voltage of the electricity consumer. Used in a conductor to find the potential difference.
     * If the voltage is too high, things might explode.
     * @return The amount of volts. E.g 120v or 240v
     */
    public double getVoltage();

    /**
     * How many world ticks there should be before this tile entity gets ticked?
     * E.x Returning "1" will make this tile entity tick every single tick, "20"
     * will make this tile entity tick every second.
     * Using a higher number will result in less lag.
     *
     * Note: You can change this in real time and it will update the amount of
     * ticks your machine gets.
     * @return - The tick intervals. Returns -1 if you wish it to not tick at all.
     */
    public int getReceiveInterval();
    
    /**
     * Called when a player logs into a server. Works also in single player.
     */
    public void onPlayerLoggedIn(EntityPlayer player);
}
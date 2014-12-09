package resonant.api.tile.node;

import net.minecraftforge.common.util.ForgeDirection;
/// Calclavia, Do not deprecate this class as its not just for usage by the grids. From, DarkGuardsman

/**  Simple interface that allows adding and removing energy from a node.
 *
 * @author Darkguardsman */
public interface IEnergyNode extends INode
{
    /** Adds energy to the node returns energy added */
    public double addEnergy(ForgeDirection from, double wattage, boolean doAdd);

    /** Removes energy from the node returns energy removed */
    public double removeEnergy(ForgeDirection from, double wattage, boolean doRemove);

	/** Current energy stored in UE joules */
	public double getEnergy(ForgeDirection from);

    /** Max limit on energy stored */
    public double getEnergyCapacity(ForgeDirection from);
}

package resonant.api;

import net.minecraftforge.common.util.ForgeDirection;
import resonant.api.grid.IEnergyNode;
import universalelectricity.core.transform.vector.Vector3;

public interface IMechanicalNode extends IEnergyNode
{
	public double getTorque();

	public double getAngularVelocity();

	public void apply(Object source, double torque, double angularVelocity);

	public float getRatio(ForgeDirection dir, IMechanicalNode with);

	public boolean inverseRotation(ForgeDirection dir, IMechanicalNode with);

	public IMechanicalNode setLoad(double load);

	public Vector3 position();
}

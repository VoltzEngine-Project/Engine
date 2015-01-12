package com.builtbroken.mc.api.tile.node;

import net.minecraftforge.common.util.ForgeDirection;
import com.builtbroken.mc.lib.transform.vector.Pos;

public interface IMechanicalNode extends IEnergyNode
{
	public double getTorque();

	public double getAngularVelocity();

	public void apply(Object source, double torque, double angularVelocity);

	public float getRatio(ForgeDirection dir, IMechanicalNode with);

	public boolean inverseRotation(ForgeDirection dir, IMechanicalNode with);

	public IMechanicalNode setLoad(double load);

	public Pos position();
}

package com.builtbroken.mc.api.tile;

import net.minecraftforge.common.util.ForgeDirection;

import java.util.Set;

/**
 * @author Calclavia
 */
public interface IIO
{
	public Set<ForgeDirection> getInputDirections();

	public Set<ForgeDirection> getOutputDirections();

	public void setIO(ForgeDirection dir, int type);

	public int getIO(ForgeDirection dir);
}

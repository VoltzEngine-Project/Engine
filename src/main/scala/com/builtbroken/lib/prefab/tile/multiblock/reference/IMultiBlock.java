package com.builtbroken.lib.prefab.tile.multiblock.reference;

import com.builtbroken.lib.transform.vector.Vector3;

/**
 * Interface to be applied to tile entity blocks that occupies more than one block space. Useful for
 * large machines.
 *
 * @author Calclavia
 */
public interface IMultiBlock
{
	/**
	 * @return An array of Vector3 containing the multiblock relative coordinates to be constructed.
	 */
	public Iterable<Vector3> getMultiBlockVectors();
}

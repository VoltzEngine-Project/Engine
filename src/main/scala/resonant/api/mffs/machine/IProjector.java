package resonant.api.mffs.machine;

import net.minecraft.inventory.IInventory;
import resonant.api.blocks.IBlockFrequency;
import universalelectricity.core.transform.vector.Vector3;

import java.util.Set;

/**
 * Also extends IDisableable, IFortronFrequency
 *
 * @author Calclavia
 */
public abstract interface IProjector extends IInventory, IFieldMatrix, IBlockFrequency
{
	/**
	 * Projects the force field.
	 */
	public void projectField();

	/**
	 * Destroys the force field.
	 */
	public void destroyField();

	/**
	 * @return The speed in which a force field is constructed.
	 */
	public int getProjectionSpeed();

	/**
	 * @return The amount of ticks this projector has existed in the world.
	 */
	public long getTicks();

	/**
	 * DO NOT modify this list. Read-only.
	 *
	 * @return The actual force field block coordinates in the world.
	 */
	public Set<Vector3> getForceFields();

}
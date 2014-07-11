package resonant.api.mffs.modules;

import resonant.api.mffs.IFieldMatrix;
import resonant.api.mffs.IProjector;
import universalelectricity.core.transform.vector.Vector3;

import java.util.Set;

public interface IProjectorMode extends IFortronCost
{
	/**
	 * Called when the force field projector calculates the shape of the module.
	 *
	 * @param projector  - The Projector Object. Can cast to TileEntity.
	 * @param forceField - The blocks actually making up the force field. This array of blocks are
	 *                   NOT affected by rotation or translation.
	 */
	public Set<Vector3> getExteriorPoints(IFieldMatrix projector);

	/**
	 * @return Gets all interior points. Not translated or rotated.
	 */
	public Set<Vector3> getInteriorPoints(IFieldMatrix projector);

	/**
	 * @return Is this specific position inside of this force field?
	 */
	public boolean isInField(IFieldMatrix projector, Vector3 position);

	/**
	 * Called to render an object in front of the projection.
	 */
	public void render(IProjector projector, double x, double y, double z, float f, long ticks);
}

package resonant.lib.multiblock.reference;

import net.minecraft.world.World;
import universalelectricity.core.transform.vector.Vector3;

public interface IMultiBlockStructure<W extends IMultiBlockStructure> extends IMultiBlock
{
	public World getWorld();

	public void onMultiBlockChanged();

	public Vector3 getPosition();

	public MultiBlockHandler<W> getMultiBlock();
}

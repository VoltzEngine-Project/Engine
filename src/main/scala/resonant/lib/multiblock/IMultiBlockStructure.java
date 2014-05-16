package resonant.lib.multiblock;

import net.minecraft.world.World;
import universalelectricity.api.vector.Vector3;

public interface IMultiBlockStructure<W extends IMultiBlockStructure> extends IMultiBlock
{
    public World getWorld();

    public void onMultiBlockChanged();

    public Vector3 getPosition();

    public MultiBlockHandler<W> getMultiBlock();
}

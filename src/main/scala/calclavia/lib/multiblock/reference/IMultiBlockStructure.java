package calclavia.lib.multiblock.reference;

import net.minecraft.world.World;
import universalelectricity.api.vector.Vector3;
import calclavia.lib.multiblock.fake.IMultiBlock;

public interface IMultiBlockStructure<W extends IMultiBlockStructure> extends IMultiBlock
{
    public World getWorld();

    public void onMultiBlockChanged();

    public Vector3 getPosition();

    public MultiBlockHandler<W> getMultiBlock();
}

package universalelectricity.core.grid;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.api.core.grid.INode;
import universalelectricity.api.core.grid.INodeProvider;
import universalelectricity.core.transform.vector.IVectorWorld;
import universalelectricity.core.transform.vector.VectorWorld;

/**
 * Created by robert on 8/14/2014.
 */
public class Node implements INode, IVectorWorld
{
    public INodeProvider parent = null;

    public Node(INodeProvider parent)
    {
        this.parent = parent;
    }

    @Override
    public void reconstruct()
    {

    }

    @Override
    public void deconstruct()
    {
        parent = null;
    }

    @Override
    public INodeProvider getParent()
    {
        return this.parent;
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "[" + hashCode()+ "]";
    }

    public VectorWorld position()
    {
        return new VectorWorld(this);
    }

    @Override
    public World world()
    {
        if(getParent() instanceof TileEntity)
        {
            return ((TileEntity) getParent()).getWorldObj();
        }
        return null;
    }

    @Override
    public double z()
    {
        if(getParent() instanceof TileEntity)
        {
            return ((TileEntity) getParent()).zCoord;
        }
        return 0;
    }

    @Override
    public double x()
    {
        if(getParent() instanceof TileEntity)
        {
            return ((TileEntity) getParent()).xCoord;
        }
        return 0;
    }

    @Override
    public double y()
    {
        if(getParent() instanceof TileEntity)
        {
            return ((TileEntity) getParent()).yCoord;
        }
        return 0;
    }
}

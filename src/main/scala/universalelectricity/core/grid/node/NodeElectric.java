package universalelectricity.core.grid.node;

import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.api.core.grid.INodeProvider;
import universalelectricity.api.core.grid.electric.IElectricNode;

/**
 * Created by robert on 8/14/2014.
 */
public class NodeElectric extends NodeEnergy implements IElectricNode
{
    public NodeElectric(INodeProvider parent)
    {
        super(parent);
    }

    public NodeElectric(INodeProvider parent, double cap)
    {
        super(parent, cap);
    }

    public NodeElectric(INodeProvider parent, double cap, double tran)
    {
        super(parent, cap, tran);
    }

    public NodeElectric(INodeProvider parent, double cap, double in, double out)
    {
        super(parent, cap, in, out);
    }

    @Override
    public double getVoltage(ForgeDirection direction)
    {
        return 0;
    }
}

package resonant.lib.prefab;

import net.minecraft.block.material.Material;
import net.minecraftforge.common.util.ForgeDirection;
import resonant.api.grid.INode;
import resonant.content.prefab.java.TileNode;
import resonant.lib.grid.branch.NodeBranchPart;

import java.util.List;

/**
 * Basic wire mainly for testing RE internal functionality, can be
 * used as a template for creating wire, pipes, or grid based blocks.
 * @author Darkguardsman
 */
public class TileConductor extends TileNode
{
    public NodeBranchPart node;

    public TileConductor()
    {
        super(Material.circuits);
    }

    @Override
    public <N extends INode> N getNode(Class<? extends N> nodeType, ForgeDirection from)
    {
        //TODO fix later when implement junk tests
        return (N) node;
    }

    @Override
    public void getNodes(List<INode> nodes)
    {
        nodes.add(node);
    }

    public NodeBranchPart getNode()
    {
        if (node == null)
        {
            node = new NodeBranchPart(this);
        }
        return node;
    }
}
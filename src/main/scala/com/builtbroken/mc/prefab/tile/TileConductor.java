package com.builtbroken.mc.prefab.tile;

import com.builtbroken.mc.api.tile.node.ITileModule;
import com.builtbroken.mc.lib.grid.electric.NodeElectric;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.util.ForgeDirection;
import com.builtbroken.mc.lib.grid.branch.NodeBranchPart;

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
    public Tile newTile()
    {
        return new TileConductor();
    }

    @Override
    public <N extends ITileModule> N getModule(Class<? extends N> nodeType, ForgeDirection from)
    {
        //TODO fix later when implement junk tests
        return (N) getNode();
    }

    @Override
    public void getNodes(List<ITileModule> nodes)
    {
        nodes.add(getNode());
    }

    public NodeBranchPart getNode()
    {
        if (node == null)
        {
            node = new NodeElectric(this);
        }
        return node;
    }
}
package resonant.lib.grid.branch.part;

import resonant.api.grid.INode;
import resonant.lib.grid.branch.NodeBranchPart;
import scala.reflect.internal.util.WeakHashSet;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by robert on 11/5/2014.
 */
public abstract class Part
{
    private Set<Part> ecapsulatedParts;
    private Set<NodeBranchPart> ecapsulatedNodes;

    public Part()
    {
        ecapsulatedParts = new HashSet<Part>();
        ecapsulatedNodes = new HashSet<NodeBranchPart>();
    }

    public void add(NodeBranchPart node)
    {
        ecapsulatedNodes.add(node);
        node.setBranch(this);
    }

    public void add(Part node)
    {
        ecapsulatedParts.add(node);
    }

    public void remove(NodeBranchPart node)
    {
        ecapsulatedNodes.remove(node);
        node.setBranch(null);
    }

    public void remove(Part node)
    {
        ecapsulatedParts.remove(node);
    }

    public Set<Part> getEcapsulatedParts()
    {
        return ecapsulatedParts;
    }

    public Set<NodeBranchPart> getEcapsulatedNodes()
    {
        return ecapsulatedNodes;
    }

    public boolean contains(NodeBranchPart key)
    {
        return ecapsulatedNodes.contains(key);
    }

    public boolean contains(Part key)
    {
        return ecapsulatedParts.contains(key);
    }

    /** Joins the part to this part,
     * Part will be cleared in the process
     * @param part - part to add to this one
     * @return this if the join was good, null if it failed
     */
    public Part join(Part part)
    {
        if(part != this)
        {
            getEcapsulatedNodes().addAll(part.getEcapsulatedNodes());
            getEcapsulatedParts().addAll(part.getEcapsulatedParts());

            //Cleanup
            part.getEcapsulatedNodes().clear();
            part.getEcapsulatedParts().clear();
            return this;
        }
        return null;
    }

    public abstract boolean hasMinimalConnections();

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "@" +  hashCode() +"[" + ecapsulatedParts.size() +"]";
    }
}

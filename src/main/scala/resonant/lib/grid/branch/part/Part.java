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
    protected boolean lockUpdates = false;

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
            lockUpdates = true;
            for(NodeBranchPart node : part.getEcapsulatedNodes())
            {
                add(node);
            }
            for(Part p : part.getEcapsulatedParts())
            {
                add(p);
            }

            //Cleanup
            part.getEcapsulatedNodes().clear();
            part.getEcapsulatedParts().clear();

            onChange(ChangeCause.ADD);
            lockUpdates = false;
            return this;
        }
        return null;
    }

    /** Called any time the part changes */
    public final  void onChange(ChangeCause cause)
    {

    }

    /** Called any time the part changes */
    protected void onChanged(ChangeCause cause)
    {

    }

    public abstract boolean hasMinimalConnections();

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "@" +  hashCode() +"[" + ecapsulatedParts.size() +"]";
    }

    public static enum ChangeCause
    {
        //Something was added
        ADD,
        //Something was removed
        REMOVE,
        //Node had changed
        NODE
    }
}

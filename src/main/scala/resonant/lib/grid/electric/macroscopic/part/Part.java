package resonant.lib.grid.electric.macroscopic.part;

import resonant.api.grid.INode;
import scala.reflect.internal.util.WeakHashSet;

/**
 * Created by robert on 11/5/2014.
 */
public abstract class Part
{
    private WeakHashSet<Object> ecapsulatedParts;

    public Part()
    {
        ecapsulatedParts = new WeakHashSet();
    }

    public void add(INode node)
    {
        ecapsulatedParts.add(node);
    }

    public void add(Part node)
    {
        ecapsulatedParts.add(node);
    }

    public void remove(INode node)
    {
        ecapsulatedParts.remove(node);
    }

    public void remove(Part node)
    {
        ecapsulatedParts.remove(node);
    }

    public abstract boolean hasMinimalConnections();
}

package resonant.lib.type;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/** Generic class for link objects of the same class type to each other.
 * 
 * @author Robert Seifert */
public class Group<J>
{
    private String name;
    protected final Set<J> members = new LinkedHashSet<J>();

    public Group(String name, J... js)
    {
        this.name = name;
        if (js != null)
        {
            for (J obj : js)
            {
                this.addMemeber(obj);
            }
        }
    }

    public Set<J> getMembers()
    {
        return members;
    }

    protected boolean isValid(J obj)
    {
        return obj != null && !members.contains(obj);
    }

    public boolean addMemeber(J obj)
    {
        if (this.isValid(obj))
        {
            return members.add(obj);
        }
        return false;
    }

    public void addMemebers(Collection<J> members)
    {
        for (J mem : members)
        {
            addMemeber(mem);
        }

    }

    public boolean removeMemeber(J obj)
    {
        return members.remove(obj);
    }

    public boolean isMemeber(J obj)
    {
        return members.contains(obj);
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof Group && ((Group<?>) obj).getName().equalsIgnoreCase(this.getName());
    }

    @Override
    public String toString()
    {
        return "[Group:" + this.getName() + "]";
    }
}

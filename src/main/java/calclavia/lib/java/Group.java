package calclavia.lib.java;

import java.util.LinkedHashSet;

/** Generic class for link objects of the same class type to each other.
 * 
 * @author Robert Seifert */
public class Group<J>
{
    private String groupName;
    protected LinkedHashSet<J> memebers = new LinkedHashSet<J>();

    public Group(String name, J... js)
    {
        this.groupName = name;
        if (js != null)
        {
            for (J obj : js)
            {
                this.addMemeber(obj);
            }
        }
    }

    public LinkedHashSet<J> getMembers()
    {
        return this.memebers;
    }

    protected boolean isValid(J obj)
    {
        return obj != null && !memebers.contains(obj);
    }

    public boolean addMemeber(J obj)
    {
        if (this.isValid(obj))
        {
            memebers.add(obj);
            return memebers.contains(obj);
        }
        return false;
    }

    public boolean removeMemeber(J obj)
    {
        return memebers.remove(obj);
    }

    public boolean isMemeber(J obj)
    {
        return memebers.contains(obj);
    }

    public String getName()
    {
        return this.groupName;
    }

    public void setName(String name)
    {
        this.groupName = name;
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

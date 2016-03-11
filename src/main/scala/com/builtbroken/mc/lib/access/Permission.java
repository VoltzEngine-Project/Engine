package com.builtbroken.mc.lib.access;

import com.builtbroken.jlib.type.TreeNode;

import java.util.List;

/**
 * @author Calclavia
 */
public class Permission extends TreeNode<Permission>
{
    public final String id;

    private String cachedValue;

    public Permission(String id)
    {
        this.id = id;
    }

    public Permission addChild(String perm)
    {
        return super.addChild(new Permission(perm));
    }

    public Permission find(String id)
    {
        for (Permission child : children)
        {
            if (child.id.equals(id))
            {
                return child;
            }
        }

        return null;
    }

    @Override
    public boolean equals(Object o)
    {
        //TODO: This does not seem to work with large Scala sets?
        return o instanceof Permission && o.toString().equals(toString());
    }

    @Override
    public String toString()
    {
        if (cachedValue == null || cachedValue.isEmpty())
        {
            List<Permission> list = getHierarchy();

            StringBuilder builder = new StringBuilder();

            for (Permission perm : list)
            {
                builder.append(perm.id);
                builder.append(".");
            }

            builder.append(id);

            cachedValue = builder.toString();
        }
        return cachedValue;
    }
}

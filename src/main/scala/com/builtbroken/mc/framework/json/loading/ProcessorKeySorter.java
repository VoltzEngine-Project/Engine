package com.builtbroken.mc.framework.json.loading;

import java.util.Comparator;

/**
 * Simple pre-sorter that attempt to place tagged string near the bottom
 * so they are added after tags they depend on.
 */
public class ProcessorKeySorter implements Comparator<String>
{
    @Override
    public int compare(String o1, String o2)
    {
        if (o1.contains("@") && !o2.contains("@"))
        {
            return 1;
        }
        else if (!o1.contains("@") && o2.contains("@"))
        {
            return -1;
        }
        //TODO attempt to sort using before & after tags
        return o1.compareTo(o2);
    }
}
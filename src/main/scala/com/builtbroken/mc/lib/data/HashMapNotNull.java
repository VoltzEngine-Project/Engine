package com.builtbroken.mc.lib.data;

import com.builtbroken.mc.core.Engine;

import java.util.HashMap;
import java.util.Map;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/15/2017.
 */
public class HashMapNotNull<K, V> extends HashMap<K, V>
{
    public final String name;

    public HashMapNotNull(String name)
    {
        super();
        this.name = name;
    }

    public HashMapNotNull(String name, Map<? extends K, ? extends V> map)
    {
        this(name);
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet())
        {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public V put(K key, V value)
    {
        if (key != null && value != null)
        {
            return super.put(key, value);
        }
        else
        {
            Engine.logger().error("HashMapNotNull: Something tried to insert a null value into map '" + name + "'", new RuntimeException());
        }
        return null;
    }
}

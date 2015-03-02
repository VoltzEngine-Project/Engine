package com.builtbroken.mc.lib.mod.compat.oc;

import com.builtbroken.jlib.data.vector.IPos2D;
import com.builtbroken.mc.api.IWorldPosition;
import li.cil.oc.api.driver.Converter;

import java.util.Map;

/**
 * Created by robert on 3/2/2015.
 */
public class ConverterIPos2D implements Converter
{
    @Override
    public void convert(Object value, Map<Object, Object> output)
    {
        if(value instanceof IPos2D)
        {
            output.put("x", ((IPos2D) value).x());
            output.put("y", ((IPos2D) value).x());
        }
    }
}

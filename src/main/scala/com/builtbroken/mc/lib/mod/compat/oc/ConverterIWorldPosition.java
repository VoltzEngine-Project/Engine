package com.builtbroken.mc.lib.mod.compat.oc;

import com.builtbroken.mc.api.IWorldPosition;
import li.cil.oc.api.driver.Converter;

import java.util.Map;

/**
 * Created by robert on 3/2/2015.
 */
public class ConverterIWorldPosition implements Converter
{
    @Override
    public void convert(Object value, Map<Object, Object> output)
    {
        if(value instanceof IWorldPosition)
        {
            output.put("x", ((IWorldPosition) value).x());
            output.put("y", ((IWorldPosition) value).x());
            output.put("z", ((IWorldPosition) value).x());
            output.put("z", ((IWorldPosition) value).world().provider.dimensionId);
        }
    }
}

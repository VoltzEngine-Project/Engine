package com.builtbroken.mc.lib.mod.compat.oc;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.IWorldPosition;
import li.cil.oc.api.driver.Converter;

import java.util.Map;

/**
 * Created by robert on 3/2/2015.
 */
public class ConverterIPos implements Converter
{
    @Override
    public void convert(Object value, Map<Object, Object> output)
    {
        if(value instanceof IPos3D)
        {
            output.put("x", ((IPos3D) value).x());
            output.put("y", ((IPos3D) value).x());
            output.put("z", ((IPos3D) value).x());
            if(value instanceof IWorldPosition)
            {
                output.put("z", ((IWorldPosition) value).world().provider.dimensionId);
            }
        }
    }
}

package com.builtbroken.mc.lib.transform;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.lib.transform.vector.Pos;

/**
 * Applied to objects that can transform vectors
 *
 * @Calclavia
 */
public interface ITransform
{
	IPos3D transform(IPos3D vector);
}

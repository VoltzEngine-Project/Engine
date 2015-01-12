package com.builtbroken.mc.api;

import com.builtbroken.jlib.data.vector.IPos3D;
import net.minecraft.world.World;

/**
 * Useful interface to define that an object has a 3D location, and a defined world.
 *
 * @author DarkGuardsman
 */
public interface IPosWorld extends IPos3D
{
	World world();
}

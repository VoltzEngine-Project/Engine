package com.builtbroken.mc.api.tile;

import net.minecraftforge.common.util.ForgeDirection;

/** Designed to be used with tile modules to restrict access from the side
 * of a machine. It however can be used for anything tile related.
 *
 * Created by robert on 1/12/2015.
 */
public interface ISided
{
    boolean isValidForSide(ForgeDirection from);
}

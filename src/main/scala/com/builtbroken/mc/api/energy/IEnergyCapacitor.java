package com.builtbroken.mc.api.energy;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * Applied to any tile that contains energy
 * Created by robert on 2/22/2015.
 */
public interface IEnergyCapacitor
{
    /**
     * Used to see how much energy the tile has stored
     *
     * @param from side coming  into the machine
     * @return amount of energy stored on the side
     */
    public int getEnergyForSide(ForgeDirection from);

    /**
     * Sets how much energy the tile contains for the side
     *
     * This is not designed to be used outside of the tile
     * instead is meant for internal use. Mainly for
     * ITileModules to access the parent tile's
     * energy bank for use
     *
     * @param from side coming  into the machine
     */
    public void setEnergyForSide(ForgeDirection from, int energy);

    /**
     * Used to see how much energy the tile can stored
     *
     * @param from side coming  into the machine
     * @return amount of energy the tile could stored on the side
     */
    public int getMaxEnergyForSide(ForgeDirection from);
}

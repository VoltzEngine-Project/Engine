package com.builtbroken.mc.api.tile;

import com.builtbroken.mc.api.IGrid;

/**
 * Any node that is part of a grid system such as a power network
 */
public interface IGridProvider
{
    /** Sets the grid reference */
    void setGrid(IGrid grid);

    /** Gets the grid reference */
    IGrid getGrid();
}

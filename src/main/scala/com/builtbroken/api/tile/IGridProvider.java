package com.builtbroken.api.tile;

import com.builtbroken.api.IGrid;

/**
 * Any node that is part of a grid system such as a power network
 */
public interface IGridProvider
{
    /** Sets the grid reference */
    public void setGrid(IGrid grid);

    /** Gets the grid reference */
    public IGrid getGrid();
}

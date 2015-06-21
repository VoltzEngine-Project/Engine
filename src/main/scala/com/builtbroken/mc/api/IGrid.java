package com.builtbroken.mc.api;

import java.util.Set;

/**
 * Basic Grid structure
 * @Author Darkguardsman
 */
public interface IGrid<N>
{
    /** Gets all objects that act as nodes in this grid */
    Set<N> getNodes();

    /** Adds a node to the grid */
    void add(N node);

    /** Removes a node from the grid */
    void remove(N node);

    /** Asks the grid to rebuild */
    void reconstruct();

    /** Asks teh grid to destroy */
    void deconstruct();
}

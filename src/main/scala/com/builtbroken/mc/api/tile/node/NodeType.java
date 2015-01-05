package com.builtbroken.mc.api.tile.node;

/**
 * Basic set of Simulator types used by a few interfaces to define the return that should be given.
 * Each type does restrict how the network will function and is noted in java docs.
 *
 * #### Helpful DEFINITIONS###
 * UNIT                     - Any object, data, energy, fluid, etc that is moved by the network
 * DISTRIBUTION NETWORK     - Means units will be divided on supply and demand
 * PRESSURE NETWORK         - Higher version of distribution network using resistance as a limiter to flow of units
 *
 * @author Darkguardsman
 */
public enum NodeType
{
    /** DISTRIBUTION NETWORK, Movement of raw energy threw any means(Magic, wires, ducts, etc) */
    ENERGY,
    /** PRESSURE NETWORK, Movement of energy based on voltage and current */
    ELECTRICITY,
    /** DISTRIBUTION NETWORK, Movement of items */
    ITEMS,
    /** DISTRIBUTION NETWORK, Movement of entities */
    ENTITY,
    /** DISTRIBUTION NETWORK, Movement of energy threw heat */
    HEAT,
    /** DISTRIBUTION NETWORK, Movement of fluids */
    FLUID,
    /** PRESSURE NETWORK, Movement of energy by mechanical movement */
    FORCE
}

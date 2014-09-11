package universalelectricity.api.core.grid.sim;

/**
 * Basic set of Simulator types used by a few interfaces to define the return that should be given
 * @author Darkguardsman
 */
public enum SimType
{
    /** Movement of raw energy threw any means(Magic, wires, ducks, etc) */
    ENERGY,
    /** Movement of energy based on voltage and current */
    ELECTRICITY,
    /** Movement of items */
    ITEMS,
    /** Movement of entities */
    ENTITY,
    /** Movement of energy threw heat */
    HEAT,
    /** Movement of fluids */
    FLUID;
}

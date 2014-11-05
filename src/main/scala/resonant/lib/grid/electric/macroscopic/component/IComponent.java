package resonant.lib.grid.electric.macroscopic.component;

import universalelectricity.core.transform.vector.VectorWorld;
import resonant.lib.grid.electric.macroscopic.SimulatedGrid;

import java.util.List;

/**
 * Wrapper for nodes existing in a simulator in a way that several nodes can exist as one object.
 * As well provides a way to store extra data not provided by the nodes.
 *
 * @author Darkguardsman
 */
public interface IComponent
{
    /** Gets the host simulator for this component */
    public SimulatedGrid getSimulator();

    /** Components connected to this component */
    public List<IComponent> connections();

    /**
     * Does this component exist at the given location.
     * Used mainly for large multi-block components, or branches
     * @param vec - location in the world
     *
     * @return true if component exists at the location
     */
    public boolean doesExistAt(VectorWorld vec);

    /**
     * Gets the percent change in pressure from one component to the next.
     * If you don't implement pressure return a percent value of the connections.
     * Eg. 1 connection in 2 connections out will result in 50%(0.5 return)
     *
     * @param from - component the check is coming from
     * @param to - component the check is going to next
     * @return percent based value of the change.
     * Is not limited to any range value, but should reflect the actual change.
     */
    public double getPressureChange(IComponent from, IComponent to);

    /**
     * Gets the percent change in flow from one component to the next.
     * If you don't implement flow return a percent value of the connections.
     * Eg. 1 connection in 2 connections out will result in 50%(0.5 return)
     *
     * @param from - component the check is coming from
     * @param to - component the check is going to next
     * @return percent based value of the change.
     * Is not limited to any range value, but should reflect the actual change.
     */
    public double getFlowChange(IComponent from, IComponent to);

    /** Called to clear out the component */
    public void destroy();

    /** Called to generate connection data */
    public void build();
}

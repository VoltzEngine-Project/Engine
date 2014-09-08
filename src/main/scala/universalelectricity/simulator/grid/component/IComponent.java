package universalelectricity.simulator.grid.component;

/**
 * Interface used by parts of a simulation network.
 *
 * If input & output device check method return false this part is considered dead weight in the network. In which it will be ignored by the network, unless to fill up a internal buffer.
 * @author Darkguardsman
 */
public interface IComponent
{
    /** Used to see if there are machines attached to this part that can input */
    public boolean hasInputDevices();

    /** Used to see if there are machines attached to this part that can output */
    public boolean hasOutputDevices();
}

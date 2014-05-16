package resonant.api;

import java.util.List;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;

/** Prefab for creating commands that most terminal entities can use
 * 
 * @author DarkGuardsman */
public interface ITerminalCommand
{
    /** The command has been called by a player in a terminal. If player is null assume that the
     * command was triggered by the terminal itself.
     * 
     * @return List of output from the command, empty or null list is treated as skipping this
     * command. Any time your command is not used return a null or empty list. This way other
     * commands have a chance to process the same input. */
    public List<String> called(EntityPlayer player, ITerminal terminal, String[] args);

    /** Safty check to make sure the command can execute on the terminal without issues. Take this
     * time to check if the machine is instance of a class that you will cast to run the command */
    public boolean canSupport(ITerminal terminal);

    /** What the command starts with like /time */
    public String getCommandName();

    @Deprecated
    /**Unused*/
    public Set<String> getPermissionNodes();

    /** Gets the node linked to the command in the args array */
    public String getNode(String[] args);
}

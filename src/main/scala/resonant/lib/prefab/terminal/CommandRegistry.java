package resonant.lib.prefab.terminal;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import resonant.api.ITerminal;
import resonant.api.ITerminalCommand;
import resonant.lib.access.AccessUtility;

public class CommandRegistry
{
    public static final List<ITerminalCommand> COMMANDS = new ArrayList<ITerminalCommand>();

    /** @param prefix - what the command starts with for example /time
     * @param cmd - Cmd instance that will execute the command */
    public static void register(ITerminalCommand cmd, String group)
    {
        if (!COMMANDS.contains(cmd))
        {
            COMMANDS.add(cmd);
            if (group != null)
            {
                if (AccessUtility.groupDefaultNodes.containsKey(group))
                {
                    List<String> stra = new ArrayList<String>();
                    stra.add(cmd.getCommandName());
                }
            }
        }
    }

    /** When a player uses a command in any CMD machine it pass threw here first
     * 
     * @param terminal - The terminal, can be cast to TileEntity. */
    public static List<String> onCommand(EntityPlayer player, ITerminal terminal, String cmd)
    {
        List<String> output_list = new ArrayList<String>();
        if (player.worldObj.isRemote)
        {
            output_list.add("Error: Command called client side");
        }
        else if (cmd != null && cmd != "")
        {
            String[] args = cmd.split(" ");

            if (args[0] != null)
            {
                for (ITerminalCommand command : COMMANDS)
                {
                    if (command.getCommandName().equalsIgnoreCase(args[0]))
                    {
                        if (command.canSupport(terminal) && command.getNode(args) != null)
                        {
                            if (!terminal.canUse(command.getNode(args), player) && !player.capabilities.isCreativeMode)
                            {
                                output_list.add("Access Denied");
                                terminal.addToConsole(output_list);
                                return output_list;
                            }
                            else
                            {
                                List<String> out = command.called(player, terminal, args);
                                if (out != null && !out.isEmpty())
                                {
                                    output_list.addAll(out);
                                    terminal.addToConsole(output_list);
                                    return output_list;
                                }
                            }
                        }
                    }
                }
            }
            output_list.add("Unkown Command.");
        }
        else
        {
            output_list.add("Error: Empty command");
        }
        terminal.addToConsole(output_list);
        return output_list;
    }
}

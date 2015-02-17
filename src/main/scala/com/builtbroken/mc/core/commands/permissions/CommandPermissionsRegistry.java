package com.builtbroken.mc.core.commands.permissions;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.access.Permission;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/** Handles all permission nodes generated or registered for commands
 * Created by robert on 2/17/2015.
 */
public class CommandPermissionsRegistry
{
    public static final Permission ALL = new Permission("root");
    public static final Logger logger = LogManager.getLogger("VE-Permissions");
    public static final HashMap<ICommand, String> commandToNodeMap = new HashMap();


    public static void handle(ICommand command, String name)
    {
        //Don't re-add nodes if something has already registered that command
        if(!commandToNodeMap.containsKey(command))
        {
            String node;
            if(command.getClass().toString().startsWith("class net.minecraft.command"))
            {
                node = "mc." + name;
            }
            else
            {
                node = command.getClass().toString();
            }
            logger.info("Registering command " + name + " with permission node " + node);
            commandToNodeMap.put(command, node);
        }
    }

    public static String getNodeFor(ICommand command, String[] args)
    {
        if(command != null && commandToNodeMap.containsKey(command))
        {
            return commandToNodeMap.get(command);
        }
        return "null";
    }
}

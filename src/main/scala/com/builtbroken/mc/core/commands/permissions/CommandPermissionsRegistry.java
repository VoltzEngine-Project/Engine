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


    public static void init(ServerCommandManager serverCommandManager)
    {
        //TODO loop threw all registered commands and create permissions nodes for them
        Map map = serverCommandManager.getCommands();
        logger.info("Processing commands registered to MC's Command Manager");
        for(Object o : map.entrySet())
        {
            if(o instanceof Map.Entry)
            {
                try
                {
                    String command_name = (String) ((Map.Entry) o).getKey();
                    ICommand command = (ICommand) ((Map.Entry) o).getValue();
                    //Don't re-add nodes if something has already registered that command
                    if(!commandToNodeMap.containsKey(command))
                    {
                        String node;
                        if(command.getClass().toString().startsWith("class net.minecraft.command"))
                        {
                            node = "mc." + command_name;
                        }
                        else
                        {
                            node = command.getClass().toString();
                        }
                        logger.info("Registering command " + command_name + " with permission node " + node);
                        commandToNodeMap.put(command, node);
                    }
                }
                catch(Exception e)
                {
                    if(Engine.runningAsDev)
                        e.printStackTrace();
                    else
                        logger.error("Failed to process entry " + o);
                }
            }
        }
        logger.info("Done processing commands...");
    }
}

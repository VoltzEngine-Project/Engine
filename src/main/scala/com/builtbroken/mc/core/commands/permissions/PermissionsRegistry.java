package com.builtbroken.mc.core.commands.permissions;

import com.builtbroken.jlib.lang.StringHelpers;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.access.Permission;
import com.builtbroken.mc.lib.helper.NBTUtility;
import net.minecraft.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles all permission nodes generated or registered for commands
 * Created by robert on 2/17/2015.
 */
public class PermissionsRegistry
{
    public static final Logger logger = LogManager.getLogger("VE-Permissions");
    public static final HashMap<ICommand, String> commandToNodeMap = new HashMap();
    public static final HashMap<String, String> packageToNodePrefix = new HashMap();

    private static int longestCommandLength = 0;

    //Static list of default permission nodes
    public static final Permission ALL = new Permission("root");
    public static final Permission MC = ALL.addChild("mc");
    public static final Permission MC_HELP = MC.addChild("help");

    static
    {
        packageToNodePrefix.put("net.minecraft.command", "minecraft");
        packageToNodePrefix.put("net.minecraftforge", "forge");
        packageToNodePrefix.put("com.builtbroken", "bbm");
        packageToNodePrefix.put("li.cil.oc", "oc");
        packageToNodePrefix.put("buildcraft", "buildcraft");
        packageToNodePrefix.put("mcp.mobius.waila", "waila");
    }

    public static void registerNodeForCommand(ICommand command, String node)
    {
        if (command != null && node != null && !node.isEmpty())
        {
            if (command.getCommandName() != null && command.getCommandName().length() > longestCommandLength)
                longestCommandLength = command.getCommandName().length();

            if (!commandToNodeMap.containsKey(command))
                commandToNodeMap.put(command, node);
        }
    }

    public static void handle(ICommand command, String name)
    {
        //Don't re-add nodes if something has already registered that command
        if (!commandToNodeMap.containsKey(command))
        {
            String node = command.getClass().toString().replace("class ", "");
            for(Map.Entry<String, String> p : packageToNodePrefix.entrySet())
            {
                if(p.getValue() != null && !p.getValue().isEmpty() && node.startsWith(p.getKey()))
                {
                    node = ALL.toString() + "." + p.getValue() + "." + name;
                    break;
                }
            }
            if(!node.startsWith(ALL.toString()))
            {
                node = ALL.toString() + "." + node;
            }
            if (Engine.runningAsDev) //Reduce console spam, since server owners don't care
                logger.info("Registering command " + name + " with permission node " + node);
            registerNodeForCommand(command, node);
        }
    }

    public static String getNodeFor(ICommand command, String[] args)
    {
        if (command != null && commandToNodeMap.containsKey(command))
        {
            return commandToNodeMap.get(command);
        }
        return "null";
    }

    public static void dumpNodesToFile()
    {
        File file = new File(NBTUtility.getBaseDirectory(), "permissionNodes.txt");
        if (file.exists())
        {
            file.delete();
        }

        try
        {
            FileOutputStream fos = new FileOutputStream(file);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            bw.write("" + StringHelpers.padRight("Name", longestCommandLength) + "  | Permission Node |");
            bw.newLine();
            int i = 0;
            for (Map.Entry<ICommand, String> entry : commandToNodeMap.entrySet())
            {
                bw.write("  " + StringHelpers.padRight(entry.getKey().getCommandName(), longestCommandLength) + "   " + entry.getValue());
                bw.newLine();
                i++;
                if (i >= 5)
                {
                    i = 0;
                    bw.newLine();
                }
            }

            bw.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

package com.builtbroken.mc.core.commands.json.override;

import com.builtbroken.jlib.data.Colors;
import com.builtbroken.mc.core.commands.prefab.SubCommand;
import com.builtbroken.mc.framework.json.JsonContentLoader;
import com.builtbroken.mc.framework.json.imp.IJsonGenObject;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.override.IModifableJson;
import com.google.gson.JsonPrimitive;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 12/17/2017.
 */
public class CommandJOSet extends SubCommand
{
    public CommandJOSet()
    {
        super("set");
    }

    @Override
    public boolean handleEntityPlayerCommand(EntityPlayer player, String[] args)
    {
        return handleConsoleCommand(player, args);
    }

    @Override
    public boolean handleConsoleCommand(ICommandSender sender, String[] args)
    {
        if (args != null && args.length > 0 && !"help".equalsIgnoreCase(args[0]))
        {
            if (args.length >= 4)
            {
                String type = args[0];
                String id = args[1];
                String field = args[2];
                String value = args[3];

                IJsonProcessor processor = JsonContentLoader.getProcessor(type);
                if (processor instanceof IModifableJson)
                {
                    //Get list
                    if (JsonContentLoader.INSTANCE.generatedObjects.get(type) != null)
                    {
                        IJsonGenObject targetContent = JsonContentLoader.getContent(type, id);

                        if (targetContent != null)
                        {
                            try
                            {
                                if (((IModifableJson) processor).replaceData(field, new JsonPrimitive(value), targetContent))
                                {
                                    Object o = ((IModifableJson) processor).getData(field, targetContent);
                                    sender.addChatMessage(new ChatComponentText("Value for '" + field + "' has been set to '" + (o != null ? o : value) + "', remember to save after testing."));
                                }
                                else
                                {
                                    sender.addChatMessage(new ChatComponentText("Failed to get value for '" + field + "' this either means its not specified, can not be accessed, or couldn't be found."));
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                                sender.addChatMessage(new ChatComponentText(Colors.RED.code + "Error:"));
                                sender.addChatMessage(new ChatComponentText("Failed to set value for '" + field + "' due to an unexpected error, " +
                                        "double check the field name and type. Keep in mind the set command only supports basic data types at the moment (string, integer, float, double, byte, boolean, etc) " +
                                        "and can not hand any types that require more conversion before being used."));
                                sender.addChatMessage(new ChatComponentText("Exception: " + e.toString()));
                            }
                            return true;
                        }
                        else
                        {
                            sender.addChatMessage(new ChatComponentText(Colors.RED.code + "ERROR: Failed to find JSON content with ID '" + type + ":" + id + "'"));
                        }
                    }
                    else
                    {
                        sender.addChatMessage(new ChatComponentText(Colors.RED.code + "ERROR: Failed to find any objects registered for type '" + type + "'"));
                    }
                }
                else if (processor == null)
                {
                    sender.addChatMessage(new ChatComponentText(Colors.RED.code + "ERROR: Failed to find a JSON processor for type '" + type + "'"));
                }
                else
                {
                    sender.addChatMessage(new ChatComponentText(Colors.RED.code + "ERROR: The JSON processor for type '" + type + "' does not support overrides."));
                }
                return true;
            }
        }
        return handleHelp(sender, args);
    }
}

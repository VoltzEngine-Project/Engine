package com.builtbroken.mc.core.commands.json;

import com.builtbroken.jlib.data.Colors;
import com.builtbroken.mc.core.commands.prefab.SubCommand;
import com.builtbroken.mc.framework.json.JsonContentLoader;
import com.builtbroken.mc.framework.json.imp.IJsonGenObject;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.override.IModifableJson;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;

/**
 * Handles commands for the JSON override system
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 12/11/2017.
 */
public class CommandJsonOverride extends SubCommand
{
    public CommandJsonOverride()
    {
        super("override");
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
            if (args[0].equals("generate") || args[0].equals("gen"))
            {
                if (args.length >= 3)
                {
                    String type = args[1];
                    String id = args[2];

                    //Fix for lowercase
                    for (String key : JsonContentLoader.INSTANCE.generatedObjects.keySet())
                    {
                        if (key.equalsIgnoreCase(type))
                        {
                            type = key;
                            break;
                        }
                    }

                    IJsonProcessor processor = JsonContentLoader.INSTANCE.get(type);
                    if (processor instanceof IModifableJson)
                    {
                        //Get list
                        List<IJsonGenObject> content = JsonContentLoader.INSTANCE.generatedObjects.get(type);
                        if (content != null)
                        {
                            //Search list for object
                            for (IJsonGenObject object : content)
                            {
                                if (object != null) //TODO add check for legacy mod:id system
                                {
                                    boolean found;

                                    if (id.contains(":"))
                                    {
                                        found = id.equalsIgnoreCase(object.getContentID());
                                    }
                                    else
                                    {
                                        found = id.equalsIgnoreCase(object.getUniqueID());
                                    }

                                    if (found)
                                    {
                                        sender.addChatMessage(new ChatComponentText("Found '" + type + ":" + id + "'"));

                                        if (object.getContentID() != null)
                                        {
                                            File outputFolder = JsonContentLoader.INSTANCE.externalContentFolder;

                                            String path = (object.getMod() != null ? object.getMod() + "/" : "") + object.getContentID() + ".json";
                                            path = path.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");

                                            File outputFile = new File(outputFolder, path);
                                            JsonObject jsonObject = ((IModifableJson) processor).getPossibleModificationsAsJson(object);
                                            if (jsonObject != null)
                                            {
                                                sender.addChatMessage(new ChatComponentText("Writing file to: §n" + outputFile)); //TODO make link clickable
                                                try (Writer writer = new FileWriter(outputFile))
                                                {
                                                    Gson gson = new GsonBuilder().create();
                                                    gson.toJson(jsonObject, writer);

                                                    sender.addChatMessage(new ChatComponentText("Completed generating modifications for JSON content '" + type + ":" + id + "'"));
                                                }
                                                catch (Exception e)
                                                {
                                                    e.printStackTrace();
                                                    sender.addChatMessage(new ChatComponentText(Colors.RED.code + "ERROR: Failed to write file due to unexpected error, see console for details"));
                                                }
                                            }
                                            else
                                            {
                                                sender.addChatMessage(new ChatComponentText(Colors.RED.code + "ERROR: Failed to get modification list for '" + type + ":" + id + "' " +
                                                        "this either means the operation is unsupported or the object has not modifications accessible via JSON."));
                                            }
                                        }
                                        return true;
                                    }
                                    else
                                    {
                                        sender.addChatMessage(new ChatComponentText(Colors.RED.code + "ERROR: Failed to find JSON content with ID '" + type + ":" + id + "'"));
                                    }
                                }
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
            else if (args[0].equals("get"))
            {
                //TODO Get value of override
            }
            else if (args[0].equals("set"))
            {
                //TODO set value of override
            }
            else if (args[0].equals("ls") || args[0].equals("list"))
            {
                //TODO list all overrides for an object or list all override objects if empty
                ///Case 1: list type id
                ///case 2: list type
                ///case 3: list
            }
        }
        return handleHelp(sender, args);
    }
}

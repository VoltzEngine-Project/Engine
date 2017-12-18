package com.builtbroken.mc.core.commands.json.override;

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

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 12/17/2017.
 */
public class CommandJOGen extends SubCommand
{
    public CommandJOGen(String name)
    {
        super(name);
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
            if (args.length >= 2)
            {
                String type = args[0];
                String id = args[1];

                IJsonProcessor processor = JsonContentLoader.getProcessor(type);
                if (processor instanceof IModifableJson)
                {
                    //Get list
                    if (JsonContentLoader.INSTANCE.generatedObjects.get(type) != null)
                    {
                        IJsonGenObject targetContent = JsonContentLoader.getContent(type, id);

                        if (targetContent != null)
                        {
                            sender.addChatMessage(new ChatComponentText("Found '" + type + ":" + id + "'"));

                            if (targetContent.getContentID() != null)
                            {
                                File outputFolder = JsonContentLoader.INSTANCE.externalContentFolder;

                                String path = (targetContent.getMod() != null ? targetContent.getMod() + "/" : "") + targetContent.getContentID() + ".json";
                                path = path.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");

                                File outputFile = new File(outputFolder, path);

                                if (outputFile.exists() && (args.length == 2 || !args[2].equalsIgnoreCase("override")))
                                {
                                    sender.addChatMessage(new ChatComponentText(Colors.YELLOW.code + "Error: Can't write data as file already exists."));
                                    sender.addChatMessage(new ChatComponentText(Colors.YELLOW.code + "Run with override at end to write over the file"));
                                    sender.addChatMessage(new ChatComponentText(Colors.YELLOW.code + "File: \u00A7n" + outputFile));
                                    return true;
                                }

                                JsonObject jsonObject = ((IModifableJson) processor).getPossibleModificationsAsJson(targetContent);
                                if (jsonObject != null)
                                {
                                    sender.addChatMessage(new ChatComponentText("Writing file to: \u00A7n" + outputFile)); //TODO make link clickable
                                    try (Writer writer = new FileWriter(outputFile))
                                    {
                                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                                        gson.toJson(jsonObject, writer);

                                        sender.addChatMessage(new ChatComponentText("Completed generating modifications for JSON content '" + type + "-" + id + "'"));
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

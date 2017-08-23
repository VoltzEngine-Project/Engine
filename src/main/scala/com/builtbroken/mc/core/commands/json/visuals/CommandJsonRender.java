package com.builtbroken.mc.core.commands.json.visuals;

import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.client.json.render.RenderData;
import com.builtbroken.mc.client.json.render.state.ModelState;
import com.builtbroken.mc.core.commands.prefab.SubCommand;
import com.builtbroken.mc.imp.transform.rotation.EulerAngle;
import com.builtbroken.mc.imp.transform.vector.Pos;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/7/2017.
 */
public class CommandJsonRender extends SubCommand
{
    public RenderData loadedData;
    public ModelState loadedState;

    public CommandJsonRender()
    {
        super("render");
    }

    @Override
    public boolean handleEntityPlayerCommand(EntityPlayer player, String[] args)
    {
        if (args != null && args.length > 0 && !"help".equalsIgnoreCase(args[0]))
        {
            if (args[0].equalsIgnoreCase("loadData"))
            {
                if (args.length > 1)
                {
                    String id = args[1];
                    loadedState = null;
                    loadedData = ClientDataHandler.INSTANCE.getRenderData(id);
                    if (loadedData != null)
                    {
                        player.sendMessage(new TextComponentString("Loaded render data for id '" + id + "', data contains " + loadedData.renderStatesByName.size() + " render states."));
                    }
                    else
                    {
                        player.sendMessage(new TextComponentString("Did not find render data with id '" + id + "'"));
                    }
                }
                else
                {
                    player.sendMessage(new TextComponentString("Need to provide render data ID"));
                }
            }
            else if (loadedData != null)
            {
                if (args[0].equalsIgnoreCase("loadState"))
                {
                    if (args.length > 1)
                    {
                        String id = args[1];
                        IRenderState state = loadedData.getState(id);
                        if (state instanceof ModelState)
                        {
                            loadedState = (ModelState) state;
                            player.sendMessage(new TextComponentString("Loaded render state for id '" + id + "'"));
                        }
                        else if (state != null)
                        {
                            player.sendMessage(new TextComponentString("Only model state data is supported for command modification."));
                        }
                        else
                        {
                            player.sendMessage(new TextComponentString("Did not find render state for id '" + id + "'"));
                        }
                    }
                    else
                    {
                        player.sendMessage(new TextComponentString("Need to provide render state ID"));
                    }
                }
                else if (loadedState != null)
                {
                    if (args[0].equalsIgnoreCase("setRotation"))
                    {
                        if (args.length > 3)
                        {
                            try
                            {
                                player.sendMessage(new TextComponentString("Previous Rotation: " + loadedState.getRotation()));
                                loadedState.rotation = new EulerAngle(Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
                                player.sendMessage(new TextComponentString("Rotation: " + loadedState.getRotation()));
                            }
                            catch (NumberFormatException e)
                            {
                                player.sendMessage(new TextComponentString("Error: could not parse numbers"));
                            }
                        }
                    }
                    else if (args[0].equalsIgnoreCase("setOffset"))
                    {
                        if (args.length > 3)
                        {
                            try
                            {
                                player.sendMessage(new TextComponentString("Previous Offset: " + loadedState.getOffset()));
                                loadedState.offset = new Pos(Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
                                player.sendMessage(new TextComponentString("Offset: " + loadedState.getOffset()));
                            }
                            catch (NumberFormatException e)
                            {
                                player.sendMessage(new TextComponentString("Error: could not parse numbers"));
                            }
                        }
                    }
                    else if (args[0].equalsIgnoreCase("setScale"))
                    {
                        if (args.length > 3)
                        {
                            try
                            {
                                player.sendMessage(new TextComponentString("Previous Offset: " + loadedState.getOffset()));
                                loadedState.offset = new Pos(Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
                                player.sendMessage(new TextComponentString("Offset: " + loadedState.getOffset()));
                            }
                            catch (NumberFormatException e)
                            {
                                player.sendMessage(new TextComponentString("Error: could not parse numbers"));
                            }
                        }
                        else if (args.length > 1)
                        {
                            try
                            {
                                player.sendMessage(new TextComponentString("Previous Scale: " + loadedState.getScale()));
                                loadedState.scale = new Pos(Double.parseDouble(args[1]));
                                player.sendMessage(new TextComponentString("Scale: " + loadedState.getScale()));
                            }
                            catch (NumberFormatException e)
                            {
                                player.sendMessage(new TextComponentString("Error: could not parse numbers"));
                            }
                        }
                    }
                    else if (args[0].equalsIgnoreCase("output"))
                    {
                        player.sendMessage(new TextComponentString("Offset: " + loadedState.getOffset()));
                        player.sendMessage(new TextComponentString("Rotation: " + loadedState.getRotation()));
                        player.sendMessage(new TextComponentString("Scale: " + loadedState.getScale()));
                    }
                }
                else
                {
                    player.sendMessage(new TextComponentString("Render data must be loaded to use any other command. Use '/ve debug render loadState <render state id>' to load render data"));
                }
            }
            else
            {
                player.sendMessage(new TextComponentString("Render data must be loaded to use any other command. Use '/ve debug render loadData <render data id>' to load render data"));
            }
        }
        return true;
    }

    @Override
    public boolean handleConsoleCommand(ICommandSender sender, String[] args)
    {
        return false;
    }
}

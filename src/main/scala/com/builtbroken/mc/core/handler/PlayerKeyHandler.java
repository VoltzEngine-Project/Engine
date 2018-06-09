package com.builtbroken.mc.core.handler;

import com.builtbroken.mc.api.items.IMouseButtonHandler;
import com.builtbroken.mc.api.items.tools.IItemMouseScroll;
import com.builtbroken.mc.api.items.tools.IModeItem;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.packet.user.PacketMouseClick;
import com.builtbroken.mc.core.network.packet.user.PacketMouseScroll;
import com.builtbroken.mc.core.network.packet.user.PacketPlayerItemMode;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * Handles Keyboard and mouse input from the player for interfaces. This way objects
 * using the interface have less to deal with.
 *
 * @author DarkGuardsman
 */
@SideOnly(Side.CLIENT)
public class PlayerKeyHandler
{
    //@SubscribeEvent TODO re-implement using key events
    public void tickEvent(TickEvent.ClientTickEvent event)
    {
        switch (event.phase)
        {
            case START:
                keyTick(false);
                break;
            case END:
                keyTick(true);
        }
    }

    @SubscribeEvent
    public void mouseHandler(MouseEvent e)
    {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        ItemStack stack = player.getCurrentEquippedItem();
        if (stack != null)
        {
            final Item item = stack.getItem();
            if (item instanceof IItemMouseScroll)
            {
                if (e.dwheel != 0)
                {
                    boolean ctrl = Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
                    boolean shift = Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
                    IItemMouseScroll.Result result = ((IItemMouseScroll) item).onMouseWheelScrolled(player, stack, ctrl, shift, e.dwheel > 0);
                    if (result == IItemMouseScroll.Result.CLIENT)
                    {
                        e.setCanceled(true);
                    }
                    else if (result == IItemMouseScroll.Result.SERVER)
                    {
                        Engine.packetHandler.sendToServer(new PacketMouseScroll(player.inventory.currentItem, ctrl, shift, e.dwheel > 0));
                        e.setCanceled(true);
                    }
                }
            }
            else if (item instanceof IModeItem.IModeScrollItem)
            {
                if (player.isSneaking() && e.dwheel != 0)
                {
                    int newMode = ((IModeItem.IModeScrollItem) stack.getItem()).cycleMode(stack, player, e.dwheel / 120);
                    if (newMode != ((IModeItem.IModeScrollItem) stack.getItem()).getMode(stack))
                    {
                        Engine.packetHandler.sendToServer(new PacketPlayerItemMode(player.inventory.currentItem, newMode));
                    }
                    e.setCanceled(true);
                }
            }

            if (item instanceof IMouseButtonHandler && e.button != -1)
            {
                Engine.packetHandler.sendToServer(new PacketMouseClick(player.inventory.currentItem, e.button, e.buttonstate));
                ((IMouseButtonHandler) item).mouseClick(stack, player, e.button, e.buttonstate);
                if (((IMouseButtonHandler) item).shouldCancelMouseEvent(stack, player, e.button, e.buttonstate))
                {
                    e.setCanceled(true);
                }
            }
        }
    }

    //@SubscribeEvent
    public void keyHandler(InputEvent.KeyInputEvent e)
    {
        //Use this to capture keys being hit without using key bindings
        final int key = Keyboard.getEventKey();
        final long time = System.currentTimeMillis();
    }

    private void keyTick(boolean tickEnd)
    {
        for (int i = 0; i < Minecraft.getMinecraft().gameSettings.keyBindings.length; i++)
        {
            KeyBinding keyBinding = Minecraft.getMinecraft().gameSettings.keyBindings[i];
            int keyCode = keyBinding.getKeyCode();
            boolean state = (keyCode < 0 ? Mouse.isButtonDown(keyCode + 100) : Keyboard.isKeyDown(keyCode));
            if (state)
            {
                //PacketManagerKeyEvent.sendPacket(this.channel, keyCode);
            }
        }
    }
}

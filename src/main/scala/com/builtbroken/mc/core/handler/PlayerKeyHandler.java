package com.builtbroken.mc.core.handler;

import com.builtbroken.mc.api.items.IMouseButtonHandler;
import com.builtbroken.mc.api.items.tools.IModeItem;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.packet.user.PacketMouseClick;
import com.builtbroken.mc.core.network.packet.user.PacketPlayerItemMode;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
        EntityPlayer player = Minecraft.getMinecraft().player;
        ItemStack stack = player.getHeldItemMainhand();
        if (stack != null)
        {
            final Item item = stack.getItem();
            if (item instanceof IModeItem.IModeScrollItem)
            {
                if (player.isSneaking() && e.getDwheel() != 0)
                {
                    int newMode = ((IModeItem.IModeScrollItem) stack.getItem()).cycleMode(stack, player, e.getDwheel() / 120);
                    if (newMode != ((IModeItem.IModeScrollItem) stack.getItem()).getMode(stack))
                    {
                        Engine.packetHandler.sendToServer(new PacketPlayerItemMode(player.inventory.currentItem, newMode));
                    }
                    e.setCanceled(true);
                }
            }
            else if (item instanceof IMouseButtonHandler && e.getButton() != -1)
            {
                Engine.packetHandler.sendToServer(new PacketMouseClick(player.inventory.currentItem, e.getButton(), e.isButtonstate()));
                ((IMouseButtonHandler) item).mouseClick(stack, player, e.getButton(), e.isButtonstate());
                if (((IMouseButtonHandler) item).shouldCancelMouseEvent(stack, player, e.getButton(), e.isButtonstate()))
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

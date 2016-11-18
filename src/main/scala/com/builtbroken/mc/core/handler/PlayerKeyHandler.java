package com.builtbroken.mc.core.handler;

import com.builtbroken.mc.api.items.IMouseButtonHandler;
import com.builtbroken.mc.api.items.tools.IModeItem;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.packet.user.PacketMouseClick;
import com.builtbroken.mc.core.network.packet.user.PacketPlayerItemMode;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
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
    @SideOnly(Side.CLIENT)
    public void mouseHandler(MouseEvent e)
    {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        ItemStack stack = player.getCurrentEquippedItem();
        if (stack != null)
        {
            final Item item = stack.getItem();
            if (item instanceof IModeItem.IModeScrollItem)
            {
                if (player.isSneaking() && e.dwheel != 0)
                {
                    int newMode = ((IModeItem.IModeScrollItem) stack.getItem()).cycleMode(stack, player, e.dwheel / 120);
                    if (newMode != ((IModeItem.IModeScrollItem) stack.getItem()).getMode(stack))
                    {
                        Engine.instance.packetHandler.sendToServer(new PacketPlayerItemMode(player.inventory.currentItem, newMode));
                    }
                    e.setCanceled(true);
                }
            }
            else if (item instanceof IMouseButtonHandler && e.button != -1)
            {
                Engine.instance.packetHandler.sendToServer(new PacketMouseClick(player.inventory.currentItem, e.button, e.buttonstate));
                ((IMouseButtonHandler) item).mouseClick(stack, player, e.button, e.buttonstate);
                if (((IMouseButtonHandler) item).shouldCancelMouseEvent(stack, player, e.button, e.buttonstate))
                {
                    e.setCanceled(true);
                }
            }
        }
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

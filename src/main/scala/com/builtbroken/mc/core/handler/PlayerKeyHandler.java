package com.builtbroken.mc.core.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * This class handles keys already binded to the game so to avoid creating new key bindings
 *
 * @author DarkGuardsman
 */
public class PlayerKeyHandler
{
	private String channel;

	public PlayerKeyHandler(String channel)
	{
		this.channel = channel;
	}

	@SubscribeEvent
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

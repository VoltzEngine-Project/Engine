package com.builtbroken.mc.lib.mod.config;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraftforge.common.config.Configuration;

/**
 * Config events to handle.
 *
 * @author Calclavia
 */
public class ConfigEvent extends Event
{
	public final Configuration config;

	public ConfigEvent(Configuration config)
	{
		this.config = config;
	}

	/**
	 * Called after the mod's settings have been synced with the config values.
	 */
	public static class PostConfigEvent extends ConfigEvent
	{
		public PostConfigEvent(Configuration config)
		{
			super(config);
		}
	}
}

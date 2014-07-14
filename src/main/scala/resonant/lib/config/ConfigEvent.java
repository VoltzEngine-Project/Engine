package resonant.lib.config;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraftforge.common.config.Configuration;

/**
 * Config events to handle.
 *
 * @author Calclavia
 */
public class ConfigEvent
{
	/**
	 * Called after the mod's settings have been synced with the config values.
	 */
	public static class PostConfigEvent extends Event
	{
		public final Configuration config;

		public PostConfigEvent(Configuration config)
		{
			this.config = config;
		}
	}
}

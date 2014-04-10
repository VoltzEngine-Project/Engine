package calclavia.lib.config;

import net.minecraftforge.event.Event;

/**
 * @author tgame14
 * @since 09/04/14
 * 
 */
public class ConfigAnnotationEvent extends Event
{
	public final String sourceClass;

	public ConfigAnnotationEvent(String sourceClass)
	{
		this.sourceClass = sourceClass;
	}
}

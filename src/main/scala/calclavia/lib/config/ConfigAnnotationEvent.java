package calclavia.lib.config;

import net.minecraftforge.event.Event;

/**
 * @author tgame14
 * @since 09/04/14
 * 
 */
public class ConfigAnnotationEvent extends Event
{
	public final Class sourceClass;

	public ConfigAnnotationEvent(Class sourceClass)
	{
		this.sourceClass = sourceClass;
	}
}

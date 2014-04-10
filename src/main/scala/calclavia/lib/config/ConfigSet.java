package calclavia.lib.config;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;

import java.util.HashSet;

/**
 * a Set used to hook into adding an entry in order to post Event in the case of Adding a value late phase.
 *
 * @author tgame14
 * @since 09/04/14
 */
public class ConfigSet extends HashSet<String>
{
	public static boolean isPostInit = false;

	@Override
	public boolean add(String c)
	{
		if (isPostInit)
		{
			Event event = new ConfigAnnotationEvent(c);
			MinecraftForge.EVENT_BUS.post(event);
			System.out.println("Posted ConfigAnnotationEvent \n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");

		}
		return super.add(c);
	}
}

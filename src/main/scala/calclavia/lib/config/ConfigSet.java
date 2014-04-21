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
@Deprecated
public class ConfigSet extends HashSet<String>
{
	@Override
	public boolean add(String c)
	{
		return super.add(c);
	}
}

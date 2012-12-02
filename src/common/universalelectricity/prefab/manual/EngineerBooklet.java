package universalelectricity.prefab.manual;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.Item;
import universalelectricity.core.UniversalElectricity;

/**
 * A booklet that provides the player a nice in-game manual.
 */
public class EngineerBooklet
{
	private static final List<Page> pages = new ArrayList<Page>();

	public static void addPage(Page page)
	{
		pages.add(page);
	}
}

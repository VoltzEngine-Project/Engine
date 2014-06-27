package resonant.lib.utility;

import net.minecraft.potion.Potion;
import resonant.engine.References;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * call on preinit
 *
 * @author tgame14
 * @since 22/02/14
 */
public class PotionUtility
{
	private static final int EXTEND_LIMIT = 32;
	public static int potionOffset;

	public static void resizePotionArray()
	{
		Potion[] headPotionArray = null;

		for (Field f : Potion.class.getDeclaredFields())
		{
			f.setAccessible(true);
			try
			{
				if (f.getName().equals("potionTypes") || f.getName().equals("field_76425_a"))
				{
					Field modfield = Field.class.getDeclaredField("modifiers");
					modfield.setAccessible(true);
					modfield.setInt(f, f.getModifiers() & ~Modifier.FINAL);

					headPotionArray = (Potion[]) f.get(null);
					final Potion[] newPotionTypes = new Potion[headPotionArray.length + EXTEND_LIMIT];
					System.arraycopy(headPotionArray, 0, newPotionTypes, 0, headPotionArray.length);
					f.set(null, newPotionTypes);

					potionOffset = newPotionTypes.length - 1;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				References.LOGGER.error("The mod has Errored, Please report to the mod authors");
			}
		}
	}

	public static int getNextOptimalPotId()
	{
		return --potionOffset + 1;
	}
}

package calclavia.lib.utility;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.potion.Potion;
import calclavia.lib.Calclavia;

/**
 * call on preinit
 *
 * @since 22/02/14
 * @author tgame14
 */
public class PotionUtility
{
	public static final int EXTEND_LIMIT = 32;
	public static int POT_ARRAY_SIZE;

    public static void resizePotionArray ()
    {
        Potion[] resizedPotionArray = null;

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
					POT_ARRAY_SIZE = resizedPotionArray.length + EXTEND_LIMIT;

                    resizedPotionArray = (Potion[]) f.get(null);
                    final Potion[] newPotionTypes = new Potion[resizedPotionArray.length + EXTEND_LIMIT];
                    System.arraycopy(resizedPotionArray, 0, newPotionTypes, 0, resizedPotionArray.length);
                    f.set(null, newPotionTypes);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Calclavia.LOGGER.severe("The mod has Errored, Please report to the mod authors");
            }
        }
    }
}

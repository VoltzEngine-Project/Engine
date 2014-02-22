package calclavia.lib.utility;

import calclavia.components.CalclaviaLoader;
import calclavia.lib.Calclavia;
import net.minecraft.potion.Potion;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * call on preinit
 *
 * @since 22/02/14
 * @author tgame14
 */
public class PotionUtility
{
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

                    resizedPotionArray = (Potion[]) f.get(null);
                    final Potion[] newPotionTypes = new Potion[256];
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

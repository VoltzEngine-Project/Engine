package resonant.lib.utility;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

/** @since 09/03/14
 * @author tgame14 */
public class ModUtility
{
    public static void removeMod(String modid, boolean doRemove)
    {
        if (doRemove)
        {
            Field modField = cpw.mods.fml.relauncher.ReflectionHelper.findField(Loader.class, "mods");
            modField.setAccessible(true);
            Field namedField = cpw.mods.fml.relauncher.ReflectionHelper.findField(Loader.class, "namedMods");
            namedField.setAccessible(true);
            try
            {
                List<ModContainer> mods = (List<ModContainer>) modField.get(Loader.instance());
                Map<String, ModContainer> namedMods = (Map<String, ModContainer>) namedField.get(Loader.instance());

                System.out.println("mods " + mods);
                System.out.println("namedMods " + namedMods);

                ImmutableList.Builder<ModContainer> modContainerBuilder = new ImmutableList.Builder<ModContainer>();

                for (ModContainer container : mods)
                {
                    if (!container.getModId().equals(modid))
                    {
                        modContainerBuilder.add(container);
                    }
                }
                ImmutableList<ModContainer> modContainerList = modContainerBuilder.build();

                ImmutableMap.Builder<String, ModContainer> builder = new ImmutableMap.Builder<String, ModContainer>();

                for (Map.Entry<String, ModContainer> entry : namedMods.entrySet())
                {
                    if (!entry.getKey().equals(modid))
                    {
                        builder.put(entry);
                    }
                }

                ImmutableMap<String, ModContainer> modContainerMap = builder.build();

                modField.set(Loader.instance(), modContainerList);
                namedField.set(Loader.instance(), modContainerMap);

                System.out.println("mods " + modContainerList);
                System.out.println("namedMods " + modContainerMap);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}

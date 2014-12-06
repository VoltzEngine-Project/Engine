package resonant.content;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.tileentity.TileEntity;
import resonant.engine.References;
import resonant.engine.ResonantEngine;
import resonant.lib.network.Synced;
import resonant.lib.network.discriminator.PacketAnnotationManager;
import resonant.lib.utility.ReflectionUtility;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CommonRegistryProxy
{
    public void registerTileEntity(String name, String prefix, Class<? extends TileEntity> clazz)
    {
        GameRegistry.registerTileEntityWithAlternatives(clazz, prefix + name, name);

        try
        {
            //Auto register any tile that contains these annotations
            if (ReflectionUtility.getAllMethods(clazz, Synced.class, Synced.SyncedInput.class, Synced.SyncedOutput.class).size() > 0)
            {
                PacketAnnotationManager.INSTANCE.register(clazz);
            }
        }
        catch(ClassNotFoundException e)
        {
            References.LOGGER.error("Error checking if " + clazz.getSimpleName() + ".class contains @Synced methods this may cause packet update issues");
            if(ResonantEngine.runningAsDev)
            {
                if (e.getMessage().contains("client"))
                {
                    References.LOGGER.error("Hey client only methods exist in this class" + clazz);
                }
                else
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public void registerDummyRenderer(Class<? extends TileEntity> clazz)
    {

    }
}

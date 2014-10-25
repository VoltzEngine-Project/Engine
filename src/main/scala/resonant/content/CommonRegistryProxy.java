package resonant.content;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.tileentity.TileEntity;
import resonant.lib.network.Synced;
import resonant.lib.network.discriminator.PacketAnnotationManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CommonRegistryProxy
{
	public void registerTileEntity(String name, String prefix, Class<? extends TileEntity> clazz)
	{
		GameRegistry.registerTileEntityWithAlternatives(clazz, name, name);
		for (Field field : clazz.getDeclaredFields())
		{
			if (field.isAnnotationPresent(Synced.class))
			{
				PacketAnnotationManager.INSTANCE.register(clazz);
				break;
			}
		}
		for (Method m : clazz.getDeclaredMethods())
		{
			if (m.isAnnotationPresent(Synced.SyncedInput.class) || m.isAnnotationPresent(Synced.SyncedOutput.class))
			{
				PacketAnnotationManager.INSTANCE.register(clazz);
				break;
			}
		}
	}

	public void registerDummyRenderer(Class<? extends TileEntity> clazz)
	{

	}
}

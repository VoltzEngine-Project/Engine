package resonant.lib.modproxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * An interface to handle mods without @Mod annotation Allows them to be turned off in the
 * configuration file easily from the Main "core" @Mod annotated mod
 * <p/>
 * to be removed and replaced as Separate jars are a thing
 *
 * @author tgame14
 * @since 23/02/14
 */
public interface IMod
{
	public void preInit(FMLPreInitializationEvent event);

	public void init(FMLInitializationEvent event);

	public void postInit(FMLPostInitializationEvent event);

}

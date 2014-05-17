package resonant.lib.modproxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/** An interface to handle mods without @Mod annotation Allows them to be turned off in the
 * configuration file easily from the Main "core" @Mod annotated mod
 * 
 * to be removed and replaced as Separate jars are a thing
 * 
 * @since 23/02/14
 * @author tgame14
 */
@Deprecated
public interface IMod
{
    public void preInit(FMLPreInitializationEvent event);

    public void init(FMLInitializationEvent event);

    public void postInit(FMLPostInitializationEvent event);

}

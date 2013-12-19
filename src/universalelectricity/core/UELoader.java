package universalelectricity.core;

import java.util.Map;

import universalelectricity.core.asm.DefaultImplementationTransformer;
import universalelectricity.core.asm.UniversalTransformer;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@Mod(modid = "UniversalElectricity", version = UniversalElectricity.VERSION, name = "Universal Electricity")
@TransformerExclusions({ "universalelectricity.core.asm" })
public class UELoader implements IFMLLoadingPlugin
{	
	@EventHandler
	public void init(FMLPreInitializationEvent evt)
	{
		new Test();
	}

	/**
	 * Return a list of classes that implement the ILibrarySet interface
	 * 
	 * @return a list of classes that implement the ILibrarySet interface
	 */
	@Deprecated
	@Override
	public String[] getLibraryRequestClass()
	{
		return new String[0];
	}

	/**
	 * Return a list of classes that implements the IClassTransformer interface
	 * 
	 * @return a list of classes that implements the IClassTransformer interface
	 */
	@Override
	public String[] getASMTransformerClass()
	{
		return new String[] { UniversalTransformer.class.getName() };
	}

	/**
	 * Return a class name that implements "ModContainer" for injection into the mod list The
	 * "getName" function should return a name that other mods can, if need be, depend on.
	 * Trivially, this modcontainer will be loaded before all regular mod containers, which means it
	 * will be forced to be "immutable" - not susceptible to normal sorting behaviour. All other mod
	 * behaviours are available however- this container can receive and handle normal loading events
	 */
	@Override
	public String getModContainerClass()
	{
		return null;
	}

	/**
	 * Return the class name of an implementor of "IFMLCallHook", that will be run, in the main
	 * thread, to perform any additional setup this coremod may require. It will be run
	 * <strong>prior</strong> to Minecraft starting, so it CANNOT operate on minecraft itself. The
	 * game will deliberately crash if this code is detected to trigger a minecraft class loading
	 * (TODO: implement crash ;) )
	 */
	@Override
	public String getSetupClass()
	{
		return null;
	}

	/**
	 * Inject coremod data into this coremod This data includes: "mcLocation" : the location of the
	 * minecraft directory, "coremodList" : the list of coremods "coremodLocation" : the file this
	 * coremod loaded from,
	 */
	@Override
	public void injectData(Map<String, Object> data)
	{
	}
}
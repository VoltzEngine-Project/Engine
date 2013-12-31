package calclavia.lib;

import java.util.Map;

import net.minecraft.world.MinecraftException;
import calclavia.components.Components;
import calclavia.lib.asm.CalclaviaTransformer;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

/**
 * The FMLLoadingPlugin for Calclavia Core
 * 
 * @author Calclavia
 * 
 */
@Mod(modid = CalclaviaCoreLoader.ID, name = CalclaviaCoreLoader.NAME, version = CalclaviaCoreLoader.VERSION, dependencies = "required-after:UniversalElectricity")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
@TransformerExclusions({ "calclavia.lib.asm" })
public class CalclaviaCoreLoader implements IFMLLoadingPlugin
{
	public static final String ID = "CalclaviaCore";
	public static final String NAME = "Calclavia Core";
	public static String CHANNEL = "calclaviacore";

	public static final String MAJOR_VERSION = "@MAJOR@";
	public static final String MINOR_VERSION = "@MINOR@";
	public static final String REVISION_VERSION = "@REVIS@";
	public static final String BUILD_VERSION = "@BUILD@";
	public static final String VERSION = MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION_VERSION;

	@Mod.Metadata(ID)
	public static ModMetadata metadata;

	public static final String RESOURCE_PATH = "/assets/calclavia/";
	public static final String TEXTURE_DIRECTORY = RESOURCE_PATH + "textures/";
	public static final String BLOCK_TEXTURE_DIRECTORY = TEXTURE_DIRECTORY + "blocks/";
	public static final String ITEM_TEXTURE_DIRECTORY = TEXTURE_DIRECTORY + "items/";
	public static final String MODEL_TEXTURE_DIRECTORY = TEXTURE_DIRECTORY + "models/";

	public static final String DOMAIN = "calclavia";
	public static final String PREFIX = DOMAIN + ":";

	public static final String LANGUAGE_PATH = RESOURCE_PATH + "languages/";
	public static final String[] LANGUAGES_SUPPORTED = new String[] { "en_US", "zh_CN", "es_ES", "it_IT", "nl_NL", "de_DE", "tr_TR", "ru_RU" };

	@EventHandler
	public void init(FMLInitializationEvent evt) throws MinecraftException
	{
		Components.init();
	}

	public String[] getLibraryRequestClass()
	{
		return null;
	}

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[] { CalclaviaTransformer.class.getName() };
	}

	@Override
	public String getModContainerClass()
	{
		return null;
	}

	@Override
	public String getSetupClass()
	{
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data)
	{

	}
}

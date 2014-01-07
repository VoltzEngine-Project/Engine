package calclavia.lib;

import java.io.File;
import java.util.logging.Logger;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Loader;

public class Calclavia
{
	public static final String NAME = "Calclavia";

	public static final String DOMAIN = "calclavia";
	public static final String TEXTURE_NAME_PREFIX = DOMAIN + ":";

	public static final String RESOURCE_DIRECTORY = "/assets/calclavia/";

	public static final String TEXTURE_DIRECTORY = "textures/";
	public static final String GUI_DIRECTORY = TEXTURE_DIRECTORY + "gui/";

	public static final ResourceLocation GUI_EMPTY_FILE = new ResourceLocation(DOMAIN, GUI_DIRECTORY + "gui_empty.png");
	public static final ResourceLocation GUI_COMPONENTS = new ResourceLocation(DOMAIN, GUI_DIRECTORY + "gui_components.png");
	public static final ResourceLocation GUI_BASE = new ResourceLocation(DOMAIN, GUI_DIRECTORY + "gui_base.png");
	/**
	 * The configuration file.
	 */
	public static final Configuration CONFIGURATION = new Configuration(new File(Loader.instance().getConfigDir(), NAME + ".cfg"));
	public static final Logger LOGGER = Logger.getLogger(NAME);

}

package resonant.engine;

import cpw.mods.fml.common.Loader;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * References too common static objects used by Resonant Engine and its sub mods
 */
public final class References
{
	public static final String ID = "ResonantEngine";
	public static final String NAME = "Resonant Engine";
	/**
	 * The configuration file.
	 */
	public static final Configuration CONFIGURATION = new Configuration(new File(Loader.instance().getConfigDir(), NAME + ".cfg"));
	public static final Logger LOGGER = LogManager.getLogger(NAME);

    public static final String MAJOR_VERSION = "@MAJOR@";
	public static final String MINOR_VERSION = "@MINOR@";
	public static final String REVISION_VERSION = "@REVIS@";
	public static final String VERSION = MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION_VERSION;
	public static final String BUILD_VERSION = "@BUILD@";

    public static final String DOMAIN = "resonant";
	public static final String PREFIX = DOMAIN + ":";

    public static final String DIRECTORY = "/assets/" + DOMAIN + "/";
	public static final String CHANNEL = "resonantengine";
	public static final String TEXTURE_DIRECTORY = "textures/";
	public static final String GUI_DIRECTORY = TEXTURE_DIRECTORY + "gui/";

    public static final ResourceLocation GUI_EMPTY_FILE = new ResourceLocation(DOMAIN, GUI_DIRECTORY + "gui_empty.png");
	public static final ResourceLocation GUI_BASE = new ResourceLocation(DOMAIN, GUI_DIRECTORY + "gui_base.png");
	public static final ResourceLocation GUI_COMPONENTS = new ResourceLocation(DOMAIN, GUI_DIRECTORY + "gui_components.png");

    public static final String BLOCK_TEXTURE_DIRECTORY = TEXTURE_DIRECTORY + "blocks/";
	public static final String ITEM_TEXTURE_DIRECTORY = TEXTURE_DIRECTORY + "items/";
	public static final String MODEL_PATH = "models/";
	public static final String MODEL_DIRECTORY = DIRECTORY + MODEL_PATH;
}

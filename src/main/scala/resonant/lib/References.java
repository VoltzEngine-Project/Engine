package resonant.lib;

import java.io.File;
import java.util.logging.Logger;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Loader;

/** References too common static objects used by Resonant Engine and its sub mods */
public class References
{
    public static final String NAME = "Resonant-Engine";

    public static final String DOMAIN = "resonant";
    public static final String TEXTURE_NAME_PREFIX = DOMAIN + ":";

    public static final String RESOURCE_DIRECTORY = "/assets/" + DOMAIN + "/";

    public static final String TEXTURE_DIRECTORY = "textures/";
    public static final String GUI_DIRECTORY = TEXTURE_DIRECTORY + "gui/";

    public static final ResourceLocation GUI_EMPTY_FILE = new ResourceLocation(DOMAIN, GUI_DIRECTORY + "gui_empty.png");
    public static final ResourceLocation GUI_BASE = new ResourceLocation(DOMAIN, GUI_DIRECTORY + "gui_base.png");
    public static final ResourceLocation GUI_COMPONENTS = new ResourceLocation(DOMAIN, GUI_DIRECTORY + "gui_components.png");

    /** The configuration file. */
    public static final Configuration CONFIGURATION = new Configuration(new File(Loader.instance().getConfigDir(), NAME + ".cfg"));
    public static final Logger LOGGER = Logger.getLogger(NAME);

}

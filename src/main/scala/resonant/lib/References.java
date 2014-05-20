package resonant.lib;

import java.io.File;
import java.util.logging.Logger;

import resonant.lib.network.PacketAnnotation;
import resonant.lib.network.PacketTile;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Loader;

/** References too common static objects used by Resonant Engine and its sub mods */
public class References
{
    public static final String NAME = "ResonantEngine";
    
    public static final String MAJOR_VERSION = "@MAJOR@";
    public static final String MINOR_VERSION = "@MINOR@";
    public static final String REVISION_VERSION = "@REVIS@";
    public static final String VERSION = MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION_VERSION;
    public static final String BUILD_VERSION = "@BUILD@";

    public static final String DOMAIN = "resonant";
    public static final String PREFIX = DOMAIN + ":";
    public static final String CHANNEL = "resonantengine";
    public static final String DIRECTORY = "/assets/" + DOMAIN + "/";
    public static final String TEXTURE_DIRECTORY = "textures/";
    public static final String GUI_DIRECTORY = TEXTURE_DIRECTORY + "gui/";
    public static final String BLOCK_TEXTURE_DIRECTORY = TEXTURE_DIRECTORY + "blocks/";
    public static final String ITEM_TEXTURE_DIRECTORY = TEXTURE_DIRECTORY + "items/";
    public static final String MODEL_PATH = "models/";
    public static final String MODEL_DIRECTORY = DIRECTORY + MODEL_PATH;
    public static final String LANGUAGE_DIRECTORY = DIRECTORY + "languages/";
    public static final String[] LANGUAGES = new String[] { "en_US", "zh_CN", "es_ES", "it_IT", "nl_NL", "de_DE", "tr_TR", "ru_RU", "pl_PL", "et_EE" };
    
    public static final PacketTile PACKET_TILE = new PacketTile(References.CHANNEL);
    public static final PacketAnnotation PACKET_ANNOTATION = new PacketAnnotation(References.CHANNEL); 
    
    public static final ResourceLocation GUI_EMPTY_FILE = new ResourceLocation(DOMAIN, GUI_DIRECTORY + "gui_empty.png");
    public static final ResourceLocation GUI_BASE = new ResourceLocation(DOMAIN, GUI_DIRECTORY + "gui_base.png");
    public static final ResourceLocation GUI_COMPONENTS = new ResourceLocation(DOMAIN, GUI_DIRECTORY + "gui_components.png");

    /** The configuration file. */
    public static final Configuration CONFIGURATION = new Configuration(new File(Loader.instance().getConfigDir(), NAME + ".cfg"));
    public static final Logger LOGGER = Logger.getLogger(NAME);

}

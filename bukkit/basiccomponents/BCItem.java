package basiccomponents;

import net.minecraft.server.Item;
import net.minecraft.server.ModLoader;
import forge.ITextureProvider;


/**
 * The Class BCItem.
 */
public class BCItem extends Item implements ITextureProvider
{
    
    /** The texture file. */
    public static String textureFile = "/basiccomponents/textures/items.png";

    /**
     * Instantiates a new bC item.
     *
     * @param s the s
     * @param i the i
     * @param j the j
     */
    public BCItem(String s, int i, int j)
    {
        super(i);
        textureId = j;
        a(s);
        ModLoader.addName(this, s);
    }

    /* (non-Javadoc)
     * @see net.minecraft.server.Item#getTextureFile()
     */
    public String getTextureFile()
    {
        return textureFile;
    }
}

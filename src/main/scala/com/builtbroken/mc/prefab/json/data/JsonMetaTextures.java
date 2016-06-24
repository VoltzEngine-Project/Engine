package com.builtbroken.mc.prefab.json.data;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

/**
 * Stores texture data read from a basic block state file
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/24/2016.
 */
public class JsonMetaTextures extends JsonData<IIcon[]>
{
    private String[] textures;

    public JsonMetaTextures()
    {
        setObject(new IIcon[16]); //TODO reduce array size based on data
        textures = new String[16];
    }

    public void setTextureName(int index, String name)
    {
        textures[index] = name;
    }

    public IIcon getTexture(int meta)
    {
        return getObject() != null && meta >= 0 && meta < 16 ? getObject()[meta] : null;
    }

    public void registerIcons(IIconRegister reg)
    {
        for (int i = 0; i < textures.length; i++)
        {
            //TODO validate texture
            //TODO check for missing textures
            if (textures[i] != null)
            {
                getObject()[i] = reg.registerIcon(textures[i]);
            }
        }
    }

}

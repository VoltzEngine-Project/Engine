package com.builtbroken.mc.client.json.render.block;

import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.render.state.RenderState;
import com.builtbroken.mc.client.json.texture.TextureData;
import net.minecraft.util.IIcon;

import java.util.List;

/** Simple textured block
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/4/2017.
 */
public class RenderStateTexturedBlock extends RenderState
{
    public final String[] textureIDs = new String[6];

    public RenderStateTexturedBlock(String id)
    {
        super(id);
    }

    @Override
    public IIcon getIcon(int side)
    {
        TextureData textureData = getTextureData(side);
        if (textureData != null && textureData.getIcon() != null)
        {
            return textureData.getIcon();
        }
        return null;
    }

    @Override
    public TextureData getTextureData(int side)
    {
        if (side >= 0 && side < 6)
        {
            return textureIDs[side] != null ? ClientDataHandler.INSTANCE.getTexture(textureIDs[side]) : null;
        }

        if (parentState != null)
        {
            return parentState.getTextureData(side);
        }
        return getTextureData(0);
    }

    @Override
    public void addDebugLines(List<String> lines)
    {
        super.addDebugLines(lines);
        int i = 0;
        lines.add("Textures");
        for (String s : textureIDs)
        {
            lines.add("  [" + i++ + "] = " + s);
        }
    }

    @Override
    public String toString()
    {
        return "RenderStateBlock[" + id + "]@" + hashCode();
    }
}

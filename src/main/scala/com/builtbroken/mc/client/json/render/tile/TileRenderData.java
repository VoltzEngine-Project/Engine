package com.builtbroken.mc.client.json.render.tile;

import com.builtbroken.mc.client.json.render.RenderData;
import com.builtbroken.mc.lib.json.imp.IJsonProcessor;
import net.minecraft.tileentity.TileEntity;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/4/2017.
 */
public class TileRenderData extends RenderData
{
    public Class<? extends TileEntity> tileClass;
    public TileRenderData(IJsonProcessor processor, String contentID, String type)
    {
        super(processor, contentID, type);
    }
}

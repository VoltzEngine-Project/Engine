package com.builtbroken.mc.client;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.render.model.loader.EngineModelLoader;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;

/**
 * Stores all assets shared between several mods produced by Built Broken Modding
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/13/2016.
 */
public final class SharedAssets
{
    public static IModelCustom TOOL_TABLE;

    public static ResourceLocation TOOL_TABLE_TEXTURE;

    private static boolean loaded = false;

    public static void loadModels()
    {
        if(!loaded)
        {
            loaded = true;
            TOOL_TABLE = model("tool-table.obj");

            TOOL_TABLE_TEXTURE = texture("tool-table");
        }
    }

    public static IModelCustom model(String name)
    {
        return EngineModelLoader.loadModel(new ResourceLocation(References.DOMAIN, References.MODEL_PATH + name));
    }

    public static ResourceLocation texture(String name)
    {
        return new ResourceLocation(References.DOMAIN, "textures/models/" + name + ".png");
    }
}

package com.builtbroken.mc.client;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.render.model.loader.EngineModelLoader;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.ModelFormatException;

/**
 * Stores all assets shared between several mods produced by Built Broken Modding
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/13/2016.
 */
public final class SharedAssets
{
    /** Model for the tool table */
    public static IModelCustom TOOL_TABLE;
    /** Texture for the tool table */
    public static ResourceLocation TOOL_TABLE_TEXTURE;

    /** Simple grey texture to use as a place holder */
    public static ResourceLocation GREY_TEXTURE = new ResourceLocation(References.DOMAIN, References.TEXTURE_DIRECTORY + "grey.png");
    /** Simple grey texture to use as a place holder, has a 40%~ish occupancy to work with alpha objects better */
    public static ResourceLocation GREY_TEXTURE_40pAlpha  = new ResourceLocation(References.DOMAIN, References.TEXTURE_DIRECTORY + "40%grey.png");

    //TODO java docs, TODO use texture() method, TODO move to loadResources() method
    public static final ResourceLocation GUI_EMPTY_FILE = new ResourceLocation(References.DOMAIN, References.GUI_DIRECTORY + "gui_empty.png");
    public static final ResourceLocation GUI_BASE = new ResourceLocation(References.DOMAIN, References.GUI_DIRECTORY + "gui_base.png");
    public static final ResourceLocation GUI__MC_EMPTY_FILE = new ResourceLocation(References.DOMAIN, References.GUI_DIRECTORY + "mc_base_empty.png");
    public static final ResourceLocation GUI_MC_BASE = new ResourceLocation(References.DOMAIN, References.GUI_DIRECTORY + "mc_base.png");
    public static final ResourceLocation GUI_COMPONENTS = new ResourceLocation(References.DOMAIN, References.GUI_DIRECTORY + "gui_components.png");


    private static boolean loaded = false;

    /**
     * Called to load the models and textures
     * <p>
     * Can only be called once
     */
    public static void loadResources()
    {
        if (!loaded)
        {
            loaded = true;
            TOOL_TABLE = model("tool-table.obj");

            TOOL_TABLE_TEXTURE = texture("tool-table");
        }
    }

    /**
     * Loads a model from system
     *
     * @param name - name of the model file, can include path as well
     * @return model as an IModeCustom instance
     * @throws ModelFormatException - if model is missing or formatted in corrrectly
     */
    public static IModelCustom model(String name)
    {
        return EngineModelLoader.loadModel(new ResourceLocation(References.DOMAIN, References.MODEL_PATH + name));
    }

    /**
     * Creates a resource location for a texture
     * <p>
     * Does not check if texture exists
     *
     * @param path - path to texture parent folder
     * @param name - name of texture, can include paths as well
     * @return resource location
     */
    public static ResourceLocation texture(String path, String name)
    {
        return new ResourceLocation(References.DOMAIN, path + name + ".png");
    }

    /**
     * Creates a resource location for a texture
     *
     * @param name
     * @return
     */
    public static ResourceLocation texture(String name)
    {
        return texture("textures/models/", name);
    }
}

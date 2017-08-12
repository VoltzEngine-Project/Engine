package com.builtbroken.mc.lib.render.model.loader;

import java.util.Map;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.render.model.FixedTechneModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.IModelCustomLoader;
import net.minecraftforge.client.model.ModelFormatException;
import net.minecraftforge.client.model.obj.ObjModelLoader;

import com.google.common.collect.Maps;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Modified version of cpw's AdvancedModelLoader
 *
 */
@SideOnly(Side.CLIENT)
public class EngineModelLoader
{
    private static Map<String, IModelCustomLoader> instances = Maps.newHashMap();

    static
    {
        registerModelHandler(new ObjModelLoader());
        registerModelHandler(new FixedTechneModelLoader());
    }

    /**
     * Register a new model handler
     * @param modelHandler The model handler to register
     */
    public static void registerModelHandler(IModelCustomLoader modelHandler)
    {
        for (String suffix : modelHandler.getSuffixes())
        {
            instances.put(suffix, modelHandler);
        }
    }

    /**
     * Load the model from the supplied classpath resolvable resource name
     * @param resource The resource name
     * @return A model
     * @throws IllegalArgumentException if the resource name cannot be understood
     * @throws ModelFormatException if the underlying model handler cannot parse the model format
     */
    public static IModelCustom loadModel(ResourceLocation resource) throws IllegalArgumentException, ModelFormatException
    {
        String name = resource.getResourcePath();
        int i = name.lastIndexOf('.');
        if (i == -1)
        {
            Engine.logger().error("The resource name %s is not valid", resource);
            throw new IllegalArgumentException("The resource name is not valid");
        }
        String suffix = name.substring(i+1);
        IModelCustomLoader loader = instances.get(suffix);
        if (loader == null)
        {
            Engine.logger().error("The resource name %s is not supported", resource);
            throw new IllegalArgumentException("The resource name is not supported");
        }

        return loader.loadInstance(resource);
    }

    public static FixedTechneModel loadTechneModel(ResourceLocation resource)
    {
        IModelCustom model = loadModel(resource);
        if(!(model instanceof FixedTechneModel))
        {
            Engine.logger().error("The resource name %s is not a techne model", resource);
            throw new IllegalArgumentException("The resource name is not supported");
        }
        return (FixedTechneModel) model;
    }
}
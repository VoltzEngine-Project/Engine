package com.builtbroken.mc.client.json.render.mc;

import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.imp.IModelState;
import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.client.json.render.RenderData;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

import java.io.IOException;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/29/2017.
 */
public class VoltzEngineModelLoader implements ICustomModelLoader
{
    public final String domain;

    public VoltzEngineModelLoader(String domain)
    {
        this.domain = domain;
    }

    @Override
    public boolean accepts(ResourceLocation modelLocation)
    {
        if(modelLocation.getResourceDomain().equals(domain) && modelLocation.getResourcePath().startsWith("json_"))
        {
            return true;
        }
        return false;
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws IOException
    {
        //How this works is to fake out a model per location
        //Each location represents a model set and model state from the old 1.7.10 JSON system
        //TO make it work in the newer version a fake key is created to reference the location
        //Then a model is created with this key to allow a look up of the content ID and state during baking
        //This is a work around for not being able to get the transform type and other data needed to get the renderStateKey

        //Key examples
        // string >> json_domain/contentID/renderStateKey/keyPart1/keyPart2..../keyPart(n)
        // json_icbm/missile/item/inventory

        //Remove json_ front
        String path = modelLocation.getResourcePath().replaceFirst("json_", "");

        //lower case since we do not care about case anyways
        path = path.toLowerCase();

        //Split by seperator
        String[] split = path.split("/");

        //Build content ID renderStateKey
        String contentID = split[0] + ":" + split[1];

        //Build state renderStateKey
        String renderStateKey = "";
        for(int i = 2; i < split.length; i++)
        {
            renderStateKey += split[i];
            if(i != split.length - 1)
            {
                renderStateKey += ".";
            }
        }

        //Get data
        RenderData data = ClientDataHandler.INSTANCE.getRenderData(contentID);

        if(data == null)
        {
            throw new RuntimeException("VoltzEngineModelLoader: Couldn't find render data for contentID = " + contentID + " while parsing model renderStateKey " + modelLocation);
        }

        IRenderState state = data.getState(renderStateKey);
        if(state == null)
        {
            throw new RuntimeException("VoltzEngineModelLoader: Couldn't find render data for contentID = " + contentID + " while parsing model renderStateKey " + modelLocation);
        }

        if(!(state instanceof IModelState))
        {
            return new ModelVEItem(data, state);
        }
        //TODO handle models
        return null;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        //TODO load resources from resource pack?
    }
}

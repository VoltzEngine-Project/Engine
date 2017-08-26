package com.builtbroken.mc.client.json.models;

import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.processors.JsonGenData;
import net.minecraft.client.renderer.block.model.IBakedModel;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/23/2016.
 */
public class ModelData extends JsonGenData
{
    String key;
    String domain;
    String name;
    IBakedModel model;

    private boolean errorLock = false;
    private int errors = 0;
    private long lastError;

    public ModelData(IJsonProcessor processor, String key, String domain, String name)
    {
        super(processor);
        this.key = key;
        this.domain = domain;
        this.name = name;
    }

    @Override
    public void register()
    {
        ClientDataHandler.INSTANCE.addModel(key, this);
    }

    @Override
    public String getContentID()
    {
        return key;
    }

    @Override
    public String toString()
    {
        return "ModelData[" + key + " >> " + domain + ":" + name + " >> " + model + "]@" + hashCode();
    }

    public boolean isValid()
    {
        return model != null;
    }

    public void render(boolean renderOnlyParts, String[] partsToRender)
    {

    }
}

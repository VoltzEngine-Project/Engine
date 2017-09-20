package com.builtbroken.mc.client.json.models;

import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.debug.IJsonDebugDisplay;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.processors.JsonGenData;
import com.builtbroken.mc.lib.render.RenderUtility;
import com.builtbroken.mc.lib.render.model.loader.EngineModelLoader;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;

import java.awt.*;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/23/2016.
 */
public class ModelData extends JsonGenData implements IJsonDebugDisplay
{
    String key;
    String domain;
    String name;
    IModelCustom model;

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

    public IModelCustom getModel()
    {
        if (model == null && !errorLock)
        {
            try
            {
                model = EngineModelLoader.loadModel(new ResourceLocation(domain, "models/" + name));
            }
            catch (Exception e)
            {
                e.printStackTrace();
                errorLock = true;
            }
        }
        return model;
    }

    public void render(boolean renderOnlyParts, String... parts)
    {
        if (!errorLock)
        {
            try
            {
                if (parts != null)
                {
                    if (renderOnlyParts)
                    {
                        model.renderOnly(parts);
                    }
                    else
                    {
                        model.renderAllExcept(parts);
                    }
                }
                else
                {
                    model.renderAll();
                }
            }
            catch (Exception e)
            {
                //TODO implement model reload to fix errors
                Engine.logger().error("Failed to render model " + this);

                //Tick to disable
                long time = System.nanoTime();
                if (time - lastError > 10000)
                {
                    lastError = time;
                    if (errors++ >= 5)
                    {
                        errorLock = true;
                    }
                }
                else
                {
                    errors = 0;
                }
            }
        }
        else if (Engine.runningAsDev)
        {
            RenderUtility.renderFloatingText("ErrorLocked: " + this, 0, 0, 0, Color.red.getRGB());
        }
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
    public String getUniqueID()
    {
        return name; //TODO adjust to use key
    }

    @Override
    public String getMod()
    {
        return domain; //TODO adjust to use key
    }

    @Override
    public String getDisplayName()
    {
        return getContentID();
    }

    @Override
    public void addDebugLines(List<String> lines)
    {
        lines.add("Model = " + getModel());
        lines.add("Domain = " + domain);
        lines.add("Path = " + name);
    }

    @Override
    public String toString()
    {
        return "ModelData[" + key + " >> " + domain + ":" + name + " >> " + model + "]@" + hashCode();
    }
}

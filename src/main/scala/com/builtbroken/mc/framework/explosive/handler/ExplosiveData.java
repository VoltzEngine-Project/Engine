package com.builtbroken.mc.framework.explosive.handler;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.framework.explosive.ExplosiveRegistry;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.loading.JsonProcessorData;
import com.builtbroken.mc.framework.json.processors.JsonGenData;

import java.lang.reflect.InvocationTargetException;

/**
 * Used to register a new explosive used to trigger a blast. This is not an item registry but a handler registry system.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/10/2017.
 */
public class ExplosiveData extends JsonGenData
{
    @JsonProcessorData(value = "id", required = true)
    public String id;

    @JsonProcessorData(value = "name", required = true)
    public String translationKey;

    @JsonProcessorData(value = "handler", required = true)
    public String blastHandlerClass;

    @JsonProcessorData(value = "powerMultiplier", type = "int")
    public int multiplier = 1;

    public ExplosiveData(IJsonProcessor processor)
    {
        super(processor);
    }

    @Override
    public void register()
    {
        try
        {
            Class clazz = Class.forName(blastHandlerClass);
            if (ExplosiveHandler.class.isAssignableFrom(clazz))
            {
                ExplosiveHandler handler = (ExplosiveHandler) clazz.getConstructor(ExplosiveData.class).newInstance(this);
                if (ExplosiveRegistry.registerExplosive(modID, id, handler))
                {
                    Engine.logger().info("ExplosiveData: Registered explosive '" + id + "' with handler '" + blastHandlerClass + "' successfully");
                }
                else
                {
                    throw new RuntimeException("ExplosiveData#register() - failed to register explosive '" + id + "' with handler '" + blastHandlerClass + "'");
                }
            }
            else
            {
                throw new RuntimeException("ExplosiveData#register() - class for '" + blastHandlerClass + "' is not an sub class of ExplosiveHandler.class");
            }
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException("ExplosiveData#register() - failed to locate class for '" + blastHandlerClass + "'", e);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException("ExplosiveData#register() - failed to access constructor for '" + blastHandlerClass + "'", e);
        }
        catch (InstantiationException e)
        {
            throw new RuntimeException("ExplosiveData#register() - failed to instantiate constructor for '" + blastHandlerClass + "'", e);
        }
        catch (NoSuchMethodException e)
        {
            throw new RuntimeException("ExplosiveData#register() - failed to locate constructor for '" + blastHandlerClass + "'", e);
        }
        catch (InvocationTargetException e)
        {
            throw new RuntimeException("ExplosiveData#register() - failed to invoke constructor for '" + blastHandlerClass + "'", e);
        }

    }

    @Override
    public String getContentID()
    {
        return id;
    }

    @Override
    public String getUniqueID()
    {
        return id;
    }
}

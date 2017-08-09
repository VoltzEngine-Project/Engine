package com.builtbroken.mc.framework.json.override;

import com.builtbroken.mc.core.registry.implement.ILoadComplete;
import com.builtbroken.mc.framework.json.JsonContentLoader;
import com.builtbroken.mc.framework.json.imp.IJsonGenObject;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.processors.JsonGenData;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/14/2017.
 */
public class JsonOverrideData extends JsonGenData implements ILoadComplete
{
    private String contentID;
    private String processorID;
    private String action;
    private JsonElement data;

    public JsonOverrideData(IJsonProcessor processor, String contentID, String processorID, String action, JsonElement data)
    {
        super(processor);
        this.contentID = contentID.toLowerCase();
        this.processorID = processorID.toLowerCase();
        this.action = action.toLowerCase();
        this.data = data;
    }

    @Override
    public void onLoadCompleted()
    {
        IJsonProcessor processor = JsonContentLoader.INSTANCE.processors.get(processorID);
        if (processor != null)
        {
            if (processor instanceof IModifableJson)
            {
                if (JsonContentLoader.INSTANCE.generatedObjects.get(processorID) != null)
                {
                    IJsonGenObject genObject = null;
                    for (IJsonGenObject obj : JsonContentLoader.INSTANCE.generatedObjects.get(processorID))
                    {
                        if (obj != null && contentID.equalsIgnoreCase(obj.getContentID()))
                        {
                            genObject = obj;
                            break;
                        }
                    }

                    if (genObject != null)
                    {
                        if ("add".equals(action))
                        {
                            if (data instanceof JsonObject)
                            {
                                for (Map.Entry<String, JsonElement> entry : ((JsonObject) data).entrySet())
                                {
                                    ((IModifableJson) processor).addData(entry.getKey(), entry.getValue(), genObject);
                                }
                            }
                            else
                            {
                                throw new IllegalArgumentException("Data for add action must used a json object for providing additions");
                            }
                        }
                        else if ("remove".equals(action))
                        {
                            if (data instanceof JsonArray)
                            {
                                //TODO do later
                            }
                            else
                            {
                                throw new IllegalArgumentException("Data for remove action must used a json array for providing additions");
                            }
                        }
                        else if ("replace".equals(action))
                        {
                            if (data instanceof JsonObject)
                            {
                                for (Map.Entry<String, JsonElement> entry : ((JsonObject) data).entrySet())
                                {
                                    ((IModifableJson) processor).replaceData(entry.getKey(), entry.getValue(), genObject);
                                }
                            }
                            else
                            {
                                throw new IllegalArgumentException("Data for replace action must used a json object for providing additions");
                            }
                        }
                        else
                        {
                            throw new IllegalArgumentException("Unknown action type[" + action + "] for override data");
                        }
                    }
                    else
                    {
                        throw new IllegalArgumentException("Could not find content by ID[" + contentID + "] in list from processor[" + processorID + "]");
                    }
                }
                else
                {
                    throw new IllegalArgumentException("No generated objects listed for processor[" + processorID + "] to find content by ID[" + contentID + "]");
                }
            }
            else
            {
                throw new IllegalArgumentException("Processor[" + processorID + "] does not support modifying json data");
            }
        }
        else
        {
            throw new IllegalArgumentException("Unknown processor[" + processorID + "] for override data");
        }
    }

    @Override
    public String getContentID()
    {
        return null;
    }
}

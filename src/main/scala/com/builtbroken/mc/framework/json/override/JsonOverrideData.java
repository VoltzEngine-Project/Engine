package com.builtbroken.mc.framework.json.override;

import com.builtbroken.mc.core.registry.implement.ILoadComplete;
import com.builtbroken.mc.framework.json.JsonContentLoader;
import com.builtbroken.mc.framework.json.imp.IJsonGenObject;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.processors.JsonGenData;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Iterator;
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
        //Get processor for type
        IJsonProcessor processor = JsonContentLoader.INSTANCE.processors.get(processorID);
        if (processor != null)
        {
            //Ensure processor supports modifying json data
            if (processor instanceof IModifableJson)
            {
                //Get content list for processor
                if (JsonContentLoader.INSTANCE.generatedObjects.get(processorID) != null)
                {
                    //Find object
                    IJsonGenObject genObject = null;
                    for (IJsonGenObject obj : JsonContentLoader.INSTANCE.generatedObjects.get(processorID))
                    {
                        if (obj != null && contentID.equalsIgnoreCase(obj.getContentID()))
                        {
                            genObject = obj;
                            break;
                        }
                    }

                    //If object is not null, apply changes
                    if (genObject != null)
                    {
                        //Add data to JSON
                        if ("add".equals(action))
                        {
                            if (data instanceof JsonObject)
                            {
                                removeComments((JsonObject) data);
                                for (Map.Entry<String, JsonElement> entry : ((JsonObject) data).entrySet())
                                {
                                    ((IModifableJson) processor).addData(entry.getKey(), entry.getValue(), genObject);
                                }
                            }
                            else
                            {
                                throw new IllegalArgumentException("Data for add action must use a json object for providing additions");
                            }
                        }
                        //Remove data from JSON, aka reset to default or disable feature
                        else if ("remove".equals(action))
                        {
                            if (data instanceof JsonArray)
                            {
                                //TODO clean up data before processing aka remove _comments
                                //TODO do later
                            }
                            else
                            {
                                throw new IllegalArgumentException("Data for remove action must use a json array for providing changes");
                            }
                        }
                        //Edit json data
                        else if ("edit".equals(action))
                        {
                            if (data instanceof JsonObject)
                            {
                                removeComments((JsonObject) data);
                                for (Map.Entry<String, JsonElement> entry : ((JsonObject) data).entrySet())
                                {
                                    ((IModifableJson) processor).replaceData(entry.getKey(), entry.getValue(), genObject);
                                }
                            }
                            else
                            {
                                throw new IllegalArgumentException("Data for edit action must use a json object for providing changes");
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

    protected void removeComments(JsonObject object)
    {
        Iterator<Map.Entry<String, JsonElement>> it = object.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<String, JsonElement> entry = it.next();
            if (entry.getKey().startsWith("_"))
            {
                it.remove();
            }
        }
    }

    @Override
    public String getContentID()
    {
        return null;
    }

    @Override
    public String getUniqueID()
    {
        return null;
    }
}

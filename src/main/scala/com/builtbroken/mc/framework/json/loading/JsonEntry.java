package com.builtbroken.mc.framework.json.loading;

import com.google.gson.JsonElement;

/**
 * Used to store loaded entries during sorting
 */
public class JsonEntry
{
    /** Name of the entry type, used for sorting */
    public final String jsonKey;
    /** Element entry that goes with the name key */
    public final JsonElement element;
    /** File the entry was created from */
    public final String fileReadFrom;

    /** Who create the entry in the file */
    public String author;
    /** Where the error can be reported if the file fails to read */
    public String authorHelpSite;

    public JsonEntry(String jsonKey, String fileReadFrom, JsonElement element)
    {
        this.jsonKey = jsonKey;
        this.fileReadFrom = fileReadFrom;
        this.element = element;
    }

    @Override
    public String toString()
    {
        return jsonKey + "[" + element + "]";
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof JsonEntry)
        {
            return jsonKey.equals(((JsonEntry) object).jsonKey) && element.equals(((JsonEntry) object).element);
        }
        return false;
    }
    //TODO add hashcode
}
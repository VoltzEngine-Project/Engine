package com.builtbroken.mc.framework.json.struct;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.framework.json.imp.IJsonKeyDataProvider;
import com.builtbroken.mc.framework.json.processors.JsonProcessor;
import com.builtbroken.mc.framework.json.settings.JsonSettingData;
import com.builtbroken.mc.framework.json.settings.data.JsonSettingBoolean;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cpw.mods.fml.common.Loader;

import java.util.Map;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/13/2017.
 */
public class JsonConditional
{
    public static boolean isConditionalTrue(JsonElement element, Object dataProvider)
    {
        if (element.isJsonPrimitive())
        {
            String value = element.getAsString();
            //TODO find a better way to handle conditions that are prefixed with "type@value"
            if (value.startsWith("setting@"))
            {
                final String key = value.substring(8, value.length());
                JsonSettingData data = Engine.getSetting(key);
                if (data instanceof JsonSettingBoolean)
                {
                    return data.getBoolean();
                }
                return false;
            }
            else if (value.startsWith("mod@"))
            {
                String modName = value.substring(4, value.length());
                if (!Loader.isModLoaded(modName))
                {
                    return false;
                }
            }
            else if (value.startsWith("if"))
            {
                //TODO parse
                //Make sure to remove spaces
                //case 1 field==value
                //case 2 field==[value something something]
                //case 3 field!=value
            }
            else if (value.equalsIgnoreCase("true"))
            {
                return true;
            }
            else if (value.equalsIgnoreCase("false"))
            {
                return false;
            }
            else if (value.equalsIgnoreCase("dev") || value.equalsIgnoreCase("devMode"))
            {
                return Engine.runningAsDev;
            }
        }
        else if (element.isJsonObject())
        {
            JsonObject conditional = element.getAsJsonObject();
            //Array of checks
            if (conditional.has("IfAllTrue"))
            {
                JsonArray array = conditional.getAsJsonArray("IfAllTrue");
                for (JsonElement e : array)
                {
                    boolean result = isConditionalTrue(e, dataProvider);
                    if (!result)
                    {
                        return false;
                    }
                }
            }
            //Single check
            else if (conditional.has("type"))
            {
                JsonProcessor.ensureValuesExist(conditional, "field", "check");
                boolean expectedResult = true;
                if (conditional.has("result"))
                {
                    expectedResult = conditional.get("result").getAsBoolean();
                }

                String type = conditional.getAsJsonPrimitive("type").getAsString();
                String field = conditional.getAsJsonPrimitive("field").getAsString();
                String check = conditional.getAsJsonPrimitive("check").getAsString(); //TODO consider allowing non-string values

                Object value = get(field, dataProvider);

                if (type.equalsIgnoreCase("is"))
                {
                    return expectedResult == is(check, value);
                }
                else if (type.equalsIgnoreCase("isNot"))
                {
                    return expectedResult == isNot(check, value);
                }

            }
        }
        else if (element.isJsonArray())
        {
            //TODO ?
        }
        return false;
    }

    protected static Object get(String field, Object dataProvider)
    {
        if (dataProvider instanceof IJsonKeyDataProvider)
        {
            return ((IJsonKeyDataProvider) dataProvider).getJsonKeyData(field);
        }
        else if (dataProvider instanceof Map)
        {
            return ((Map) dataProvider).get(field);
        }
        return null;
    }

    protected static boolean is(String check, Object object)
    {
        if (object == null || check.isEmpty())
        {
            return false;
        }
        if (object instanceof String)
        {
            return ((String) object).equalsIgnoreCase(check);
        }
        else if (object instanceof Integer)
        {
            try
            {
                int i = Integer.parseInt(check);
                return i == (int) object;
            }
            catch (NumberFormatException e)
            {
                return false;
            }
        }
        else if (object instanceof Byte)
        {
            try
            {
                byte i = Byte.parseByte(check);
                return i == (byte) object;
            }
            catch (NumberFormatException e)
            {
                return false;
            }
        }
        else if (object instanceof Double)
        {
            try
            {
                double i = Double.parseDouble(check);
                return i == (double) object;
            }
            catch (NumberFormatException e)
            {
                return false;
            }
        }
        else if (object instanceof Float)
        {
            try
            {
                float i = Float.parseFloat(check);
                return i == (float) object;
            }
            catch (NumberFormatException e)
            {
                return false;
            }
        }
        else if (object instanceof Long)
        {
            try
            {
                long i = Long.parseLong(check);
                return i == (long) object;
            }
            catch (NumberFormatException e)
            {
                return false;
            }
        }
        String toString = object.toString();
        if (toString.equalsIgnoreCase(check))
        {
            return true;
        }
        return false;
    }

    protected static boolean isNot(String check, Object object)
    {
        return !is(check, object);
    }
}

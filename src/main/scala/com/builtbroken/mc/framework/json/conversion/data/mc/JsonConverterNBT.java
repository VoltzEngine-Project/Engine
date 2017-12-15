package com.builtbroken.mc.framework.json.conversion.data.mc;

import com.builtbroken.mc.framework.json.conversion.JsonConverter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.*;

import java.util.Map;
import java.util.Set;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/12/2017.
 */
public class JsonConverterNBT extends JsonConverter<NBTTagCompound>
{
    public JsonConverterNBT()
    {
        super("nbt");
    }

    @Override
    public NBTTagCompound convert(JsonElement element, String... args)
    {
        return handle(element);
    }

    @Override
    public JsonElement build(String type, Object data, String... args)
    {
        if(data instanceof NBTTagCompound)
        {
            return toJson((NBTTagCompound) data);
        }
        return null;
    }

    public static NBTTagCompound handle(JsonElement element)
    {
        NBTTagCompound nbt = new NBTTagCompound();
        if (element instanceof JsonObject)
        {
            handle((JsonObject) element, nbt, 0);
        }
        return nbt;
    }

    public static void handle(JsonObject object, NBTTagCompound nbt, int depth)
    {
        for (Map.Entry<String, JsonElement> entry : object.entrySet())
        {
            final String entryKey = entry.getKey();
            //Objects are considered nested NBT structures
            if (entry.getValue() instanceof JsonObject)
            {
                NBTTagCompound tag = new NBTTagCompound();
                handle((JsonObject) entry.getValue(), tag, depth++); //TODO add depth limit
                nbt.setTag(entryKey, tag);
            }
            //Primitives are data points
            else if (entry.getValue() instanceof JsonPrimitive)
            {
                JsonPrimitive primitive = (JsonPrimitive) entry.getValue();
                if (primitive.isNumber())
                {
                    //Separate out key from data type
                    final String[] split = entryKey.split(":"); //TODO error if more than two
                    final String key = split[0];
                    final String type = split[1].toLowerCase();

                    //Match type to nbt structure
                    if (type.equals("int") || type.equals("integer"))
                    {
                        nbt.setInteger(key, primitive.getAsInt());
                    }
                    else if (type.equals("byte"))
                    {
                        nbt.setByte(key, primitive.getAsByte());
                    }
                    else if (type.equals("short"))
                    {
                        nbt.setShort(key, primitive.getAsShort());
                    }
                    else if (type.equals("double"))
                    {
                        nbt.setDouble(key, primitive.getAsDouble());
                    }
                    else if (type.equals("float"))
                    {
                        nbt.setFloat(key, primitive.getAsFloat());
                    }
                    else if (type.equals("long"))
                    {
                        nbt.setLong(key, primitive.getAsLong());
                    }
                    else
                    {
                        throw new RuntimeException("Unknown number type for " + type + " while reading " + object);
                    }
                }
                else if (primitive.isBoolean())
                {
                    nbt.setBoolean(entry.getKey(), primitive.getAsBoolean());
                }
                else if (primitive.isString())
                {
                    nbt.setString(entry.getKey(), primitive.getAsString());
                }
            }
            else if (entry.getValue() instanceof JsonArray)
            {
                JsonArray array = (JsonArray) entry.getValue();
                if (array.size() > 0)
                {
                    JsonElement element = array.get(0);
                    if (element instanceof JsonPrimitive)
                    {
                        String[] split = entry.getKey().split(":");
                        String key = split[0];
                        String type = split[1].toLowerCase();
                        if (type.equals("int") || type.equals("integer"))
                        {
                            int[] ar = new int[array.size()];
                            for (int i = 0; i < array.size(); i++)
                            {
                                JsonPrimitive p = array.get(i).getAsJsonPrimitive();
                                ar[i] = p.getAsInt();
                            }
                            nbt.setIntArray(key, ar);
                        }
                        else if (type.equals("byte"))
                        {
                            byte[] ar = new byte[array.size()];
                            for (int i = 0; i < array.size(); i++)
                            {
                                JsonPrimitive p = array.get(i).getAsJsonPrimitive();
                                ar[i] = p.getAsByte();
                            }
                            nbt.setByteArray(key, ar);
                        }
                        else
                        {
                            throw new RuntimeException("Unsupported type of " + type + " for array read");
                        }
                    }
                    else if (element instanceof JsonObject)
                    {
                        NBTTagList list = new NBTTagList();
                        for (int i = 0; i < array.size(); i++)
                        {
                            NBTTagCompound tag = new NBTTagCompound();
                            handle((JsonObject) array.get(i), tag, depth++);
                            list.appendTag(tag);
                        }
                        nbt.setTag(entry.getKey(), list);
                    }
                }
            }
            else
            {
                throw new RuntimeException("Unknown type to convert to NBT -> " + entry.getValue());
            }
        }
    }

    public static JsonElement toJson(NBTTagCompound nbt)
    {
        if (nbt != null && !nbt.hasNoTags())
        {
            JsonObject object = new JsonObject();

            Set set = nbt.func_150296_c();
            for (Object s : set)
            {
                if (s instanceof String)
                {
                    String key = (String) s;
                    NBTBase tag = nbt.getTag(key);
                    if (tag instanceof NBTTagByte)
                    {
                        object.add(key + ":byte", new JsonPrimitive(((NBTTagByte) tag).func_150290_f()));
                    }
                    else if (tag instanceof NBTTagShort)
                    {
                        object.add(key + ":short", new JsonPrimitive(((NBTTagShort) tag).func_150289_e()));
                    }
                    else if (tag instanceof NBTTagInt)
                    {
                        object.add(key + ":int", new JsonPrimitive(((NBTTagInt) tag).func_150287_d()));
                    }
                    else if (tag instanceof NBTTagLong)
                    {
                        object.add(key + ":long", new JsonPrimitive(((NBTTagLong) tag).func_150291_c()));
                    }
                    else if (tag instanceof NBTTagFloat)
                    {
                        object.add(key + ":float", new JsonPrimitive(((NBTTagFloat) tag).func_150288_h()));
                    }
                    else if (tag instanceof NBTTagDouble)
                    {
                        object.add(key + ":double", new JsonPrimitive(((NBTTagDouble) tag).func_150286_g()));
                    }
                    else if (tag instanceof NBTTagByteArray)
                    {
                        JsonArray array = new JsonArray();
                        for (byte b : ((NBTTagByteArray) tag).func_150292_c())
                        {
                            array.add(new JsonPrimitive(b));
                        }
                        object.add(key + ":byte", array);
                    }
                    else if (tag instanceof NBTTagString)
                    {
                        object.add(key, new JsonPrimitive(((NBTTagString) tag).func_150285_a_()));
                    }
                    else if (tag instanceof NBTTagList)
                    {
                        JsonArray array = new JsonArray();
                        for (int i = 0; i < ((NBTTagList) tag).tagCount(); i++)
                        {
                            array.add(toJson(((NBTTagList) tag).getCompoundTagAt(i)));
                        }
                        object.add(key, array);
                    }
                    else if (tag instanceof NBTTagCompound)
                    {
                        object.add(key, toJson((NBTTagCompound) tag));
                    }
                    else if (tag instanceof NBTTagIntArray)
                    {
                        JsonArray array = new JsonArray();
                        for (int b : ((NBTTagIntArray) tag).func_150302_c())
                        {
                            array.add(new JsonPrimitive(b));
                        }
                        object.add(key + ":int", array);
                    }
                }
            }

            return object;
        }
        return new JsonPrimitive("");
    }
}

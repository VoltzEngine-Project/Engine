package com.builtbroken.mc.lib.json.conversion;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.Map;

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
    public NBTTagCompound convert(JsonElement element)
    {
        NBTTagCompound nbt = new NBTTagCompound();
        if (element instanceof JsonObject)
        {
            handle((JsonObject) element, nbt, 0);
        }
        return nbt;
    }

    protected void handle(JsonObject object, NBTTagCompound nbt, int depth)
    {
        for (Map.Entry<String, JsonElement> entry : object.entrySet())
        {
            if (entry.getValue() instanceof JsonObject)
            {
                NBTTagCompound tag = new NBTTagCompound();
                handle((JsonObject) entry.getValue(), tag, depth++); //TODO add depth limit
                nbt.setTag(entry.getKey(), tag);
            }
            else if (entry.getValue() instanceof JsonPrimitive)
            {
                JsonPrimitive primitive = (JsonPrimitive) entry.getValue();
                if (primitive.isNumber())
                {
                    String[] split = entry.getKey().split(":");
                    String key = split[0];
                    String type = split[1].toLowerCase();
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
}

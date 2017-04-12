package com.builtbroken.mc.lib.json.loading;

import com.builtbroken.mc.lib.helper.ReflectionUtility;
import com.builtbroken.mc.lib.json.conversion.JsonConverter;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/7/2017.
 */
public class JsonProcessorInjectionMap<O extends Object>
{
    public final HashMap<String, Field> injectionFields = new HashMap();
    public final HashMap<String, Method> injectionMethods = new HashMap();
    public final HashMap<String, String> injectionTypes = new HashMap();

    public JsonProcessorInjectionMap(Class clazz)
    {
        load(clazz);
    }

    protected void load(Class clazz)
    {
        //Locate all fields, both in the target and parent of target
        List<Field> fields = ReflectionUtility.getAllFields(clazz);
        for (Field field : fields)
        {
            //Get all annotations, right now we only want one but might want more later
            Annotation[] annotations = field.getDeclaredAnnotations();
            if (annotations != null && annotations.length > 0)
            {
                for (Annotation annotation : annotations)
                {
                    //Find our annotation, allow multiple for different keys
                    if (annotation instanceof JsonProcessorData)
                    {
                        //Get keys and add each
                        final String[] values = ((JsonProcessorData) annotation).value();
                        if (values != null)
                        {
                            for (final String keyValue : values)
                            {
                                if (keyValue != null)
                                {
                                    final String key = keyValue.toLowerCase();
                                    if (injectionFields.containsKey(key))
                                    {
                                        throw new NullPointerException("Duplicate key detected for  " + field + " owned by " + injectionFields.get(key));
                                    }

                                    injectionFields.put(key, field);
                                    if (((JsonProcessorData) annotation).type() != null && !((JsonProcessorData) annotation).type().equals("unknown"))
                                    {
                                        injectionTypes.put(key, ((JsonProcessorData) annotation).type().toLowerCase());
                                    }
                                }
                                else
                                {
                                    throw new NullPointerException("Value for JsonProcessorData was null on " + field);
                                }
                            }
                        }
                        else
                        {
                            throw new NullPointerException("Value for JsonProcessorData was null on " + field);
                        }
                    }
                }
            }
        }

        List<Method> methods = ReflectionUtility.getMethods(clazz);
        for (Method method : methods)
        {
            Annotation[] annotations = method.getDeclaredAnnotations();
            if (annotations != null && annotations.length > 0)
            {
                for (Annotation annotation : annotations)
                {
                    //Find our annotation, allow multiple for different keys
                    if (annotation instanceof JsonProcessorData)
                    {
                        if (method.getParameterCount() != 1)
                        {
                            throw new NullPointerException("Method " + method + " should only have 1 parameter to use JsonProcessorData tag");
                        }
                        //Get keys and add each
                        final String[] values = ((JsonProcessorData) annotation).value();
                        if (values != null)
                        {
                            for (final String keyValue : values)
                            {
                                if (keyValue != null)
                                {
                                    final String key = keyValue.toLowerCase();
                                    if (injectionMethods.containsKey(key))
                                    {
                                        throw new NullPointerException("Duplicate key detected for  " + method + " owned by " + injectionMethods.get(key));
                                    }

                                    injectionMethods.put(key, method);
                                    if (((JsonProcessorData) annotation).type() != null && !((JsonProcessorData) annotation).type().equals("unknown"))
                                    {
                                        injectionTypes.put(key, ((JsonProcessorData) annotation).type().toLowerCase());
                                    }
                                }
                                else
                                {
                                    throw new NullPointerException("Value for JsonProcessorData was null on " + method);
                                }
                            }
                        }
                        else
                        {
                            throw new NullPointerException("Value for JsonProcessorData was null on " + method);
                        }
                    }
                }
            }
        }
    }

    public boolean handle(O objectToInjection, String keyValue, Object valueToInject)
    {
        try
        {
            final String injectionKeyID = keyValue.toLowerCase();
            if (injectionFields.containsKey(injectionKeyID) || injectionMethods.containsKey(injectionKeyID))
            {
                if (valueToInject instanceof JsonElement)
                {
                    if (valueToInject instanceof JsonPrimitive)
                    {
                        if (((JsonPrimitive) valueToInject).isBoolean())
                        {
                            Boolean bool = ((JsonPrimitive) valueToInject).getAsBoolean();
                            if (injectionFields.containsKey(injectionKeyID))
                            {
                                Field field = injectionFields.get(injectionKeyID);
                                try
                                {
                                    field.setAccessible(true);
                                    field.setBoolean(objectToInjection, bool);
                                    return true;
                                }
                                catch (IllegalAccessException e)
                                {
                                    throw new RuntimeException("Failed to access field " + field, e);
                                }
                                catch (Exception e)
                                {
                                    throw new RuntimeException("Unexpected error setting " + field + " with " + bool, e);
                                }
                            }
                            else
                            {
                                Method method = injectionMethods.get(injectionKeyID);
                                try
                                {
                                    method.setAccessible(true);
                                    method.invoke(objectToInjection, bool);
                                    return true;
                                }
                                catch (InvocationTargetException e)
                                {
                                    throw new RuntimeException("Failed to invoke method " + method + " with data " + bool, e);
                                }
                                catch (IllegalAccessException e)
                                {
                                    throw new RuntimeException("Failed to access method " + method, e);
                                }
                                catch (Exception e)
                                {
                                    throw new RuntimeException("Unexpected error invoking " + method + " with " + bool, e);
                                }
                            }
                        }
                        else if (((JsonPrimitive) valueToInject).isNumber())
                        {
                            if (injectionFields.containsKey(injectionKeyID))
                            {
                                Field field = injectionFields.get(injectionKeyID);
                                try
                                {
                                    field.setAccessible(true);
                                    String type = injectionTypes.get(injectionKeyID);
                                    if (type != null)
                                    {
                                        if (type.equals("int") || type.equals("integer"))
                                        {
                                            field.setInt(objectToInjection, ((JsonPrimitive) valueToInject).getAsInt());
                                        }
                                        else if (type.equals("byte"))
                                        {
                                            field.setByte(objectToInjection, ((JsonPrimitive) valueToInject).getAsByte());
                                        }
                                        else if (type.equals("short"))
                                        {
                                            field.setShort(objectToInjection, ((JsonPrimitive) valueToInject).getAsShort());
                                        }
                                        else if (type.equals("double"))
                                        {
                                            field.setDouble(objectToInjection, ((JsonPrimitive) valueToInject).getAsDouble());
                                        }
                                        else if (type.equals("float"))
                                        {
                                            field.setFloat(objectToInjection, ((JsonPrimitive) valueToInject).getAsFloat());
                                        }
                                        else if (type.equals("long"))
                                        {
                                            field.setLong(objectToInjection, ((JsonPrimitive) valueToInject).getAsLong());
                                        }
                                        else
                                        {
                                            throw new RuntimeException("Unknown number type for " + field);
                                        }
                                        return true;
                                    }
                                    else
                                    {
                                        throw new RuntimeException("Failed to get number type for " + field);
                                    }
                                }
                                catch (IllegalAccessException e)
                                {
                                    throw new RuntimeException("Failed to access field " + field, e);
                                }
                            }
                            else
                            {
                                Method method = injectionMethods.get(injectionKeyID);
                                try
                                {
                                    method.setAccessible(true);
                                    String type = injectionTypes.get(injectionKeyID);
                                    if (type != null)
                                    {
                                        if (type.equals("int") || type.equals("integer"))
                                        {
                                            method.invoke(objectToInjection, (int) ((JsonPrimitive) valueToInject).getAsInt());
                                        }
                                        else if (type.equals("byte"))
                                        {
                                            method.invoke(objectToInjection, (byte) ((JsonPrimitive) valueToInject).getAsByte());
                                        }
                                        else if (type.equals("short"))
                                        {
                                            method.invoke(objectToInjection, (short) ((JsonPrimitive) valueToInject).getAsShort());
                                        }
                                        else if (type.equals("double"))
                                        {
                                            method.invoke(objectToInjection, (double) ((JsonPrimitive) valueToInject).getAsDouble());
                                        }
                                        else if (type.equals("float"))
                                        {
                                            method.invoke(objectToInjection, (float) ((JsonPrimitive) valueToInject).getAsFloat());
                                        }
                                        else if (type.equals("long"))
                                        {
                                            method.invoke(objectToInjection, (long) ((JsonPrimitive) valueToInject).getAsLong());
                                        }
                                        else
                                        {
                                            throw new RuntimeException("Unknown number type " + type);
                                        }
                                        return true;
                                    }
                                    else
                                    {
                                        throw new RuntimeException("Failed to get type");
                                    }
                                }
                                catch (InvocationTargetException e)
                                {
                                    throw new RuntimeException("Failed to invoke method " + method + " with data " + valueToInject, e);
                                }
                                catch (IllegalAccessException e)
                                {
                                    throw new RuntimeException("Failed to access method " + method, e);
                                }
                                catch (Exception e)
                                {
                                    throw new RuntimeException("Error injecting " + valueToInject + " into method " + method, e);
                                }
                            }
                        }
                        else if (((JsonPrimitive) valueToInject).isString())
                        {
                            String string = ((JsonPrimitive) valueToInject).getAsString();
                            return handle(objectToInjection, keyValue, string);
                        }
                    }
                    else
                    {
                        if (injectionFields.containsKey(injectionKeyID))
                        {
                            Field field = injectionFields.get(injectionKeyID);
                            field.setAccessible(true);
                            try
                            {
                                String type = injectionTypes.get(injectionKeyID);
                                if (type != null && JsonLoader.conversionHandlers.containsKey(type.toLowerCase()))
                                {
                                    JsonConverter converter = JsonLoader.conversionHandlers.get(type.toLowerCase());
                                    if (converter != null)
                                    {
                                        Object conversion = converter.convert((JsonElement) valueToInject);
                                        if (conversion != null)
                                        {
                                            field.set(objectToInjection, conversion);
                                        }
                                        else
                                        {
                                            throw new IllegalArgumentException("Field was marked as type[" + type + "] but could not be converted to inject into " + field + ", data: " + objectToInjection);
                                        }
                                    }
                                    else
                                    {
                                        throw new IllegalArgumentException("Field was marked as type[" + type + "] but a converter could not be found to use with " + field + ", data: " + objectToInjection);
                                    }
                                }
                                else
                                {
                                    field.set(objectToInjection, valueToInject);
                                }
                                return true;
                            }
                            catch (IllegalAccessException e)
                            {
                                throw new RuntimeException("Failed to access field " + field, e);
                            }
                            catch (Exception e)
                            {
                                throw new RuntimeException("Unexpected error setting " + field + " with " + valueToInject, e);
                            }
                        }
                        else
                        {
                            Method method = injectionMethods.get(injectionKeyID);
                            try
                            {
                                method.setAccessible(true);
                                String type = injectionTypes.get(injectionKeyID);
                                if (type != null && JsonLoader.conversionHandlers.containsKey(type.toLowerCase()))
                                {
                                    JsonConverter converter = JsonLoader.conversionHandlers.get(type.toLowerCase());
                                    if (converter != null)
                                    {
                                        Object conversion = converter.convert((JsonElement) valueToInject);
                                        if (conversion != null)
                                        {
                                            method.invoke(objectToInjection, conversion);
                                        }
                                        else
                                        {
                                            throw new IllegalArgumentException("Method was marked as type[" + type + "] but could not be converted to inject into " + method + ", data: " + objectToInjection);
                                        }
                                    }
                                    else
                                    {
                                        throw new IllegalArgumentException("Method was marked as type[" + type + "] but a converter could not be found to use with " + method + ", data: " + objectToInjection);
                                    }
                                }
                                else
                                {
                                    method.invoke(objectToInjection, valueToInject);
                                }
                                return true;
                            }
                            catch (InvocationTargetException e)
                            {
                                throw new RuntimeException("Failed to invoke method " + method + " with data " + valueToInject, e);
                            }
                            catch (IllegalAccessException e)
                            {
                                throw new RuntimeException("Failed to access method " + method, e);
                            }
                            catch (Exception e)
                            {
                                throw new RuntimeException("Unexpected error invoking " + method + " with " + valueToInject, e);
                            }
                        }
                    }
                }
                else if (valueToInject instanceof String)
                {
                    if (injectionFields.containsKey(injectionKeyID))
                    {
                        Field field = injectionFields.get(injectionKeyID);
                        try
                        {
                            field.setAccessible(true);
                            field.set(objectToInjection, valueToInject);
                            return true;
                        }
                        catch (IllegalAccessException e)
                        {
                            throw new RuntimeException("Failed to access field " + field, e);
                        }
                        catch (Exception e)
                        {
                            throw new RuntimeException("Unexpected error setting " + field + " with " + valueToInject, e);
                        }
                    }
                    else
                    {
                        Method method = injectionMethods.get(injectionKeyID);
                        try
                        {
                            method.setAccessible(true);
                            method.invoke(objectToInjection, valueToInject);
                            return true;
                        }
                        catch (InvocationTargetException e)
                        {
                            throw new RuntimeException("Failed to invoke method " + method + " with data " + valueToInject, e);
                        }
                        catch (IllegalAccessException e)
                        {
                            throw new RuntimeException("Failed to access method " + method, e);
                        }
                        catch (Exception e)
                        {
                            throw new RuntimeException("Unexpected error invoking " + method + " with " + valueToInject, e);
                        }
                    }
                }
                else if (injectionFields.containsKey(injectionKeyID))
                {
                    Field field = injectionFields.get(injectionKeyID);
                    try
                    {
                        field.setAccessible(true);
                        field.set(objectToInjection, valueToInject);
                        return true;
                    }
                    catch (IllegalAccessException e)
                    {
                        throw new RuntimeException("Failed to access field " + field, e);
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException("Unexpected error setting " + field + " with " + valueToInject, e);
                    }
                }
                else
                {
                    Method method = injectionMethods.get(injectionKeyID);
                    try
                    {
                        method.setAccessible(true);
                        method.invoke(objectToInjection, valueToInject);
                        return true;
                    }
                    catch (InvocationTargetException e)
                    {
                        throw new RuntimeException("Failed to invoke method " + method + " with data " + valueToInject, e);
                    }
                    catch (IllegalAccessException e)
                    {
                        throw new RuntimeException("Failed to access method " + method, e);
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException("Unexpected error invoking " + method + " with " + valueToInject, e);
                    }
                }
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to inject data " + valueToInject + " into " + objectToInjection, e);
        }
        return false;
    }
}

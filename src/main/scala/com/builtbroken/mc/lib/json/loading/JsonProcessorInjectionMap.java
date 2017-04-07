package com.builtbroken.mc.lib.json.loading;

import com.builtbroken.mc.lib.helper.ReflectionUtility;
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

    public boolean handle(O object, String keyValue, Object value)
    {
        final String key = keyValue.toLowerCase();
        if (injectionFields.containsKey(key) || injectionMethods.containsKey(key))
        {
            if (value instanceof JsonElement)
            {
                if (value instanceof JsonPrimitive)
                {
                    if (((JsonPrimitive) value).isBoolean())
                    {
                        Boolean bool = ((JsonPrimitive) value).getAsBoolean();
                        if (injectionFields.containsKey(key))
                        {
                            Field field = injectionFields.get(key);
                            try
                            {
                                field.setAccessible(true);
                                field.setBoolean(object, bool);
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
                            Method method = injectionMethods.get(key);
                            try
                            {
                                method.setAccessible(true);
                                method.invoke(object, bool);
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
                    else if (((JsonPrimitive) value).isNumber())
                    {
                        if (injectionFields.containsKey(key))
                        {
                            Field field = injectionFields.get(key);
                            try
                            {
                                field.setAccessible(true);
                                String type = injectionTypes.get(key);
                                if (type != null)
                                {
                                    if (type.equals("int") || type.equals("integer"))
                                    {
                                        field.setInt(object, ((JsonPrimitive) value).getAsInt());
                                    }
                                    else if (type.equals("byte"))
                                    {
                                        field.setByte(object, ((JsonPrimitive) value).getAsByte());
                                    }
                                    else if (type.equals("short"))
                                    {
                                        field.setShort(object, ((JsonPrimitive) value).getAsShort());
                                    }
                                    else if (type.equals("double"))
                                    {
                                        field.setDouble(object, ((JsonPrimitive) value).getAsDouble());
                                    }
                                    else if (type.equals("float"))
                                    {
                                        field.setFloat(object, ((JsonPrimitive) value).getAsFloat());
                                    }
                                    else if (type.equals("long"))
                                    {
                                        field.setLong(object, ((JsonPrimitive) value).getAsLong());
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
                            Method method = injectionMethods.get(key);
                            try
                            {
                                method.setAccessible(true);
                                String type = injectionTypes.get(key);
                                if (type != null)
                                {
                                    if (type.equals("int") || type.equals("integer"))
                                    {
                                        method.invoke(object, ((JsonPrimitive) value).getAsInt());
                                    }
                                    else if (type.equals("byte"))
                                    {
                                        method.invoke(object, ((JsonPrimitive) value).getAsByte());
                                    }
                                    else if (type.equals("short"))
                                    {
                                        method.invoke(object, ((JsonPrimitive) value).getAsShort());
                                    }
                                    else if (type.equals("double"))
                                    {
                                        method.invoke(object, ((JsonPrimitive) value).getAsDouble());
                                    }
                                    else if (type.equals("float"))
                                    {
                                        method.invoke(object, ((JsonPrimitive) value).getAsFloat());
                                    }
                                    else if (type.equals("long"))
                                    {
                                        method.invoke(object, ((JsonPrimitive) value).getAsLong());
                                    }
                                    else
                                    {
                                        throw new RuntimeException("Unknown number type for " + method);
                                    }
                                    return true;
                                }
                                else
                                {
                                    throw new RuntimeException("Failed to get number type for " + method);
                                }
                            }
                            catch (InvocationTargetException e)
                            {
                                throw new RuntimeException("Failed to invoke method " + method + " with data " + value, e);
                            }
                            catch (IllegalAccessException e)
                            {
                                throw new RuntimeException("Failed to access method " + method, e);
                            }
                        }
                    }
                    else if (((JsonPrimitive) value).isString())
                    {
                        String string = ((JsonPrimitive) value).getAsString();
                        return handle(object, keyValue, string);
                    }
                }
                else
                {
                    if (injectionFields.containsKey(key))
                    {
                        Field field = injectionFields.get(key);
                        try
                        {
                            field.setAccessible(true);
                            field.set(object, value);
                            return true;
                        }
                        catch (IllegalAccessException e)
                        {
                            throw new RuntimeException("Failed to access field " + field, e);
                        }
                        catch (Exception e)
                        {
                            throw new RuntimeException("Unexpected error setting " + field + " with " + value, e);
                        }
                    }
                    else
                    {
                        Method method = injectionMethods.get(key);
                        try
                        {
                            method.setAccessible(true);
                            method.invoke(object, value);
                            return true;
                        }
                        catch (InvocationTargetException e)
                        {
                            throw new RuntimeException("Failed to invoke method " + method + " with data " + value, e);
                        }
                        catch (IllegalAccessException e)
                        {
                            throw new RuntimeException("Failed to access method " + method, e);
                        }
                        catch (Exception e)
                        {
                            throw new RuntimeException("Unexpected error invoking " + method + " with " + value, e);
                        }
                    }
                }
            }
            else if (value instanceof String)
            {
                if (injectionFields.containsKey(key))
                {
                    Field field = injectionFields.get(key);
                    try
                    {
                        field.setAccessible(true);
                        field.set(object, value);
                        return true;
                    }
                    catch (IllegalAccessException e)
                    {
                        throw new RuntimeException("Failed to access field " + field, e);
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException("Unexpected error setting " + field + " with " + value, e);
                    }
                }
                else
                {
                    Method method = injectionMethods.get(key);
                    try
                    {
                        method.setAccessible(true);
                        method.invoke(object, value);
                        return true;
                    }
                    catch (InvocationTargetException e)
                    {
                        throw new RuntimeException("Failed to invoke method " + method + " with data " + value, e);
                    }
                    catch (IllegalAccessException e)
                    {
                        throw new RuntimeException("Failed to access method " + method, e);
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException("Unexpected error invoking " + method + " with " + value, e);
                    }
                }
            }
            else if (injectionFields.containsKey(key))
            {
                Field field = injectionFields.get(key);
                try
                {
                    field.setAccessible(true);
                    field.set(object, value);
                    return true;
                }
                catch (IllegalAccessException e)
                {
                    throw new RuntimeException("Failed to access field " + field, e);
                }
                catch (Exception e)
                {
                    throw new RuntimeException("Unexpected error setting " + field + " with " + value, e);
                }
            }
            else
            {
                Method method = injectionMethods.get(key);
                try
                {
                    method.setAccessible(true);
                    method.invoke(object, value);
                    return true;
                }
                catch (InvocationTargetException e)
                {
                    throw new RuntimeException("Failed to invoke method " + method + " with data " + value, e);
                }
                catch (IllegalAccessException e)
                {
                    throw new RuntimeException("Failed to access method " + method, e);
                }
                catch (Exception e)
                {
                    throw new RuntimeException("Unexpected error invoking " + method + " with " + value, e);
                }
            }
        }
        return false;
    }
}

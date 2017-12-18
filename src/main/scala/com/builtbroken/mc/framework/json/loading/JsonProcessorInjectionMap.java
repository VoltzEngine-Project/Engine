package com.builtbroken.mc.framework.json.loading;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.framework.json.conversion.JsonConverter;
import com.builtbroken.mc.framework.json.imp.IJsonGenObject;
import com.builtbroken.mc.lib.helper.ReflectionUtility;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/7/2017.
 */
public class JsonProcessorInjectionMap<O extends Object>
{
    public final List<String> injectionKeys = new ArrayList();

    public final HashMap<String, Field> jsonDataFields = new HashMap();
    public final HashMap<String, Method> jsonDataSetters = new HashMap();
    public final HashMap<String, Method> jsonDataGetters = new HashMap();

    public final HashMap<String, JsonProcessorData> jsonDataAnnotation = new HashMap();

    public JsonProcessorInjectionMap(Class clazz)
    {
        load(clazz);
    }

    protected void load(Class clazz)
    {
        boolean loadServer = Engine.shouldDoServerLogic();
        boolean loadClient = Engine.shouldDoClientLogic();
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
                        JsonProcessorData jAnno = ((JsonProcessorData) annotation);
                        //Get keys and add each
                        final String[] values = jAnno.value();
                        if (values != null && (loadServer && jAnno.loadForServer() || loadClient && jAnno.loadForClient()))
                        {
                            for (final String keyValue : values)
                            {
                                if (keyValue != null)
                                {
                                    final String key = keyValue.toLowerCase();
                                    if (jsonDataFields.containsKey(key))
                                    {
                                        throw new NullPointerException("Duplicate key detected for  " + field + " owned by " + jsonDataFields.get(key));
                                    }

                                    jsonDataFields.put(key, field);
                                    injectionKeys.add(key);
                                    cacheAnnotationData(key, jAnno);
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
                        JsonProcessorData jAnno = ((JsonProcessorData) annotation);
                        if (method.getParameterCount() != 1)
                        {
                            throw new NullPointerException("Method " + method + " should only have 1 parameter to use JsonProcessorData tag");
                        }

                        //Get keys and add each
                        final String[] values = jAnno.value();
                        if (values != null && (loadServer && jAnno.loadForServer() || loadClient && jAnno.loadForClient()))
                        {
                            for (final String keyValue : values)
                            {
                                if (keyValue != null)
                                {
                                    final String key = keyValue.toLowerCase();
                                    if (jsonDataSetters.containsKey(key))
                                    {
                                        throw new NullPointerException("Duplicate key detected for  " + method + " owned by " + jsonDataSetters.get(key));
                                    }

                                    jsonDataSetters.put(key, method);
                                    injectionKeys.add(key);
                                    cacheAnnotationData(key, jAnno);
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
                    else if (annotation instanceof JsonProcessorDataGetter)
                    {
                        JsonProcessorDataGetter jAnno = ((JsonProcessorDataGetter) annotation);
                        //Get keys and add each
                        final String[] values = jAnno.value();
                        if (values != null)
                        {
                            for (final String keyValue : values)
                            {
                                if (keyValue != null)
                                {
                                    final String key = keyValue.toLowerCase();
                                    if (jsonDataGetters.containsKey(key))
                                    {
                                        throw new NullPointerException("Duplicate key detected for  " + method + " owned by " + jsonDataGetters.get(key));
                                    }
                                    jsonDataGetters.put(key, method);
                                }
                                else
                                {
                                    throw new NullPointerException("Value for JsonProcessorDataGetter was null on " + method);
                                }
                            }
                        }
                        else
                        {
                            throw new NullPointerException("Value for JsonProcessorDataGetter was null on " + method);
                        }
                    }
                }
            }
        }
    }

    /**
     * Caches information about the annotation for reuse during handling
     *
     * @param key        - field key
     * @param annotation - annotation containing data to cache
     */
    protected void cacheAnnotationData(String key, JsonProcessorData annotation)
    {
        jsonDataAnnotation.put(key.toLowerCase(), annotation);
    }

    protected String getInjectionType(String key)
    {
        if (jsonDataAnnotation.containsKey(key))
        {
            return jsonDataAnnotation.get(key).type();
        }
        return null;
    }

    protected String[] getInjectionArgs(String key)
    {
        if (jsonDataAnnotation.containsKey(key))
        {
            return jsonDataAnnotation.get(key).args();
        }
        return null;
    }

    protected boolean allowsRunTimeChanges(String key)
    {
        if (jsonDataAnnotation.containsKey(key))
        {
            return jsonDataAnnotation.get(key).allowRuntimeChanges();
        }
        return false;
    }

    public boolean supports(String keyValue, boolean override, String overrideType)
    {
        if (jsonDataFields.containsKey(keyValue) || jsonDataSetters.containsKey(keyValue))
        {
            return !override || allowsRunTimeChanges(keyValue);
        }
        return false;
    }

    /**
     * Called to handle injection for the object
     *
     * @param objectToInjection - object to inject data into
     * @param keyValue          - key to inject
     * @param valueToInject     - value
     * @return
     */
    public boolean handle(O objectToInjection, String keyValue, Object valueToInject)
    {
        return handle(objectToInjection, keyValue, valueToInject, false, "");
    }

    /**
     * Called to handle injection for the object
     *
     * @param objectToInjection - object to inject data into
     * @param keyValue          - key to inject
     * @param valueToInject     - value
     * @param override
     * @param overrideType
     * @return
     */
    public boolean handle(O objectToInjection, String keyValue, Object valueToInject, boolean override, String overrideType)
    {
        try
        {
            final String injectionKeyID = keyValue.toLowerCase();
            if (supports(injectionKeyID, override, overrideType))
            {
                if (valueToInject instanceof JsonElement)
                {
                    if (valueToInject instanceof JsonPrimitive)
                    {
                        if (((JsonPrimitive) valueToInject).isBoolean())
                        {
                            return handleBoolean(objectToInjection, injectionKeyID, valueToInject);
                        }
                        else if (((JsonPrimitive) valueToInject).isNumber())
                        {
                            return handleNumbers(objectToInjection, injectionKeyID, valueToInject);
                        }
                        else if (((JsonPrimitive) valueToInject).isString())
                        {
                            String string = ((JsonPrimitive) valueToInject).getAsString();
                            return handle(objectToInjection, keyValue, string);
                        }
                    }
                    else
                    {
                        if (jsonDataFields.containsKey(injectionKeyID))
                        {
                            Field field = jsonDataFields.get(injectionKeyID);
                            field.setAccessible(true);
                            try
                            {
                                String type = getInjectionType(injectionKeyID);
                                if (type != null && JsonLoader.hasConverterFor(type))
                                {
                                    JsonConverter converter = JsonLoader.getConversionHandler(type);
                                    if (converter != null)
                                    {
                                        Object conversion = converter.convert((JsonElement) valueToInject, getInjectionArgs(injectionKeyID));
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
                            Method method = jsonDataSetters.get(injectionKeyID);
                            try
                            {
                                method.setAccessible(true);
                                String type = getInjectionType(injectionKeyID);
                                if (type != null && JsonLoader.hasConverterFor(type))
                                {
                                    JsonConverter converter = JsonLoader.getConversionHandler(type);
                                    if (converter != null)
                                    {
                                        Object conversion = converter.convert((JsonElement) valueToInject, getInjectionArgs(injectionKeyID));
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
                //Handle string
                else if (valueToInject instanceof String)
                {
                    if (jsonDataFields.containsKey(injectionKeyID))
                    {
                        Field field = jsonDataFields.get(injectionKeyID);
                        try
                        {
                            field.setAccessible(true);
                            //Attempt to process type conversions
                            String type = getInjectionType(injectionKeyID);
                            if (type != null && !type.equalsIgnoreCase("unknown"))
                            {
                                return handleFieldType(type, objectToInjection, field, valueToInject);
                            }
                            //Attempt to inject string
                            else
                            {
                                field.set(objectToInjection, valueToInject);
                                return true;
                            }
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
                        Method method = jsonDataSetters.get(injectionKeyID);
                        try
                        {
                            method.setAccessible(true);

                            //Attempt to process type conversions
                            String type = getInjectionType(injectionKeyID);
                            if (type != null && !type.equalsIgnoreCase("unknown"))
                            {
                                return handleMethodType(type, objectToInjection, method, valueToInject);
                            }
                            //Attempt to inject string
                            else
                            {
                                method.invoke(objectToInjection, valueToInject);
                                return true;
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
                            throw new RuntimeException("Unexpected error invoking " + method + " with " + valueToInject, e);
                        }
                    }
                }
                //Brute force attempt to inject the object into the field
                else if (jsonDataFields.containsKey(injectionKeyID))
                {
                    Field field = jsonDataFields.get(injectionKeyID);
                    try
                    {
                        field.setAccessible(true);
                        field.set(objectToInjection, valueToInject);
                        return true;
                    }
                    catch (IllegalAccessException e)
                    {
                        throw new RuntimeException("Failed to access field '" + field + "'", e);
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException("Unexpected error setting field '" + field + "' with value '" + valueToInject + "'", e);
                    }
                }
                //Brute force attempt to inject the object into the method
                else
                {
                    Method method = jsonDataSetters.get(injectionKeyID);
                    try
                    {
                        method.setAccessible(true);
                        method.invoke(objectToInjection, valueToInject);
                        return true;
                    }
                    catch (InvocationTargetException e)
                    {
                        throw new RuntimeException("Failed to invoke method '" + method + "' with data '" + valueToInject + "'", e);
                    }
                    catch (IllegalAccessException e)
                    {
                        throw new RuntimeException("Failed to access method '" + method + "'", e);
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException("Unexpected error invoking method '" + method + "' with value '" + valueToInject + "'", e);
                    }
                }
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to inject data '" + valueToInject + "' for key '" + keyValue + "' into object '" + objectToInjection + "'", e);
        }
        return false;
    }

    public boolean handleMethodType(String type, O objectToInjection, Method method, Object valueToInject) throws InvocationTargetException, IllegalAccessException
    {
        //TODO move this to an object system so types can easily be registered. rather than manually checking and converting
        if (type.equals("int") || type.equals("integer"))
        {
            if (valueToInject instanceof JsonPrimitive)
            {
                method.invoke(objectToInjection, ((JsonPrimitive) valueToInject).getAsInt());
                return true;
            }
            else if (valueToInject instanceof Number)
            {
                method.invoke(objectToInjection, ((Number) valueToInject).intValue());
                return true;
            }
            else if (valueToInject instanceof String)
            {
                method.invoke(objectToInjection, Integer.parseInt((String) valueToInject));
                return true;
            }
        }
        else if (type.equals("byte"))
        {
            if (valueToInject instanceof JsonPrimitive)
            {
                method.invoke(objectToInjection, ((JsonPrimitive) valueToInject).getAsByte());
                return true;
            }
            else if (valueToInject instanceof Number)
            {
                method.invoke(objectToInjection, ((Number) valueToInject).byteValue());
                return true;
            }
            else if (valueToInject instanceof String)
            {
                method.invoke(objectToInjection, (byte) Integer.parseInt((String) valueToInject));
                return true;
            }
        }
        else if (type.equals("short"))
        {
            if (valueToInject instanceof JsonPrimitive)
            {
                method.invoke(objectToInjection, ((JsonPrimitive) valueToInject).getAsShort());
                return true;
            }
            else if (valueToInject instanceof Number)
            {
                method.invoke(objectToInjection, ((Number) valueToInject).shortValue());
                return true;
            }
            else if (valueToInject instanceof String)
            {
                method.invoke(objectToInjection, (short) Integer.parseInt((String) valueToInject));
                return true;
            }
            return true;
        }
        else if (type.equals("double"))
        {
            if (valueToInject instanceof JsonPrimitive)
            {
                method.invoke(objectToInjection, ((JsonPrimitive) valueToInject).getAsDouble());
                return true;
            }
            else if (valueToInject instanceof Number)
            {
                method.invoke(objectToInjection, ((Number) valueToInject).doubleValue());
                return true;
            }
            else if (valueToInject instanceof String)
            {
                method.invoke(objectToInjection, Double.parseDouble((String) valueToInject));
                return true;
            }
            return true;
        }
        else if (type.equals("float"))
        {
            if (valueToInject instanceof JsonPrimitive)
            {
                method.invoke(objectToInjection, ((JsonPrimitive) valueToInject).getAsFloat());
                return true;
            }
            else if (valueToInject instanceof Number)
            {
                method.invoke(objectToInjection, ((Number) valueToInject).floatValue());
                return true;
            }
            else if (valueToInject instanceof String)
            {
                method.invoke(objectToInjection, Float.parseFloat((String) valueToInject));
                return true;
            }
            return true;
        }
        else if (type.equals("long"))
        {
            if (valueToInject instanceof JsonPrimitive)
            {
                method.invoke(objectToInjection, ((JsonPrimitive) valueToInject).getAsLong());
                return true;
            }
            else if (valueToInject instanceof Number)
            {
                method.invoke(objectToInjection, ((Number) valueToInject).longValue());
                return true;
            }
            else if (valueToInject instanceof String)
            {
                method.invoke(objectToInjection, Long.parseLong((String) valueToInject));
                return true;
            }
            return true;
        }
        else if (type.equals("string"))
        {
            if (valueToInject instanceof JsonPrimitive)
            {
                method.invoke(objectToInjection, ((JsonPrimitive) valueToInject).getAsString());
                return true;
            }
            else if (valueToInject instanceof String)
            {
                method.invoke(objectToInjection, (String) valueToInject);
                return true;
            }
        }
        return false;
    }

    public boolean handleFieldType(String type, O objectToInjection, Field field, Object valueToInject) throws IllegalAccessException
    {
        //TODO move this to an object system so types can easily be registered. rather than manually checking and converting
        if (type.equals("int") || type.equals("integer"))
        {
            if (valueToInject instanceof JsonPrimitive)
            {
                field.setInt(objectToInjection, ((JsonPrimitive) valueToInject).getAsInt());
                return true;
            }
            else if (valueToInject instanceof Number)
            {
                field.setInt(objectToInjection, ((Number) valueToInject).intValue());
                return true;
            }
            else if (valueToInject instanceof String)
            {
                field.setInt(objectToInjection, Integer.parseInt((String) valueToInject));
                return true;
            }
        }
        else if (type.equals("byte"))
        {
            if (valueToInject instanceof JsonPrimitive)
            {
                field.setByte(objectToInjection, ((JsonPrimitive) valueToInject).getAsByte());
                return true;
            }
            else if (valueToInject instanceof Number)
            {
                field.setByte(objectToInjection, ((Number) valueToInject).byteValue());
                return true;
            }
            else if (valueToInject instanceof String)
            {
                field.setByte(objectToInjection, (byte) Integer.parseInt((String) valueToInject));
                return true;
            }
        }
        else if (type.equals("short"))
        {
            if (valueToInject instanceof JsonPrimitive)
            {
                field.setShort(objectToInjection, ((JsonPrimitive) valueToInject).getAsShort());
                return true;
            }
            else if (valueToInject instanceof Number)
            {
                field.setShort(objectToInjection, ((Number) valueToInject).shortValue());
                return true;
            }
            else if (valueToInject instanceof String)
            {
                field.setShort(objectToInjection, (short) Integer.parseInt((String) valueToInject));
                return true;
            }
        }
        else if (type.equals("double"))
        {
            if (valueToInject instanceof JsonPrimitive)
            {
                field.setDouble(objectToInjection, ((JsonPrimitive) valueToInject).getAsDouble());
                return true;
            }
            else if (valueToInject instanceof Number)
            {
                field.setDouble(objectToInjection, ((Number) valueToInject).doubleValue());
                return true;
            }
            else if (valueToInject instanceof String)
            {
                field.setDouble(objectToInjection, Double.parseDouble((String) valueToInject));
                return true;
            }
        }
        else if (type.equals("float"))
        {
            if (valueToInject instanceof JsonPrimitive)
            {
                field.setFloat(objectToInjection, ((JsonPrimitive) valueToInject).getAsFloat());
                return true;
            }
            else if (valueToInject instanceof Number)
            {
                field.setFloat(objectToInjection, ((Number) valueToInject).floatValue());
                return true;
            }
            else if (valueToInject instanceof String)
            {
                field.setFloat(objectToInjection, Float.parseFloat((String) valueToInject));
                return true;
            }
        }
        else if (type.equals("long"))
        {
            if (valueToInject instanceof JsonPrimitive)
            {
                field.setLong(objectToInjection, ((JsonPrimitive) valueToInject).getAsLong());
                return true;
            }
            else if (valueToInject instanceof Number)
            {
                field.setLong(objectToInjection, ((Number) valueToInject).longValue());
                return true;
            }
            else if (valueToInject instanceof String)
            {
                field.setLong(objectToInjection, Long.parseLong((String) valueToInject));
                return true;
            }
        }
        else if (type.equals("string"))
        {
            if (valueToInject instanceof JsonPrimitive)
            {
                field.set(objectToInjection, ((JsonPrimitive) valueToInject).getAsString());
                return true;
            }
            else if (valueToInject instanceof String)
            {
                field.set(objectToInjection, (String) valueToInject);
                return true;
            }
        }
        return false;
    }

    @Deprecated //Replace with handle type methods
    public boolean handleNumbers(O objectToInjection, String injectionKeyID, Object valueToInject)
    {
        if (jsonDataFields.containsKey(injectionKeyID))
        {
            Field field = jsonDataFields.get(injectionKeyID);
            try
            {
                field.setAccessible(true);
                String type = getInjectionType(injectionKeyID);
                if (type != null)
                {
                    if (!handleFieldType(type, objectToInjection, field, valueToInject))
                    {
                        throw new RuntimeException("Failed to get number type for " + field);
                    }
                }
                else
                {
                    throw new RuntimeException("Failed to get type");
                }
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException("Failed to access field " + field, e);
            }
        }
        else
        {
            Method method = jsonDataSetters.get(injectionKeyID);
            try
            {
                method.setAccessible(true);
                String type = getInjectionType(injectionKeyID);
                if (type != null)
                {
                    if (!handleMethodType(type, objectToInjection, method, valueToInject))
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
        return false;
    }

    @Deprecated //Replace with handle type methods
    public boolean handleBoolean(O objectToInjection, String injectionKeyID, Object valueToInject)
    {
        Boolean bool = ((JsonPrimitive) valueToInject).getAsBoolean();
        if (jsonDataFields.containsKey(injectionKeyID))
        {
            Field field = jsonDataFields.get(injectionKeyID);
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
            Method method = jsonDataSetters.get(injectionKeyID);
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

    public <D extends IJsonGenObject> void enforceRequired(D objectToInject) throws IllegalAccessException
    {
        for (Field field : jsonDataFields.values())
        {
            Annotation[] annotations = field.getDeclaredAnnotations();
            if (annotations != null && annotations.length > 0)
            {
                for (Annotation annotation : annotations)
                {
                    //Find our annotation, allow multiple for different keys
                    if (annotation instanceof JsonProcessorData && ((JsonProcessorData) annotation).required())
                    {
                        if (!field.isAccessible())
                        {
                            Engine.logger().error("JsonProcessorInjectionMap: Failed to access field '" + field + "' to check required state.");
                        }
                        else
                        {
                            Object object = field.get(objectToInject);
                            if (object == null)
                            {
                                throw new RuntimeException("JsonProcessorInjectionMap: Missing required value from JSON file '" + ((JsonProcessorData) annotation).value()[0] + "'");
                            }
                        }
                    }
                }
            }
        }
    }
}

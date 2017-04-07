package com.builtbroken.mc.lib.json.loading;

import com.builtbroken.mc.lib.helper.ReflectionUtility;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/7/2017.
 */
public class JsonProcessorInjectionMap
{
    protected HashMap<String, Field> injectionFields = new HashMap();
    protected HashMap<String, Method> injectionMethods = new HashMap();

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
            Annotation[] annotations = field.getAnnotations();
            if (annotations != null && annotations.length > 0)
            {
                for (Annotation annotation : annotations)
                {
                    //Find our annotation, allow multiple for different keys
                    if (annotation instanceof JsonProcessorData)
                    {
                        //Get keys and add each
                        String[] values = ((JsonProcessorData) annotation).value();
                        if (values != null)
                        {
                            for (String key : values)
                            {
                                if (key != null)
                                {
                                    injectionFields.put(key, field);
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
    }

    public boolean handle(String key, Object value)
    {
        return false;
    }
}

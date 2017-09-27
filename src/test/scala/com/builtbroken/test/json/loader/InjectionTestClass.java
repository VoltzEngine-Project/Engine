package com.builtbroken.test.json.loader;

import com.builtbroken.mc.framework.json.loading.JsonProcessorData;
import com.google.gson.JsonElement;

/**
 * Test object for {@link TestProcessorInjection}
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/7/2017.
 */
public class InjectionTestClass
{
    @JsonProcessorData("key")
    public String key;

    @JsonProcessorData("element")
    public JsonElement element;

    @JsonProcessorData("bool")
    public boolean bool;

    @JsonProcessorData(value = "i", type = "int")
    public int i;

    @JsonProcessorData(value = "i2", type = "integer")
    public int i2;

    @JsonProcessorData(value = "b", type = "byte")
    public byte b;

    @JsonProcessorData(value = "s", type = "short")
    public short s;

    @JsonProcessorData(value = "l", type = "long")
    public long l;

    @JsonProcessorData(value = "f", type = "float")
    public float f;

    @JsonProcessorData(value = "d", type = "double")
    public double d;


    @JsonProcessorData("key2")
    public void keyMethod(String key)
    {
        this.key = key;
    }

    @JsonProcessorData("bool2")
    public void keyMethod(boolean key)
    {
        this.bool = key;
    }

    @JsonProcessorData("element2")
    public void keyMethod(JsonElement element)
    {
        this.element = element;
    }

    @JsonProcessorData(value = "i3", type = "int")
    public void keyMetod(int i)
    {
        this.i = i;
    }

    @JsonProcessorData(value = "i4", type = "integer")
    public void keyMetod2(int i)
    {
        this.i2 = i;
    }

    @JsonProcessorData(value = "b2", type = "byte")
    public void keyMetod(byte i)
    {
        this.b = i;
    }

    @JsonProcessorData(value = "s2", type = "short")
    public void keyMetod(short i)
    {
        this.s = i;
    }

    @JsonProcessorData(value = "l2", type = "long")
    public void keyMetod(long i)
    {
        this.l = i;
    }

    @JsonProcessorData(value = "f2", type = "float")
    public void keyMetod(float i)
    {
        this.f = i;
    }

    @JsonProcessorData(value = "d2", type = "double")
    public void keyMetod(double i)
    {
        this.d = i;
    }
}

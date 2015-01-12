package com.builtbroken.mc.test;

import com.builtbroken.mc.core.annotation.TestAnnotation;

/**
 * Created by robert on 1/12/2015.
 */
@TestAnnotation
public class TestProcessor
{
    String name;

    public TestProcessor(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}

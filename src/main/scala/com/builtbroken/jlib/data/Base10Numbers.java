package com.builtbroken.jlib.data;

import com.google.common.collect.ImmutableList;

/**
 * Basic number enum used for many different purposes. Mainly as a way
 * to restrict text boxes, or defined a list of numbers.
 * <p/>
 * Created by robert on 1/13/2015.
 */
public enum Base10Numbers
{
    ZERO('0'),
    ONE('1'),
    TWO('2'),
    THREE('3'),
    FOUR('4'),
    FIVE('5'),
    SIX('6'),
    SEVEN('7'),
    EIGHT('8'),
    NINE('9');

    private static ImmutableList<Integer> cache;
    private static ImmutableList<Character> cache_char;
    private static ImmutableList<String> cache_string;

    private final char c;

    Base10Numbers(char c)
    {
        this.c = c;
    }

    public static ImmutableList<Integer> asList()
    {
        if (cache == null)
        {
            ImmutableList.Builder<Integer> l = new ImmutableList.Builder();
            for (int i = 0; i < values().length; i++)
            {
                l.add(i);
            }
            cache = l.build();
        }
        return cache;
    }

    public static ImmutableList<Character> asCharList()
    {
        if (cache_char == null)
        {
            ImmutableList.Builder<Character> l = new ImmutableList.Builder();
            for (int i = 0; i < values().length; i++)
            {
                l.add(values()[i].c);
            }
            cache_char = l.build();
        }
        return cache_char;
    }

    public static ImmutableList<String> asStringList()
    {
        if (cache_string == null)
        {
            ImmutableList.Builder<String> l = new ImmutableList.Builder();
            for (int i = 0; i < values().length; i++)
            {
                l.add("" + values()[i].c);
            }
            cache_string = l.build();
        }
        return cache_string;
    }

    /**
     * Is the input object a valid number or can be converted
     * into a valid number
     * @param obj - object, supports int, float, double, long, char, string
     * @return true if it is a number
     */
    public static boolean isNumber(Object obj)
    {
        if(obj instanceof Integer)
            return true;
        if(obj instanceof Float)
            return true;
        if(obj instanceof Double)
            return true;
        if(obj instanceof Character)
            return asCharList().contains(obj);
        if(obj instanceof String)
        {
            char[] chars = ((String)obj).toCharArray();
            for(char c : chars)
            {
                if(!asCharList().contains(obj))
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public int num()
    {
        return this.ordinal();
    }

    @Override
    public String toString()
    {
        return name().toLowerCase();
    }
}

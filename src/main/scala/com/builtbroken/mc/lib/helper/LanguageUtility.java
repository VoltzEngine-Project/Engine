package com.builtbroken.mc.lib.helper;

import com.builtbroken.mc.lib.helper.wrapper.StringWrapper;

import java.util.List;

/**
 * A class to help you out with strings
 *
 * @author Calclavia, Darkguardsman
 */
public class LanguageUtility
{
	private static StringWrapper.WrappedString wrap(String str)
	{
		return new StringWrapper.WrappedString(str);
	}

	/**
	 * Grabs the localization for the string provided. Make sure the string
     * matches the exact key in a translation file.
     * @param key - translation key, Example 'tile.sometile.name' or 'tile.modname:sometile.name'
     * @return translated key, or the same string provided if the key didn't match anything
	 */
	public static String getLocal(String key)
	{
		return wrap(key).getLocal();
	}

    /**
     * Same as getLocal(String) but appends '.name' if it is missing
     * @param key - translation key, Example 'tile.sometile.name' or 'tile.modname:sometile.name'
     * @return translated key, or the same string provided if the key didn't match anything
     */
    public static String getLocalName(String key)
    {
        return wrap(key + (key.endsWith(".name") ? ".name" : "")).getLocal();
    }

	public static List<String> splitStringPerWord(String string, int characters)
	{
		return wrap(string).listWrap(characters);
	}

    public static String[] splitStringPerWordIntoArray(String string, int characters)
    {
        return wrap(string).wrap(characters);
    }

	public static String capitalizeFirst(String str)
	{
		return wrap(str).capitalizeFirst();
	}

	public static String decapitalizeFirst(String str)
	{
		return wrap(str).decapitalizeFirst();
	}

	public static String toCamelCase(String str)
	{
		return wrap(str).toCamelCase();
	}

	public static String toPascalCase(String str)
	{
		return wrap(str).toPascalCase();
	}

	public static String camelToLowerUnderscore(String str)
	{
		return wrap(str).camelToLowerUnderscore();
	}

	public static String camelToReadable(String str)
	{
		return wrap(str).camelToReadable();
	}

	public static String underscoreToCamel(String str)
	{
		return wrap(str).underscoreToCamel();
	}

	public static String toProperCase(String str)
	{
		return wrap(str).toProperCase();
	}
}

package com.builtbroken.mc.lib.helper;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.helper.wrapper.StringWrapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import java.util.List;

/**
 * A class to help you out with strings
 *
 * @author Calclavia, Darkguardsman
 */
public class LanguageUtility
{
    //Being lazy :P
    private static StringWrapper.WrappedString wrap(String str)
    {
        return new StringWrapper.WrappedString(str);
    }

    /**
     * Grabs the localization for the string provided. Make sure the string
     * matches the exact key in a translation file.
     *
     * @param key - translation key, Example 'tile.sometile.name' or 'tile.modname:sometile.name'
     * @return translated key, or the same string provided if the key didn't match anything
     */
    public static String getLocal(String key)
    {
        //Check for empty or null keys
        if (key == null || key.isEmpty())
        {
            if (Engine.runningAsDev)
            {
                Engine.instance.logger().error("LanguageUtility.getLocal(" + key + ") - invalid key", new RuntimeException());
            }
            return "error.key.empty";
        }
        String translation = wrap(key).getLocal();
        if (translation == null || translation.isEmpty())
        {
            if (Engine.runningAsDev)
            {
                Engine.instance.logger().error("LanguageUtility.getLocal(" + key + ") - no translation", new RuntimeException());
            }
            return key;
        }
        return translation;
    }

    /**
     * Same as getLocal(String) but appends '.name' if it is missing
     *
     * @param key - translation key, Example 'tile.sometile.name' or 'tile.modname:sometile.name'
     * @return translated key, or the same string provided if the key didn't match anything
     */
    public static String getLocalName(String key)
    {
        //Check for empty or null keys
        if (key == null || key.isEmpty())
        {
            if (Engine.runningAsDev)
            {
                Engine.instance.logger().error("LanguageUtility.getLocalName(" + key + ")", new RuntimeException());
            }
            return "error.key.empty";
        }
        return wrap(key + (!key.endsWith(".name") ? ".name" : "")).getLocal();
    }


    /**
     * Uses the language file as a place to store settings
     * for GUI components that use translations. In which
     * the same component may need to change sizes with
     * changes in words.
     *
     * @param key    - string to use to look up the result
     * @param backup - returned if key fails to be found or parsed
     * @return integer parsed from a lang file
     */
    public static Integer getLangSetting(String key, int backup)
    {
        String result = getLocal(key);
        if (result != null && !result.isEmpty())
        {
            try
            {
                return Integer.parseInt(key);
            }
            catch (NumberFormatException e)
            {
                if (Engine.runningAsDev)
                {
                    Engine.instance.logger().error("LanguageUtility.getLangSetting(" + key + ")", e);
                }
            }
        }
        return backup;
    }

    /**
     * Helper version of getLocalName that places the translated string inside
     * minecraft's chat component system.
     *
     * @param key
     * @return
     */
    public static IChatComponent getLocalChat(String key)
    {
        String translation = getLocalName(key);
        if (translation == null || translation.isEmpty())
        {
            if (Engine.runningAsDev)
            {
                Engine.instance.logger().error("LanguageUtility.getLocalChat(" + key + ")", new RuntimeException());
            }
            return new ChatComponentText("error.translation.empty");
        }

        return new ChatComponentText(translation);
    }

    /**
     * Helper method to translate, wrap, then send the msg to the player.
     * Designed to save line length when creating a lot of feed back for the player
     *
     * @param player - player who will receive the message
     * @param key    - - translation key, Example 'tile.sometile.name' or 'tile.modname:sometile.name'
     */
    public static void addChatToPlayer(EntityPlayer player, String key)
    {
        if (player != null)
        {
            player.addChatComponentMessage(getLocalChat(key));
        }
        else if (Engine.runningAsDev)
        {
            Engine.instance.logger().error("LanguageUtility.addChatToPlayer(Null Player, " + key + ")", new RuntimeException());
        }
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

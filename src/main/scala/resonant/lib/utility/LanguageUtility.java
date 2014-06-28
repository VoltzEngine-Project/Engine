package resonant.lib.utility;

import com.google.common.base.CaseFormat;
import net.minecraft.client.resources.I18n;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to help you out with translations.
 *
 * @author Calclavia
 */
public class LanguageUtility
{
	/**
	 * Gets the local text of your translation based on the given key. This will look through your
	 * mod's translation file that was previously registered. Make sure you enter the full name.
	 *
	 * @param key - e.g tile.block.name
	 * @return The translated string or the default English translation if none was found.
	 */
	public static String getLocal(String key)
	{
		return I18n.format(key);
	}

	public static List<String> splitStringPerWord(String string, int wordsPerLine)
	{
		String[] words = string.split(" ");
		List<String> lines = new ArrayList();

		for (int lineCount = 0; lineCount < Math.ceil((float) words.length / (float) wordsPerLine); lineCount++)
		{
			String stringInLine = "";

			for (int i = lineCount * wordsPerLine; i < Math.min(wordsPerLine + lineCount * wordsPerLine, words.length); i++)
			{
				stringInLine += words[i] + " ";
			}

			lines.add(stringInLine.trim());
		}

		return lines;
	}

	public static List<Integer> decodeIDSplitByComma(String string)
	{
		List<Integer> intList = new ArrayList<Integer>();

		if (string != null)
		{
			for (String IDString : string.split(","))
			{
				if (IDString != null && !IDString.isEmpty())
				{
					try
					{
						int contentID = Integer.parseInt(IDString);
						intList.add(contentID);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}

		return intList;
	}

	public static String capitalizeFirst(String str)
	{
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	public static String decapitalizeFirst(String str)
	{
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

	public static String toCamelCase(String s)
	{
		return LanguageUtility.decapitalizeFirst(toPascalCase(s));
	}

	public static String toPascalCase(String s)
	{
		String[] parts = s.split("_");
		String camelCaseString = "";

		for (String part : parts)
		{
			camelCaseString = camelCaseString + toProperCase(part);
		}

		return camelCaseString;
	}

	public static String camelToLowerUnderscore(String s)
	{
		return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, s);
	}

	public static String underscoreToCamel(String s)
	{
		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, s);
	}

	public static String toProperCase(String s)
	{
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}
}

package calclavia.lib;

import java.util.ArrayList;
import java.util.List;

public class Calclavia
{
	public static final String RESOURCE_DIRECTORY = "/mods/calclavia/";
	public static final String TEXTURE_DIRECTORY = RESOURCE_DIRECTORY + "textures/";
	public static final String GUI_DIRECTORY = TEXTURE_DIRECTORY + "gui/";
	public static final String GUI_COMPONENTS = GUI_DIRECTORY + "gui_components.png";
	public static final String GUI_BASE_FILE = GUI_DIRECTORY + "gui_base.png";
	public static final String GUI_EMPTY_FILE = GUI_DIRECTORY + "gui_empty.png";

	public static List<String> splitStringPerWord(String string, int wordsPerLine)
	{
		String[] words = string.split(" ");
		List<String> lines = new ArrayList<String>();

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
}

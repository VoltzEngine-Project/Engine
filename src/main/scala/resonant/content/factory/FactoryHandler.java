package resonant.content.factory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Master class used to track, load, and handle any object that is used to create new objects.
 *
 * @author Darkguardsman
 */
public class FactoryHandler
{
	protected HashMap<String, Factory> factories;
	private HashMap<String, List<Factory>> fileExtensions;
	private String modID;

	public FactoryHandler(String modID)
	{
		this.modID = modID;
		factories = new HashMap();
		fileExtensions = new HashMap();
	}

	/**
	 * Called to generate an object using a type and some data
	 */
	public Object generate(String type, String objectName, Object... data)
	{
		if (factories.containsKey(type))
		{
			return factories.get(type).generate(modID, objectName, data);
		}
		return null;
	}

	/**
	 * Called to load an object from a file
	 */
	public Object generate(File file)
	{
		if (file != null)
		{
			String extension = file.getName();
			String[] sub = extension.split(".");
			extension = sub[sub.length - 1];
			List<Factory> list = null;
			Object obj = null;

			//Time saver by using the file extension cache
			if (fileExtensions.containsKey(extension))
			{
				list = fileExtensions.get(extension);
				for (Factory factory : list)
				{
					if (factory.canGenerateFromFile(file))
					{
						Object a = factory.generate(file);
						if (a != null)
						{
							obj = a;
							break;
						}
					}
				}
			}
			if (list != null)
			{
				list = new ArrayList<Factory>();
			}

			//Normal factory finder that builds the file extension cache as a result of its normal iteration
			for (Factory factory : factories.values())
			{
				if (factory.canGenerateFromFile(file))
				{
					Object a = factory.generate(file);
					if (a != null)
					{
						obj = a;
						list.add(factory);
						break;
					}
				}
			}
			fileExtensions.put(extension, list);
			return obj;
		}
		return null;
	}
}

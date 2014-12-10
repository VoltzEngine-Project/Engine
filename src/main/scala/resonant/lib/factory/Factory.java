package resonant.lib.factory;

import java.io.File;

/**
 * Basic prefab for any object that will be used to automatically create new objects.
 *
 * @author Darkguardsman
 */
public class Factory
{
	protected String modID;
	protected String prefix;

	public Factory(String modID, String prefix)
	{
		this.modID = modID;
		this.prefix = prefix;
	}

	public Object generate(String objectName, Object... data)
	{
		return null;
	}

	public Object generate(File file)
	{
		return null;
	}

	public boolean canGenerateFromFile(File file)
	{
		return false;
	}
}

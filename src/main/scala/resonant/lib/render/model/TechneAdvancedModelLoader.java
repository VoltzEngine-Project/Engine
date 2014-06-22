package resonant.lib.render.model;

import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.IModelCustomLoader;
import net.minecraftforge.client.model.ModelFormatException;

import java.net.URL;

public class TechneAdvancedModelLoader implements IModelCustomLoader
{

	private static final String[] types = { "tcn" };

	@Override
	public String getType()
	{
		return "Techne model";
	}

	@Override
	public String[] getSuffixes()
	{
		return types;
	}

	@Override
	public IModelCustom loadInstance(String resourceName, URL resource) throws ModelFormatException
	{
		return new TechneAdvancedModel(resourceName, resource);
	}

}

package resonant.lib.render.model;

import java.net.URL;

import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.IModelCustomLoader;
import net.minecraftforge.client.model.ModelFormatException;

public class TechneAdvancedModelLoader implements IModelCustomLoader
{

    @Override
    public String getType()
    {
        return "Techne model";
    }

    private static final String[] types = { "tcn" };

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

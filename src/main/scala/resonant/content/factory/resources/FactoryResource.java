package resonant.content.factory.resources;

import resonant.content.factory.Factory;

/**
 * Created by robert on 8/22/2014.
 */
public class FactoryResource extends Factory
{
    protected ResourceFactoryHandler gen;
    public FactoryResource(ResourceFactoryHandler gen, String modID, String prefix)
    {
        super(modID, prefix);
        this.gen = gen;
    }
}

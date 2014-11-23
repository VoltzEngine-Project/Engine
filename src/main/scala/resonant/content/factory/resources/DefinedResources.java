package resonant.content.factory.resources;

/**
 * Generic set of ore types that are commonly found in most mods, and MC itself
 *
 * @author Darkguardsman
 */
public enum DefinedResources
{
	COPPER(true),
	TIN(true),
	IRON(false),
	SILVER(true),
	GOLD(false),
    LEAD(true),
    ZINC(true),
    NICKEL(true),
    ALUMINIUM(true),
    MAGNESIUM(true);

    boolean shouldGenerate;
    int minY = 1;
    int maxY = 100;

	private DefinedResources(boolean gen)
	{
        this.shouldGenerate = gen;
	}
}

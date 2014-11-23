package resonant.content.factory.resources;

/**
 * Generic set of ore types that are commonly found in most mods, and MC itself
 *
 * @author Darkguardsman
 */
public enum DefinedOres
{
	COPPER(true),
	TIN(true),
	IRON(false),
	SILVER(true),
	GOLD(false);

    boolean shouldGenerate;
    int minY = 1;
    int maxY = 100;

	private DefinedOres(boolean gen)
	{
        this.shouldGenerate = gen;
	}
}

package universalelectricity.compatibility;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 
 * A module to extend for compatibility with other energy systems.
 * 
 * @author Calclavia
 * 
 */
public abstract class CompatibilityModule
{
	public static final Set<CompatibilityModule> loadedModules = new LinkedHashSet<CompatibilityModule>();

	public abstract boolean isHandler(Object obj);
	
	
}

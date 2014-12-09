package resonant.lib.mod.loadable;

/**
 * An interface to apply on mostly integration modules, and to be called on the Main mod boot cycle
 *
 * @author tgame14
 * @since 23/02/14
 */
public interface ICompatProxy extends ILoadable
{
	public String modId();
}

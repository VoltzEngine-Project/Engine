package resonant.lib.modproxy;

/** An interface to apply on mostly integration modules, and to be called on the Main mod boot cycle
 * 
 * @since 23/02/14
 * @author tgame14 */
public interface ICompatProxy
{
    public void preInit();

    public void init();

    public void postInit();

    public String modId();
}

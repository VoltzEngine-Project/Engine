package resonant.lib.content;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Used to handle info about the block that would normally be handled by the mod main class. Use the
 * BlockRegistry in order for these methods to be called on load of the mod.
 * 
 * @author DarkGuardsman */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BlockInfo
{
    /** List of all tileEntities this block needs */
    public String[] tileEntity();

    public String[] renderer() default {};

}

/**
 * 
 */
package universalelectricity.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.EnumSet;

import universalelectricity.api.Compatibility.CompatibilityType;

/**
 * @author Calclavia
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UniversalClass
{
	/**
	 * The mods to integrate with.
	 * 
	 * e.g: "INDUSTRIALCRAFT;THERMAL_EXPANSION" <- Enable IC and TE compatibility.
	 * e.g: "" <- Enable all mod compatibility
	 * 
	 * @return Return an empty string to be compatible with all available mods, or each
	 * CompatibilityType's enum.name separated by semi-columns.
	 * 
	 */
	public String integration() default "";
}

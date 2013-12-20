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
	public String integration() default "all";
}

package resonant.content.loader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be applied to fields to dictate that the name of the block/item registered will be of the same name as the field.
 *
 * @author Calclavia
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ImplicitContentName
{
}

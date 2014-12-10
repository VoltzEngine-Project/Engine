package resonant.junit.world;

import net.minecraft.util.RegistryNamespaced;

/** Injected into Block.class during units tests to prevent FML from
 * loading and crashing the unit test due to it not checking before
 * casting its class loader.
 *
 * Created by robert on 11/20/2014.
 */
public class FakeRegistryNamespaced extends RegistryNamespaced
{
    //Just in case i need to override methods
}

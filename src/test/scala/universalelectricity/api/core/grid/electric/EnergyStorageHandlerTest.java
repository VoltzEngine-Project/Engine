package universalelectricity.api.core.grid.electric;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author tgame14
 * @since 17/06/14
 */
public class EnergyStorageHandlerTest
{

	@Test
	public void testModifyEnergyStored() throws Exception
	{
		EnergyStorageHandler handler = new EnergyStorageHandler(10000, 1000);
		handler.setEnergy(5000);

		handler.modifyEnergyStored(5001);
		Assert.assertTrue("tests energy modification and capacity consideration", handler.getEnergy() == 10000 && handler.getEnergy() <= handler.getEnergyCapacity() && handler.getEmptySpace() == 0);

	}

	@Test
	public void testReceiveEnergy() throws Exception
	{
		EnergyStorageHandler handler = new EnergyStorageHandler(10000, 1000);
		double receive = 5000;

		double result1 = handler.receiveEnergy(receive, true);
		Assert.assertEquals(1000, result1, receive - result1);

		double result2 = handler.receiveEnergy(receive, false);
		Assert.assertEquals(1000, result2, receive - result2);


	}

	@Test
	public void testExtractEnergy() throws Exception
	{
		EnergyStorageHandler handler = new EnergyStorageHandler(10000, 1000);
		Assert.assertTrue("check energy extraction", handler.checkExtract());
	}

	@Test
	public void testIsFull() throws Exception
	{
		EnergyStorageHandler handler = new EnergyStorageHandler(10000, 1000);
		Assert.assertFalse("checks wether when not full, will return false", handler.isFull());
		handler.setEnergy(handler.getEnergyCapacity());
		Assert.assertTrue("checks wether isFull is proper", handler.isFull());
	}

	@Test
	public void testIsEmpty() throws Exception
	{

		EnergyStorageHandler handler = new EnergyStorageHandler(10000, 1000);
		Assert.assertTrue("checks wether when not empty, will return false", handler.isEmpty());
		handler.setEnergy(1);
		Assert.assertFalse("checks wether isFull is proper", handler.isEmpty());
	}
}

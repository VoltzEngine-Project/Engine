package resonant.lib.grid;

import universalelectricity.api.core.grid.IUpdate;
import universalelectricity.core.transform.vector.Vector3;

import java.util.HashMap;

/**
 * Basic grid like system that logs data in an area to be accessed later on. Designed to be used for things like thermal, or air mapping of the game.
 *
 * @author Darkguardsman
 */
public class DataGrid implements IUpdate
{
	//TODO save data per chunk to reduce loading a ton of data
	//TODO implement a chunk load/unload handler
	//TODO implement thermal map
	//TODO implement air map
	//TODO implement player event map
	private HashMap<Vector3, HashMap<String, Object[]>> dataMap;
	private HashMap<String, DataHandler> dataHandlers;

	public DataGrid()
	{
		dataMap = new HashMap();
		dataHandlers = new HashMap();
	}

	@Override
	public void update(double deltaTime)
	{
		//TODO call to each data handler allowing it to update any data
		//TODO cache changes to the data to better improve update speeds
	}

	@Override
	public boolean canUpdate()
	{
		return true;
	}

	@Override
	public boolean continueUpdate()
	{
		return true;
	}
}

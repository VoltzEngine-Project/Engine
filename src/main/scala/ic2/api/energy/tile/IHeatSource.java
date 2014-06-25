package ic2.api.energy.tile;

public interface IHeatSource {
	
	/*
	 *  @return max heat transfer bandwidth / tick
	 */
	
	int getMaxDrawHeatSize();
	
	/*
	 * @param requested amount of heat to transfer
	 * 
	 * @return transmitted amount of heat
	 * 
	 * example: You Request 100 units of heat but the Source have only 50 units left
	 * 
	 * requestDrawHeat(100) : return 50 : so 50 units of heat remove from HeatSource
	 */
	
	int requestDrawHeat(int requestheat);

}

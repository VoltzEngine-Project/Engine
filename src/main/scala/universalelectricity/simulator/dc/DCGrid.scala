package universalelectricity.simulator.dc

import universalelectricity.api.core.grid.IUpdate
import universalelectricity.simulator.dc.component.SeriesComponent
import universalelectricity.simulator.grid.LinkedGrid

/**
 * Basic network of parts that function together to simulate a collection of co-existing tiles.
 * @author Darkguardsman, Calclavia
 */
class DCGrid extends LinkedGrid with IUpdate
{
  private var circuit: SeriesComponent = _

  /** Called each update to simulate changes */
  override def updateSimulation()
  {
    super.updateSimulation()
    circuit.solve()
  }
}
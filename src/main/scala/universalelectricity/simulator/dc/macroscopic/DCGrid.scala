package universalelectricity.simulator.dc.macroscopic

import universalelectricity.api.core.grid.IUpdate
import universalelectricity.simulator.dc.macroscopic.component.SeriesComponent
import universalelectricity.simulator.grid.SimulatedGrid

/**
 * Basic network of parts that function together to simulate a collection of co-existing tiles.
 * @author Darkguardsman, Calclavia
 */
class DCGrid extends SimulatedGrid with IUpdate
{
  private var circuit: SeriesComponent = _

  /** Called each update to simulate changes */
  override def updateSimulation()
  {
    super.updateSimulation()
    circuit.solve()
  }
}
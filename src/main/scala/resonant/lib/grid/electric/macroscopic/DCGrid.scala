package resonant.lib.grid.electric.macroscopic

import resonant.api.grid.IUpdate
import universalelectricity.api.core.grid.IUpdate
import resonant.lib.grid.electric.macroscopic.component.SeriesComponent
import SimulatedGrid

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
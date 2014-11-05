package resonant.lib.grid.electric.macroscopic

import resonant.api.grid.IUpdate
import resonant.lib.grid.electric.macroscopic.component.SeriesComponent

/**
 * Basic network of parts that function together to simulate a collection of co-existing tiles.
 * @author Darkguardsman, Calclavia
 */
class DCGrid extends PathGrid with IUpdate
{
  private var circuit: SeriesComponent = _

  /** Called each update to simulate changes */
  override def updateSimulation()
  {
    super.updateSimulation()
    circuit.solve()
  }
}
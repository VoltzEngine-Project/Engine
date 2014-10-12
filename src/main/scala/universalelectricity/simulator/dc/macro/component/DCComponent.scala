package universalelectricity.simulator.dc

/**
 * A component is defined as part of the circuit that have well defined current and voltage
 * @author Calclavia
 */
trait DCComponent
{
  protected var current = 0D
  protected var voltage = 0D

  def getCurrent = current

  def setCurrent(current: Double)
  {
    this.current = Math.max(current, 0)

    //Calculate each component's voltage using Ohm's Law. V = IR
    voltage = current * resistance
  }

  def getVoltage = voltage

  def setVoltage(voltage: Double)
  {
    this.voltage = Math.max(voltage, 0)

    //Calculate each component's current using Ohm's Law. V = IR
    current = voltage / resistance
  }

  def resistance: Double

  def solve()
  {

  }
}
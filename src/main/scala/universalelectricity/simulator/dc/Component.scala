package universalelectricity.simulator.dc

/**
 * A component is defined as "one block" part of the circuit
 * @author Calclavia
 */
abstract class Component
{
  protected var current = 0D
  protected var voltage = 0D

  def setVoltage(voltage: Double)
  {
    this.voltage = voltage
    current = voltage / getResistance
  }

  def getResistance: Double
}
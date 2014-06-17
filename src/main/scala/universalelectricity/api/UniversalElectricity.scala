package universalelectricity.api

/**
 * General Universal Electricity variable reference class.
 *
 * @author Calclavia
 */
object UniversalElectricity
{
  final val ID = "UniversalElectricity"
  final val NAME = "Universal Electricity"

  /**
   * The version of the Universal Electricity API.
   */
  final val MAJOR_VERSION = "@MAJOR@"
  final val MINOR_VERSION = "@MINOR@"
  final val REVISION_VERSION = "@REVIS@"
  final val BUILD_VERSION = "@BUILD@"
  final val VERSION = MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION_VERSION
}
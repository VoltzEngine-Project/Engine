package universalelectricity.core.transform

/**
 * @author Calclavia
 */
trait TraitVector[T <: TraitVector[T]] extends TraitOperation[T]
{
  /**
   * Magnitudes
   */
  final def dot(other: T) = $(other)

  def $(other: T): Double

  final def magnitudeSquared: Double = this.asInstanceOf[T] $ this.asInstanceOf[T];

  final def magnitude = Math.sqrt(magnitudeSquared)

  final def normalize = this / magnitude

  final def distance(other: T) = (other - this.asInstanceOf[T]).magnitude

  final def midpoint(other: T): T = (this + other) / 2
}

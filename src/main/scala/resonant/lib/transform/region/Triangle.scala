package resonant.lib.transform.region

import resonant.lib.transform.vector.IVector2

/** Triangle shape. Assumes that connections go
  * a -> b -> c -> a forming a triangle shape
 * Created by robert on 12/14/2014.
 */
class Triangle(var a: IVector2, var b: IVector2, var c: IVector2)
{
  def area(): Double =
  {
    return (a.x * (b.y - c.y) + b.x * (c.y - a.y) + c.x * (a.y - b.y)) /  2
  }
}
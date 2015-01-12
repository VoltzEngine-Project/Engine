package com.builtbroken.mc.lib.transform.region

import com.builtbroken.mc.lib.transform.vector.Pos2D

/** Simple point with a radius. Can be used for just about anything including
  * Collision boxes, detection areas, effects, and GUIs
  *
  * Yes i put circles in MC
  * ....................../´¯/)
  * ....................,/¯../
  * .................../..../ 
  * ............./´¯/'...'/´¯¯·¸
  * ........../'/.../..../......./¨¯\
  * ........('(...´...´.... ¯~/'...')
  * .........\.................'..../
  * ..........\.....\.......... _.·´
  * ............\..............(
  * ..............\.............\...
 * Created by robert on 12/17/2014.
 */
class Circle(var center: Pos2D, var r: Double) extends Shape[Circle]
{
  def this(center: Pos2D)
  {
    this(center, 1)
  }

  def getArea: Double =
  {
    return Math.PI * (r * r)
  }

  def isWithin(x: Double, y: Double): Boolean =
  {
    return center.distance(new Pos2D(x, y)) <= r
  }

  override def set(other: Circle): Circle =
  {
    this.center = other.center
    this.r = other.r
    return this
  }

  override def +(amount: Double): Circle = new Circle(center, r  + amount)

  override def +(amount: Circle): Circle = new Circle(center.midpoint(amount.center), r + amount.r)

  override def *(amount: Double): Circle = new Circle(center, r  * amount)

  override def *(amount: Circle): Circle = new Circle(center.midpoint(amount.center), r * amount.r)

  /** Distance the shape takes in the X axis */
  override def getSizeX: Double = r

  /** Distance the shape takes in the Y axis */
  override def getSizeY: Double = r
}
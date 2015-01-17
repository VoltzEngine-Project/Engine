package com.builtbroken.slib.wrapper

import com.builtbroken.jlib.data.vector.IPos2D
import com.builtbroken.mc.lib.transform.vector.Point

/**
 * Created by robert on 1/11/2015.
 */
object Pos2DScalaWrapper
{
  implicit class Pos2DScalaWrapper(pos: IPos2D)
  {
    def +(other: IPos2D) : Point = new Point(pos.x() + other.x(), pos.y() + other.y())

    def +(a: Double) : Point = new Point(pos.x() + a, pos.y() + a)

    def +=(other: IPos2D) : Point = new Point(pos.x() + other.x(), pos.y() + other.y())


    def -(other: IPos2D) : Point = new Point(pos.x() - other.x(), pos.y() - other.y())

    def -(a: Double) : Point = new Point(pos.x() - a, pos.y() - a)

    def -=(other: IPos2D) : Point = new Point(pos.x() - other.x(), pos.y() - other.y())


    def *(other: IPos2D) : Point = new Point(pos.x() * other.x(), pos.y() * other.y())

    def *(a: Double) : Point = new Point(pos.x() * a, pos.y() * a)

    def *=(other: IPos2D) : Point = new Point(pos.x() * other.x(), pos.y() * other.y())


    def /(other: IPos2D) : Point = new Point(pos.x() / other.x(), pos.y() / other.y())

    def /(a: Double) : Point = new Point(pos.x() / a, pos.y() / a)

    def /=(other: IPos2D) : Point = new Point(pos.x() / other.x(), pos.y() / other.y())
  }
}

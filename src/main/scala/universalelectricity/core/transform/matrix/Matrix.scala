package universalelectricity.core.transform.matrix

import scala.Array._

/**
 * @author Calclavia
 */
object Matrix
{
  implicit def arrayToMatrix(m: Array[Array[Double]]) = new Matrix(m)
}

class Matrix(val row: Int, val column: Int)
{
  val matrix = ofDim[Double](row, column)

  def this(m: Array[Array[Double]])
  {
    this(m.size, m(0).size)

    for (x <- 0 until m.size; y <- 0 until m(0).size)
    {
      matrix(x)(y) = m(x)(y)
    }
  }

  def apply(row: Int)(column: Int): Double = matrix(row)(column)

  /**
   * Multiplies two matrices. This is non-commutative.
   */
  def *(otherMatrix: Matrix): Matrix =
  {
    val res = new Matrix(matrix.length, otherMatrix.column)

    for (row <- 0 until row; col <- 0 until otherMatrix.column; i <- 0 until column)
    {
      res.matrix(row)(col) += matrix(row)(i) * otherMatrix(i)(col)
    }

    return res
  }

  def multiply(otherMatrix: Matrix) = this * otherMatrix
}

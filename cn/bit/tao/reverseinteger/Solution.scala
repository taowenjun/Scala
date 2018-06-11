package cn.bit.tao.reverseinteger

/**
 * Reverse Integer:将整数翻转
 * @author Tao Wenjun
 */
object Solution {
  def reverse(x: Int): Int = {
    val s = if(x<0) x.toString().tail else x.toString()
    val d = s.reverse.toDouble*x.signum
    return if(d.isValidInt) d.toInt else 0
  }
  
  def main(args: Array[String]): Unit = {
    println(reverse(123))
    println(reverse(-123))
  }
}
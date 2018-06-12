package cn.bit.tao.stringtointeger

/**
 * ×Ö·û´®×ªÕûÊý
 * @author Tao Wenjun
 */
object Solution {
  def myAtoi(str: String): Int = {
        """^\s*([+-]?)0*(\d+)""".r.findFirstMatchIn(str) match {
            case None => 0
            case Some(m) => {
                val sign = m.group(1)
                val nums = m.group(2)
                if(nums.length>10){
                    if(sign=="-") Int.MinValue else Int.MaxValue
                }else{
                    (sign+nums).toLong match {
                        case n if n>Int.MaxValue =>Int.MaxValue
                        case n if n<Int.MinValue =>Int.MinValue
                        case n=>n.toInt
                    }
                }
            }
        }
    }
  
  def main(args: Array[String]): Unit = {
    println(myAtoi("4193 with words"))
    println(myAtoi("words 987 and"))
  }
  
}
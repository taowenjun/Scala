package cn.bit.tao.longest_substring_without_repeating_characters

import scala.annotation.tailrec
import scala.collection.mutable
/**
 * 求字符串中没有重复字符的子串的最大长度
 * @author Tao Wenjun
 */
object Solution {
    def lengthOfLongestSubstring(s: String): Int = {
        val set = new mutable.HashSet[Char]()
        @tailrec
        def _do(from: Int, to: Int, max: Int): Int = {
            if(to==s.length) max
            else if(set.contains(s.charAt(to))){
                set.remove(s.charAt(from))
                _do(from+1,to,max)
            }else{
                set+=s.charAt(to)
                val m = if(to-from<max) max else max+1
                _do(from,to+1,m)
            }
        }
        _do(0,0,0)
    }
    
    def main(args: Array[String]): Unit = {
      println(lengthOfLongestSubstring("abcabcbb"))
    }
}
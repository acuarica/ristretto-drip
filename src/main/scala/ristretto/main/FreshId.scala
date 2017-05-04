package ristretto.main

object FreshId {
  var next = 0
  def freshId(prefix: String) = {
    next = next + 1
    s"$prefix$next"
  }
}

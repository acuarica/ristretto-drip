package ristretto.perc

import ristretto.perc.PercSyntax._

trait Dataflow[A] {

  import CFG._

  def init(s: Stm): A
  def transfer(s: Stm, in: A): A
  def meet(a: A, b: A): A

  def flow(p: Root): Unit = p match {
    case Root(procs) =>
      for (p <- procs) {
        flow(p)
      }
  }

  def flow(p: Proc) = p match {
    case Proc(f, xs, body) =>
      val g = CFG.graph(body)
      var m: Map[Node, A] = Map()

      for (n <- g.nodes) {
        m += (n -> init(n.body))
      }

      var changed: Boolean = true

      while (changed) {
        changed = false
        for (n <- g.nodes) {
          transfer(n.body, m(n))
          n.preds.toList match {
            case Nil =>
            case p::ps =>
              val in: A = ps.foldLeft(m(p)) {
                case (a, p) => meet(a, m(p))
              }
              val out = transfer(n.body, in)
              if (out != m(n)) {
                changed = true
              }
              m += (n -> out)
          }
        }
      }
  }
}

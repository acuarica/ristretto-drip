package ristretto.perc

import ristretto.perc.PercSyntax._
import scala.collection.mutable.ListBuffer

object CFG {
  type Name = String

  class Node(val body: Stm) {
    val preds: ListBuffer[Node] = ListBuffer()
    val succs: ListBuffer[Node] = ListBuffer()
  }

  class CFG(val entry: Node, val nodes: List[Node])

  def graph(ss: List[Stm]): CFG = {
    val nodes = graph(ss, Map.empty)._1
    new CFG(nodes.head, nodes)
  }

  private def graph(ss: List[Stm], labels: Map[Name, Node]): (List[Node], Map[Name, Node]) = ss match {
    case Nil =>
      val n = new Node(Nop())
      (n::Nil, labels)
    case (s @ LabelStm(l))::ss =>
      val n = new Node(s)
      val (succ::nodes, labels1) = graph(ss, labels + (l -> n))
      n.succs += succ
      succ.preds += n
      (n::succ::nodes, labels1)
    case (s @ Jmp(l))::ss =>
      val n = new Node(s)
      val (nodes, labels1) = graph(ss, labels)
      labels1.get(l) match {
        case Some(succ) =>
          n.succs += succ
          succ.preds += n
        case None =>
      }
      (n::nodes, labels1)
    case (s @ CJmp(cmp, e, l))::ss =>
      val n = new Node(s)
      val (succ::nodes, labels1) = graph(ss, labels)
      n.succs += succ
      succ.preds += n
      labels1.get(l) match {
        case Some(target) =>
          n.succs += target
          target.preds += n
        case None =>
      }
      (n::nodes, labels1)
    case s::ss =>
      val n = new Node(s)
      val (succ::nodes, labels1) = graph(ss, labels)
      n.succs += succ
      succ.preds += n
      (n::succ::nodes, labels1)
  }
}

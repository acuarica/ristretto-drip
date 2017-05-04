package ristretto.main

////////////////////////////////////////////////////////////////
// Compiler error handling
////////////////////////////////////////////////////////////////
trait Errors {

  import org.bitbucket.inkytonik.kiama.util.Positions
  import ristretto.frontend.RistrettoSyntax.ASTNode

  class CompilerError(s: String) extends Exception(s)

  object Posns extends Positions

  private var errorCount = 0

  type Position = Object

  def fatalError(t: ASTNode, s: String): Nothing = {
    error(t, s)
    throw new CompilerError(s)
  }
  
  def fatalError(s: String): Nothing = {
    error(s)
    throw new CompilerError(s)
  }

  def error(t: ASTNode, s: String) = {
    Posns.getStart(t) match {
      case Some(p) =>
        println(p.format + " " + s)
      case None =>
        println(s)
    }
    errorCount += 1
  }

  def error(s: String) = {
    println(s)
    errorCount += 1
  }

  def hasErrors = errorCount > 0
}

package org.multibot

import org.scalatest.FunSuite

class InterpretersCacheTest extends FunSuite {

  test("testScalaInterpreter") {
    val r = InterpretersCache(Nil).scalaInterpreter("hello"){(imain, out) =>
      imain.interpret("1")
      out.toString.trim
    }
    assert(r === "res0: Int = 1")
  }

}

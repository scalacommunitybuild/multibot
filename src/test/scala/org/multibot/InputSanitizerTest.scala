package org.multibot

import org.scalatest.Assertion
import org.scalatest.flatspec.AnyFlatSpec

class InputSanitizerTest extends AnyFlatSpec {

  def ensureSanitizedInput(in: String, expected: String): Assertion =
    assert(GitterInputSanitizer.sanitize(in) === expected)

  def ensureNoSanitization(in: String) : Assertion =
    ensureSanitizedInput(in, in)

  "Inputs" should "be sanitized" in {
    ensureSanitizedInput("```foo```", "foo")
    ensureSanitizedInput("`foo`", "foo")

    // I have no idea why anyone would do this, but make sure the result is expected
    ensureSanitizedInput("````foo````", "`foo`")
    ensureSanitizedInput("``foo``", "`foo`")
  }

  it should "not be sanitized" in {
    ensureNoSanitization("foo")
  }

  it should "parse cmd" in {
    "[edit] ! `foo` abc" match {
      case Cmd("!" :: "`foo` abc" :: Nil) => true
    }
    "! `foo abc`" match {
      case Cmd("!" :: "`foo abc`" :: Nil) => true
    }
    "! foo abc" match {
      case Cmd("!" :: "foo abc" :: Nil) => true
    }
  }
}

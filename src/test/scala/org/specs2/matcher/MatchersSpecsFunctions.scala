package org.specs2
import matcher.MustExpectations._
import matcher.StringMatchers._

package object matcher {
  
  implicit def ToReturns[T](t: =>MatchResult[T]): Returns[T] = new Returns(t)
  class Returns[T](t: =>MatchResult[T]) {
    def returns(m: String) = t must contain(m) ^^ { (m: MatchResult[T]) => m.message }
  }
}

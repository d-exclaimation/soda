//
//  Common.scala
//  soda
//
//  Created by d-exclaimation on 2:28 PM.
//

package io.github.dexclaimation.soda.codegen.generate

object Common {
  /** Indentation */
  def indent(n: Int = 1)(t: => String): String = (1 to n).map(_ => "  ").mkString + t

  /** Create a package requirement */
  def pkgInit(
    pkg: String
  ): String = s"package $pkg\n\nimport io.github.dexclaimation.soda.schema._\nimport sangria.schema._\n\n"
}

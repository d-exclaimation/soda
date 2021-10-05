//
//  Union.scala
//  soda
//
//  Created by d-exclaimation on 2:35 PM.
//

package io.github.dexclaimation.soda.codegen.generate

import io.github.dexclaimation.soda.codegen.generate.Common.pkgInit
import sangria.ast.UnionTypeDefinition

object Union {

  /** Make the compilation for the UnionType */
  def apply(uni: UnionTypeDefinition, pkg: String): String = {
    val members = uni.types.map(SodaGql.certainType).mkString(", ")
    s"""
       |${pkgInit(pkg)}object ${uni.name} extends SodaUnionType[Unit]("${uni.name}") {
       |  def definition: Def = { t =>
       |    t.members($members)
       |  }
       |}
       |""".stripMargin
  }
}

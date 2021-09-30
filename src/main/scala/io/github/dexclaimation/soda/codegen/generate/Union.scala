//
//  Union.scala
//  soda
//
//  Created by d-exclaimation on 2:35 PM.
//

package io.github.dexclaimation.soda.codegen.generate

import io.github.dexclaimation.soda.codegen.generate.Common.PACKAGE_INIT
import sangria.ast.UnionTypeDefinition

object Union {
  def apply(uni: UnionTypeDefinition): String = {
    val members = uni.types.map(SodaGql.certainType).mkString(", ")
    s"""
       |${PACKAGE_INIT}object ${uni.name} extends SodaUnionType[Unit]("${uni.name}") {
       |  def definition: Def = { t =>
       |    t.members($members)
       |  }
       |}
       |""".stripMargin
  }
}

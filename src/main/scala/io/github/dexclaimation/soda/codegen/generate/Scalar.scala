//
//  Scalar.scala
//  soda
//
//  Created by d-exclaimation on 3:33 PM.
//

package io.github.dexclaimation.soda.codegen.generate

import sangria.ast.ScalarTypeDefinition

object Scalar {
  /** Make the compilation for the ScalarType */
  def apply(s: ScalarTypeDefinition, pkg: String): String = {
    val PACKAGE_INIT =
      s"""package $pkg
         |import io.github.dexclaimation.soda.schema._
         |import sangria.ast
         |import sangria.validation.ValueCoercionViolation
         |""".stripMargin

    s"""
       |$PACKAGE_INIT
       |object ${s.name} extends SodaScalar[Any]("${s.name}") {
       |  case object ${s.name}Violation extends ValueCoercionViolation("Not implemented yet")
       |
       |  def serialize: Serializer = { (_, _) =>
       |    throw new Error("Not implemented yet")
       |  }
       |
       |  def parseValue: Parser[Any] = {
       |    _ => Left(${s.name}Violation)
       |  }
       |
       |  def parseLiteral: Parser[ast.Value] = {
       |    _ => Left(${s.name}Violation)
       |  }
       |}
       |""".stripMargin
  }
}

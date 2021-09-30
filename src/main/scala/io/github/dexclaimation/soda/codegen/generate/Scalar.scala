//
//  Scalar.scala
//  soda
//
//  Created by d-exclaimation on 3:33 PM.
//

package io.github.dexclaimation.soda.codegen.generate

import sangria.ast.ScalarTypeDefinition

object Scalar {
  def apply(s: ScalarTypeDefinition): String = {
    val PACKAGE_INIT =
      s"""
         |import io.github.dexclaimation.soda.schema._
         |import sangria.ast
         |import sangria.validation.ValueCoercionViolation
         |""".stripMargin

    s"""
       |$PACKAGE_INIT
       |
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

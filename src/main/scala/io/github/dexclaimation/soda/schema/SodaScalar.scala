//
//  SodaScalar.scala
//  soda
//
//  Created by d-exclaimation on 12:51 AM.
//

package io.github.dexclaimation.soda.schema

import sangria.ast
import sangria.marshalling.MarshallerCapability
import sangria.schema.ScalarType
import sangria.validation.Violation

abstract class SodaScalar[Val](name: String) {

  /** Serialize scalar to the proper JSON Serialized form */
  type Serializer = (Val, Set[MarshallerCapability]) => Any

  /** Parse given input to either the Value or a violation */
  type Parser[Input] = Input => Either[Violation, Val]

  def desc: String = ""

  def serialize: Serializer

  def parseValue: Parser[Any]

  def parseLiteral: Parser[ast.Value]

  /**
   * Sangria Scalar Type derivation
   */
  lazy final val t: ScalarType[Val] = ScalarType[Val](
    name = name,
    description = if (desc.isEmpty) None else Some(desc),
    coerceInput = parseLiteral,
    coerceUserInput = parseValue,
    coerceOutput = serialize
  )
}

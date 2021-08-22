//
//  SodaInputType.scala
//  graphql-soda
//
//  Created by d-exclaimation on 6:24 PM.
//

package io.github.dexclaimation.graphqlSoda.schema

import sangria.schema.{InputField, InputObjectType}

/**
 * Soda Implementable Object Type definition.
 *
 * Help to avoid nesting, avoid prefixing with `Types`
 *
 * Accessing the InputObjectType from `t: InputObjectType` properties
 *
 * @param name Name of the Input Object.
 * @tparam Val Value paired for this Object (*best to implement this on a case class's companion object)
 */
abstract class SodaInputType[Val](name: String) {
  def description: String = ""

  def definition: List[InputField[Val]]

  /**
   * Sangria InputObjectType derivation.
   */
  val t: InputObjectType[Val] = InputObjectType(
    name = name,
    description = if (description == "") None else Some(description),
    fieldsFn = () => definition,
    astDirectives = Vector.empty,
    astNodes = Vector.empty
  )
}

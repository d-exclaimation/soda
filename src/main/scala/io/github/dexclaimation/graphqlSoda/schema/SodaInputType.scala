//
//  SodaInputType.scala
//  graphql-soda
//
//  Created by d-exclaimation on 6:24 PM.
//

package io.github.dexclaimation.graphqlSoda.schema

import sangria.schema.InputObjectType

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
  type Def = SodaInputBlock[Val] => Unit

  private val __block = new SodaInputBlock[Val]()

  def description: String = ""

  def definition: Def

  /**
   * Sangria InputObjectType derivation.
   */
  lazy val t: InputObjectType[Val] = {
    definition(__block)
    val fields = __block.typedefs.toList
    InputObjectType(
      name = name,
      description = if (description == "") None else Some(description),
      fieldsFn = () => fields,
      astDirectives = Vector.empty,
      astNodes = Vector.empty
    )
  }
}

//
//  SodaUnionType.scala
//  soda
//
//  Created by d-exclaimation on 6:36 PM.
//

package io.github.dexclaimation.soda.schema

import io.github.dexclaimation.soda.schema.defs.SodaUnionBlock
import sangria.schema.UnionType

/**
 * Soda Implementable Union Type definition.
 *
 * Help to avoid nesting, avoid prefixing with `Types`
 *
 * Accessing the UnionType from `t: UnionType` properties
 *
 * @param name Name of the Union.
 * @tparam Ctx Context value for all of the members.
 */
abstract class SodaUnion[Ctx](name: String) {
  def desc: String = ""

  type Def = SodaUnionBlock[Ctx] => Unit

  private val __block = new SodaUnionBlock[Ctx]()

  def definition: Def

  /**
   * Sangria UnionType derivation.
   */
  lazy final val t: UnionType[Ctx] = {
    definition(__block)
    val fields = __block.typedefs.toList
    UnionType(
      name = name,
      description = if (desc.isEmpty) None else Some(desc),
      typesFn = () => fields,
      astDirectives = Vector.empty,
      astNodes = Vector.empty
    )
  }
}
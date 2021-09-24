//
//  SodaInputType.scala
//  soda
//
//  Created by d-exclaimation on 6:24 PM.
//

package io.github.dexclaimation.soda.schema

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

  /** Definition Block */
  type Def = SodaInputBlock[Val] => Unit

  private val __block = new SodaInputBlock[Val]()

  def desc: String = ""

  def definition: Def

  /**
   * Sangria InputObjectType derivation.
   */

  /**
   * Sangria InputObjectType derivation.
   */
  lazy final val t: InputObjectType[InputObjectType.DefaultInput] = {
    definition(__block)
    val fields = __block.typedefs.toList
    if (desc.isEmpty) {
      InputObjectType(
        name, fields
      )
    } else
      InputObjectType(
        name, desc, fields
      )
  }

}

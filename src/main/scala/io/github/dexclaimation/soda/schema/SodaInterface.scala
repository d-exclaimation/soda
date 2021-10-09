//
//  GraphQLInterface.scala
//  soda
//
//  Created by d-exclaimation on 2:19 PM.
//


package io.github.dexclaimation.soda.schema

import io.github.dexclaimation.soda.schema.defs.SodaDefinitionBlock
import sangria.schema.InterfaceType
import sangria.schema.InterfaceType.emptyPossibleTypes

import scala.reflect.ClassTag

/**
 * Soda Implementable Interface Type definition.
 *
 * Help to avoid nesting, avoid prefixing with `Types`
 *
 * Accessing the InterfaceType from `t: InterfaceType` properties
 *
 * @param name Name of the Interface.
 * @tparam Ctx Context type for this Interface.
 * @tparam Val Value paired for this Interface (*best to implement this on a case class's companion object)
 */
abstract class SodaInterface[Ctx, Val: ClassTag](name: String) {

  /** Definition Block */
  type Def = SodaDefinitionBlock[Ctx, Val] => Unit

  private val __block = new SodaDefinitionBlock[Ctx, Val]

  def desc: String = ""

  def definition: Def

  /**
   * Sangria InterfaceType derivation.
   */
  lazy final val t: InterfaceType[Ctx, Val] = {
    definition(__block)
    val interfaces = __block.interfaces.map(_.interfaceType).toList
    InterfaceType(
      name = name,
      description = Some(desc),
      fieldsFn = () => __block.typedefs.map(_.apply()).toList,
      interfaces = interfaces,
      manualPossibleTypes = emptyPossibleTypes,
      astDirectives = Vector.empty,
      astNodes = Vector.empty
    )
  }
}
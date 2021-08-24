//
//  GraphQLInterface.scala
//  graphql-soda
//
//  Created by d-exclaimation on 2:19 PM.
//


package io.github.dexclaimation.graphqlSoda.schema

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
abstract class SodaInterfaceType[Ctx, Val: ClassTag](name: String) {
  type Def = SodaDefinitionBlock[Ctx, Val] => Unit

  private val __block = new SodaDefinitionBlock[Ctx, Val]

  def desc: String = ""

  def definition: Def

  /**
   * Sangria InterfaceType derivation.
   */
  def t: InterfaceType[Ctx, Val] = {
    definition(__block)
    val fields = __block.typedefs.toList
    val interfaces = __block.interfaces.map(_.interfaceType).toList
    InterfaceType(
      name = name,
      description = Some(desc),
      fieldsFn = () => fields,
      interfaces = interfaces,
      manualPossibleTypes = emptyPossibleTypes,
      astDirectives = Vector.empty,
      astNodes = Vector.empty
    )
  }
}
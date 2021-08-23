//
//  SodaObjectType.scala
//  graphql-soda
//
//  Created by d-exclaimation on 1:47 PM.
//

package io.github.dexclaimation.graphqlSoda.schema

import sangria.schema.ObjectType.defaultInstanceCheck
import sangria.schema.{ObjectType, PossibleInterface}

import scala.reflect.ClassTag

/**
 * Soda Implementable Object Type definition.
 *
 * Help to avoid nesting, avoid prefixing with `Types`
 *
 * Accessing the ObjectType from `t: ObjectType` properties
 *
 * @param name Name of the Object.
 * @tparam Ctx Context type for this Object.
 * @tparam Val Value paired for this Object (*best to implement this on a case class's companion object)
 */
abstract class SodaObjectType[Ctx, Val: ClassTag](name: String) {
  type Def = SodaDefinitionBlock[Ctx, Val] => Unit

  private val __block = new SodaDefinitionBlock[Ctx, Val]

  def description: String = ""

  def definition: Def

  def implement: List[PossibleInterface[Ctx, Val]] = Nil

  /**
   * Sangria ObjectType derivation.
   */
  val t: ObjectType[Ctx, Val] = {
    definition(__block)
    ObjectType(
      name = name,
      description = if (description == "") None else Some(description),
      fieldsFn = () => __block.typedefs.toList,
      interfaces = implement.map(_.interfaceType),
      instanceCheck = defaultInstanceCheck,
      astDirectives = Vector.empty,
      astNodes = Vector.empty
    )
  }
}
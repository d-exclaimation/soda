//
//  SodaObjectType.scala
//  graphql-soda
//
//  Created by d-exclaimation on 1:47 PM.
//

package io.github.dexclaimation.graphqlSoda.schema

import sangria.schema.ObjectType.defaultInstanceCheck
import sangria.schema.{Field, ObjectType, PossibleInterface}

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
  def description: String = ""

  def definition: List[Field[Ctx, Val]]

  def interfaces: List[PossibleInterface[Ctx, Val]] = Nil

  /**
   * Sangria ObjectType derivation.
   */
  val t: ObjectType[Ctx, Val] = ObjectType(
    name = name,
    description = if (description == "") None else Some(description),
    fieldsFn = () => definition,
    interfaces = interfaces.map(_.interfaceType),
    instanceCheck = defaultInstanceCheck,
    astDirectives = Vector.empty,
    astNodes = Vector.empty
  )
}


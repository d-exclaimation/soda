//
//  SodaObjectType.scala
//  soda
//
//  Created by d-exclaimation on 1:47 PM.
//

package io.github.dexclaimation.soda.schema

import io.github.dexclaimation.soda.schema.defs.SodaDefinitionBlock
import sangria.schema.ObjectType
import sangria.schema.ObjectType.defaultInstanceCheck

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
abstract class SodaObject[Ctx, Val: ClassTag](name: String) {

  /** Definition block */
  type Def = SodaDefinitionBlock[Ctx, Val] => Unit

  private val __block = new SodaDefinitionBlock[Ctx, Val]

  def desc: String = ""

  def definition: Def

  /**
   * Sangria ObjectType derivation.
   */
  lazy final val t: ObjectType[Ctx, Val] = {
    definition(__block)
    ObjectType(
      name = name,
      description = if (desc.isEmpty) None else Some(desc),
      fieldsFn = () => __block.typedefs.map(_.apply()).toList,
      interfaces = __block.interfaces.map(_.interfaceType).toList,
      instanceCheck = defaultInstanceCheck,
      astDirectives = Vector.empty,
      astNodes = Vector.empty
    )
  }
}
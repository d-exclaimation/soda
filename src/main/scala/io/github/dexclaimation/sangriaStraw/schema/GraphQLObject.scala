//
//  GraphQLObject.scala
//  sangria-straw
//
//  Created by d-exclaimation on 1:47 PM.
//

package io.github.dexclaimation.sangriaStraw.schema

import sangria.schema.ObjectType.defaultInstanceCheck
import sangria.schema.{Field, ObjectType, PossibleInterface}

import scala.reflect.ClassTag

abstract class GraphQLObject[Ctx, Val: ClassTag](name: String) {
  def description: String = ""

  def definition: List[Field[Ctx, Val]]

  def interfaces: List[PossibleInterface[Ctx, Val]] = Nil

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


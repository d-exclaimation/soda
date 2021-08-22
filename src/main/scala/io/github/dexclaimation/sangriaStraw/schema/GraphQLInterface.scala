//
//  GraphQLInterface.scala
//  sangria-straw
//
//  Created by d-exclaimation on 2:19 PM.
//


package io.github.dexclaimation.sangriaStraw.schema

import sangria.schema.InterfaceType.emptyPossibleTypes
import sangria.schema.{Field, InterfaceType, PossibleInterface}

import scala.reflect.ClassTag

abstract class GraphQLInterface[Ctx, Val: ClassTag](name: String) {
  def description: String = ""

  def definition: List[Field[Ctx, Val]]

  def interfaces: List[PossibleInterface[Ctx, Val]] = Nil

  def t: InterfaceType[Ctx, Val] = InterfaceType(
    name,
    Some(description),
    fieldsFn = () => definition,
    interfaces.map(_.interfaceType),
    emptyPossibleTypes,
    Vector.empty,
    Vector.empty
  )
}
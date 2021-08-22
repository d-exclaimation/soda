//
//  MutationField.scala
//  hello-world
//
//  Created by d-exclaimation on 5:40 PM.
//


package io.github.dexclaimation.sangriaStraw

import sangria.schema.{Field, ObjectType}

import scala.reflect.ClassTag

case class MutationField[C, T: ClassTag](fields: Field[C, T]*) extends RootSchemaField[C, T] {
  override def extendTo: String = "Mutation"
}

object MutationField {
  def makeMutation[C, T: ClassTag](mutationField: MutationField[C, T]*): ObjectType[C, T] =
    ObjectType(
      name = "Mutation",
      fields =
        mutationField.flatMap(_.fields).toList
    )
}
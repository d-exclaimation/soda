//
//  MutationField.scala
//  soda
//
//  Created by d-exclaimation on 5:40 PM.
//


package io.github.dexclaimation.soda.core

import sangria.schema.{Field, ObjectType}

import scala.reflect.ClassTag

/**
 * Extension of Mutation Field
 *
 * @param fields GraphQL Field into the Mutation.
 * @tparam C Context type.
 * @tparam T Type value.
 */
case class MutationField[C, T: ClassTag](fields: Field[C, T]*) extends SodaSchemaField[C, T] {
  override def extendTo: String = "Mutation"
}

object MutationField {
  /**
   * Compose a Mutation Object Type.
   *
   * @param mutationField Extension of Mutation Fields.
   * @tparam C Context type.
   * @tparam T Value type.
   * @return An ObjectType representing the Root Mutation
   */
  def makeMutation[C, T: ClassTag](mutationField: MutationField[C, T]*): ObjectType[C, T] =
    ObjectType(
      name = "Mutation",
      fields =
        mutationField.flatMap(_.fields).toList
    )
}
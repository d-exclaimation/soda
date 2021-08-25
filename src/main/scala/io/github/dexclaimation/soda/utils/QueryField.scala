//
//  QueryField.scala
//  soda
//
//  Created by d-exclaimation on 5:21 PM.
//


package io.github.dexclaimation.soda.utils

import sangria.schema.{Field, ObjectType}

import scala.reflect.ClassTag

/**
 * Extension of Query Field
 *
 * @param fields GraphQL Field into the query.
 * @tparam C Context type.
 * @tparam T Type value.
 */
case class QueryField[C, T: ClassTag](fields: Field[C, T]*) extends SodaSchemaField[C, T] {
  /** Fields conversion */
  def fieldList: List[Field[C, T]] = fields.toList

  override def extendTo: String = "Query"
}


object QueryField {
  /**
   * Compose a Query Object Type.
   *
   * @param queryField Extension of Query Fields.
   * @tparam C Context type.
   * @tparam T Value type.
   * @return An ObjectType representing the Root Query
   */
  def makeQuery[C, T: ClassTag](queryField: QueryField[C, T]*): ObjectType[C, T] =
    ObjectType(
      name = "Query",
      fields =
        queryField.flatMap(_.fieldList).toList
    )
}
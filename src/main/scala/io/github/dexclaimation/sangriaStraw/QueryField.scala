//
//  QueryField.scala
//  hello-world
//
//  Created by d-exclaimation on 5:21 PM.
//


package io.github.dexclaimation.sangriaStraw

import sangria.schema.{Field, ObjectType}

import scala.reflect.ClassTag

case class QueryField[C, T: ClassTag](fields: Field[C, T]*) extends RootSchemaField[C, T] {
  def fieldList: List[Field[C, T]] = fields.toList

  override def extendTo: String = "Query"
}


object QueryField {
  def makeQuery[C, T: ClassTag](queryField: QueryField[C, T]*): ObjectType[C, T] =
    ObjectType(
      name = "Query",
      fields =
        queryField.flatMap(_.fieldList).toList
    )
}
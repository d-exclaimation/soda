//
//  QueryField.scala
//  hello-world
//
//  Created by d-exclaimation on 5:21 PM.
//


package com.dexclaimation.sangriaStraw.schema

import sangria.schema.{Field, ObjectType}

case class QueryField[C, T](fields: Field[C, T]*) extends RootSchemaField[C, T] {
  def fieldList: List[Field[C, T]] = fields.toList

  override def extendTo: String = "Query"
}


object QueryField {
  def makeQuery[C, T](queryField: QueryField[C, T]*): ObjectType[C, T] =
    ObjectType(
      name = "Query",
      fields =
        queryField.flatMap(_.fieldList).toList
    )
}
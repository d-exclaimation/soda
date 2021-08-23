//
//  SodaInputBlock.scala
//  graphql-soda
//
//  Created by d-exclaimation on 4:06 PM.
//

package io.github.dexclaimation.graphqlSoda.schema

import sangria.marshalling.ToInput
import sangria.schema.{IDType, InputField, InputType, WithoutInputTypeTags}

import scala.collection.mutable


class SodaInputBlock[Val] {
  private[schema] val typedefs: mutable.ArrayBuffer[InputField[_]] = mutable.ArrayBuffer.empty

  /**
   * Added a properties
   *
   * @param name        Name of the property field.
   * @param fieldType   The GraphQL Type of that field.
   * @param description Additional descriptions.
   */
  def prop[T](
    name: String,
    fieldType: InputType[T],
    description: String = "",
  )(implicit res: WithoutInputTypeTags[T]): Unit = {
    typedefs.addOne(
      if (description.nonEmpty)
        InputField(
          name = name,
          fieldType = fieldType,
          description = description,
        )
      else
        InputField(
          name = name,
          fieldType = fieldType
        )
    )
  }

  /**
   * Added an optional properties
   *
   * @param name         Name of the property field.
   * @param fieldType    The GraphQL Type of that field.
   * @param description  Additional descriptions.
   * @param defaultValue Default value for the given
   */
  def optional[T, Default](
    name: String,
    fieldType: InputType[T],
    description: String = "",
    defaultValue: Default
  )(implicit toInput: ToInput[Default, _], res: WithoutInputTypeTags[T]): Unit =
    typedefs.addOne(
      if (description.nonEmpty)
        InputField(
          name = name,
          fieldType = fieldType,
          description = description,
          defaultValue = defaultValue
        )
      else
        InputField(
          name = name,
          fieldType = fieldType,
          defaultValue = defaultValue
        )
    )

  /** ID Properties */
  def id(
    name: String = "id",
    description: String = ""
  )(implicit res: WithoutInputTypeTags[String]): Unit =
    prop[String](
      name = name,
      fieldType = IDType,
      description = description,
    )(res)

}
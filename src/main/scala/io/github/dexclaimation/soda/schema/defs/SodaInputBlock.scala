//
//  SodaInputBlock.scala
//  soda
//
//  Created by d-exclaimation on 4:06 PM.
//

package io.github.dexclaimation.soda.schema.defs

import sangria.marshalling.ToInput
import sangria.schema.{IDType, InputField, InputType, WithoutInputTypeTags}

import scala.collection.mutable


class SodaInputBlock[Val] {
  private[schema] val typedefs: mutable.ArrayBuffer[InputField[_]] = mutable.ArrayBuffer.empty

  /**
   * Added a properties
   *
   * @param name      Name of the property field.
   * @param fieldType The GraphQL Type of that field.
   * @param desc      Additional descriptions.
   */
  def prop[T](
    name: String,
    fieldType: InputType[T],
    desc: String = "",
  )(implicit res: WithoutInputTypeTags[T]): Unit = {
    typedefs.addOne(
      if (desc.nonEmpty)
        InputField(
          name = name,
          fieldType = fieldType,
          description = desc,
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
   * @param desc         Additional descriptions.
   * @param defaultValue Default value for the given
   */
  def optional[T, Default](
    name: String,
    fieldType: InputType[T],
    desc: String = "",
    defaultValue: Default
  )(implicit toInput: ToInput[Default, _], res: WithoutInputTypeTags[T]): Unit =
    typedefs.addOne(
      if (desc.nonEmpty)
        InputField(
          name = name,
          fieldType = fieldType,
          description = desc,
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
    desc: String = ""
  )(implicit res: WithoutInputTypeTags[String]): Unit =
    prop[String](
      name = name,
      fieldType = IDType,
      desc = desc,
    )(res)

}
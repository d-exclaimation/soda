//
//  SodaDefinitionBlock.scala
//  graphql-soda
//
//  Created by d-exclaimation on 1:27 PM.
//


package io.github.dexclaimation.graphqlSoda.schema

import sangria.schema.{Action, Argument, Context, Field, IDType, OutputType, ValidOutType}

import scala.collection.mutable
import scala.reflect.ClassTag

class SodaDefinitionBlock[Ctx, Val: ClassTag] {
  private[schema] val typedefs: mutable.ArrayBuffer[Field[Ctx, Val]] = mutable.ArrayBuffer.empty

  /**
   * Added a field from properties
   *
   * @param name        Name of the property field.
   * @param fieldType   The GraphQL Type of that field.
   * @param description Additional descriptions.
   * @param args        Required arguments from the field.
   * @param of          Getter of the field
   */
  def prop[Out, Res](
    name: String,
    fieldType: OutputType[Out],
    description: String = "",
    args: List[Argument[_]] = Nil,
    of: Val => Action[Ctx, Res],
  )(
    implicit ev: ValidOutType[Res, Out]
  ): Unit = {
    typedefs.addOne(
      Field(
        name = name,
        fieldType = fieldType,
        description = if (description.isEmpty) None else Some(description),
        arguments = args,
        resolve = c => of(c.value)
      )
    )
  }

  /** ID Properties */
  def id[Res](
    name: String = "id",
    description: String = "",
    of: Val => Action[Ctx, Res]
  )(implicit ev: ValidOutType[Res, String]): Unit =
    prop[String, Res](
      name = name,
      fieldType = IDType,
      description = description,
      of = of
    )(ev)

  /**
   * Added a field either from a properties or computed
   *
   * @param name        Name of the property field.
   * @param fieldType   The GraphQL Type of that field.
   * @param description Additional descriptions.
   * @param args        Required arguments from the field.
   * @param resolve     Compute or resolve the field
   */
  def field[Out, Res](
    name: String,
    fieldType: OutputType[Out],
    description: String = "",
    args: List[Argument[_]] = Nil,
  )(resolve: Context[Ctx, Val] => Action[Ctx, Res])
    (implicit ev: ValidOutType[Res, Out]): Unit = {
    typedefs.addOne(
      Field(
        name = name,
        fieldType = fieldType,
        description = if (description.isEmpty) None else Some(description),
        arguments = args,
        resolve = c => resolve(c)
      )
    )
  }
}
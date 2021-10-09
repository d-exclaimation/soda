//
//  SodaBlock.scala
//  soda
//
//  Created by d-exclaimation on 4:05 PM.
//

package io.github.dexclaimation.soda.schema.defs.traits

import sangria.execution.FieldTag
import sangria.schema.{Action, Argument, Context, Field, IDType, OutputType, ValidOutType}

import scala.collection.mutable
import scala.reflect.ClassTag

abstract class SodaBlock[Ctx, Val: ClassTag] {
  private[schema] val typedefs: mutable.ArrayBuffer[() => Field[Ctx, Val]] = mutable.ArrayBuffer.empty

  /**
   * Added a field from properties
   *
   * @param name      Name of the property field.
   * @param fieldType The GraphQL Type of that field.
   * @param desc      Additional descriptions.
   * @param of        Getter of the field
   */
  def prop[Out, Res](
    name: String,
    fieldType: OutputType[Out],
    desc: String = "",
    of: Val => Action[Ctx, Res],
  )(
    implicit ev: ValidOutType[Res, Out]
  ): Unit = {
    typedefs.addOne(
      () => Field(name, fieldType,
        description = if (desc.isEmpty) None else Some(desc),
        arguments = Nil,
        resolve = c => of(c.value)
      )
    )
  }

  /**
   * Added a field from properties
   *
   * @param name      Name of the property field.
   * @param fieldType The GraphQL Type of that field.
   * @param desc      Additional descriptions.
   * @param of        Getter of the field
   */
  def lazyProp[Out, Res](
    name: String,
    fieldType: () => OutputType[Out],
    desc: String = "",
    of: Val => Action[Ctx, Res],
  )(
    implicit ev: ValidOutType[Res, Out]
  ): Unit = {
    typedefs.addOne(
      () => Field(name, fieldType(),
        description = if (desc.isEmpty) None else Some(desc),
        arguments = Nil,
        resolve = c => of(c.value)
      )
    )
  }

  /** ID Properties */
  def id[Res](
    name: String = "id",
    desc: String = "",
    of: Val => Action[Ctx, Res]
  )(implicit ev: ValidOutType[Res, String]): Unit =
    prop[String, Res](name, IDType,
      desc = desc,
      of = of
    )(ev)

  /**
   * Added a field either from a properties or computed
   *
   * @param name      Name of the property field.
   * @param fieldType The GraphQL Type of that field.
   * @param desc      Additional descriptions.
   * @param args      Required arguments from the field.
   * @param resolve   Compute or resolve the field
   */
  def field[Out, Res](
    name: String,
    fieldType: OutputType[Out],
    desc: String = "",
    args: List[Argument[_]] = Nil,
    deprecated: Option[String] = None,
    tags: List[FieldTag] = Nil,
  )(resolve: Context[Ctx, Val] => Action[Ctx, Res])
    (implicit ev: ValidOutType[Res, Out]): Unit = {
    typedefs.addOne(
      () => Field(name, fieldType,
        description = if (desc.isEmpty) None else Some(desc),
        arguments = args,
        deprecationReason = deprecated,
        tags = tags,
        resolve = c => resolve(c)
      )
    )
  }

  /**
   * Added a field either from a properties or computed
   *
   * @param name      Name of the property field.
   * @param fieldType The GraphQL Type of that field.
   * @param desc      Additional descriptions.
   * @param args      Required arguments from the field.
   * @param resolve   Compute or resolve the field
   */
  def lazyField[Out, Res](
    name: String,
    fieldType: () => OutputType[Out],
    desc: String = "",
    args: List[Argument[_]] = Nil,
    deprecated: Option[String] = None,
    tags: List[FieldTag] = Nil,
  )(resolve: Context[Ctx, Val] => Action[Ctx, Res])
    (implicit ev: ValidOutType[Res, Out]): Unit = {
    typedefs.addOne(
      () => Field(name, fieldType(),
        description = if (desc.isEmpty) None else Some(desc),
        arguments = args,
        deprecationReason = deprecated,
        tags = tags,
        resolve = c => resolve(c)
      )
    )
  }
}

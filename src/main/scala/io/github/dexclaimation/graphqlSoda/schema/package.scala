package io.github.dexclaimation.graphqlSoda

import sangria.marshalling.{FromInput, ToInput}
import sangria.schema.{Argument, ArgumentType, InputType, WithoutInputTypeTags}

package object schema {
  /** Argument type, following GraphQL Variable convention ($ prefix) */
  def $[T, Default](name: String, argType: InputType[T], desc: String, default: Default)(
    implicit toInput: ToInput[Default, _], fromInput: FromInput[T], res: ArgumentType[T]
  ): Argument[res.Res] =
    Argument(name, argType, Some(desc), Some(default -> toInput), fromInput, Vector.empty, Vector.empty)

  /** Argument type, following GraphQL Variable convention ($ prefix) */
  def $[T, Default](name: String, argumentType: InputType[T], defaultValue: Default)(
    implicit
    toInput: ToInput[Default, _],
    fromInput: FromInput[T],
    res: ArgumentType[T]
  ): Argument[res.Res] =
    Argument(
      name,
      argumentType,
      None,
      Some(defaultValue -> toInput),
      fromInput,
      Vector.empty,
      Vector.empty
    )

  /** Argument type, following GraphQL Variable convention ($ prefix) */
  def $[T](name: String, argumentType: InputType[T], description: String)(
    implicit
    fromInput: FromInput[T],
    res: WithoutInputTypeTags[T]
  ): Argument[res.Res] =
    Argument(name, argumentType, Some(description), None, fromInput, Vector.empty, Vector.empty)

  /** Argument type, following GraphQL Variable convention ($ prefix) */
  def $[T](name: String, argumentType: InputType[T])(
    implicit
    fromInput: FromInput[T],
    res: WithoutInputTypeTags[T]
  ): Argument[res.Res] =
    Argument(name, argumentType, None, None, fromInput, Vector.empty, Vector.empty)
}

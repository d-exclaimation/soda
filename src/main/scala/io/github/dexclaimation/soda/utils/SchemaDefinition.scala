//
//  SchemaDefinition.scala
//  soda
//
//  Created by d-exclaimation on 3:35 PM.
//

package io.github.dexclaimation.soda.utils

import io.github.dexclaimation.soda.common.Artifacts
import io.github.dexclaimation.soda.utils.MutationField.makeMutation
import io.github.dexclaimation.soda.utils.QueryField.makeQuery
import io.github.dexclaimation.soda.utils.SubscriptionField.makeSubscription
import sangria.schema.Schema

import scala.reflect.ClassTag

object SchemaDefinition {
  /**
   * Compose all Root schema extensions to create a Sangria Schema.
   *
   * @param fields Any Schema extensions (Query, Mutations, Subscriptions)
   * @tparam C Context type.
   * @tparam T Value type.
   * @return A full Sangria Schema with the right values.
   */
  def makeSchema[C, T: ClassTag](fields: SodaSchemaField[C, T]*): Schema[C, T] = {
    val QueryType = makeQuery(
      fields.flatMap {
        case QueryField(fields@_*) => Some(QueryField(fields: _*))
        case _ => None
      }: _*
    )

    val MutationType = makeMutation(
      fields.flatMap {
        case MutationField(fields@_*) => Some(MutationField(fields: _*))
        case _ => None
      }: _*
    )

    val SubscriptionType = makeSubscription(
      fields.flatMap {
        case SubscriptionField(fields@_*) => Some(SubscriptionField(fields: _*))
        case _ => None
      }: _*
    )

    Schema[C, T](
      query = QueryType,
      mutation = if (MutationType.fields.isEmpty) None else Some(MutationType),
      subscription = if (SubscriptionType.fields.isEmpty) None else Some(SubscriptionType)
    )
  }


  /**
   * Make schema and compile artifacts into the resource folder
   * Default file path: "src/main/resources/artifacts/schema.graphql"
   *
   * @param fields The schema fields to be composed into a single schema.
   * @param path   The resulting path.
   * @tparam C The context type.
   * @tparam T The root value type.
   * @return The schema and perform side effect writing the SDL into "src/main/resources/artifacts/schema.graphql"
   */
  def makeSchemaAndArtifacts[C, T: ClassTag](
    fields: Seq[SodaSchemaField[C, T]], path: String = "./src/main/resources/artifacts/schema.graphql"
  ): Schema[C, T] = {
    val schema = makeSchema[C, T](fields: _*)
    Artifacts.compile(schema, path)
    schema
  }
}

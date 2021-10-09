//
//  RootTypesTest.scala
//  soda
//
//  Created by d-exclaimation on 7:19 PM.
//

package io.github.dexclaimation.soda

import io.github.dexclaimation.soda.core.MutationField.makeMutation
import io.github.dexclaimation.soda.core.QueryField.makeQuery
import io.github.dexclaimation.soda.core.SchemaDefinition.makeSchema
import io.github.dexclaimation.soda.core.SubscriptionField.makeSubscription
import io.github.dexclaimation.soda.core._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import sangria.parser.QueryParser
import sangria.schema.{Field, Schema, StringType}

class RootTypesTest extends AnyWordSpec with Matchers {

  "QueryField" when {

    "applied" should {
      val query1 = QueryField[Unit, Unit](
        Field("hello", StringType, resolve = _ => "Hello World"),
        Field("hello2", StringType, resolve = _ => "Hello World"),
        Field("hello3", StringType, resolve = _ => "Hello World"),
        Field("hello4", StringType, resolve = _ => "Hello World"),
      )

      "return the proper `extendTo`" in {
        query1.extendTo shouldEqual "Query"
      }

      "return all of the fields" in {
        val Seq(f1, f2, f3, f4) = query1.fields
        f1.name shouldEqual "hello"
        f2.name shouldEqual "hello2"
        f3.name shouldEqual "hello3"
        f4.name shouldEqual "hello4"
      }
    }
  }

  "MutationField" when {

    "applied" should {
      val mutation1 = MutationField[Unit, Unit](
        Field("hello", StringType, resolve = _ => "Hello World"),
      )

      "return the proper `extendTo`" in {
        mutation1.extendTo shouldEqual "Mutation"
      }
    }
  }

  "SubscriptionField" when {

    "applied" should {
      val subscription1 = SubscriptionField[Unit, Unit](
        Field("hello", StringType, resolve = _ => "Hello World"),
      )

      "return the proper `extendTo`" in {
        subscription1.extendTo shouldEqual "Subscription"
      }
    }
  }

  "Root types" when {
    val q0 = QueryField[Unit, Unit](Field("field0", StringType, resolve = _ => "Hello From Field 0"))
    val q1 = QueryField[Unit, Unit](Field("field1", StringType, resolve = _ => "Hello From Field 1"))
    val m0 = MutationField[Unit, Unit](Field("mutation0", StringType, resolve = _ => "Mutated for field0"))
    val m1 = MutationField[Unit, Unit](Field("mutation1", StringType, resolve = _ => "Mutated for field1"))
    val s = SubscriptionField[Unit, Unit](Field("subscription", StringType, resolve = _ => "Not used"))

    "composed (query-only)" should {
      val query = makeQuery(q0, q1)

      "return a full Query ObjectType" in {
        query.name shouldEqual "Query"

        query.fields.map(_.name) match {
          case Vector("field0", "field1") => succeed
          case _ => fail("Does not contain the proper fields")
        }
      }
    }

    "composed (mutation-only)" should {
      val mutation = makeMutation(m0, m1)

      "return a full Query ObjectType" in {
        mutation.name shouldEqual "Mutation"

        mutation.fields.map(_.name) match {
          case Vector("mutation0", "mutation1") => succeed
          case _ => fail("Does not contain the proper fields")
        }
      }
    }

    "composed (subscription-only)" should {
      val subscription = makeSubscription(s)

      "return a full Query ObjectType" in {
        subscription.name shouldEqual "Subscription"

        subscription.fields.map(_.name) match {
          case Vector("subscription") => succeed
          case _ => fail("Does not contain the proper fields")
        }
      }
    }

    "composed (all)" should {
      val schema = makeSchema(q0, q1, m0, m1, s)

      "return the schema with the proper types" in {
        schema match {
          case _: Schema[Unit, Unit] => ()
          case _ => fail("Does not return a schema")
        }

        schema.query.name shouldEqual "Query"
        schema.mutation.map(_.name) match {
          case Some(name) => name shouldEqual "Mutation"
          case _ => fail("Mutation does not exist")
        }
        schema.subscription.map(_.name) match {
          case Some(value) => value shouldEqual "Subscription"
          case _ => fail("Subscription does not exist")
        }
      }

      "return a valid GraphQL Schema" in {
        QueryParser.parse(schema.renderPretty).get
      }
    }
  }
}
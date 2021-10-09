//
//  SodaSchemaTest.scala
//  soda
//
//  Created by d-exclaimation on 8:25 PM.
//

package io.github.dexclaimation.soda

import io.github.dexclaimation.soda.core.SchemaDefinition.makeSchema
import io.github.dexclaimation.soda.schema.{SodaInterface, SodaObject, SodaQuery}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import sangria.schema.{FloatType, ListType}

class SodaSchemaTest extends AnyWordSpec with Matchers {
  trait Node {
    def id: String
  }

  object Node extends SodaInterface[Unit, Node]("Node") {
    def definition: Def = { t =>
      t.id(of = _.id)
    }
  }

  case class Item(id: String, price: Double, userId: String) extends Node

  object Item extends SodaObject[Unit, Item]("Item") {
    def definition: Def = { t =>
      t.implements(Node.t)
      t.id(of = _.id)
      t.prop("price", FloatType, of = _.price)
      t.lazyProp("user", () => User.t,
        of = i => User(i.userId)
      )
    }
  }

  case class User(id: String) extends Node

  object User extends SodaObject[Unit, User]("User") {
    def definition: Def = { t =>
      t.implements(Node.t)

      t.id(of = _.id)
      t.lazyProp("items", () => ListType(Item.t),
        of = u => List[Item]().filter(_.userId == u.id)
      )
    }
  }


  object Query extends SodaQuery[Unit, Unit] {
    def definition: Def = { t =>
      t.field("items", ListType(Item.t)) { _ =>
        List()
      }
    }
  }

  val schema = makeSchema(Query.t)

  "ObjectTypes" when {

    "recursive" should {
      "still compile using `lazyField`" in {
        succeed
      }
    }

    "accessed" should {
      "return the proper name" in {
        User.t.name shouldEqual "User"
        Item.t.name shouldEqual "Item"
      }


      "return the proper fields" in {
        val Vector(id, item, _@_*) = User.t.fields
        id.name shouldEqual "id"
        item.name shouldEqual "items"
      }
    }

    "implementing interfaces" should {

      "return the appropriate interfaces" in {
        assert(User.t.interfaces.map(_.name).contains("Node"))
      }

      "have all the required fields" in {
        val has = Set.from(User.t.fields.map(_.name))
        assert(User.t.interfaces.flatMap(_.fields).map(_.name).forall(has.contains))
      }
    }
  }
}

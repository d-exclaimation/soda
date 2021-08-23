<p align="center">
    <img src="./icon.png" width="200" />
</p>
<p align="center"> <h1>GraphQL Soda</h1></p>


A GraphQL Schema Tooling to make schema composing in Scala more convenient, built on Sangria.

## Setup

**Latest Published Version**: `0.2.0`

```sbt
"io.github.d-exclaimation" % "graphql-soda" % latestVersion
```

## Quick start

Target SDL

```graphql
type Picture {
    width: Int!
    height: Int!
    url: String
}

interface Identifiable {
    id: String!
}

type Product implements Identifiable {
    id: String!
    name: String!
    description: String
    picture(size: Int!): Picture
}

type Query {
    product(id: Int!): Product
    products: [Product]
}
```

### Picture Object Type

```scala
import io.github.dexclaimation.graphqlSoda.schema._
import sangria.schema._

case class Picture(
  width: Int,
  height: Int,
  url: Option[String]
)

object Picture extends SodaObjectType[Unit, Picture]("Picture") {
  override def description: String = "The product picture"

  def definition: Def = { t =>
    t.prop("width", IntType, of = _.width)
    t.prop("height", IntType, of = _.height)
    t.prop("url", OptionType(StringType), of = _.url)
  }
}
```

### Product Type and Identifiable Interface

Identifiable trait

```scala
import io.github.dexclaimation.graphqlSoda.schema._
import sangria.schema._

trait Identifiable {
  def id: String
}

object Identifiable extends SodaInterfaceType[Unit, Identifiable]("Identifiable") {

  override def description: String = "Entity that can be identified"

  override def definition: Def = { t =>
    t.id(of = _.id)
  }
}
```

Product type

```scala
import io.github.dexclaimation.graphqlSoda.schema._
import sangria.schema._

case class Product(id: String, name: String, description: String) extends Identifiable {
  def picture(size: Int): Picture =
    Picture(width = size, height = size, url = Some(s"//cdn.com/$size/$id.jpg"))
}

object Product extends SodaObjectType[Unit, Product]("Product") {
  override def definition: Def = { t =>
    val s = Argument("size", IntType)

    t.id(of = _.id)
    t.prop("name", StringType, of = _.name)
    t.prop("description", StringType, of = _.description)
    t.field("picture", Picture.t, args = s :: Nil)( c =>
      c.value.picture(c arg s)
    )
  }

  override def implement: List[PossibleInterface[Unit, Product]] = interfaces(Identifiable.t)
}
```

### Query type

```scala
import io.github.dexclaimation.graphqlSoda.schema._
import sangria.schema._

class ProductRepo {
  private val Products = List(
    Product("1", "Cheesecake", "Tasty"),
    Product("2", "Health Potion", "+50 HP")
  )

  def product(id: String): Option[Product] =
    Products find (_.id == id)

  def products: List[Product] = Products
}

object ProductQuery extends SodaQuery[ProductRepo, Unit] {

  override def definition: Def = { t =>
    val id = Argument("id", IDType)

    t.field("product", OptionType(Product.t),
      description = "Returns a product with specific `id`.",
      args = id :: Nil
    ) { c =>
      c.ctx.product(c.arg(id))
    }

    t.field("products", ListType(Product.t),
      description = "Returns a list of all available products."
    )(_.ctx.products)
  }
}
```

Get the final schema

```scala
import io.github.dexclaimation.graphqlSoda.utils.SchemaDefinition.makeSchema
import sangria.schema._

val schema: Schema[ProductRepo, Unit] = makeSchema(ProductQuery.t)
```

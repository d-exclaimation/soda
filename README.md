<p align="center">
    <img src="./icon.png" width="200" />
</p>
<p align="center"> <h1>GraphQL Soda</h1></p>


A GraphQL Schema Tooling to make schema composing in Scala more convenient, built on Sangria.

## Documentation

[Docs](https://graphqlsoda.netlify.app)

## Setup

**Latest Published Version**: `0.4.1`

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
  override def desc = "The product picture"

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

  override def desc = "Entity that can be identified"

  def definition: Def = { t =>
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
  def definition: Def = { t =>
    t.implements(Identifiable.t)

    t.id(of = _.id)
    t.prop("name", StringType, of = _.name)
    t.prop("description", StringType, of = _.description)

    val s = $("size", IntType) // You can have arguments as local variable (object global / static works fine)
    t.field("picture", Picture.t, args = s :: Nil)(c =>
      c.value.picture(c arg s)
    )
  }
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
  val id = $("id", IDType)

  def definition: Def = { t =>
    t.field("product", OptionType(Product.t),
      desc = "Returns a product with specific `id`.",
      args = id :: Nil
    ) { c =>
      c.ctx.product(c.arg(id))
    }

    t.field("products", ListType(Product.t),
      desc = "Returns a list of all available products."
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

## Acknowledgements

This package is inspired by [`GraphQL Nexus`](https://github.com/graphql-nexus/nexus)
, [`Slick`](https://scala-slick.org/) and [`Exposed`](https://github.com/JetBrains/Exposed).

Basically, my effort making [`Sangria`](https://github.com/sangria-graphql/sangria)
schema definition similar to what's used by [`Akka`](https://akka.io)'s typed
AbstractBehaviour [`Slick`](https://scala-slick.org/) 's Table and [`Exposed`](https://github.com/JetBrains/Exposed) 's
Table that take advantage of implementing / extending a Trait / Abstract class, but have APIs more closely
to [`GraphQL Nexus`](https://github.com/graphql-nexus/nexus).

<i>Icons made by <a href="" title="fjstudio">fjstudio</a> from <a href="https://www.flaticon.com/" title="Flaticon">
flaticon</a></i>


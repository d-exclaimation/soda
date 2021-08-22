<p align="center">
    <img src="./icon.png" width="200" />
</p>
<p align="center"> <h1>GraphQL Soda</h1></p>


A GraphQL Schema Tooling to make schema composing in Scala more convenient built on top of Sangria.

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
import io.github.dexclaimation.graphqlSoda.schema.SodaObjectType
import sangria.schema._

case class Picture(
  width: Int,
  height: Int,
  url: Option[String]
)

object Picture extends SodaObjectType[Unit, Picture]("Picture") {
  override def description: String = "The product picture"

  override def definition: List[Field[Unit, Picture]] = fields(
    Field("width", IntType, resolve = _.value.width),
    Field("height", IntType, resolve = _.value.height),
    Field("url", OptionType(StringType), resolve = _.value.url)
  )
}
```

### Product Type and Identifiable Interface

Identifiable trait

```scala
import io.github.dexclaimation.graphqlSoda.schema.SodaInterfaceType
import sangria.schema._

trait Identifiable {
  def id: String
}

object Identifiable extends SodaInterfaceType[Unit, Identifiable]("Identifiable") {

  override def description: String = "Entity that can be identified"

  override def definition: List[Field[Unit, Identifiable]] = fields(
    Field("id", StringType, resolve = _.value.id)
  )
}
```

Product type

```scala
import io.github.dexclaimation.graphqlSoda.schema.SodaObjectType
import sangria.schema._

case class Product(id: String, name: String, description: String) extends Identifiable {
  def picture(size: Int): Picture =
    Picture(width = size, height = size, url = Some(s"//cdn.com/$size/$id.jpg"))
}

object Product extends SodaObjectType[Unit, Product]("Product") {
  val Size = Argument("size", IntType)

  override def definition: List[Field[Unit, Product]] = fields(
    Field("id", IDType, resolve = _.value.id),
    Field("name", StringType, resolve = _.value.name),
    Field("description", StringType, resolve = _.value.description),
    Field("picture", Picture.t,
      arguments = Size :: Nil,
      resolve = ctx => ctx.value.picture(ctx.arg(Size))
    )
  )

  override def implement: List[PossibleInterface[Unit, Product]] = interfaces(Identifable.t)
}
```

### Query type

```scala
import io.github.dexclaimation.graphqlSoda.schema.SodaQuery
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
  val Id = Argument("id", StringType)

  override def definition: List[Field[ProductRepo, Unit]] = fields(
    Field("product", OptionType(ProductType),
      description = Some("Returns a product with specific `id`."),
      arguments = Id :: Nil,
      resolve = c => c.ctx.product(c.arg(Id))
    ),

    Field("products", ListType(ProductType),
      description = Some("Returns a list of all available products."),
      resolve = _.ctx.products
    )
  )
}
```

Get the final schema

```scala
import io.github.dexclaimation.graphqlSoda.utils.SchemaDefinition.makeSchema
import sangria.schema._

val schema: Schema[ProductRepo, Unit] = makeSchema(ProductQuery.t)
```
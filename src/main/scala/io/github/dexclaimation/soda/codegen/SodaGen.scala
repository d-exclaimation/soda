//
//  SodaGen.scala
//  soda
//
//  Created by d-exclaimation on 3:09 PM.
//

package io.github.dexclaimation.soda.codegen

import sangria.ast
import sangria.ast._
import sangria.parser.QueryParser

import java.io.File
import java.nio.file.Paths

object SodaGen {
  def main(args: Array[String]): Unit = {
    generateFrom(
      """
        |type Query {
        |  state: Int!
        |}
        |
        |type Mutation {
        |  increment: Int!
        |}
        |
        |interface Node {
        |  id: ID!
        |}
        |
        |type Friend implements Node {
        |  id: ID!
        |  name: String!
        |}
        |""".stripMargin
    )
  }

  def genSchema(
    pkg: String = "schema",
    path: String = "./src/main/scala/"
  ): Unit = {
    val filepath = Paths.get(path, pkg).toAbsolutePath.toString
    new File(filepath)
  }

  def generateFrom(doc: String): Unit =
    QueryParser
      .parse(doc)
      .getOrElse(ast.Document.emptyStub)
      .definitions
      .flatMap {
        case definition: TypeSystemDefinition => Some(definition)
        case _ => None
      }
      .flatMap {
        case definition: TypeDefinition => Some(definition)
        case _ => None
      }
      .map {
        case obj: ObjectTypeDefinition => {
          if (Set("Query", "Mutation", "Subscription").contains(obj.name)) s"${obj.name}Field"
          else s"[${obj.name}.scala]:\n\n" + generate.Object(obj)
        }
        case scalar: ScalarTypeDefinition => s"Scalar: ${scalar.name}"
        case interface: InterfaceTypeDefinition => s"[${interface.name}.scala]:\n\n" + generate.Interface(interface)
        case union: UnionTypeDefinition => s"Union: ${union.name}"
        case enum: EnumTypeDefinition => s"Enum: ${enum.name}"
        case input: InputObjectTypeDefinition => s"Input: ${input.name}"
      }
      .foreach(println)
}

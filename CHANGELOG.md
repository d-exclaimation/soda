# CHANGELOG

## v0.5

- Added `generateFrom` and `generateSchema` code generator function.
- Moved File system related to `common` package.

## v0.4

- Removed long unnecessary naming convention
- Moved implement into the definition block to reduce the use of override and boilerplate
- Added some utilities function for argument reducing reliance on importing the entire Sangria schema package

## v0.3

- Changes all definition `API` (To over abstraction on Sangria schema definitions)
- Updated `SodaEnumType` to have members instead of definition

## v0.2

- Added `SodaQuery` abstraction over `QueryField`.
- Added `SodaMutation` abstraction over `MutationField`.
- Added `SodaSubscription` abstraction over `SubscriptionField`.
- Added `SodaInputType` and `SodaUnionType`

## v0.1

- Schema tooling for `ObjectType`, `InterfaceType`.
- Root schema fields.

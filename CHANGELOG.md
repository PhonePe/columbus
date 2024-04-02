# Changelog

## [2.0.2] - 2024-04-02

### Fixed

- Return `UNALTERED` when an upsert does not change the stored document.

## [2.0.1] - 2024-03-21

### Breaking changes

- Replace the v1 category/unit implementation with a new extensible, multi-module
  framework. V1 APIs and features are not implicitly carried forward.
- Move the rewritten implementation into the `com.phonepe.commons.columbus` namespace.

### Added

- Introduce framework utilities for reflection and mixins, registries, extensible
  visitors, commands/executors, lazy initialization, entity types, and JSON serialization.
- Introduce toolkit and storage initialization, configurable command handlers,
  storage factories, and registration/override extension points.
- Add data-access and search models for IDs, indices, routing, queries, filters,
  scoring, sorting, pagination, and aggregations.
- Support per-call visitor properties, text-match and constant-score filters,
  wildcard queries, and scoring within AND filters.
- Add Elasticsearch storage with index and entity operations, bulk commands,
  search, scanning, index-template commands, and geographic point queries.
- Add optional Dropwizard lifecycle/health-check integration and a Columbus bundle;
  also support creating Elasticsearch storage without Dropwizard.
- Add reusable JUnit extensions, in-memory test storage, and Elasticsearch
  container utilities.
- Add module guides, initialization examples, and extension documentation.

### Fixed

- Preserve all index settings when loading Elasticsearch index configuration.
- Return `null` when deserializing null JSON strings or byte arrays.

## Legacy v1 releases

Version 1.x features apply to the original implementation, replaced in v2.

## [1.0.11] - 2023-12-07

### Changed

- Update parent/model dependencies and static-analysis configuration.

## [1.0.10] - 2023-08-09

### Added

- Add wildcard filtering.

## [1.0.9] - 2023-05-29

### Added

- Add an operation to delete multiple entities.

## [1.0.8] - 2023-03-23

### Added

- Add paginated Elasticsearch search returning entities and the total match count.

## [1.0.7] - 2023-01-04

### Changed

- Version-only release; no source changes relative to 1.0.6.

## [1.0.6] - 2022-12-22

### Added

- Support sorter context and resolve references in stored sorters.

## [1.0.5] - 2022-11-20

### Fixed

- Cache all categories regardless of their supported platforms.

## [1.0.4] - 2022-11-17

### Fixed

- Correct analysis settings in index-template creation.
- Default omitted category platforms to all platforms.

## [1.0.3] - 2022-09-21

### Fixed

- Preserve category `updatedAt` values during reads.
- Correct entity-type deserialization.

## [1.0.2] - 2022-09-07

### Added

- Add SymSpell autocorrection, word splitting/combining, and keyboard-weighted
  edit distance for the v1 implementation.

## [1.0.1] - 2022-09-05

### Fixed

- Honor requested supported platforms when creating categories.

## [1.0.0] - 2022-07-28

### Added

- Initial category/unit search implementation with REST resources and Elasticsearch integration.

### Changed

- Use JSON properties for enum serialization.

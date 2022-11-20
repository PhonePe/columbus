# Changelog

## Legacy v1 releases

Version 1.x features apply to the original implementation, replaced in v2.

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

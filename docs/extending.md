# Adding new katana

1. Add `katana` class implementing `Katana` interface.
2. Add its instance to list in `Katanas` class.
3. Add class implementing `KatanaItem`.
4. Add its instance to `Katanas` class.
5. Add item registry in the `Items` class.
6. Make `katana`'s `getItem` return the item registry.
7. Add translation keys for `katana`'s `getDescription` and `getName` methods.
8. Add texture to `textures/item/<katana_id>.png` and make sure it's returned by `katana`'s `getIconTexture` method.
9. Add model to `models/item/<katana_id>.json`.
10. Add item to `items/<katana_id>.json`.
11. Add corresponding abilities to `abilities` field of the `katana`.
12. Add tag to `data/c/tags/item/tools/melee_weapons.json`.
13. Add tag to `data/minecraft/tags/item/swords.json`.
14. Add recipe to `data/katanamod/recipe/<katana_id>.json`.

# Adding new ability

1. Add `ability` class implementing `Ability` using new or already existing `callback` interface.
2. Add corresponding texture, generic description and detailed description to the `ability`.
3. Add ability to `Abilities` list.
4. Add ability to corresponding `katana`'s `abilities` list.
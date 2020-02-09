# 2.0 Migration Guide: Theming

This guide concerns itself with the new JSON5 theming format introduced in Spinnery 2.0.

## Theming API overview

`WThemable` is the most generic interface for an element that may have a `Theme`; conversely,
`WStyleProvider` is the most generic interface for an element that may provide a `Style` object.

Generally, `WThemable` is something that may _have_ a theme, whereas `WStyleProvider` is something
that may have its appearance styled with a theme: for example, interfaces in Spinnery have themes
(which are applied to children if the children have no themes of their own), but do not have styles
of their own. Widgets, on the other hand, have themes _and_ styles.

### Applying themes

Themes are applied to widgets via `WAbstractWidget#setTheme(Identifier theme)`; a `String` argument
overload also exists. If granularity isn't needed, you may also apply a theme to a screen's interface
with same signature methods on `WInterface`.

If a widget does not have a theme specified, it will fall back onto parent widget themes in order
of hierarchy, then the linked interface, then the `spinnery:default` theme.

### Style overrides

Themes are a powerful styling tool, but if you'd like to override specific properties at runtime,
use `WAbstractWidget#overrideStyle(String key, Object value)`. Style overrides are applied dynamically
to the widget's current base style, which is dictated by its current theme.

### Getting style variables

To get a style variable, access the widget style via `WStyleProvider#getStyle()` and use any one of the
`WStyle#as*(String key)` methods to get a style variable cast to that type: parsers include colors,
positions, paddings, and Java primitives.

## Creating custom themes

Custom themes should be created in the `spinnery/` subdirectory of your mod's assets, and should have
the `.theme.json5` extension.

Your theme may extend an existing one if it has a root-level `parent` key with an identifier string
value. At runtime, the styles from your child theme will be merged with the base styles from the parent
theme and applied to widgets using it.

## Format

The `theme` root-level object should describe widget styles, keyed to those widgets'
registry identifier strings. Style objects may have nested objects; those will be flattened at load time,
and nested values will be available as dot-notation keys (`on` and `off` in a `background` object will
become `background.on` and `background.off`).

To reduce duplication and have single sources of truth, you may also use the root-level `vars` object, which
may contain variables that can be referenced anywhere in `theme` using a `$` prefix.

Themes may also contain a `prototypes` root-level object, which is essentially a collection of styles
that may be extended by any number of other styles. In a style in `theme`, use the special `$extend`
key to specify an array of prototypes that style inherits from.

To see an example of this functionality in action, check out the default themes Spinnery ships with.
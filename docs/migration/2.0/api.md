# 2.0 Migration Guide: API

This guide is a broad stroke overview of the API changes in Spinnery 2.0.

## Data classes

### Position

`WPosition` has been renamed to `Position`.

`Position` is now always relative by design, and the API differs significantly.

* Type enum (FREE/ANCHORED) has been removed; different `of` overloads should be used instead:
  * `Position.of(Positioned source)` creates a new position equal to that of the given positionable element
  (interface, widget etc.).
  * `Position.of(int x, int y, int z)` creates a new position relative to GL origin (`Position.ORIGIN`).
  * `Position.of(Positioned anchor, int x, int y, int z)` creates a new position relative to the given positionable
  element.
  * `Position.of(Positioned anchor, int x, int y)` creates a new position relative to the given anchor, but with a 0
  offset along the Z axis.
  * `Position.ofTopRight(WLayoutElement source)` creates a new position at the top right corner of the
  source layout element.
  * `Position.ofBottomLeft(WLayoutElement source)` creates a new position at the bottom left corner of the
  source layout element.
  * `Position.ofBottomRight(WLayoutElement source)` creates a new position at the bottom right corner of the
  source layout element.
* `Position#add(int x, int y, int z)` is a new utility method that creates a copy of the position object
and adds `x, y, z` to the copy's anchor offset.
* `Position#getRaw*` methods have been renamed to `Position#getRelative*`.
* `Position#setOffset(int x, int y, int z)`, `Position#getOffset*` and `Position#setOffset*` have been added, which
add an additional temporary offset on top of the position; this is useful for lists and other virtual containers which
may change the positions of their children (see e.g. scrollable containers and dropdown classes).

### Size

`WSize` has been renamed to `Size`.

* `Size` now only contains one pair of values as opposed to several.
* For clarity, `Size#getX()` and `Size#getY()` have been renamed to `Size#getWidth()` and `Size#getHeight()`,
respectively.
* `Size.of(Sized source)` creates a new size equal to that of the given sizable element (interface, widget etc.).
* `Size#add(int width, int height)` creates a copy of the size object and adds the provided width and height to it;
useful in conjunction with `Size.of(Sized source)` to create sizes relative to an existing widget.

### Color

* `WColor` can now be created via `of` from a color `int`.

## New utilities

### Padding

New data class that describes a set of four directioned offsets. It is designed as a layouting utility for
classes implementing `WPadded` that have an outer (e.g. decorative) area and an inner (e.g. functional) area.
To see examples of its usage, check out list and slider classes.

### WVirtualArea

New utility class that describes a virtual area for layouting. It is a simple implementation of `WLayoutElement`
that has no drawing code, but offers various layouting methods. To see an example of its usage, see the `WDropdown`
class.
# 2.0 Migration Guide: Coding UIs

This guide concerns itself with migrating your `Screen` classes (`BaseScreen` and `BaseContainerScreen`), including
short descriptions of changes made and examples of commonly required operations.

For brevity, all code snippets are assumed to be located inside the `Screen` class constructor.

## Setting up the interface

`WInterface` now represents the "canvas" on which all widgets are placed and drawn, as opposed to a "window"
or panel. Thus, it does not have styles itself. It is automatically sized to be equal to the client's window
size and positioned to be at `(0; 0; 0)`.

_To create a window, create a `WPanel` widget and add it to the interface; see
[Creating widgets](#creating-widgets) for more information._

### Before

```java
WInterface mainInterface = new WInterface(WPosition.of(WType.FREE, 0, 0, 0));
getInterfaceHolder().add(mainInterface);
```

### After

```java
WInterface mainInterface = getInterface();
```

## Creating widgets

One of the biggest differences between 1.0 and 2.0 is widget creation. 2.0 now creates widgets via `WWidgetFactory`,
which eliminates repeated boilerplate code. Widgets and interfaces also have convenient helper methods to further
reduce the amount of boilerplate.

There are two principal widget creation flows: in addition to the classic procedural flow, Spinnery now also offers a
more concise chainable API.

### Before

```java
WInterface mainInterface = new WInterface(WPosition.of(WType.FREE, 0, 0, 0));
getInterfaceHolder().add(mainInterface);

mainInterface.center();
mainInterface.setY(32);

mainInterface.setOnAlign(() -> {
    mainInterface.center();
    mainInterface.setY(32);
});

WButton button = new WButton(
        WPosition.of(WType.ANCHORED, entry.getWidth() - 110, 0, 15, mainInterface),
        WSize.of(45, 20),
        mainInterface
);
button.setLabel(new LiteralText("Edit"));
mainInterface.add(button);
```

### After

#### Procedural flow

```java
WInterface mainInterface = getInterface();

WPanel panel = mainInterface.createChild(WPanel.class, Position.of(screen, 0, 32), Size.of(170, 170));
panel.setOnAlign(WAbstractWidget::centerX);
panel.centerX();

WButton button = panel.createChild(WButton.class, Position.of(mainPanel, 8, 8), Size.of(48, 18));
button.setLabel("Edit"); // Convenience method that creates a LiteralText from the string
```

#### Chainable flow

```java
WInterface mainInterface = getInterface();

WPanel panel = mainInterface.createChild(WPanel.class, Position.of(screen, 0, 32), Size.of(170, 170))
    .setOnAlign(WAbstractWidget::centerX)
    .centerX(); // Returns void, so should be the terminal operation in the chain

panel.createChild(Button.class, Position.of(mainPanel, 8, 8), Size.of(48, 18))
    .setLabel("Edit");
```

With the chainable flow, you only need to assign created widgets to variables if you need to use them elsewhere later;
if they are only created to be added to another widget, storing them in a variable becomes unnecessary.

## Widget changes

* `WDynamicText` has been removed and replaced with `WTextArea` (multiline, supports line wrapping) and `WTextField`
(single-line, supports maximum length).
* `WHorizontalList` and `WVerticalList` have been removed. In place of these, use a `WPanel` with a
`WHorizontalScrollableContainer` or `WVerticalScrollableContainer` child.

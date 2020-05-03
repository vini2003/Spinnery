package com.github.vini2003.spinnery.debug;

import net.minecraft.entity.player.PlayerInventory;
import com.github.vini2003.spinnery.common.BaseContainerScreen;
import com.github.vini2003.spinnery.widget.*;
import com.github.vini2003.spinnery.widget.api.Position;
import com.github.vini2003.spinnery.widget.api.Size;
import net.minecraft.util.text.ITextComponent;

public class TestContainerScreen extends BaseContainerScreen<TestContainer> {
	public TestContainerScreen(TestContainer container, PlayerInventory playerInventory, ITextComponent name) {
		super(name, container, playerInventory.player);

		WInterface mainInterface = getInterface();

		WPanel panel = mainInterface.createChild(WPanel::new, Position.of(0, 0, 0), Size.of(176, 128));
		panel.centerX(); // Center it on the screen in the X (horizontal) axis.
		panel.centerY(); // Center it on the screen in the Y (vertical) axis.
		panel.center(); // Center it on the screen in both axis.
		panel.align(); // Adjust its position based on its anchor. Well, there's no anchor here so this method is worthless for this widget instance.

		panel.setLabel("Furnace");

		WSlot.addPlayerInventory(Position.of(panel, 7, 36, 0), Size.of(18, 18), panel);

		getInterface().setTheme("default");

//		WButton button = panel.createChild(WButton::new, Position.of(panel, 4, 4, 0), Size.of(18, 9));
//		button.setLabel(new StringTextComponent("You can use a StringTextComponent here,"));
//		button.setLabel(new TranslatableText("Or a TranslatableText,"));
//		button.setLabel(new String("Or even a String!"));
//
//		WTexturedButton texturedButton = panel.createChild(WTexturedButton::new);
//
//		WDropdown dropdown = panel.createChild(WDropdown::new, Position.of(panel, 26, 4, 0), Size.of(48, 32));
//		dropdown.setSize(Size.of(32, 48)); // Here's the Size when the Dropdown is hidden!
//		dropdown.setDropdownSize(Size.of(48, 64)); // Here's the Size when the Dropdown is expanded!
//		dropdown.setState(true); // Make it expanded by default.
//		dropdown.setHideBehavior(WDropdown.HideBehavior.TOGGLE); // Make it hide only when toggled.
//		dropdown.setLabel("By the way, I can also be labelled!"); // Make it have a label.
//
//		WStaticImage staticImage = panel.createChild(WStaticImage::new, Position.of(panel, 112, 4, 0), Size.of(16, 16));
//		staticImage.setTexture(new ResourceLocation(Spinnery.MOD_ID, "textures/kirby.png")); // Make it render a Kirby sprite. Yes, Spinnery includes one by default.
//
//		WKibbyImmage kibbyImage = panel.createChild(WKibbyImmage::new, Position.of(panel, 132, 4, 0), Size.of(16, 16)); // Hey, I'm a Kirby. Yep, this is a thing.
//
//		WDynamicImage dynamicImage = panel.createChild(WDynamicImage::new, Position.of(panel, 152, 4, 0), Size.of(16, 16));
//		dynamicImage.setTextures(new ResourceLocation(Spinnery.MOD_ID, "textures/kirby.png"), new ResourceLocation("textures/item/iron_ingot")); // Make it show two textures.
//		dynamicImage.next(); // Move to next available image, or go back to start if out of them.
//		dynamicImage.previous(); // Move to the previous available image, or go to the end if out of them.
//
//		WHorizontalBar horizontalBar = panel.createChild(WHorizontalBar::new, Position.of(panel, 4, 36, 0), Size.of(48, 9));
//		horizontalBar.setLimit(new MutableInt(128)); // Make the limit 128, using a <T extends Number> so it updates automatically with the variable.
//		horizontalBar.setProgress(new MutableInt(64)); // Make the progress 64, using a <T extends Number>, so it updates automatically with the variable.
//		horizontalBar.setBackgroundTexture(new ResourceLocation("textures/item/potato")); // Make the background textures (empty area) be a potato.
//		horizontalBar.setForegroundTexture(new ResourceLocation("textures/item/carrot")); // Make the foreground texture (filled area) be a carrot.
//
//		WVerticalBar verticalBar = panel.createChild(WVerticalBar::new, Position.of(panel, 4, 49, 0), Size.of(9, 48));
//		verticalBar.setLimit(horizontalBar.getLimit()); // We've done this before.
//		verticalBar.setProgress(horizontalBar.getProgress()); // And, well, this is no different.
//		verticalBar.setBackgroundTexture(new ResourceLocation("textures/item/carrot")); // Let's at least switch up the background texture...
//		verticalBar.setForegroundTexture(new ResourceLocation("textures/item/carrot")); //  And the foreground texture.
//
//		WHorizontalSlider horizontalSlider = panel.createChild(WHorizontalSlider::new, Position.of(panel, 4, 101, 0), Size.of(32, 9));
//		horizontalSlider.setMax(128); // A little different from a bar.
//		horizontalSlider.setMin(64); // Only a little.
//
//		WVerticalSlider verticalSlider = panel.createChild(WVerticalSlider::new, Position.of(panel, 4, 114, 0), Size.of(9, 32));
//		verticalSlider.setMax(64); // Utterly amazing - let's switch these up.
//		verticalSlider.setMin(128); // Ha - so original!
//
//
//		WDraggableContainer draggable = panel.createChild(WDraggableContainer::new, Position.of(panel, 78, 4, 0), Size.of(32, 32));
//		draggable.setLabel("I'm a bit more complicated, but I hold widgets like a Thaumonomicon!"); // Make it have a label. It will never be drawn, however, as this is a widget container.
//
//		for (int i = 0; i < 8; ++i) {
//			draggable.createChild(WButton::new, Position.of(draggable, new Random().nextInt(24), new Random().nextInt(24), 0), Size.of(4, 4)); // Scatter some buttons throughout it.
//		}


	}
}
package spinnery.debug;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import spinnery.Spinnery;
import spinnery.common.BaseContainer;
import spinnery.common.BaseContainerScreen;
import spinnery.common.BaseInventory;
import spinnery.widget.*;

public class TestContainerScreen extends BaseContainerScreen<TestContainer> {
	public TestContainerScreen(Text name, TestContainer linkedContainer, PlayerEntity player) {
		super(name, linkedContainer, player);

		// WInterface
		WInterface mainInterface = new WInterface(WPosition.of(WType.FREE, 0, 0, 0), WSize.of(170, 170), linkedContainer);

		getHolder().add(mainInterface);

		mainInterface.center();
		mainInterface.setY(32);

		mainInterface.setOnAlign(() -> {
			mainInterface.center();
			mainInterface.setY(32);
		});
		// WInterface


		// WDropdown
		WDropdown dropdownA = new WDropdown(WPosition.of(WType.ANCHORED, 174, 0, 0, mainInterface), WSize.of(64, 16, 64, 154), mainInterface);

		WToggle toggleA = new WToggle(WPosition.of(WType.ANCHORED, 0, 0, 1, dropdownA), WSize.of(18, 9), mainInterface);

		dropdownA.add(toggleA);
		dropdownA.setLabel(new LiteralText("Dropdown"));
		// WDropdown


		// WList
		WVerticalList listA = new WVerticalList(WPosition.of(WType.ANCHORED, -70, 0, 1, mainInterface), WSize.of(64, 118), mainInterface);
		listA.setScroller(true);
		for (int i = 0; i < 60; i++) {
			WToggle toggle = new WToggle(WPosition.of(WType.ANCHORED, 0, 0, 1, listA), WSize.of(18, 9), mainInterface);
			WStaticText caption = new WStaticText(WPosition.of(WType.ANCHORED, 25, 0, 1, listA), mainInterface, new LiteralText("B" + i));
			listA.add(toggle, caption);
		}
		listA.setLabel(new LiteralText("List"));

		WHorizontalList listB = new WHorizontalList(WPosition.of(WType.ANCHORED, -70, 122, 10, mainInterface), WSize.of(64, 42), mainInterface);
		listB.setScroller(true);
		for (int i = 0; i < 60; i++) {
			WToggle toggle = new WToggle(WPosition.of(WType.ANCHORED, 0, 0, 1, listB), WSize.of(18, 9), mainInterface);
			WStaticText caption = new WStaticText(WPosition.of(WType.ANCHORED, 25, 0, 1, listB), mainInterface, new LiteralText("B" + i));
			listB.add(toggle, caption);
		}
		// WList


		// WTabHolder
		WTabHolder tabHolderA = new WTabHolder(WPosition.of(WType.ANCHORED, 0, 174, 1, mainInterface), WSize.of(170, 48), mainInterface);

		WTabHolder.WTab tabA = tabHolderA.addTab(Items.EMERALD, new LiteralText("Tab A"));
		WTabHolder.WTab tabB = tabHolderA.addTab(Items.EMERALD, new LiteralText("Tab B"));
		WTabHolder.WTab tabC = tabHolderA.addTab(Items.EMERALD, new LiteralText("Tab C"));
		// WTabHolder


		// WButton
		WButton buttonA = new WButton(WPosition.of(WType.ANCHORED, 8, 8, 1, mainInterface), WSize.of(48, 18), mainInterface);
		WButton buttonB = new WButton(WPosition.of(WType.ANCHORED, 8, 28, 1, mainInterface), WSize.of(27, 18), mainInterface);

		buttonA.setLabel(new LiteralText("Button A"));
		buttonB.setLabel(new LiteralText("Button B"));
		// WButton


		// WDynamicText
		WDynamicText dynamicTextA = new WDynamicText(WPosition.of(WType.ANCHORED, 8, 130, 2, mainInterface), WSize.of(156, 69), mainInterface);

		dynamicTextA.setLabel(new LiteralText("§oDynamicText A..."));

		tabA.add(dynamicTextA);
		// WDynamicText


		// WStaticText
		WStaticText staticTextA = new WStaticText(WPosition.of(WType.ANCHORED, 8, 74, 0, mainInterface), mainInterface, new LiteralText("StaticText A"));
		// WStaticText


		// WHorizontalSlider
		WHorizontalSlider horizontalSliderA = new WHorizontalSlider(WPosition.of(WType.ANCHORED, 8, 48, 0, mainInterface), WSize.of(48, 12), mainInterface, 9);
		// WHorizontalSlider


		// WSlot
		WSlot.addPlayerInventory(WSize.of(18, 18), mainInterface, BaseContainer.PLAYER_INVENTORY);
		// WSlot


		/// WStaticImage
		WStaticImage staticImageA = new WStaticImage(WPosition.of(WType.ANCHORED, 80, 54, 3, mainInterface), WSize.of(32, 32), mainInterface, new Identifier(Spinnery.MOD_ID, "textures/kirby.png"));
		// WStaticImage


		// WTexturedButton
		//WTexturedButton texturedButtonA = new WTexturedButton(WPosition.of(WType.ANCHORED, 0, 0, 0, mainInterface), WSize.of(200, 20), mainInterface, new Identifier(Spinnery.MOD_ID, "textures/button_inactive.png"), new Identifier(Spinnery.MOD_ID, "textures/button_active.png"), new Identifier(Spinnery.MOD_ID, "textures/button_disabled.png"));
		// WTexturedButton



		// WSlot Array
		WSlot.addArray(WPosition.of(WType.ANCHORED, 80, 8, 0, mainInterface), WSize.of(18, 18), mainInterface, 0, 1, 3, 2);
		// WSlot Array

		mainInterface.add(dropdownA, listA, listB, buttonA, buttonB, staticTextA, horizontalSliderA, staticImageA, tabHolderA);

		buttonA.setOnMouseClicked(() -> {
			System.out.println(linkedContainer.getInventory(1));
		});

		mainInterface.setTheme("dark");

		for (WWidget widgetA : mainInterface.getWidgets()) {
			if (widgetA instanceof WSlot && ((WSlot) widgetA).getLinkedInventory() instanceof BaseInventory) {
				((WSlot) widgetA).setOverrideMaximumCount(true);
				((WSlot) widgetA).setMaximumCount(256);
			}
			widgetA.setTheme("dark");

			if (widgetA instanceof WCollection) {
				for (WWidget widgetB : ((WCollection) widgetA).getWidgets()) {
					widgetB.setTheme("dark");
				}
			}
		}
	}
}

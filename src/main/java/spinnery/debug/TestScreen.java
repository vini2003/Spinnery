package spinnery.debug;

import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import spinnery.client.BaseScreen;
import spinnery.widget.*;

public class TestScreen extends BaseScreen {
	public TestScreen() {
		super();

		WInterface mainInterface = new WInterface(WPosition.of(WType.FREE, 0, 0, 0));

		getInterfaceHolder().add(mainInterface);

		mainInterface.center();
		mainInterface.setY(32);

		mainInterface.setOnAlign(() -> {
			mainInterface.center();
			mainInterface.setY(32);
		});


		// WDropdown
		WDropdown dropdownA = new WDropdown(WPosition.of(WType.ANCHORED, 174, 0, 0, mainInterface), WSize.of(64, 16, 64, 154), mainInterface);

		WToggle toggleA = new WToggle(WPosition.of(WType.ANCHORED, 0, 0, 1, dropdownA), WSize.of(18, 9), mainInterface);

		dropdownA.add(toggleA);
		dropdownA.setLabel(new LiteralText("Dropdown"));
		// WDropdown


		// WList
		WVerticalList listA = new WVerticalList(WPosition.of(WType.ANCHORED, -70, 0, 1, mainInterface), WSize.of(64, 154), mainInterface);

		WToggle toggleB = new WToggle(WPosition.of(WType.ANCHORED, 0, 0, 1, listA), WSize.of(18, 9), mainInterface);

		listA.add(toggleB);
		listA.setLabel(new LiteralText("List"));
		// WList


		// WTabHolder
		WTabHolder tabHolderA = new WTabHolder(WPosition.of(WType.ANCHORED, 0, 100, 1, mainInterface), WSize.of(170, 64), mainInterface);

		WTabHolder.WTab tabA = tabHolderA.addTab(Items.EMERALD, new LiteralText("Tab A"));
		WTabHolder.WTab tabB = tabHolderA.addTab(Items.EMERALD, new LiteralText("Tab B"));
		// WTabHolder


		// WButton
		WButton buttonA = new WButton(WPosition.of(WType.ANCHORED, 8, 8, 1, mainInterface), WSize.of(48, 18), mainInterface);
		WButton buttonB = new WButton(WPosition.of(WType.ANCHORED, 8, 28, 1, mainInterface), WSize.of(27, 18), mainInterface);

		buttonA.setLabel(new LiteralText("Button A"));
		buttonB.setLabel(new LiteralText("Button B"));
		// WButton


		// WDynamicText
		WDynamicText dynamicTextA = new WDynamicText(WPosition.of(WType.ANCHORED, 8, 130, 0, mainInterface), WSize.of(156, 18), mainInterface);

		dynamicTextA.setLabel(new LiteralText("§oDynamicText A..."));

		tabA.add(dynamicTextA);
		// WDynamicText


		// WStaticText
		WStaticText staticTextA = new WStaticText(WPosition.of(WType.ANCHORED, 8, 74, 0, mainInterface), mainInterface, new LiteralText("StaticText A"));
		// WStaticText


		// WHorizontalSlider
		WHorizontalSlider horizontalSliderA = new WHorizontalSlider(WPosition.of(WType.ANCHORED, 8, 48, 0, mainInterface), WSize.of(48, 12), mainInterface, 9);
		// WHorizontalSlider

		mainInterface.add(dropdownA, listA, tabHolderA, buttonA, buttonB, staticTextA, horizontalSliderA);
	}
}

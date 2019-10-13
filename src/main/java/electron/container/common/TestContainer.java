package electron.container.common;

import electron.container.common.widget.ItemSlot;
import electron.container.common.widget.Panel;
import electron.container.common.widget.Sprite;
import electron.container.common.widget.Toggle;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class TestContainer extends BaseContainer {
	public TestContainer(int synchronizationID, Inventory linkedInventory, PlayerInventory linkedPlayerInventory) {
		super(synchronizationID, linkedInventory, linkedPlayerInventory);
		linkedInventory = new BasicInventory(9);
		setLinkedPanel(new Panel(this));
		getLinkedPanel().setSizeX(150 + 32);
		getLinkedPanel().setSizeY(178 + 32);
		getLinkedPanel().alignWithContainerEdge();
		getLinkedPanel().addWidget(new Sprite(0, 40, 137, 70, new Identifier("electron:textures/widget/test.png"), linkedPanel));
		getLinkedPanel().addWidget(new Toggle(0, 140, 18, 18, linkedPanel));
		Identifier OwO = new Identifier("electron:textures/widget/owo.png");
		Identifier UwU = new Identifier("electron:textures/widget/uwu.png");
		Sprite sprite = (Sprite) getLinkedPanel().getLinkedWidgets().get(0);
		sprite.setOnMouseClicked(() -> {
			if (sprite.getTexture().toString().equals(OwO.toString())) {
				sprite.setTexture(UwU);
			} else {
				sprite.setTexture(OwO);
			}
		});
		//getLinkedPanel().addWidget(new ItemSlot(0, 0, 18, 18, 0, linkedInventory, linkedPanel));
		//getLinkedPanel().addWidget(new ItemSlot(18, 0, 18, 18, 1, linkedInventory, linkedPanel));

		//((ItemSlot) getLinkedPanel().getLinkedWidgets().get(1)).internalSlot.setStack((new ItemStack(Items.COBBLESTONE, 64)));

		//getLinkedPanel().getLinkedWidgets().forEach(Widget::center);
	}
}

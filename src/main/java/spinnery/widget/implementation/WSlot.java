package spinnery.widget.implementation;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.slot.Slot;
import spinnery.Spinnery;
import spinnery.client.texture.PartitionedTexture;
import spinnery.widget.declaration.collection.WCollection;

public class WSlot extends WAbstractWidget {
	private final Slot backendSlot;

	private final PartitionedTexture texture = new PartitionedTexture(Spinnery.identifier("textures/widget/slot.png"), 18F, 18F, 0.05555555555555555556F, 0.05555555555555555556F, 0.05555555555555555556F, 0.05555555555555555556F);

	public WSlot(Slot backendSlot) {
		this.backendSlot = backendSlot;
	}

	@Override
	public void onAdded(WInterface linkedInterface, WCollection parent) {
		super.onAdded(linkedInterface, parent);

		updateSlotPosition();

		if (linkedInterface.getHandler() != null) {
			(linkedInterface.getHandler()).addSlot(backendSlot);
		}
	}

	@Override
	public void onRemoved(WInterface linkedInterface, WCollection parent) {
		super.onRemoved(linkedInterface, parent);

		updateSlotPosition();

		if (linkedInterface.getHandler() != null) {
			(linkedInterface.getHandler()).removeSlot(backendSlot);
		}
	}

	private int getSlotX() {
		return (int) (getX() + ((getWidth() <= 18) ? 1F : getWidth() / 2F - 9F));
	}

	private int getSlotY() {
		return (int) (getY() + ((getHeight() <= 18) ? 1F : getHeight() / 2F - 9F));
	}

	@Override
	public <W extends WAbstractWidget> W setHidden(boolean isHidden) {
		WAbstractWidget widget = super.setHidden(isHidden);

		updateSlotPosition();

		return (W) widget;
	}

	public void updateSlotPosition() {
		if (isHidden()) {
			backendSlot.x = Integer.MAX_VALUE;
			backendSlot.y = Integer.MAX_VALUE;
		} else {
			backendSlot.x = getSlotX();
			backendSlot.y = getSlotY();
		}
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		if (isHidden()) {
			return;
		}

		texture.draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
	}
}
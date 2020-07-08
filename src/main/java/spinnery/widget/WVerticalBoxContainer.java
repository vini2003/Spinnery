package spinnery.widget;

import com.google.common.collect.ImmutableSet;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import spinnery.client.render.BaseRenderer;
import spinnery.client.utility.ScissorArea;
import spinnery.widget.api.*;

import java.util.*;

public class WVerticalBoxContainer extends WAbstractWidget implements WModifiableCollection, WDelegatedEventListener {
	protected Set<WAbstractWidget> widgets = new HashSet<>();


	public float topBottomPadding = 0;
	public boolean topBottomOverride = false;

	public float leftRightPadding = 0;
	public boolean leftRightOverride = false;

	public float intermediaryPadding = 0;
	public boolean intermediaryOverride = false;

	public float outerBorderWidth = 1;

	public boolean hasBorder;

	public <W extends WVerticalBoxContainer> W setBorder(boolean hasBorder) {
		this.hasBorder = hasBorder;
		return (W) this;
	}

	public boolean hasBorder() {
		return hasBorder;
	}

	public <W extends WVerticalBoxContainer> W setTopBottomPadding(float topBottomPadding) {
		this.topBottomPadding = topBottomPadding;
		this.topBottomOverride = true;
		return (W) this;
	}

	public float getTopBottomPadding() {
		return topBottomPadding;
	}

	public <W extends WVerticalBoxContainer> W clearTopBottomPadding() {
		topBottomPadding = 0;
		topBottomOverride = false;
		return (W) this;
	}

	public <W extends WVerticalBoxContainer> W setLeftRightPadding(float leftRightPadding) {
		this.leftRightPadding = leftRightPadding;
		this.leftRightOverride = true;
		return (W) this;
	}

	public float getLeftRightPadding() {
		return leftRightPadding;
	}

	public <W extends WVerticalBoxContainer> W clearLeftRightPadding() {
		leftRightPadding = 0;
		leftRightOverride = false;
		return (W) this;
	}

	public <W extends WVerticalBoxContainer> W setIntermediaryPadding(float intermediaryPadding) {
		this.intermediaryPadding = intermediaryPadding;
		this.intermediaryOverride = true;
		return (W) this;
	}

	public float getIntermediaryPadding() {
		return intermediaryPadding;
	}

	public <W extends WVerticalBoxContainer> W clearIntermediaryPadding() {
		intermediaryPadding = 0;
		intermediaryOverride = false;
		return (W) this;
	}

	public <W extends WVerticalBoxContainer> W setOuterBorderWidth(float outerBorderWidth) {
		this.outerBorderWidth = outerBorderWidth;
		return (W) this;
	}

	public float getOuterBorderWidth() {
		return outerBorderWidth;
	}

	@Override
	public Collection<? extends WEventListener> getEventDelegates() {
		Set<WAbstractWidget> delegates = new HashSet<>(widgets);
		return ImmutableSet.copyOf(delegates);
	}

	@Override
	public void add(WAbstractWidget... widgets) {
		this.widgets.addAll(Arrays.asList(widgets));

		updateContents();
	}

	@Override
	public void remove(WAbstractWidget... widgets) {
		this.widgets.removeAll(Arrays.asList(widgets));

		updateContents();
	}

	@Override
	public Set<WAbstractWidget> getWidgets() {
		return widgets;
	}

	@Override
	public boolean contains(WAbstractWidget... widgets) {
		return this.widgets.containsAll(Arrays.asList(widgets));
	}

	public void updateContents() {
		if (!leftRightOverride) leftRightPadding = ((0.1f) * getWidth());
		if (!topBottomOverride) topBottomPadding = ((0.05f) * getHeight());
		if (!intermediaryOverride) intermediaryPadding = ((0.025f) * getHeight());

		float totalWidgetSizeX = getWidth() - (2 * leftRightPadding);
		float totalWidgetSizeY = getHeight() - (2 * topBottomPadding) - ((this.widgets.size() > 1 ? this.widgets.size() - 1 : 0) * intermediaryPadding);

		float lastPositionY = topBottomPadding;
		float positionX = leftRightPadding;
		float widgetSizeX = totalWidgetSizeX;
		float widgetSizeY = totalWidgetSizeY / Math.max(this.widgets.size(), 1);

		for (WAbstractWidget widget : widgets) {
			Size newWidgetSize = Size.of(widgetSizeX, widgetSizeY);

			if (widget.getMaximumAutoSize().isSmallerInWidthOrHeight(newWidgetSize)) {
				newWidgetSize = widget.getMaximumAutoSize();
			}

			Position newWidgetPosition = Position.of(this, positionX, lastPositionY);

			widget.setPosition(newWidgetPosition);
			widget.setSize(newWidgetSize);
			lastPositionY += newWidgetSize.getHeight() + intermediaryPadding;
		}
	}

	@Override
	public void onLayoutChange() {
		super.onLayoutChange();
		updateContents();
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider.Immediate provider) {
		if (isHidden()) {
			return;
		}

		ScissorArea area = new ScissorArea(this);

		for (WAbstractWidget widget : widgets) {
			widget.draw(matrices, provider);
		}

		if (hasBorder()) {
			BaseRenderer.drawQuad(matrices, provider, getX(), getY(), getZ(), getWidth(), outerBorderWidth, getStyle().asColor("border"));
			BaseRenderer.drawQuad(matrices, provider, getX(), getY(), getZ(), outerBorderWidth, getHeight(), getStyle().asColor("border"));
			BaseRenderer.drawQuad(matrices, provider, getX(), getHighY() - 1, getZ(), getWidth(), outerBorderWidth, getStyle().asColor("border"));
			BaseRenderer.drawQuad(matrices, provider, getWideX() - 1, getY(), getZ(), outerBorderWidth, getHeight(), getStyle().asColor("border"));
		}

		area.destroy();
	}
}

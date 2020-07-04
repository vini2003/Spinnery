package spinnery.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;
import spinnery.client.render.BaseRenderer;
import spinnery.client.render.TextRenderer;
import spinnery.widget.api.*;

import java.util.*;

@SuppressWarnings("unchecked")
@Environment(EnvType.CLIENT)
public class WDropdown extends WAbstractWidget implements WDrawableCollection, WModifiableCollection, WDelegatedEventListener {
	protected Set<WAbstractWidget> widgets = new HashSet<>();
	protected List<WLayoutElement> orderedWidgets = new ArrayList<>();
	protected boolean state = false;
	protected Size dropdownSize;
	protected WVirtualArea toggle;
	protected HideBehavior hideBehavior = HideBehavior.TOGGLE;

	@Override
	public Collection<? extends WEventListener> getEventDelegates() {
		return getWidgets();
	}

	@Override
	public Set<WAbstractWidget> getWidgets() {
		return widgets;
	}

	@Override
	public boolean contains(WAbstractWidget... widgetArray) {
		return widgets.containsAll(Arrays.asList(widgetArray));
	}

	public HideBehavior getHideBehavior() {
		return hideBehavior;
	}

	public <W extends WDropdown> W setHideBehavior(HideBehavior hideBehavior) {
		this.hideBehavior = hideBehavior;
		return (W) this;
	}

	@Override
	public void align() {
		super.align();
		updateChildren();
	}

	@Override
	public boolean updateFocus(float positionX, float positionY) {
		super.updateFocus(positionX, positionY);

		setFocus(isWithinBounds(positionX, positionY) && getAllWidgets().stream().noneMatch((WAbstractWidget::isFocused)));

		return isFocused();
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider.Immediate provider) {
		if (isHidden()) {
			return;
		}

		RenderSystem.translatef(0, 0, getZ() * 400f);
  		matrices.translate(0, 0, getZ() * 400f);

		float x = getX();
		float y = getY();
		float z = getZ();

		float sX = getWidth();
		float sY = getHeight();

		BaseRenderer.drawPanel(matrices, provider, x, y, z, sX, sY + 1.75f,
				getStyle().asColor("shadow"), getStyle().asColor("background"),
				getStyle().asColor("highlight"), getStyle().asColor("outline"));

		if (hasLabel()) {
			TextRenderer.pass().shadow(isLabelShadowed())
					.text(getLabel()).at(x + 8, y + 6, z)
					.color(getStyle().asColor("label.color")).shadowColor(getStyle().asColor("label.shadow_color")).render(matrices, provider);

			if (getState()) {
				BaseRenderer.drawQuad(matrices, provider, x, y + 16, z, sX, 1, getStyle().asColor("outline"));
				BaseRenderer.drawQuad(matrices, provider, x + 1, y + 17, z, sX - 2, 0.75f, getStyle().asColor("shadow"));
			}
		}

		if (getState()) {
			for (WLayoutElement widgetC : getOrderedWidgets()) {
				widgetC.draw(matrices, provider);
			}
		}

  		matrices.translate(0, 0, getZ() * -400f);
		RenderSystem.translatef(0, 0, getZ() * -400f);
	}

	@Override
	public float getHeight() {
		return getToggleHeight() + (state ? dropdownSize.getHeight() : 0);
	}

	@Override
	public float getWidth() {
		return Math.max(getToggleWidth(), state ? dropdownSize.getWidth() : 0);
	}

	@Override
	public void onMouseClicked(float mouseX, float mouseY, int mouseButton) {
		boolean shouldOpen = isWithinBounds(mouseX, mouseY);
		boolean shouldClose = false;

		super.onMouseClicked(mouseX, mouseY, mouseButton);
		if (getState()) {
			switch (hideBehavior) {
				case TOGGLE:
					shouldClose = toggle.isWithinBounds(mouseX, mouseY);
					break;
				case ANYWHERE:
					shouldClose = true;
					break;
				case INSIDE:
					shouldClose = isWithinBounds(mouseX, mouseY);
					break;
				case ONLY_CHILD:
					shouldClose = (isWithinBounds(mouseX, mouseY) && !isFocused());
					break;
				case ANYWHERE_EXCEPT_CHILD:
					shouldClose = (!isWithinBounds(mouseX, mouseY) || isFocused());
					break;
				case INSIDE_EXCEPT_CHILD:
					shouldClose = (isWithinBounds(mouseX, mouseY) && isFocused());
					break;
			}
		}

		boolean shouldToggle = !getState() ? shouldOpen : shouldClose;

		if (shouldToggle && mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			setState(!getState());
			updateChildren();
		}
	}

	public boolean getState() {
		return state;
	}

	protected void updateChildren() {
		for (WAbstractWidget widget : widgets) {
			widget.getPosition().setOffset(0, getToggleHeight() + 2, 0);
			widget.setHidden(!getState());
		}
	}

	public float getToggleHeight() {
		return super.getHeight();
	}

	@SuppressWarnings("UnusedReturnValue")
	public <W extends WDropdown> W setState(boolean state) {
		this.state = state;
		updateChildren();
		return (W) this;
	}

	public float getToggleWidth() {
		return super.getWidth();
	}

	public Size getDropdownSize() {
		return dropdownSize;
	}

	public <W extends WDropdown> W setDropdownSize(Size dropdownSize) {
		this.dropdownSize = dropdownSize;
		return (W) this;
	}

	@Override
	public void add(WAbstractWidget... widgetArray) {
		widgets.addAll(Arrays.asList(widgetArray));
		updateChildren();
		onLayoutChange();
	}

	@Override
	public void onLayoutChange() {
		super.onLayoutChange();
		toggle = new WVirtualArea(Position.of(this), Size.of(getToggleWidth(), getToggleHeight()));
		updateChildren();
		recalculateCache();
	}

	@Override
	public void recalculateCache() {
		orderedWidgets = new ArrayList<>(getWidgets());
		Collections.sort(orderedWidgets);
		Collections.reverse(orderedWidgets);
	}

	@Override
	public List<WLayoutElement> getOrderedWidgets() {
		return orderedWidgets;
	}

	@Override
	public void remove(WAbstractWidget... widgetArray) {
		widgets.removeAll(Arrays.asList(widgetArray));
		updateChildren();
		onLayoutChange();
	}

	public enum HideBehavior {
		TOGGLE,
		ANYWHERE,
		ANYWHERE_EXCEPT_CHILD,
		ONLY_CHILD,
		INSIDE,
		INSIDE_EXCEPT_CHILD
	}
}

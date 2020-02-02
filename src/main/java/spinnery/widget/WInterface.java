package spinnery.widget;

import spinnery.client.BaseRenderer;
import spinnery.common.BaseContainer;

import java.util.*;

public class WInterface extends WWidget implements WModifiableCollection {
	public static final boolean INSTANCE_CLIENT = false;
	public static final boolean INSTANCE_SERVER = true;
	protected BaseContainer linkedContainer;
	protected Set<WWidget> heldWidgets = new LinkedHashSet<>();
	protected Map<Object, WWidget> cachedWidgets = new HashMap<>();
	protected boolean isClientside;
	protected boolean instanceType;

	public WInterface(BaseContainer linkedContainer) {
		setContainer(linkedContainer);
	}

	public WInterface(WPosition position) {
		setPosition(position);
		setSize(WSize.of(0, 0));
		setClientside(true);
		setInstanceType(INSTANCE_CLIENT);
	}

	public void setClientside(Boolean clientside) {
		isClientside = clientside;
	}

	public boolean isClient() {
		return instanceType == INSTANCE_CLIENT;
	}

	public WInterface(WPosition position, WSize size) {
		setPosition(position);
		setSize(size);
		setClientside(true);
		setInstanceType(INSTANCE_CLIENT);
	}

	public WInterface(WPosition position, WSize size, BaseContainer linkedContainer) {
		setPosition(position);
		setSize(size);
		setContainer(linkedContainer);
		setClientside(false);

		if (getContainer().getLinkedWorld().isClient()) {
			setInstanceType(INSTANCE_CLIENT);
		} else {
			setInstanceType(INSTANCE_SERVER);
		}
	}

	public BaseContainer getContainer() {
		return linkedContainer;
	}

	public void setContainer(BaseContainer linkedContainer) {
		this.linkedContainer = linkedContainer;
	}

	public WInterface(WPosition position, BaseContainer linkedContainer) {
		setPosition(position);
		setSize(WSize.of(0, 0));
		setContainer(linkedContainer);
		setClientside(false);

		if (getContainer().getLinkedWorld().isClient()) {
			setInstanceType(INSTANCE_CLIENT);
		} else {
			setInstanceType(INSTANCE_SERVER);
		}
	}

	public boolean getInstanceType() {
		return instanceType;
	}

	public void setInstanceType(boolean instanceType) {
		this.instanceType = instanceType;
	}

	public boolean isServer() {
		return instanceType == INSTANCE_SERVER;
	}

	@Override
	public Set<WWidget> getWidgets() {
		return heldWidgets;
	}

	@Override
	public Set<WWidget> getAllWidgets() {
		return heldWidgets;
	}

	@Override
	public void add(WWidget... widgets) {
		for (WWidget widget : widgets) {
			if (widget instanceof WServer && isClientside()) {
				throw new RuntimeException("Cannot add server-side WWidget to non-server-side WIntertface!");
			}
		}

		heldWidgets.addAll(Arrays.asList(widgets));
	}

	@Override
	public void remove(WWidget... widgets) {
		heldWidgets.removeAll(Arrays.asList(widgets));
	}

	@Override
	public boolean contains(WWidget... widgets) {
		return heldWidgets.containsAll(Arrays.asList(widgets));
	}

	public Boolean isClientside() {
		return isClientside;
	}

	@Override
	public void draw() {
		if (isHidden()) {
			return;
		}

		if (isDrawable()) {
			int x = getX();
			int y = getY();
			int z = getZ();

			int sX = getWidth();
			int sY = getHeight();

			BaseRenderer.drawPanel(x, y, z, sX, sY, getStyle().asColor("shadow"), getStyle().asColor("background"), getStyle().asColor("highlight"), getStyle().asColor("outline"));

			if (hasLabel()) {
				BaseRenderer.drawText(isLabelShadowed(), getLabel().asFormattedString(), x + sX / 2 - BaseRenderer.getTextRenderer().getStringWidth(getLabel().asFormattedString()) / 2, y + 6, getStyle().asColor("label"));
				BaseRenderer.drawRectangle(x, y + 16, z, sX, 1, getStyle().asColor("outline"));
				BaseRenderer.drawRectangle(x + 1, y + 17, z, sX - 2, 0.75, getStyle().asColor("shadow"));
			}
		}

		List<WWidget> widgets = new ArrayList<>(getAllWidgets());
		Collections.sort(widgets);
		Collections.reverse(widgets);

		for (WWidget widget : widgets) {
			widget.draw();
		}
	}

	public boolean isDrawable() {
		return size.getX() != 0 && size.getY() != 0;
	}
}

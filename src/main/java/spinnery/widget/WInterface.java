package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import spinnery.client.screen.BaseHandledScreen;
import spinnery.common.utilities.Networks;
import spinnery.common.screenhandler.BaseScreenHandler;
import spinnery.widget.api.WDrawableElement;
import spinnery.widget.api.WEventListener;
import spinnery.widget.api.WModifiableCollection;
import spinnery.widget.api.WThemable;

import java.util.*;

public class WInterface implements WModifiableCollection, WEventListener, WDrawableElement, WThemable {
	private BaseScreenHandler handler;

	private final ArrayList<WAbstractWidget> widgets = new ArrayList<>();

	private Identifier theme;

	public WInterface() {
	}

	public WInterface(BaseScreenHandler handler) {
		setHandler(handler);
	}

	@Override
	public ArrayList<WAbstractWidget> getWidgets() {
		return widgets;
	}

	public BaseScreenHandler getHandler() {
		return handler;
	}

	public <W extends WInterface> W setHandler(BaseScreenHandler handler) {
		this.handler = handler;
		return (W) this;
	}

	@Deprecated
	public BaseScreenHandler getContainer() {
		return getHandler();
	}

	public <W extends WInterface> W setContainer(BaseScreenHandler handler) {
		return setHandler(handler);
	}

	@Override
	public Identifier getTheme() {
		return theme;
	}

	public <W extends WInterface> W setTheme(Identifier theme) {
		this.theme = theme;
		return (W) this;
	}

	public <W extends WInterface> W setTheme(String theme) {
		return setTheme(new Identifier(theme));
	}

	public void tick() {
		for (WAbstractWidget widget : getAllWidgets()) {
			widget.tick();
		}
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		for (WDrawableElement widget : widgets) {
			widget.draw(matrices, provider);
		}
	}

	@Override
	public void onLayoutChange() {
		if (handler != null && FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			if (MinecraftClient.getInstance().currentScreen instanceof BaseHandledScreen<?>) {
				BaseHandledScreen<?> screen = (BaseHandledScreen<?>) MinecraftClient.getInstance().currentScreen;

				screen.getHandler().onLayoutChange();
			}
		}
	}
}

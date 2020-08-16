package spinnery.widget.implementation;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import spinnery.client.screen.BaseHandledScreen;
import spinnery.common.screenhandler.BaseScreenHandler;
import spinnery.widget.declaration.collection.WModifiableCollection;
import spinnery.widget.declaration.event.WEventConsumer;
import spinnery.widget.declaration.position.WPositioned;
import spinnery.widget.declaration.size.WSized;
import spinnery.widget.declaration.theme.WThemed;

import java.util.*;

public class WInterface extends WAbstractWidget implements WModifiableCollection, WEventConsumer, WThemed, WPositioned, WSized {
	private BaseScreenHandler handler;

	private final ArrayList<WAbstractWidget> widgets = new ArrayList<>();

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


	@Override
	public void tick() {
		for (WAbstractWidget widget : getAllWidgets()) {
			widget.tick();
		}
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		for (WAbstractWidget widget : widgets) {
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

package spinnery.client;

import net.minecraft.client.gui.hud.InGameHud;
import spinnery.widget.WInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InGameHudScreen {
	protected static WInterface hudInterface = null;
	protected static InGameHud inGameHudCache = null;
	protected static List<Runnable> onInitialize = new ArrayList<>();

	public static void onInitialize(InGameHud inGameHud) {
		inGameHudCache = inGameHud;
		hudInterface = ((Accessor) inGameHud).getInterface();
		for (Runnable r : onInitialize) {
			r.run();
		}
	}

	public static void addOnInitialize(Runnable... r) {
		onInitialize.addAll(Arrays.asList(r));
	}

	public static void removeOnInitialize(Runnable... r) {
		onInitialize.removeAll(Arrays.asList(r));
	}

	public static WInterface getHolder() {
		return hudInterface;
	}

	public static InGameHud getInGameHude() {
		return inGameHudCache;
	}

	public interface Accessor {
		WInterface getInterface();

		InGameHud getInGameHud();
	}
}

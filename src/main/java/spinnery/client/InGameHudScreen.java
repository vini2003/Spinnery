package spinnery.client;

import net.minecraft.client.gui.hud.InGameHud;
import spinnery.widget.WInterfaceHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InGameHudScreen {
	protected static WInterfaceHolder interfaceHolder = null;
	protected static InGameHud inGameHudCache = null;
	protected static List<Runnable> onInitialize = new ArrayList<>();

	public static void onInitialize(InGameHud inGameHud) {
		inGameHudCache = inGameHud;
		interfaceHolder = ((InGameHudScreen.Acessor) inGameHud).getHolder();
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

	public static WInterfaceHolder getHolder() {
		return interfaceHolder;
	}

	public static InGameHud getInGameHude() {
		return inGameHudCache;
	}

	public interface Acessor {
		WInterfaceHolder getHolder();

		InGameHud getInGameHud();
	}
}

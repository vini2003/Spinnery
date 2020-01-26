package spinnery.debug;

import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import spinnery.client.InGameHudScreen;
import spinnery.widget.WInterface;
import spinnery.widget.WInterfaceHolder;
import spinnery.widget.WPosition;
import spinnery.widget.WStaticText;
import spinnery.widget.WType;

public class ScreenRegistry {
	public static final Identifier TEST_SCREEN = register(new Identifier("test"));

	public ScreenRegistry() {
		// NO-OP
	}

	public static void initialize() {
		InGameHudScreen.addOnInitialize(() -> {
			// WInterfaceHolder
			WInterfaceHolder holder = InGameHudScreen.getHolder();
			// WInterfaceHolder


			// WInterface
			WInterface mainInterface = new WInterface(WPosition.of(WType.FREE, 8, 8, 0));

			holder.add(mainInterface);
			// WInterface


			// WStaticText
			WStaticText staticTextA = new WStaticText(WPosition.of(WType.ANCHORED, 0, 0, 0, mainInterface), mainInterface, new LiteralText("StaticText A"));
			// WStaticText


			mainInterface.add(staticTextA);
		});
	}

	public static <I extends Identifier> I register(I ID) {
		ScreenProviderRegistry.INSTANCE.registerFactory(ID, (syncId, identifier, player, buf) -> new TestContainerScreen(new LiteralText("test"), new TestContainer(syncId, player.inventory), player));
		return ID;
	}
}
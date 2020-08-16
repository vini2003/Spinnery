package spinnery.debug;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class DebugCommands {
	public static void initialize() {
	}

	public static final ExtendedScreenHandlerFactory DEBUG = new ExtendedScreenHandlerFactory() {
		@Override
		public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf packetByteBuf) {
		}

		@Override
		public Text getDisplayName() {
			return new LiteralText("Debug");
		}

		@Override
		public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
			return new DebugScreenHandler(syncId, player);
		}
	};

	static {
		CommandRegistrationCallback.EVENT.register((dispatcher, source) -> {
			dispatcher.getRoot().addChild(
					CommandManager.literal("spinnery_debug")
						.requires(conditionalSource -> conditionalSource.hasPermissionLevel(4))
						.executes((context) -> {
							context.getSource().getPlayer().openHandledScreen(DEBUG);
							return 1;
					}).build()
			);
		});
	}
}

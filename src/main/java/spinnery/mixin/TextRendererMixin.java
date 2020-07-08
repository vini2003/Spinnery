package spinnery.mixin;

import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.TextHandler;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import spinnery.access.TextRendererAccessor;

import java.util.function.Function;

@Mixin(TextRenderer.class)
public class TextRendererMixin implements TextRendererAccessor {
	@Shadow
	@Final
	private Function<Identifier, FontStorage> fontStorageAccessor;
	@Shadow
	@Final
	private TextHandler handler;

	@Override
	public Function<Identifier, FontStorage> spinnery_getStorageAccessor() {
		return fontStorageAccessor;
	}

	@Override
	public TextHandler spinnery_getTextHandler() {
		return handler;
	}
}

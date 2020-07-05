package spinnery.access;

import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.TextHandler;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public interface TextRendererAccessor {
    Function<Identifier, FontStorage> spinnery_getStorageAccessor();
    TextHandler spinnery_getTextHandler();
}

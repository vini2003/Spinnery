package spinnery.access;

import net.minecraft.client.font.FontStorage;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public interface TextRendererAccessor {
    Function<Identifier, FontStorage> spinnery_getStorageAccessor();
}

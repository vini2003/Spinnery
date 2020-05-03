package com.github.vini2003.spinnery.registry;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.impl.SyntaxError;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import org.apache.logging.log4j.Level;
import com.github.vini2003.spinnery.Spinnery;
import com.github.vini2003.spinnery.util.ResourceListener;
import com.github.vini2003.spinnery.widget.api.Theme;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/**
 * Registers all the resource-related
 * assortments Spinnery makes use of,
 * including our theme parser and
 * reload listeners.
 */
@OnlyIn(Dist.CLIENT)
public class ResourceRegistry {
	public static final ResourceListener RESOURCE_LISTENER = new ResourceListener();

	public static void register(FMLClientSetupEvent event) {
		((IReloadableResourceManager) event.getMinecraftSupplier().get().getResourceManager()).addReloadListener(RESOURCE_LISTENER);
	}

	public static void clear() {
		ThemeRegistry.clear();
	}

	public static void load(SimpleReloadableResourceManager resourceManager) {
		Collection<ResourceLocation> themeFiles = resourceManager.getAllResourceLocations("spinnery",
				(string) -> string.endsWith(".theme.json5"));

		for (ResourceLocation id : themeFiles) {
			try {
				ResourceLocation themeId = new ResourceLocation(id.getNamespace(),
						id.getPath().replaceFirst("^spinnery/", "").replaceFirst("\\.theme\\.json5", ""));
				registerTheme(themeId, resourceManager.getResource(id).getInputStream());
			} catch (IOException e) {
				Spinnery.LOGGER.warn("[Spinnery] Failed to load theme {}.", id);
			}
		}
	}

	public static void registerTheme(ResourceLocation id, InputStream inputStream) {
		try {
			JsonObject themeDef = Jankson.builder().build().load(inputStream);
			Theme theme = Theme.of(id, themeDef);
			ThemeRegistry.register(theme);
		} catch (IOException e) {
			Spinnery.LOGGER.log(Level.ERROR, "Could not read theme file", e);
		} catch (SyntaxError syntaxError) {
			Spinnery.LOGGER.log(Level.ERROR, "Syntax error in theme file", syntaxError);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				Spinnery.LOGGER.log(Level.ERROR, "Could not close input stream", e);
			}
		}
	}
}

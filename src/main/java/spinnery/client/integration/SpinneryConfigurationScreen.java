package spinnery.client.integration;

import spinnery.Spinnery;
import spinnery.client.configuration.screen.ConfigurationScreen;
import spinnery.common.configuration.data.ConfigurationHolder;
import spinnery.common.configuration.data.ConfigurationOption;
import spinnery.common.configuration.registry.ConfigurationRegistry;

public class SpinneryConfigurationScreen extends ConfigurationScreen {
	@ConfigurationOption(name = Spinnery.MOD_ID)
	public static final ConfigurationHolder<String> preferredTheme = new ConfigurationHolder<>("spinnery:default");

	@ConfigurationOption(name = Spinnery.MOD_ID)
	public static final ConfigurationHolder<Boolean> smoothing = new ConfigurationHolder<>(true);

	@ConfigurationOption(name = Spinnery.MOD_ID)
	public static final ConfigurationHolder<Boolean> arrows = new ConfigurationHolder<>(true);

	@ConfigurationOption(name = Spinnery.MOD_ID)
	public static final ConfigurationHolder<Boolean> fading = new ConfigurationHolder<>(true);

	@ConfigurationOption(name = Spinnery.MOD_ID)
	public static final ConfigurationHolder<Float> kineticReductionCoefficient = new ConfigurationHolder<>(1.1f);

	@ConfigurationOption(name = Spinnery.MOD_ID)
	public static final ConfigurationHolder<Float> kineticAccelerationCoefficient = new ConfigurationHolder<>(1.5f);

	@ConfigurationOption(name = Spinnery.MOD_ID)
	public static final ConfigurationHolder<Float> dragScrollAccelerationCoefficient = new ConfigurationHolder<>(0.0005f);

	public static void initialize() {
		// NO-OP
	}

	static {
		name = Spinnery.MOD_ID;

		ConfigurationRegistry.register(SpinneryConfigurationScreen.class);
	}
}

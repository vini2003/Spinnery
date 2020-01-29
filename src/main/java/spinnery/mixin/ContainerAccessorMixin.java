package spinnery.mixin;

import net.minecraft.container.Container;
import net.minecraft.container.ContainerListener;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import spinnery.util.ContainerAccessorInterface;

import java.util.List;

@Mixin(Container.class)
public class ContainerAccessorMixin implements ContainerAccessorInterface {
	@Final
	@Shadow
	private List<ContainerListener> listeners;

	@Override
	public List<ContainerListener> getListeners() {
		return listeners;
	}
}

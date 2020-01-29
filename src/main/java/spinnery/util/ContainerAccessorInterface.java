package spinnery.util;

import net.minecraft.container.Container;
import net.minecraft.container.ContainerListener;

import java.util.List;

public interface ContainerAccessorInterface  {
	List<ContainerListener> getListeners();
}

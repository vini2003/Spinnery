package com.github.vini2003.spinnery.debug;

import com.github.vini2003.spinnery.widget.api.Position;
import com.github.vini2003.spinnery.widget.api.Size;
import net.minecraft.entity.player.PlayerInventory;
import com.github.vini2003.spinnery.common.BaseContainer;
import com.github.vini2003.spinnery.common.BaseInventory;
import com.github.vini2003.spinnery.widget.WSlot;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.ResourceLocation;

import java.util.HashSet;

public class TestContainer extends BaseContainer {
	public static final ContainerType<TestContainer> TYPE = (ContainerType<TestContainer>) new ContainerType<>(TestContainer::new).setRegistryName(new ResourceLocation("spinnery", "debug"));

	@Override
	public ContainerType<?> getType() {
		return TYPE;
	}

	public TestContainer(int synchronizationID, PlayerInventory newLinkedPlayerInventory) {
		super(synchronizationID, newLinkedPlayerInventory);

		HashSet<WSlot> slots = (HashSet<WSlot>) WSlot.addHeadlessPlayerInventory(getInterface());

		BaseInventory inv = new BaseInventory(27);

		addInventory(1, inv);

		WSlot.addHeadlessArray(getInterface(), 0, 1, 3, 9).forEach(slot -> {
			slot.setOverrideMaximumCount(true);
			slot.setMaximumCount(256);
		});

		WSlot.addHeadlessPlayerInventory(getInterface());
	}
}
package net.dadamalda.ocr_extra_drivers.integration.ocr_extra_drivers;

import li.cil.oc.api.driver.EnvironmentProvider;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverItem;
import li.cil.oc.common.Slot;
import net.dadamalda.ocr_extra_drivers.init.OEDItems;
import net.minecraft.world.item.ItemStack;

public final class DriverSatelliteCard extends DriverItem {
    public static final DriverSatelliteCard INSTANCE = new DriverSatelliteCard();

    @Override
    public boolean worksWith(ItemStack stack) {
        return stack.is(OEDItems.SATELLITE_CARD);
    }

    @Override
    public ManagedEnvironment createEnvironment(ItemStack itemStack, EnvironmentHost environmentHost) {
        if(environmentHost.getEnvironmentLevel() == null || environmentHost.getEnvironmentLevel().isClientSide) return null;
        return new SatelliteCard(environmentHost);
    }

    @Override
    public String slot(ItemStack itemStack) {
        return Slot.Card();
    }

    public static class Provider implements EnvironmentProvider {
        public static final Provider INSTANCE = new Provider();

        @Override
        public Class<?> getEnvironment(ItemStack itemStack) {
            if(DriverSatelliteCard.INSTANCE.worksWith(itemStack)) return SatelliteCard.class;
            return null;
        }
    }
}

package net.dadamalda.ocr_extra_drivers.integration.immersiveengineering;

import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.AlloySmelterLogic;
import li.cil.oc.api.driver.EnvironmentProvider;
import li.cil.oc.api.network.ManagedEnvironment;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class IEMultiblockDrivers {
    private static final HashMap<ResourceLocation, Class<? extends ManagedEnvironment>> ENVIRONMENT_CLASSES = new HashMap<>();

    static {
        ENVIRONMENT_CLASSES.put(ResourceLocation.parse("immersiveengineering:alloy_smelter"), EnvAlloySmelter.class);
    }

    public static @Nullable ManagedEnvironment findEnvironment(IMultiblockState state) {
        if(state instanceof AlloySmelterLogic.State state2) {
            return new EnvAlloySmelter(state2);
        }
        return null;
    }

    public static class Provider implements EnvironmentProvider {

        @Override
        public Class<?> getEnvironment(ItemStack itemStack) {
            if(itemStack.isEmpty()) return null;
            return ENVIRONMENT_CLASSES.get(BuiltInRegistries.ITEM.getKey(itemStack.getItem()));
        }
    }
}

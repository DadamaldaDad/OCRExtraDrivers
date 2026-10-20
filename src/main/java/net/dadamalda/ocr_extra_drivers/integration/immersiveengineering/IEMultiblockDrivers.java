package net.dadamalda.ocr_extra_drivers.integration.immersiveengineering;

import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockLevel;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.AdvBlastFurnaceLogic;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.AlloySmelterLogic;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.BlastFurnaceLogic;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.CokeOvenLogic;
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
        ENVIRONMENT_CLASSES.put(ResourceLocation.parse("immersiveengineering:coke_oven"), EnvCokeOven.class);
        ENVIRONMENT_CLASSES.put(ResourceLocation.parse("immersiveengineering:blast_furnace"), EnvBlastFurnace.class);
        ENVIRONMENT_CLASSES.put(ResourceLocation.parse("immersiveengineering:advanced_blast_furnace"), EnvAdvBlastFurnace.class);
    }

    public static @Nullable ManagedEnvironment findEnvironment(IMultiblockState state, IMultiblockLevel level) {
        if(state instanceof AlloySmelterLogic.State state2) {
            return new EnvAlloySmelter(state2);
        } else if(state instanceof CokeOvenLogic.State state2) {
            return new EnvCokeOven(state2);
        } else if(state instanceof BlastFurnaceLogic.State state2) {
            return new EnvBlastFurnace(state2);
        } else if(state instanceof AdvBlastFurnaceLogic.State state2) {
            return new EnvAdvBlastFurnace(state2, level);
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

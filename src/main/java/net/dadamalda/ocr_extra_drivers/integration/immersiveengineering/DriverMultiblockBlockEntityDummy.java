package net.dadamalda.ocr_extra_drivers.integration.immersiveengineering;

import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.api.multiblocks.blocks.registry.MultiblockBlockEntityDummy;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class DriverMultiblockBlockEntityDummy extends DriverSidedBlockEntity {
    @Override
    public Class<?> getBlockEntityClass() {
        return MultiblockBlockEntityDummy.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(Level level, BlockPos blockPos, Direction direction) {
        if(level.getBlockEntity(blockPos) instanceof MultiblockBlockEntityDummy<? extends IMultiblockState> be) {
            IMultiblockState state = be.getHelper().getState();
            IMultiblockContext<? extends IMultiblockState> context = be.getHelper().getContext();
            if(context == null) return null;
            return IEMultiblockDrivers.findEnvironment(state, context.getLevel());
        } else {
            return null;
        }
    }
}

package net.dadamalda.ocr_extra_drivers.integration.immersiveengineering;

import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.api.multiblocks.blocks.registry.MultiblockBlockEntityMaster;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class DriverMultiblockBlockEntityMaster extends DriverSidedBlockEntity {
    @Override
    public Class<?> getBlockEntityClass() {
        return MultiblockBlockEntityMaster.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(Level level, BlockPos blockPos, Direction direction) {
        if(level.getBlockEntity(blockPos) instanceof MultiblockBlockEntityMaster<? extends IMultiblockState> be) {
            IMultiblockState state = be.getHelper().getState();
            return IEMultiblockDrivers.findEnvironment(state);
        } else {
            return null;
        }
    }
}

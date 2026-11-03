package net.dadamalda.ocr_extra_drivers.core;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Optional;

@FunctionalInterface
public interface SatelliteCollisionProvider {
    Optional<VoxelShape> getShape(BlockState state, BlockGetter level, BlockPos pos);
}

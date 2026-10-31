package net.dadamalda.ocr_extra_drivers.core;

import net.dadamalda.ocr_extra_drivers.init.OEDTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SatelliteClipContext extends ClipContext {
    private final BlockPos blockPos;

    public SatelliteClipContext(Vec3 from, Vec3 to, @Nullable BlockPos blockPos) {
        super(from, to, Block.COLLIDER, Fluid.ANY, CollisionContext.empty());
        this.blockPos = blockPos;
    }

    public @Nullable BlockPos getBlockPos() {
        return this.blockPos;
    }

    @Override
    public @NotNull VoxelShape getBlockShape(@NotNull BlockState blockState, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        if(pos.equals(this.blockPos)) return Shapes.empty();
        if(blockState.is(OEDTags.TRANSPARENT_TO_SATELLITE)) return Shapes.empty();
        return super.getBlockShape(blockState, level, pos);
    }
}

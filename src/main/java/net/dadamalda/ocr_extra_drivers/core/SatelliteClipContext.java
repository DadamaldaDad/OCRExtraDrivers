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

public class SatelliteClipContext extends ClipContext {
    private final boolean isBlock;
    public SatelliteClipContext(Vec3 from, Vec3 to, boolean isBlock) {
        super(from, to, Block.COLLIDER, Fluid.ANY, CollisionContext.empty());
        this.isBlock = isBlock;
    }

    public boolean isBlock() {
        return this.isBlock;
    }

    @Override
    public @NotNull VoxelShape getBlockShape(@NotNull BlockState blockState, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        if(this.isBlock && pos.equals(BlockPos.containing(getFrom()))) return Shapes.empty();
        if(blockState.is(OEDTags.TRANSPARENT_TO_SATELLITE)) return Shapes.empty();
        return super.getBlockShape(blockState, level, pos);
    }
}

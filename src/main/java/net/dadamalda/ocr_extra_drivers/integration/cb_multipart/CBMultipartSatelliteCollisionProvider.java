package net.dadamalda.ocr_extra_drivers.integration.cb_multipart;

import codechicken.microblock.api.MicroMaterial;
import codechicken.microblock.part.MicroblockPart;
import codechicken.multipart.api.MultipartType;
import codechicken.multipart.api.part.MultiPart;
import codechicken.multipart.block.TileMultipart;
import codechicken.multipart.init.CBMultipartModContent;
import codechicken.multipart.init.MultiPartRegistries;
import net.dadamalda.ocr_extra_drivers.OCRExtraDrivers;
import net.dadamalda.ocr_extra_drivers.core.SatelliteCollisionProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CBMultipartSatelliteCollisionProvider implements SatelliteCollisionProvider {
    private static final TagKey<MultipartType<?>> MULTIPART_TRANSPARENT_TO_SATELLITE = TagKey.create(
            MultiPartRegistries.multipartTypes().key(),
            ResourceLocation.fromNamespaceAndPath(OCRExtraDrivers.MODID, "transparent_to_satellite")
    );
    private static final TagKey<MicroMaterial> MATERIAL_TRANSPARENT_TO_SATELLITE = TagKey.create(
            MicroMaterial.MULTIPART_TYPES,
            ResourceLocation.fromNamespaceAndPath(OCRExtraDrivers.MODID, "transparent_to_satellite")
    );

    @Override
    public Optional<VoxelShape> getShape(BlockState state, BlockGetter level, BlockPos pos) {
        if(!state.is(CBMultipartModContent.MULTIPART_BLOCK)) return Optional.empty();
        Optional<?> be = level.getBlockEntity(pos, CBMultipartModContent.MULTIPART_TILE_TYPE.get());
        if(be.isEmpty()) return Optional.empty();
        if(!(be.get() instanceof TileMultipart tile)) return Optional.empty();
        List<VoxelShape> shapes = new ArrayList<>();
        for(MultiPart part : tile.getPartList()) {
            ResourceLocation partType = part.getType().getRegistryName();
            boolean isMultipartInTag = MultiPartRegistries.multipartTypes().getOrCreateTag(MULTIPART_TRANSPARENT_TO_SATELLITE)
                    .stream().anyMatch(v -> {
                        ResourceKey<MultipartType<?>> key = v.getKey();
                        if(key == null) return false;
                        return key.location() == partType;
                    });
            if(isMultipartInTag) continue;
            if(!(part instanceof MicroblockPart microblockPart)) {
                shapes.add(part.getCollisionShape(CollisionContext.empty()));
                continue;
            }
            ResourceLocation material = microblockPart.material.getRegistryName();
            boolean isMaterialInTag = MicroMaterial.REGISTRY.getOrCreateTag(MATERIAL_TRANSPARENT_TO_SATELLITE)
                    .stream().anyMatch(v -> {
                        ResourceKey<MicroMaterial> key = v.getKey();
                        if(key == null) return false;
                        return key.location() == material;
                    });
            if(isMaterialInTag) continue;
            shapes.add(part.getCollisionShape(CollisionContext.empty()));
        }
        return Optional.of(Shapes.or(Shapes.empty(), shapes.toArray(VoxelShape[]::new)));
    }
}

package net.dadamalda.ocr_extra_drivers.core;

import dev.ryanhcode.sable.companion.SableCompanion;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import dev.ryanhcode.sable.companion.math.Pose3dc;
import li.cil.oc.api.network.Packet;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class SatelliteNetwork {
    private static final Map<ResourceLocation, Set<SatelliteNode>> network = new HashMap<>();

    private static final double[][] raycastOffsets = {
            {0.0, 0.0},
            {-0.3, -0.3},
            {0.3, -0.3},
            {-0.3, 0.3},
            {0.3, 0.3}
    };

    private static final double[][] sublevelRaycastOffsets = {
            {0.0, 0.0, 0.0},
            {-0.3, -0.3, -0.3},
            {-0.3, -0.3, 0.3},
            {-0.3, 0.3, -0.3},
            {-0.3, 0.3, 0.3},
            {0.3, -0.3, -0.3},
            {0.3, -0.3, 0.3},
            {0.3, 0.3, -0.3},
            {0.3, 0.3, 0.3}
    };

    private static final List<SatelliteLinkProvider> linkProviders = new ArrayList<>();

    public static void registerLinkProvider(SatelliteLinkProvider provider) {
        linkProviders.add(provider);
    }

    public static List<ResourceLocation> getDimensionsAbove(Level level) {
        List<ResourceLocation> result = new ArrayList<>();
        for(SatelliteLinkProvider provider : linkProviders) {
            result.addAll(provider.getDimensionsAbove(level));
        }
        return result;
    }

    public static List<ResourceLocation> getDimensionsBelow(Level level) {
        List<ResourceLocation> result = new ArrayList<>();
        for(SatelliteLinkProvider provider : linkProviders) {
            result.addAll(provider.getDimensionsBelow(level));
        }
        return result;
    }

    public static void add(SatelliteNode node, ResourceLocation dimension) {
        Set<SatelliteNode> nodesInDimension = network.computeIfAbsent(dimension, dimension2 -> new HashSet<>());
        nodesInDimension.add(node);
    }

    public static void remove(SatelliteNode node, ResourceLocation dimension) {
        Set<SatelliteNode> nodesInDimension = network.computeIfAbsent(dimension, dimension2 -> new HashSet<>());
        nodesInDimension.remove(node);
    }

    public static void receivePacketInDimension(Packet packet, ResourceLocation dimension, SatelliteDirection sentFrom) {
        Set<SatelliteNode> nodesInDimension = network.computeIfAbsent(dimension, dimension2 -> new HashSet<>());
        for (SatelliteNode node : nodesInDimension) {
            node.receiveSatellitePacket(packet, sentFrom);
        }
    }

    public static boolean canSee(Level level, Vec3 pos, boolean isBlock, SatelliteDirection direction) {
        if(isBlock && SableCompanion.INSTANCE.isInPlotGrid(level, pos)) {
            return canSeeWithSublevels(level, pos, direction);
        }
        for (double[] offset : raycastOffsets) {
            ClipContext context = new SatelliteClipContext(
                    new Vec3(pos.x+offset[0], pos.y+(isBlock ? 0 : 0.5), pos.z+offset[1]),
                    new Vec3(pos.x+offset[0], direction == SatelliteDirection.ABOVE ? level.getMaxBuildHeight()+1 : level.getMinBuildHeight(), pos.z+offset[1]),
                    isBlock ? BlockPos.containing(pos) : null
            );
            BlockHitResult hit = level.clip(context);
            if(hit.getType() == HitResult.Type.MISS) {
                return true;
            }
        }
        return false;
    }

    private static boolean canSeeWithSublevels(Level level, Vec3 pos, SatelliteDirection direction) {
        SubLevelAccess sublevel = SableCompanion.INSTANCE.getContaining(level, pos.x, pos.z);
        if(sublevel == null) return false;
        Pose3dc pose = sublevel.logicalPose();
        for (double[] offset : sublevelRaycastOffsets) {
            Vec3 startLocal = new Vec3(pos.x+offset[0], pos.y+offset[1], pos.z+offset[2]);
            Vec3 start = pose.transformPosition(startLocal);
            ClipContext context = new SatelliteClipContext(
                    start,
                    new Vec3(start.x, direction == SatelliteDirection.ABOVE ? level.getMaxBuildHeight()+1 : level.getMinBuildHeight(), start.z),
                    BlockPos.containing(pos)
            );
            BlockHitResult hit = level.clip(context);
            if(hit.getType() == HitResult.Type.MISS) {
                return true;
            }
        }
        return false;
    }

    public interface SatelliteNode {
        void receiveSatellitePacket(Packet packet, SatelliteDirection sentFrom);
    }

    public enum SatelliteDirection {
        ABOVE, BELOW
    }
}

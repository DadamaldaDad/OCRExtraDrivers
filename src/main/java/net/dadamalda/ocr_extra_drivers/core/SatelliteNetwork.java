package net.dadamalda.ocr_extra_drivers.core;

import li.cil.oc.api.network.Packet;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

import java.util.*;

public class SatelliteNetwork {
    private static final Map<ResourceLocation, Set<SatelliteNode>> network = new HashMap<>();

    private static final double[][] raycastOffsets = {
            {0.0, 0.0},
            {-0.4, -0.4},
            {0.4, -0.4},
            {-0.4, 0.4},
            {0.4, 0.4}
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

    public static boolean canSeeAbove(Level level, double x, double y, double z) {
        for (double[] offset : raycastOffsets) {
            ClipContext context = new ClipContext(
                    new Vec3(x+offset[0], y+0.51, z+offset[1]),
                    new Vec3(x+offset[0], level.getMaxBuildHeight()+1, z+offset[1]),
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.ANY,
                    CollisionContext.empty()
            );
            BlockHitResult hit = level.clip(context);
            if(hit.getType() == HitResult.Type.MISS) {
                return true;
            }
        }
        return false;
    }

    public static boolean canSeeBelow(Level level, double x, double y, double z) {
        for (double[] offset : raycastOffsets) {
            ClipContext context = new ClipContext(
                    new Vec3(x+offset[0], y-0.51, z+offset[1]),
                    new Vec3(x+offset[0], level.getMinBuildHeight(), z+offset[1]),
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.ANY,
                    CollisionContext.empty()
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

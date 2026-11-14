package io.github.blockesp;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Keeps a bounded nearby cache so rendering never needs to search world blocks. */
public final class BlockEspScanner {
    private static final int HORIZONTAL_RANGE = 48;
    private static final int VERTICAL_RANGE = 32;
    private static final int SCAN_INTERVAL_TICKS = 20;
    private static final int MAX_BLOCKS = 4_096;
    private static final List<BlockPos> TARGETS = new ArrayList<>();

    private static BlockPos lastScanCenter;
    private static long lastScanTick = Long.MIN_VALUE;
    private static int lastConfigRevision = -1;

    private BlockEspScanner() {
    }

    public static void tick(MinecraftClient client) {
        if (client.world == null || client.player == null) {
            TARGETS.clear();
            lastScanCenter = null;
            return;
        }

        BlockEspConfig config = BlockEspConfig.get();
        if (!config.isEnabled()) {
            TARGETS.clear();
            return;
        }

        BlockPos center = client.player.getBlockPos();
        boolean moved = lastScanCenter == null || lastScanCenter.getSquaredDistance(center) >= 16;
        boolean settingsChanged = lastConfigRevision != config.revision();
        long worldTime = client.world.getTime();
        if (!moved && !settingsChanged && worldTime - lastScanTick < SCAN_INTERVAL_TICKS) {
            return;
        }

        scan(client.world, center, config);
        lastScanCenter = center.toImmutable();
        lastScanTick = worldTime;
        lastConfigRevision = config.revision();
    }

    private static void scan(ClientWorld world, BlockPos center, BlockEspConfig config) {
        TARGETS.clear();
        int minX = center.getX() - HORIZONTAL_RANGE;
        int maxX = center.getX() + HORIZONTAL_RANGE;
        int minY = Math.max(world.getBottomY(), center.getY() - VERTICAL_RANGE);
        int maxY = Math.min(world.getTopYInclusive(), center.getY() + VERTICAL_RANGE);
        int minZ = center.getZ() - HORIZONTAL_RANGE;
        int maxZ = center.getZ() + HORIZONTAL_RANGE;
        BlockPos.Mutable pos = new BlockPos.Mutable();

        for (int x = minX; x <= maxX && TARGETS.size() < MAX_BLOCKS; x++) {
            for (int z = minZ; z <= maxZ && TARGETS.size() < MAX_BLOCKS; z++) {
                for (int y = minY; y <= maxY && TARGETS.size() < MAX_BLOCKS; y++) {
                    pos.set(x, y, z);
                    OreType ore = OreType.fromBlock(world.getBlockState(pos).getBlock());
                    if (ore != null && config.isSelected(ore)) {
                        TARGETS.add(pos.toImmutable());
                    }
                }
            }
        }
    }

    public static List<BlockPos> targets() {
        return Collections.unmodifiableList(TARGETS);
    }
}

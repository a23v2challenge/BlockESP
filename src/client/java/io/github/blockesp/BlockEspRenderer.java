package io.github.blockesp;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/** Draws a depth-test-free box layer so selected ores remain visible through walls. */
public final class BlockEspRenderer {
    private static final RenderLayer ESP_LINES = new RenderLayer(
            "blockesp_lines",
            VertexFormats.LINES,
            VertexFormat.DrawMode.LINES,
            1_536,
            false,
            true,
            () -> {
                RenderSystem.setShader(ShaderProgramKeys.RENDERTYPE_LINES);
                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.disableCull();
                RenderSystem.lineWidth(2.5F);
            },
            () -> {
                RenderSystem.lineWidth(1.0F);
                RenderSystem.enableCull();
                RenderSystem.depthMask(true);
                RenderSystem.enableDepthTest();
                RenderSystem.disableBlend();
            }
    ) {
    };

    private BlockEspRenderer() {
    }

    public static void render(WorldRenderContext context) {
        if (!BlockEspConfig.get().isEnabled() || BlockEspScanner.targets().isEmpty()) {
            return;
        }

        Vec3d camera = context.camera().getPos();
        VertexConsumer consumer = context.consumers().getBuffer(ESP_LINES);
        context.matrixStack().push();
        context.matrixStack().translate(-camera.x, -camera.y, -camera.z);
        for (BlockPos pos : BlockEspScanner.targets()) {
            OreType ore = OreType.fromBlock(context.world().getBlockState(pos).getBlock());
            if (ore != null && BlockEspConfig.get().isSelected(ore)) {
                // Slightly expand the box to prevent z-fighting with the block surface.
                VertexRendering.drawBox(context.matrixStack(), consumer,
                        pos.getX() - 0.002, pos.getY() - 0.002, pos.getZ() - 0.002,
                        pos.getX() + 1.002, pos.getY() + 1.002, pos.getZ() + 1.002,
                        ore.red(), ore.green(), ore.blue(), 1.0F);
            }
        }
        context.matrixStack().pop();
    }
}

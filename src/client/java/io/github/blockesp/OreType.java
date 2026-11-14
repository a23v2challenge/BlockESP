package io.github.blockesp;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.util.IdentityHashMap;
import java.util.Map;

/** Each target is intentionally separate so normal and deepslate blocks can be selected independently. */
public enum OreType {
    COAL("coal_ore", "석탄 광석", Blocks.COAL_ORE, 45, 45, 45),
    DEEPSLATE_COAL("deepslate_coal_ore", "심층암 석탄 광석", Blocks.DEEPSLATE_COAL_ORE, 65, 65, 65),
    IRON("iron_ore", "철 광석", Blocks.IRON_ORE, 210, 185, 150),
    DEEPSLATE_IRON("deepslate_iron_ore", "심층암 철 광석", Blocks.DEEPSLATE_IRON_ORE, 190, 165, 135),
    COPPER("copper_ore", "구리 광석", Blocks.COPPER_ORE, 230, 130, 80),
    DEEPSLATE_COPPER("deepslate_copper_ore", "심층암 구리 광석", Blocks.DEEPSLATE_COPPER_ORE, 205, 105, 65),
    GOLD("gold_ore", "금 광석", Blocks.GOLD_ORE, 255, 220, 55),
    DEEPSLATE_GOLD("deepslate_gold_ore", "심층암 금 광석", Blocks.DEEPSLATE_GOLD_ORE, 230, 195, 45),
    REDSTONE("redstone_ore", "레드스톤 광석", Blocks.REDSTONE_ORE, 255, 35, 35),
    DEEPSLATE_REDSTONE("deepslate_redstone_ore", "심층암 레드스톤 광석", Blocks.DEEPSLATE_REDSTONE_ORE, 225, 25, 25),
    EMERALD("emerald_ore", "에메랄드 광석", Blocks.EMERALD_ORE, 25, 235, 120),
    DEEPSLATE_EMERALD("deepslate_emerald_ore", "심층암 에메랄드 광석", Blocks.DEEPSLATE_EMERALD_ORE, 20, 205, 100),
    LAPIS("lapis_ore", "청금석 광석", Blocks.LAPIS_ORE, 50, 95, 255),
    DEEPSLATE_LAPIS("deepslate_lapis_ore", "심층암 청금석 광석", Blocks.DEEPSLATE_LAPIS_ORE, 40, 80, 225),
    DIAMOND("diamond_ore", "다이아몬드 광석", Blocks.DIAMOND_ORE, 45, 245, 255),
    DEEPSLATE_DIAMOND("deepslate_diamond_ore", "심층암 다이아몬드 광석", Blocks.DEEPSLATE_DIAMOND_ORE, 35, 215, 230),
    NETHER_GOLD("nether_gold_ore", "네더 금 광석", Blocks.NETHER_GOLD_ORE, 255, 185, 35),
    NETHER_QUARTZ("nether_quartz_ore", "네더 석영 광석", Blocks.NETHER_QUARTZ_ORE, 245, 245, 245),
    ANCIENT_DEBRIS("ancient_debris", "고대 잔해", Blocks.ANCIENT_DEBRIS, 130, 70, 45),
    BARREL("barrel", "통", Blocks.BARREL, 185, 125, 60),
    CHEST("chest", "상자", Blocks.CHEST, 255, 190, 75),
    DEEPSLATE_TILES("deepslate_tiles", "심층암 타일", Blocks.DEEPSLATE_TILES, 150, 115, 255);

    private static final Map<Block, OreType> BY_BLOCK = new IdentityHashMap<>();

    static {
        for (OreType ore : values()) {
            BY_BLOCK.put(ore.block, ore);
        }
    }

    private final String configKey;
    private final String displayName;
    private final Block block;
    private final int red;
    private final int green;
    private final int blue;

    OreType(String configKey, String displayName, Block block, int red, int green, int blue) {
        this.configKey = configKey;
        this.displayName = displayName;
        this.block = block;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public static OreType fromBlock(Block block) {
        return BY_BLOCK.get(block);
    }

    public String configKey() {
        return configKey;
    }

    public String displayName() {
        return displayName;
    }

    public float red() {
        return red / 255.0F;
    }

    public float green() {
        return green / 255.0F;
    }

    public float blue() {
        return blue / 255.0F;
    }
}

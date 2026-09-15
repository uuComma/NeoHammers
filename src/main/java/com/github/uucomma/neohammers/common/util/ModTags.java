package com.github.uucomma.neohammers.common.util;

import com.github.uucomma.neohammers.NeoHammers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class ModTags {
    public static final class ItemTags {
        public static final TagKey<Item> HAMMERS = tag("hammers");

        private static TagKey<Item> tag(String name) {
            return TagKey.create(Registries.ITEM, NeoHammers.resource(name));
        }

        private static TagKey<Item> conventionTag(String name) {
            return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", name));
        }
    }

    public static final class BlockTags {
        public static final TagKey<Block> MINEABLE_WITH_HAMMER = tag("mineable/hammer");

        private static TagKey<Block> tag(String name) {
            return TagKey.create(Registries.BLOCK, NeoHammers.resource(name));
        }

        private static TagKey<Block> conventionTag(String name) {
            return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", name));
        }
    }
}

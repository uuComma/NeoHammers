package com.github.uucomma.neohammers.data;

import com.github.uucomma.neohammers.NeoHammers;
import com.github.uucomma.neohammers.common.advancement.UseExtendedAreaMineTrigger;
import com.github.uucomma.neohammers.common.item.ModItems;
import com.github.uucomma.neohammers.common.util.ModTags;
import net.minecraft.advancements.*;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends AdvancementProvider {
    public ModAdvancementProvider() {
        super(List.of(ModAdvancementGenerator::new));
    }

    @NullMarked
    private static class ModAdvancementGenerator extends AdvancementSubProvider {
        protected ModAdvancementGenerator(BootstrapContext<Advancement> output) {
            super(output);
        }

        @Override
        public void generate() {
            var itemLookup = output.lookup(Registries.ITEM);
            Identifier adventureRootId = Identifier.withDefaultNamespace("adventure/root");
            AdvancementHolder hammerTime = Advancement.Builder.advancement()
                    .display(
                            ModItems.IRON_HAMMER.get(),
                            NeoHammers.translate("advancement.", ".hammerTime.title"),
                            NeoHammers.translate("advancement.", ".hammerTime.desc"),
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .parent(adventureRootId)
                    .addCriterion(
                            "has_any_hammer",
                            InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(itemLookup, ModTags.ItemTags.HAMMERS))
                    )
                    .save(output, NeoHammers.resource("adventure/hammer_time"));

            AdvancementHolder seriousDestructionNeeds = Advancement.Builder.advancement()
                    .display(
                            ModItems.NETHERITE_HAMMER.get(),
                            NeoHammers.translate("advancement.", ".seriousDestructionNeeds.title"),
                            NeoHammers.translate("advancement.", ".seriousDestructionNeeds.desc"),
                            AdvancementType.CHALLENGE,
                            true,
                            true,
                            false
                    )
                    .parent(hammerTime)
                    .rewards(AdvancementRewards.Builder.experience(115))
                    .addCriterion(
                            "has_netherite_hammer",
                            InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.NETHERITE_HAMMER.get())
                    )
                    .save(output, NeoHammers.resource("adventure/serious_destruction_needs"));

            AdvancementHolder seriousDestruction = Advancement.Builder.advancement()
                    .display(
                            Items.ENCHANTED_BOOK,
                            NeoHammers.translate("advancement.", ".seriousDestruction.title"),
                            NeoHammers.translate("advancement.", ".seriousDestruction.desc"),
                            AdvancementType.CHALLENGE,
                            true,
                            true,
                            false
                    )
                    .parent(hammerTime)
                    .rewards(AdvancementRewards.Builder.experience(95))
                    .addCriterion(
                            "destroy_everything",
                            UseExtendedAreaMineTrigger.TriggerInstance.instance(6)
                    )
                    .save(output, NeoHammers.resource("adventure/serious_destruction"));
        }
    }
}

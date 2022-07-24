package net.pyerter.pootsadditions.integration.rei.entry;

import me.shedaniel.rei.api.client.entry.renderer.EntryRenderer;
import me.shedaniel.rei.api.common.entry.EntrySerializer;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.comparison.ComparisonContext;
import me.shedaniel.rei.api.common.entry.type.EntryDefinition;
import me.shedaniel.rei.api.common.entry.type.EntryType;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import net.minecraft.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.pyerter.pootsadditions.PootsAdditions;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class TridiEntryType implements EntryDefinition<VanillaEntryTypes> {

    public static final EntryType<VanillaEntryTypes> TRIDI_ENTRY_TYPE = EntryType.deferred(new Identifier(PootsAdditions.MOD_ID, "tridi"));

    @Override
    public Class<VanillaEntryTypes> getValueType() {
        return null;
    }

    @Override
    public EntryType<VanillaEntryTypes> getType() {
        return TRIDI_ENTRY_TYPE;
    }

    @Override
    public EntryRenderer<VanillaEntryTypes> getRenderer() {
        return null;
    }

    @Override
    public @Nullable Identifier getIdentifier(EntryStack<VanillaEntryTypes> entry, VanillaEntryTypes value) {
        return null;
    }

    @Override
    public boolean isEmpty(EntryStack<VanillaEntryTypes> entry, VanillaEntryTypes value) {
        return false;
    }

    @Override
    public VanillaEntryTypes copy(EntryStack<VanillaEntryTypes> entry, VanillaEntryTypes value) {
        return null;
    }

    @Override
    public VanillaEntryTypes normalize(EntryStack<VanillaEntryTypes> entry, VanillaEntryTypes value) {
        return null;
    }

    @Override
    public VanillaEntryTypes wildcard(EntryStack<VanillaEntryTypes> entry, VanillaEntryTypes value) {
        return null;
    }

    @Override
    public long hash(EntryStack<VanillaEntryTypes> entry, VanillaEntryTypes value, ComparisonContext context) {
        return 0;
    }

    @Override
    public boolean equals(VanillaEntryTypes o1, VanillaEntryTypes o2, ComparisonContext context) {
        return false;
    }

    @Override
    public @Nullable EntrySerializer<VanillaEntryTypes> getSerializer() {
        return null;
    }

    @Override
    public Text asFormattedText(EntryStack<VanillaEntryTypes> entry, VanillaEntryTypes value) {
        return null;
    }

    @Override
    public Stream<? extends TagKey<?>> getTagsFor(EntryStack<VanillaEntryTypes> entry, VanillaEntryTypes value) {
        return null;
    }
}

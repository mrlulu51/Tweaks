package fr.mrlulu51.tweaks.util;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class NbtParser {

    public static NonNullList<ItemStack> readInventoryNbt(@NotNull CompoundTag tag) {
        ListTag list = tag.getList("Items", Tag.TAG_COMPOUND);
        NonNullList<ItemStack> stacks = NonNullList.withSize(27, ItemStack.EMPTY);

        for(int i = 0; i < list.size(); i++) {
            CompoundTag cmp = list.getCompound(i);
            int j = cmp.getByte("Slot") & 0xFF;
            if(j < 0 || j >= stacks.size()) continue;
            stacks.set(j, ItemStack.of(cmp));
        }

        return stacks;
    }
}

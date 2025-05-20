package net.miron.playervaults;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess; // ✅ Correct for Mojang mappings


import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class VaultStorage {
    private static final File baseDir = new File("vaults");
    private static final Gson gson = new Gson();

    public static void saveVault(UUID uuid, int number, NonNullList<ItemStack> items, RegistryAccess registryAccess) {
        try {
            baseDir.mkdirs();
            File file = new File(baseDir, uuid + "_pv" + number + ".json");

            List<String> itemNbtList = new ArrayList<>();
            for (ItemStack stack : items) {
                if (!stack.isEmpty()) {
                    Tag rawTag = stack.save(registryAccess);
                    if (rawTag instanceof CompoundTag tag) {
                        itemNbtList.add(tag.toString());
                    } else {
                        itemNbtList.add(null); // fallback in case it isn't a CompoundTag
                    }
                }
            }

            try (FileWriter writer = new FileWriter(file)) {
                gson.toJson(itemNbtList, writer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static NonNullList<ItemStack> loadVault(UUID uuid, int number, int size, RegistryAccess registryAccess) {
        try {
            File file = new File(baseDir, uuid + "_pv" + number + ".json");
            if (!file.exists()) {
                return NonNullList.withSize(size, ItemStack.EMPTY);
            }

            Type listType = new TypeToken<List<String>>() {}.getType();
            List<String> nbtStrings;

            try (FileReader reader = new FileReader(file)) {
                nbtStrings = gson.fromJson(reader, listType);
            }

            NonNullList<ItemStack> items = NonNullList.withSize(size, ItemStack.EMPTY);

            for (int i = 0; i < nbtStrings.size(); i++) {
                String nbt = nbtStrings.get(i);
                if (nbt != null) {
                    try {
                        CompoundTag tag = TagParser.parseTag(nbt);
                        ItemStack stack = ItemStack.parse(registryAccess, tag).orElse(ItemStack.EMPTY);
                        items.set(i, stack);
                    } catch (CommandSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }

            return items;
        } catch (IOException e) {
            e.printStackTrace();
            return NonNullList.withSize(size, ItemStack.EMPTY);
        }
    }
}

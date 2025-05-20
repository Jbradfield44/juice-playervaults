package net.miron.playervaults;

import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class ReadOnlyVaultScreenHandler extends ChestMenu {

    public ReadOnlyVaultScreenHandler(int syncId, Inventory playerInventory, Container vaultInventory) {
        super(MenuType.GENERIC_9x6, syncId, playerInventory, vaultInventory, 6); // 6 rows = 9x6 vault

        // Override vault slots to make them read-only
        for (int i = 0; i < this.slots.size(); i++) {
            Slot slot = this.slots.get(i);
            if (slot.container == vaultInventory) {
                int index = slot.index;
                int x = slot.x;
                int y = slot.y;
                this.slots.set(i, new Slot(vaultInventory, index, x, y) {
                    @Override
                    public boolean mayPickup(Player player) {
                        return false;
                    }

                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return false;
                    }
                });
            }
        }
    }
}

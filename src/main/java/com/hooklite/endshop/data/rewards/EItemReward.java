package com.hooklite.endshop.data.rewards;

import com.hooklite.endshop.data.config.Transaction;
import com.hooklite.endshop.data.models.EItem;
import com.hooklite.endshop.logging.MessageSender;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EItemReward implements EReward {
    private Material reward;
    private RewardAction action;

    private EItem eItem;
    private ItemStack item;
    private int amount;

    @Override
    public void executeReward(EItem eItem, Player player, int amount) {
        double price = eItem.buyPrice * amount;
        Inventory playerInventory = player.getInventory();

        this.eItem = eItem;
        this.item = new ItemStack(reward, 1);
        this.amount = amount;

        if(action == RewardAction.BUY) {
            if(item.getMaxStackSize() > 1) {
                boolean match = false;

                for(ItemStack buyItem : playerInventory) {
                    if(buyItem != null && buyItem.getAmount() != buyItem.getMaxStackSize() && buyItem.getType() == item.getType()) {
                        if(buyItem.getAmount() < item.getMaxStackSize() - amount + 1) {
                            buyTransaction(player, price);

                            match = true;
                            break;
                        }
                    }
                }

                if(match && getEmptySlots(player) >= Math.ceil((double) amount / item.getMaxStackSize()))
                    buyTransaction(player, price);
                else if(!match)
                    MessageSender.toPlayer(player, "You do not have enough inventory space!");
            }
            else {
                if(getEmptySlots(player) >= amount) {
                    buyTransaction(player, price);
                }
                else {
                    MessageSender.toPlayer(player, "You do not have enough inventory space!");
                }
            }

        }
        else {
            if(playerInventory.containsAtLeast(item, amount)) {
                for(int i = 0; i < amount; i++) {
                    playerInventory.removeItem(item);
                    playerInventory.addItem(item);
                }
                MessageSender.sellMessage(player, eItem.name, reward.name().replace("_", " ").toLowerCase(), amount);
            }
            else {
                MessageSender.toPlayer(player, "You do not have enough items to sell!");
            }
        }
    }

    @Override
    public void setReward(String reward) {
        this.reward = Material.matchMaterial(reward);
    }

    private void addItems(Player player, ItemStack item, int amount) {
        for(int i = 0; i < amount; i++) {
            player.getInventory().addItem(item);
        }
    }

    private void buyTransaction(Player player, double price) {
        if(Transaction.withdraw(player, price)) {
            addItems(player, item, amount);
            MessageSender.buyMessage(player, eItem.name, price, amount);
        }
        else {
            MessageSender.toPlayer(player, "You do not have enough balance!");
        }
    }

    @Override
    public String getReward() {
        return reward.name();
    }


    private int getEmptySlots(Player player) {
        int amount = 0;
        Inventory inventory = player.getInventory();

        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chestplate = player.getInventory().getChestplate();
        ItemStack leggings = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();

        for(int i = 0; i < inventory.getContents().length; i++) {
            ItemStack currentItem = inventory.getContents()[i];

            if(currentItem == null)
                amount++;
            else {
                if(currentItem == helmet || currentItem == chestplate || currentItem == leggings || currentItem == boots)
                    amount--;
            }
        }

        for(ItemStack item : player.getInventory().getExtraContents()) {
            if(item == null)
                amount--;
        }

        if(helmet == null)
            amount--;
        if(chestplate == null)
            amount--;
        if(leggings == null)
            amount--;
        if(boots == null)
            amount--;

        return amount;
    }

    @Override
    public String getType() {
        return "item";
    }

    @Override
    public EReward getInstance() {
        return new EItemReward();
    }

    @Override
    public void setAction(RewardAction action) {
        this.action = action;
    }
}

package com.hooklite.endshop.data.config;

import com.hooklite.endshop.data.models.EItem;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;

class ItemLoader {

    /**
     * Deserializes the data from a config file.
     *
     * @param config The configuration file.
     * @return A list of EItem data models.
     * @throws InvalidConfigurationException If the configuration file is improperly configured.
     * @throws NullPointerException          If the configuration section doesn't exist.
     */
    static List<EItem> getModels(YamlConfiguration config) throws InvalidConfigurationException, NullPointerException {
        if (config.getConfigurationSection("items") != null) {
            List<EItem> items = new ArrayList<>();

            for (String item : config.getConfigurationSection("items").getKeys(true)) {
                if (!item.contains(".")) {
                    EItem eItem = new EItem();

                    eItem.name = config.getString(String.format("items.%s.name", item));
                    eItem.description = config.getStringList(String.format("items.%s.description", item)).isEmpty() ? config.getStringList(String.format("items.%s.description", item)) : new ArrayList<>();
                    eItem.slot = config.getInt(String.format("items.%s.slot", item));
                    eItem.displayItem = Material.matchMaterial(String.format("items.%s.display-item", item));
                    eItem.buyPrice = config.getDouble(String.format("items.%s.buy-price", item));
                    eItem.sellPrice = config.getDouble(String.format("items.%s.sell-price", item));

                    // TODO: Get a valid reward from the configuration file

                    items.add(eItem);
                }
            }
            return items;
        } else {
            throw new InvalidConfigurationException("Items are not properly configured!");
        }
    }
}
package com.truespace.datagen;

import com.truespace.TrueSpaceMod;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

/**
 * Generates {@code assets/truespace/lang/<locale>.json}.
 * One instance per locale.
 */
public class ModLanguageProvider extends LanguageProvider {

    private final String locale;

    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, TrueSpaceMod.MODID, locale);
        this.locale = locale;
    }

    @Override
    protected void addTranslations() {
        switch (locale) {
            case "ru_ru" -> addRu();
            default -> addEn();
        }
    }

    private void addEn() {
        // Creative tab
        add("itemGroup.truespace", "True Space");

        // Blocks
        add("block.truespace.bauxite_ore", "Bauxite Ore");
        add("block.truespace.cryo_chamber", "Cryo Chamber");

        // Items
        add("item.truespace.aluminum_ingot", "Aluminium Ingot");
        add("item.truespace.crushed_bauxite", "Crushed Bauxite");
        add("item.truespace.alumina", "Alumina");
        add("item.truespace.cryolite", "Cryolite");
        add("item.truespace.carbon_anode", "Carbon Anode");
        add("item.truespace.red_mud", "Red Mud");

        // Config screen
        add("truespace.configuration.title", "True Space Mod Configs");
        add("truespace.configuration.section.truespace.common.toml", "True Space Mod Configs");
        add("truespace.configuration.section.truespace.common.toml.title", "True Space Mod Configs");
        add("truespace.configuration.realisticMaterials", "Realistic material chains");
        add("truespace.configuration.lifeSupport", "Life support");
        add("truespace.configuration.radiation", "Ionizing radiation");
        add("truespace.configuration.realisticRocketry", "Realistic rocketry");
        add("truespace.configuration.realDataTooltips", "Show real-world data in tooltips");
    }

    private void addRu() {
        add("itemGroup.truespace", "True Space");

        add("block.truespace.bauxite_ore", "Бокситовая руда");
        add("block.truespace.cryo_chamber", "Криокамера");

        add("item.truespace.aluminum_ingot", "Алюминиевый слиток");
        add("item.truespace.crushed_bauxite", "Дроблёный боксит");
        add("item.truespace.alumina", "Глинозём");
        add("item.truespace.cryolite", "Криолит");
        add("item.truespace.carbon_anode", "Угольный анод");
        add("item.truespace.red_mud", "Красный шлам");

        add("truespace.configuration.title", "Настройки True Space Mod");
        add("truespace.configuration.section.truespace.common.toml", "Настройки True Space Mod");
        add("truespace.configuration.section.truespace.common.toml.title", "Настройки True Space Mod");
        add("truespace.configuration.realisticMaterials", "Реалистичные цепочки материалов");
        add("truespace.configuration.lifeSupport", "Жизнеобеспечение");
        add("truespace.configuration.radiation", "Ионизирующее излучение");
        add("truespace.configuration.realisticRocketry", "Реалистичная ракетная физика");
        add("truespace.configuration.realDataTooltips", "Показывать реальные данные в подсказках");
    }
}

package com.example.fantasygame.api.module.hero.model.valueobject;

import com.example.fantasygame.api.module.hero.model.Exception.InvalidHeroClassException;

import java.util.EnumSet;
import java.util.Set;

public enum HeroClass {
    WARRIOR(12, 0, AbilityType.STRENGTH, WeaponType.LONGSWORD, ArmorType.CHAIN_MAIL,
            EnumSet.of(WeaponType.LONGSWORD, WeaponType.AXE, WeaponType.MACE, WeaponType.DAGGER, WeaponType.STAFF),
            EnumSet.of(ArmorType.NONE, ArmorType.LEATHER, ArmorType.CHAIN_MAIL)),

    RANGER(10, 0, AbilityType.DEXTERITY, WeaponType.BOW, ArmorType.LEATHER,
           EnumSet.of(WeaponType.BOW, WeaponType.DAGGER, WeaponType.LONGSWORD),
           EnumSet.of(ArmorType.NONE, ArmorType.LEATHER)),

    MAGE(6, 10, AbilityType.INTELLIGENCE, WeaponType.STAFF, ArmorType.NONE,
         EnumSet.of(WeaponType.STAFF, WeaponType.DAGGER),
         EnumSet.of(ArmorType.NONE)),

    CLERIC(10, 8, AbilityType.WISDOM, WeaponType.MACE, ArmorType.CHAIN_MAIL,
           EnumSet.of(WeaponType.MACE, WeaponType.STAFF),
           EnumSet.of(ArmorType.NONE, ArmorType.LEATHER, ArmorType.CHAIN_MAIL));

    private final int baseHp;
    private final int maxMagic;
    private final AbilityType dominantAbility;
    private final WeaponType startingWeapon;
    private final ArmorType startingArmor;
    private final Set<WeaponType> allowedWeapons;
    private final Set<ArmorType> allowedArmors;

    HeroClass(int baseHp, int maxMagic, AbilityType dominantAbility,
              WeaponType startingWeapon, ArmorType startingArmor,
              Set<WeaponType> allowedWeapons, Set<ArmorType> allowedArmors) {
        this.baseHp = baseHp;
        this.maxMagic = maxMagic;
        this.dominantAbility = dominantAbility;
        this.startingWeapon = startingWeapon;
        this.startingArmor = startingArmor;
        this.allowedWeapons = allowedWeapons;
        this.allowedArmors = allowedArmors;
    }

    public int baseHp()                  { return baseHp; }
    public int maxMagic()                { return maxMagic; }
    public AbilityType dominantAbility() { return dominantAbility; }
    public WeaponType startingWeapon()   { return startingWeapon; }
    public ArmorType startingArmor()     { return startingArmor; }

    public boolean canUse(WeaponType weapon) { return allowedWeapons.contains(weapon); }
    public boolean canWear(ArmorType armor)  { return allowedArmors.contains(armor); }

    public static HeroClass from(String value) {
        if (value == null) {
            throw new InvalidHeroClassException("Hero class is required.");
        }
        try {
            return valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new InvalidHeroClassException("Unknown hero class: " + value);
        }
    }
}

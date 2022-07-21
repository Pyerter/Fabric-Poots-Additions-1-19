package net.pyerter.pootsadditions.item.custom.engineering;

import net.pyerter.pootsadditions.item.ModItemGroup;

public class Augments {

    public static final Augment[] HONED_EDGE = (new SimpleAugment.Builder("honed_edge"))
                    .setLevel(1).setLevelMultiplier(1f).addAttackDamage(3).buildLevels(5);

    public static void registerAugments() {

    }

}

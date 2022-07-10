package net.pyerter.pootsadditions.screen.input;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.pyerter.pootsadditions.screen.AccessoryTabAssistant;
import net.pyerter.pootsadditions.screen.handlers.ModScreenHandlers;

public class ModInputs {

    public static void registerKeyBindings() {
        registerCombatKeyBinding();
    }

    public static void registerCombatKeyBinding() {
        KeyBinding combatKey = new KeyBinding("key.pootsadditions.combat_key", InputUtil.GLFW_KEY_G, "PootsAdditions");
        KeyBindingRegistryImpl.registerKeyBinding(combatKey);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (combatKey.wasPressed()) {
                AccessoryTabAssistant.tryOpenTabScreen(ModScreenHandlers.ACCESSORIES_INVENTORY_SCREEN_HANDLER, true);
            }
        });
    }

}

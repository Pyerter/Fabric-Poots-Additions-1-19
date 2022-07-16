package net.pyerter.pootsadditions.integration.rei;

import me.shedaniel.rei.api.common.plugins.REIPluginProvider;

import java.util.Collection;
import java.util.List;

public class REIPootsAdditionsPluginProvider implements REIPluginProvider {
    @Override
    public Collection provide() {
        return null; //List.of(REIPootsAdditionsClientPlugin, REIPootsAdditionsServerPlugin);
    }

    @Override
    public Class getPluginProviderClass() {
        return this.getClass();
    }
}

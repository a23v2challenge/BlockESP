package io.github.blockesp;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/** Optional Mod Menu integration; this class is only loaded when Mod Menu asks for it. */
public final class BlockEspModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return BlockEspConfigScreen::new;
    }
}

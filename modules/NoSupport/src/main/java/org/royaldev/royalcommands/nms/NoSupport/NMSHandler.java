package org.royaldev.royalcommands.nms.NoSupport;

import org.bukkit.entity.Player;
import org.royaldev.royalcommands.nms.api.NMSFace;

public class NMSHandler implements NMSFace {
    @Override
    public boolean hasSupport() {
        return false;
    }

    @Override
    public int getPing(Player p) {
        return 0;
    }
}
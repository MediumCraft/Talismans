package com.willfp.talismans.talismans.talismans;

import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.util.TalismanChecks;
import com.willfp.talismans.talismans.util.TalismanUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

public class AlchemyTalisman extends Talisman {
    public AlchemyTalisman() {
        super("alchemy");
    }

    @EventHandler
    public void onPotionEffect(@NotNull final EntityPotionEffectEvent event) {
        if (event.getNewEffect() == null) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();

        if (player.hasMetadata(event.getNewEffect().toString())) {
            return;
        }

        int level = TalismanChecks.getTalismanLevel(player, this);

        if (level == 0) {
            return;
        }

        if (!TalismanUtils.passedChance(this.getLevel(level))) {
            return;
        }

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        PotionEffect effect = event.getNewEffect();

        PotionEffect newEffect = new PotionEffect(
                effect.getType(),
                effect.getDuration(),
                ((effect.getAmplifier() + 1) * 2) - 1,
                effect.isAmbient(),
                effect.hasParticles(),
                effect.hasIcon()
        );

        player.setMetadata(newEffect.toString(), this.getPlugin().getMetadataValueFactory().create(true));

        player.removePotionEffect(effect.getType());

        this.getPlugin().getScheduler().run(() -> newEffect.apply(player));

        this.getPlugin().getScheduler().runLater(() -> player.removeMetadata(newEffect.toString(), this.getPlugin()), 1);
    }
}

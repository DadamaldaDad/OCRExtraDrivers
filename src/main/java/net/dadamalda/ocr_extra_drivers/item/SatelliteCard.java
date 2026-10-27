package net.dadamalda.ocr_extra_drivers.item;

import li.cil.oc.common.item.traits.ComponentItem;
import li.cil.oc.common.item.traits.ItemTier;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.extensions.IItemExtension;

public class SatelliteCard extends Item implements ComponentItem, ItemTier, IItemExtension {
    private String unlocalizedName;

    public SatelliteCard(Properties properties) {
        super(properties);
    }

    @Override
    public String unlocalizedName() {
        return "Satellite Card";
    }

    @Override
    public void unlocalizedName_$eq(String x$1) {
        this.unlocalizedName = x$1;
    }
}

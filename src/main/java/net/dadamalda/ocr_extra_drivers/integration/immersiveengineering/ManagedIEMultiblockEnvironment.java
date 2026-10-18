package net.dadamalda.ocr_extra_drivers.integration.immersiveengineering;

import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import li.cil.oc.api.Network;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;

public class ManagedIEMultiblockEnvironment<T extends IMultiblockState> extends AbstractManagedEnvironment {
    protected final T state;

    public ManagedIEMultiblockEnvironment(T state, String name) {
        this.state = state;
        this.setNode(Network.newNode(this, Visibility.Network).withComponent(name).create());
    }
}

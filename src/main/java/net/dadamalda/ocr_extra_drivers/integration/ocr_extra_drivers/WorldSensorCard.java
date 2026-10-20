package net.dadamalda.ocr_extra_drivers.integration.ocr_extra_drivers;

import li.cil.oc.api.Network;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;
import net.dadamalda.ocr_extra_drivers.core.PlanetDataProviders;

public class WorldSensorCard extends AbstractManagedEnvironment {
    private final EnvironmentHost host;

    public WorldSensorCard(EnvironmentHost host) {
        this.host = host;
        setNode(Network.newNode(this, Visibility.Neighbors)
                .withComponent("world_sensor", Visibility.Neighbors).withConnector().create());
    }

    @Callback(doc = "function():number -- Get the gravity of the world the device is in compared to earth's.")
    public Object[] getGravity(final Context context, final Arguments args) {
        return new Object[]{PlanetDataProviders.gather(host.getEnvironmentLevel()).gravity()};
    }

    @Callback(doc = "function():boolean -- Get whether the world the device is in has a breathable atmosphere.")
    public Object[] hasBreathableAtmosphere(final Context context, final Arguments args) {
        return new Object[]{PlanetDataProviders.gather(host.getEnvironmentLevel()).hasOxygen()};
    }

    @Callback(doc = "function():boolean -- Get the temperature in Celsius of the world the device is in.")
    public Object[] getTemperature(final Context context, final Arguments args) {
        return new Object[]{PlanetDataProviders.gather(host.getEnvironmentLevel()).temperature()};
    }

    @Callback(doc = "function():number -- Get the solar panel efficiency of the world the device is in compared to earth's.")
    public Object[] getSolarEfficiency(final Context context, final Arguments args) {
        return new Object[]{PlanetDataProviders.gather(host.getEnvironmentLevel()).solarPower()};
    }
}

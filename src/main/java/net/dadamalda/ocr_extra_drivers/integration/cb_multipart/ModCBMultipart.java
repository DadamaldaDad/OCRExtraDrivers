package net.dadamalda.ocr_extra_drivers.integration.cb_multipart;

import net.dadamalda.ocr_extra_drivers.core.SatelliteClipContext;

public class ModCBMultipart {
    public static void initialize() {
        SatelliteClipContext.registerCollisionProvider(new CBMultipartSatelliteCollisionProvider());
    }
}

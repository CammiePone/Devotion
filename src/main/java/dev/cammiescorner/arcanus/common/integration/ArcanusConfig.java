package dev.cammiescorner.arcanus.common.integration;

import eu.midnightdust.lib.config.MidnightConfig;

public class ArcanusConfig extends MidnightConfig {
	@Entry(min = 0F, max = 20F) public static float auraSharpness = 6F;
	@Entry(min = 0F, max = 20F) public static float auraGradiant = 3F;
}

package com.falanero.katanamod.registry;

import net.minecraft.util.Identifier;

import static com.falanero.katanamod.KatanaMod.MOD_ID;

public class IDs {
    private static Identifier IdentifierOf(String name) {
        return new Identifier(MOD_ID, name);
    }

    // PACKETS
    public static final Identifier SKYBOUND_S2C_PACKET_ID = IdentifierOf("diamond_skybound");
    public static final Identifier WINDBOMB_S2C_PACKET_ID = IdentifierOf("diamond_windbomb");

    // ITEMS
    public static final Identifier IRON_KATANA = IdentifierOf("iron_katana");
    public static final Identifier DIAMOND_KATANA = IdentifierOf("diamond_katana");
    public static final Identifier DIAMOND_SOULGEM = IdentifierOf("diamond_soulgem");

    // ENTITIES
    public static final Identifier FEATHERBLADE_ENTITY = IdentifierOf("featherblade_entity");
}

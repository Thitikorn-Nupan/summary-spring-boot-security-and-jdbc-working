package com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.entities.common_primery;

import java.util.UUID;

public class PrimaryKey {

    private UUID uuid;

    public PrimaryKey(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
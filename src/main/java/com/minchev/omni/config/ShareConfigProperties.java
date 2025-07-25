package com.minchev.omni.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Validated
@Component
@ConfigurationProperties("share.server")
public class ShareConfigProperties {

    @NonNull
    private String url;

    @NonNull
    private String key;

    @NonNull
    public String getUrl() {
        return url;
    }

    public String getKey() {
        return key;
    }

    public void setUrl(@NonNull String url) {
        this.url = url;
    }

    public void setKey(@NonNull String key) {
        this.key = key;
    }
}

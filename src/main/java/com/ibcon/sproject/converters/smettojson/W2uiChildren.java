package com.ibcon.sproject.converters.smettojson;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public @Data class W2uiChildren {
    private List<SmetTypeJson> children = new ArrayList<>();

    public W2uiChildren() {
    }


    public void addChildren(SmetTypeJson smetTypeJson) {
        children.add(smetTypeJson);
    }
}

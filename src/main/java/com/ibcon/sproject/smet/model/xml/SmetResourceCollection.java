package com.ibcon.sproject.smet.model.xml;

import com.ibcon.sproject.smet.model.objects.SmetResource;

import java.util.HashMap;

public class SmetResourceCollection extends HashMap<String, SmetResource> {

    public SmetResourceCollection() {
        //todo заполнить коллекцию
    }

    public boolean containsRes(String resourceName) {
        return containsKey(resourceName);
    }
}

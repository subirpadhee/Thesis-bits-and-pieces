package com.dreamfactory.sampleapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by subir on 3/4/17.
 */

public class ResponseDelete {

    @SerializedName("resource")
    @Expose
    private List<ResourceDelete> resource = null;

    public List<ResourceDelete> getResource() {
        return resource;
    }

    public void setResource(List<ResourceDelete> resource) {
        this.resource = resource;
    }

}
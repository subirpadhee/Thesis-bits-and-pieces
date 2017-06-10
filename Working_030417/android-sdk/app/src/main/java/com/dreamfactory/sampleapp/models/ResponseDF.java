package com.dreamfactory.sampleapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
/**
 * Created by subir on 3/4/17.
 */

public class ResponseDF {

    @SerializedName("resource")
    @Expose
    private List<ResourceI> resource = null;

    public List<ResourceI> getResource() {
        return resource;
    }

    public void setResource(List<ResourceI> resource) {
        this.resource = resource;
    }

}

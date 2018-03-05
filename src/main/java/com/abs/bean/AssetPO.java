package com.abs.bean;

import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "asset")
public class AssetPO {
    public static final String COLLECTION_NAME = "asset";

    @Indexed(unique=true)
    @Field("assetUID")
    private String assetUid;// String √ 资产唯一ID

    private List<Stage> stages;

    public String getAssetUid() {
        return assetUid;
    }

    public void setAssetUid(String assetUid) {
        this.assetUid = assetUid;
    }

    public List<Stage> getStages() {
        return stages;
    }

    public void setStages(List<Stage> stages) {
        this.stages = stages;
    }
}


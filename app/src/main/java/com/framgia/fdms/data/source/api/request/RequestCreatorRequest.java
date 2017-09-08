package com.framgia.fdms.data.source.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by MyPC on 18/05/2017.
 */

public class RequestCreatorRequest extends BaseRequest {
    @SerializedName("request[title]")
    private String mTitle;
    @SerializedName("request[description]")
    private String mDescription;


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
}

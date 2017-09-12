package com.framgia.fdms.data.source.api.response;

import com.framgia.fdms.data.model.BaseModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by tri on 06/07/2017.
 */

public class ErrorMessage extends BaseModel {
    @Expose
    @SerializedName("base")
    private List<String> mMessageList;

    public List<String> getMessages() {
        return mMessageList;
    }

    public String getMessageList() {
        String listMessage = "";
        for (String message : mMessageList) {
            listMessage += "\n" + "-" + message;
        }
        return listMessage;
    }
}

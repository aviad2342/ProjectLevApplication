package app.projectortalapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Aviad on 13/02/2017.
 */

public class Result {
    @SerializedName("result")
    @Expose
    private String result;

    /**
     * @return The result
     */
    public String getResult() {
        return result;
    }

    /**
     * @param result The result
     */
    public void setResult(String result) {
        this.result = result;
    }
}

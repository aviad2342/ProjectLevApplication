package app.projectortalapplication.model;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * Created by Aviad on 13/02/2017.
 */

public class RetroClient {
    /**
     * Upload URL of your folder with php file name...
     * You will find this file in php_upload folder in this project
     * You can copy that folder and paste in your htdocs folder...
     */
    private static final String ROOT_URL = "http://arianlev.esy.es/ArianLev_Community/utils/upload_android.php/";


    public RetroClient() {

    }

    /**
     * Get Retro Client
     *
     * @return JSON Object
     */
    private static Retrofit getRetroClient() {
        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiService getApiService() {
        return getRetroClient().create(ApiService.class);
    }
}

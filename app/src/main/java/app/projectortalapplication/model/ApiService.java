package app.projectortalapplication.model;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Aviad on 13/02/2017.
 */

public interface ApiService {
    /*
Retrofit get annotation with our URL
And our method that will return us the List of Contacts
*/
    @Multipart
    @POST("upload.php")
    Call<Result> uploadImage(@Part MultipartBody.Part file);
}

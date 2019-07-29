package com.gdogaru.spacescoop.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SpacescoopService {

    @GET("api/languages/")
    Call<String> getLanguages();


    @GET("kidsfullfeed/{lang}/{page}/")
    Call<String> getFeedPage(@Path("lang") String language, @Path("page") int page);

}

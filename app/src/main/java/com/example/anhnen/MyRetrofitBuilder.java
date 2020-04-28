package com.example.anhnen;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyRetrofitBuilder {

    public static FlickrService service;
    public static FlickrService getInstance() {

        if (service == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://www.flickr.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service = retrofit.create(FlickrService.class);
        }
        return service;
    }

}

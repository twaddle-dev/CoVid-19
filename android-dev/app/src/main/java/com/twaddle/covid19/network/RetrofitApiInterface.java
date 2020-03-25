package com.twaddle.covid19.network;

import com.google.gson.JsonObject;
import com.twaddle.covid19.model.UserDetails;
import com.twaddle.covid19.model.WantHelpElder;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitApiInterface {
    @POST("user_register?")
    Call<UserDetails> createUser(@Body UserDetails userDetails);

    @POST("current_location")
    Call<Void> sendPeriodicCoordinates(@Body JsonObject drive_data );

    @POST("want_help")
    Call<UserDetails> sendWantHelpRequest(@Body WantHelpElder wantHelpElder);
}
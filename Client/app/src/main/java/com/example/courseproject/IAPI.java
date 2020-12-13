package com.example.courseproject;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IAPI {

    @POST("api/register")
    Observable<String> registerUser(@Body Users user);

    @POST("api/login")
    Observable<String> loginUser(@Body Users user);

//    @GET("api/selectall")
//    Observable<String> selectAll();

    @GET("api/getevents/{id}")
    Call<String> getEvents(@Path("id") int id);

    @GET("api/getsignupevents/{id}")
    Call<String> getSignUpEvents(@Path("id") int id);

    @GET("api/synchronize")
    Call<String> synchronize( );

//    @GET("api/getevents")
//    Call<Events> getEventsById(@Query("id") Integer id);

//    @GET("users")
//    Call<User> getUserById(@Query("id") Integer id);

    @POST("api/insertevent")
    Observable<String> insertEvent(@Body Events event);

    @PUT("api/insertevent")
    Call<Events> updateEvent(@Body Events event);

    @DELETE("api/insertevent/{id}")
    Call<Void> deleteEvent(@Path("id") int id);

    @POST("api/insertmember")
    Observable<String> insertMember(@Body Members member);

    @DELETE("api/insertmember/{id}")
    Call<Void> deleteMembers(@Path("id") int id);

    @DELETE("api/delmember/{id}/{id2}")
    Observable<String> delMember(@Path("id") int id, @Path("id2") int id2);
}

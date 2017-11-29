package com.freebitcoins.btcmining.satoshi.core.managers

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Request {

    companion object {
        val INTERSTITIAL_APPNEXT = 1
        val APPNEXT_VIDEO = 2
        val NATIVEX = 3
        val ADCOLONY_ZONE = 4
        val ADCOLONY_APP = 5
        val OFFERTORO_APP = 6
        val OFFERTORO_SECRET = 7
        val VUNGLE = 8
        val BANNER_APPNEXT = 9
        val CHARTBOOST_APP = 10
        val CHARTBOOST_SIGNATURE = 11
        var UNITY = 12
    }

    @FormUrlEncoded
    @POST("/login.php")
    fun login(
            @Field("user") user: String,
            @Field("pass") pass: String
    ) : Call<Any>

    @FormUrlEncoded
    @POST("/signup.php")
    fun signup(
            @Field("user") user: String,
            @Field("pass") pass: String
    ) : Call<Any>

    @FormUrlEncoded
    @POST("/checkinvite.php")
    fun checkinvite(
            @Field("code") invite: String
    ) : Call<Any>

    @FormUrlEncoded
    @POST("/getinvite.php")
    fun getinvite (
            @Field("user") user: String
    ) : Call<Any>

    @FormUrlEncoded
    @POST("/invitesatoshi.php")
    fun invitesatoshi(
            @Field("user") user: String,
            @Field("pass") pass: String
    ) : Call<Any>

    @FormUrlEncoded
    @POST("/savedata.php")
    fun savedata(
            @Field("user") user: String,
            @Field("pass") pass: String,
            @Field("userdata") userdata: String
    ) : Call<Any>

    @FormUrlEncoded
    @POST("/getdata.php")
    fun getdata(
            @Field("user") user: String,
            @Field("pass") pass: String
    ) : Call<Any>
}
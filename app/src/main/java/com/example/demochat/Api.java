package com.example.demochat;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {


    @Headers({"Host: shop.payberry.ru",
              "Connection: keep-alive",
    "X-MOD-SBB-CTYPE: xhr",
    "Origin: https://shop.payberry.ru",
    "X-Requested-With: XMLHttpRequest",
    "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36",
    "Referer: https://shop.payberry.ru/pay/4253",
    "Accept-Language: ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7",
    "Cookie: _ym_uid=1550009018533336137; _ym_d=1550009018; SPSI=7ba3b8f16bdd2a721492ca25a10a86de; __RequestVerificationToken=dVPmDmcHjywIV2g5DigB1okiAURWXbuiFHmBxf8MoLtzdtHAIOigxWrgyZCTXlEAe4ZDZ9UerLL4lvY1MEMh6-gmvboJDMcwGE8YzJZnTCQ1; sp_lit=owcUsQU0Y2H21LIIneNzcw==; PRLST=TW; _ym_isad=2; _ym_visorc_49545454=w; spcsrf=b4c1f7b8e9cb3f02ddbff039eaa36655; UTGv2=D-h41268a63ed9bfbca26357266e35df50ee61",
    "Content-Type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("Validate")
    Call<PostResponse> createPost(@Field("request__2") String request__2,
                              @Field("acquirerId") int acquirerId,
                              @Field("payType") int payType,
                              @Field("payElementId") int payElementId,
                              @Field("inputprop__0") String inputprop__0,
                              @Field("__RequestVerificationToken") String __RequestVerificationToken);
}

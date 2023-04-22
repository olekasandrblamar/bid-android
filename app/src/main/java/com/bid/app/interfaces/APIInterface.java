package com.bid.app.interfaces;

import com.bid.app.model.request.AcceptRequestForFamilyMemberRequest;
import com.bid.app.model.request.AddACardRequest;
import com.bid.app.model.request.AddCardRequest;
import com.bid.app.model.request.AddDocumentRequest;
import com.bid.app.model.request.AddressRequest;
import com.bid.app.model.request.BIDRequestModel;
import com.bid.app.model.request.CancelTicketRequest;
import com.bid.app.model.request.FamilyMembersRequest;
import com.bid.app.model.request.FindSomeOneWithMobileRequest;
import com.bid.app.model.request.ForgetPasswordRequest;
import com.bid.app.model.request.LoginRequest;
import com.bid.app.model.request.PassStopRequest;
import com.bid.app.model.request.PostSOSRequest;
import com.bid.app.model.request.ProfileRequest;
import com.bid.app.model.request.PurchaseTicketByDriverRequest;
import com.bid.app.model.request.PurchaseTicketRequest;
import com.bid.app.model.request.RateToUserRequest;
import com.bid.app.model.request.SaveBusCurrentPosRequest;
import com.bid.app.model.request.SignUpRequest;
import com.bid.app.model.request.StringRequest;
import com.bid.app.model.request.TellAFriendRequest;
import com.bid.app.model.request.TopUpByCardRequest;
import com.bid.app.model.request.TowardBusesRequest;
import com.bid.app.model.request.VerifyPhoneRequest;
import com.bid.app.model.response.AddCardResponse;
import com.bid.app.model.response.AddressAddResponse;
import com.bid.app.model.response.AddressListResponse;
import com.bid.app.model.response.BIDResponse;
import com.bid.app.model.response.ATripResponse;
import com.bid.app.model.response.BookScheduleTest;
import com.bid.app.model.response.BookScheduleTestResponse;
import com.bid.app.model.response.CardsListResponse;
import com.bid.app.model.response.CenterListResponse;
import com.bid.app.model.response.CheckFaceResponse;
import com.bid.app.model.response.CheckNotificationResponse;
import com.bid.app.model.response.DailyTestListResponse;
import com.bid.app.model.response.DailyTestResponse;
import com.bid.app.model.response.DirectionResults;
import com.bid.app.model.response.DiseaseResponse;
import com.bid.app.model.response.DistanceResponse;
import com.bid.app.model.response.FindSomeoneWithMobileResponse;
import com.bid.app.model.response.ForgetPasswordResponse;
import com.bid.app.model.request.BookATripRequest;
import com.bid.app.model.response.GetABStopResponse;
import com.bid.app.model.response.GetAvailableRoutesResponse;
import com.bid.app.model.response.FamilyMembersResponse;
import com.bid.app.model.response.GetReviewListResponse;
import com.bid.app.model.response.GetTicketDetailResponse;
import com.bid.app.model.response.GetAvailableSeatResponse;
import com.bid.app.model.response.GetBusLocationResponse;
import com.bid.app.model.response.GetDriverProfileResponse;
import com.bid.app.model.response.GetRouteByBusResponse;
import com.bid.app.model.response.GetSpecificCardResponse;
import com.bid.app.model.response.GetTripHistoryResponse;
import com.bid.app.model.response.GetWalletBalanceResponse;
import com.bid.app.model.response.IdDocuments;
import com.bid.app.model.response.ImageUploadResponse;
import com.bid.app.model.response.LoginResponse;
import com.bid.app.model.response.NotificationsResultResponse;
import com.bid.app.model.response.ProfileResponse;
import com.bid.app.model.response.PurchaseTicketResponse;
import com.bid.app.model.response.RefreshTokenResponse;
import com.bid.app.model.response.RegisterResponse;
import com.bid.app.model.response.RestaurantResponse;
import com.bid.app.model.response.ScheduleCovidResultResponse;
import com.bid.app.model.response.ScheduleScanResult;
import com.bid.app.model.response.ScheduleTestLatestResponse;
import com.bid.app.model.response.StaticPageResponse;
import com.bid.app.model.response.StatusResponse;
import com.bid.app.model.response.SymptomsResponse;
import com.bid.app.model.response.TellAFriendResponse;
import com.bid.app.model.response.TowardBusesResponse;
import com.bid.app.model.response.TransactionsResponse;
import com.bid.app.model.view.DailyTestRequest;
import com.bid.app.ui.fragment.IdDocuments.DocumentUploadResponseModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface APIInterface {

    ///////////Family members//////
    @GET("passenger/family-members")
    Call<FamilyMembersResponse> listFamilyMembers(@Query("token") String token, @Query("type") String profile);

    @POST("passenger/family-members")
    Call<StatusResponse> createFamilyMembers(@Query("token") String token, @Body FamilyMembersRequest request);

    @POST("find_person")
    Call<FindSomeoneWithMobileResponse> findSomeoneWithMobile(@Body FindSomeOneWithMobileRequest request);

    @POST("passenger/family-members/{id}")
    Call<StatusResponse> updateFamilyMembers(@Path("id") String familyId, @Query("token") String token, @Body FamilyMembersRequest request);

    @DELETE("passenger/family-members/{id}")
    Call<StatusResponse> deleteFamilyMembers(@Path("id") String familyId, @Query("token") String token);

    @POST("passenger/accept-family-members")
    Call<StatusResponse> acceptRequestForFamilyMember(@Query("token") String token,
                                                      @Body AcceptRequestForFamilyMemberRequest request);


    //////////////////////////////


    ///////////Passenger//////////

    @GET("passenger/get_available_routes_db")
    Call<GetAvailableRoutesResponse> getAvailableRoutesDB(@Query("token") String token);

//    @GET("get_schedule_routes")
//    Call<GetScheduleRouteResponse> getScheduleRoute(@Query("token") String token,
//                                                    @Query("route_id") String routeId,
//                                                    @Query("start_stop") String startStop,
//                                                    @Query("end_stop") String endStop);
//
//
    @POST("passenger/trip")
    Call<ATripResponse> bookATrip(@Query("token") String token, @Body BookATripRequest request);

    @GET("passenger/trip")
    Call<ATripResponse> getATrip(@Query("token") String token,
                                    @Query("tripId") String tripId);

    @GET("passenger/trips")
    Call<GetTripHistoryResponse> getTripList(@Query("token") String token, @Query("familyMemberId") String familyMemberId);

    @POST("passenger/get_toward_buses")
    Call<TowardBusesResponse> getTowardBuses(@Query("token") String token,
                                             @Body TowardBusesRequest towardBusesRequest);


//
//    @GET("get_ticket_history")
//    Call<GetTripHistoryResponse> getTicketHistory(@Query("token") String token);

    @POST("passenger/purchase_ticket")
    Call<StatusResponse> purchaseTicket(@Query("token") String token,
                                        @Body PurchaseTicketRequest request);

    @GET("passenger/get_ticket")
    Call<GetTicketDetailResponse> getTicketDetail(@Query("token") String token,
                                                  @Query("ticketId") String ticketId);

    @POST("passenger/cancel_ticket")
    Call<StatusResponse> cancelTicket(@Query("token") String token,
                                      @Body CancelTicketRequest request);


    //    @GET("get_location")
    @GET("get_location")
    Call<GetBusLocationResponse> getBusLocationFake(@Query("token") String token,
                                                    @Query("busId") String busId);

    @GET("get_bus")
    Call<GetBusLocationResponse> getBusLocationFromDB(@Query("token") String token,
                                                      @Query("busID") String busID,
                                                      @Query("type") String type);

    //To do
    @GET("get_bus")
    Call<GetBusLocationResponse> getBusRoute( @Query("token") String token,
                                              @Query("busID") String busID,
                                              @Query("type") String type);


    @GET("passenger/get_driver")
    Call<GetDriverProfileResponse> getDriverProfile(@Query("token") String token,
                                                    @Query("driverId") String driverId);



    @POST("sos")
    Call<StatusResponse> postSOS(@Query("token") String token,
                                 @Query("lat") String latitude,
                                 @Query("long") String longitude,
                                 @Body RequestBody requestBody);

    @POST("add_kid")
    Call<StatusResponse> addKid(@Query("token") String token,
                                 @Body PostSOSRequest request);


    @GET("notifications?")
    Call<NotificationsResultResponse> notifications(@Query("token") String token);

    @GET("check_notifications")
    Call<CheckNotificationResponse> checkNotifications(@Query("token") String token);

    ///////////////////////////////////////////

    //////////////////Driver///////////////////////

    @POST("pass_stop")
    Call<StatusResponse> passStop(@Query("token") String token,
                                                @Body PassStopRequest request);

    @POST("driver/purchase_ticket_by_driver")
    Call<StatusResponse> purchaseTicketByDriver(@Query("token") String token,
                                        @Body PurchaseTicketByDriverRequest request);

    @POST("driver/check_ticket_with_qr")
    Call<StatusResponse> checkTicketWithQR(@Query("token") String token,
                                           @Query("ticket_id") String ticketId);

    @POST("send_location")
    Call<StatusResponse> saveBusCurrentPos(@Query("token") String token,
                                           @Body SaveBusCurrentPosRequest request);

    @POST("test_driver")
    Call<GetBusLocationResponse> putData(@Body StringRequest data);

    @GET("driver/current_stops")
    Call<GetABStopResponse> getABStops(@Query("token") String token);

    @POST("driver/slide")
    Call<GetABStopResponse> slide(@Query("token") String token);


    @Multipart
    @POST("driver/check_ticket_with_face")
    Call<StatusResponse> scanTicketWithFace(@Query("token") String token, @Part MultipartBody.Part image);



    ///////////////////////////////////////////////

    //////////////////Bus Common/////////////////////
    @GET("get_available_seat")
    Call<GetAvailableSeatResponse> getAvailableSeat(@Query("token") String token,
                                                    @Query("startStop") String startStop,
                                                    @Query("endStop") String endStop,
                                                    @Query("busId") String busId);


    @GET("get_bus")
    Call<GetRouteByBusResponse> getRouteByBus(@Query("token") String token,
                                              @Query("busId") String busId,
                                              @Query("type") String type);


    @GET("test_driver")
    Call<GetBusLocationResponse> getBusLocation(@Query("token") String token,
                                                    @Query("bid") String busId);
    @POST("passenger/review")
    Call<StatusResponse> rateToUser(@Query("token") String token, @Body RateToUserRequest request);

    @GET("passenger/reviews")
    Call<GetReviewListResponse> getReviewList(@Query("userId") String userId, @Query("page") String page);


    @POST("schedule_test?")
    Call<BookScheduleTestResponse> book_schedule_test(@Query("token") String token, @Body BookScheduleTest request);


    ////////////////////////////////////////////////

    ///////////////////Auth//////////////////////////
    @POST("verify_phone")
    Call<StatusResponse> verifyPhone1(@Body VerifyPhoneRequest request);

    @POST("users/register")
    Call<RegisterResponse> register(@Body SignUpRequest request);

    @POST("users/login")
    Call<LoginResponse> loginUser(@Body LoginRequest request);



    /////////////////////////////////////////////////


    //////////////////Profile///////////////////////////////

    @PUT("users?")
    Call<ProfileResponse> updateUser(@Query("token") String token, @Body ProfileRequest profileRequest);

    @Multipart
    @POST("attachments?class=UserAvatar")
    Call<ImageUploadResponse> updateUserImage(@Query("token") String token, @Part MultipartBody.Part image);


    @POST("users/generate-id")
    Call<BIDResponse> getUserBID(@Query("token") String token, @Body BIDRequestModel bidRequestModel);

    @POST("users/forgot_password")
    Call<ForgetPasswordResponse> forgetPassword(@Body ForgetPasswordRequest request);

    @GET("user_address?")
    Call<AddressListResponse> getUserAddress(@Query("token") String token);

    @POST("user_address?")
    Call<AddressAddResponse> addUserAddress(@Body AddressRequest request, @Query("token") String token);

    @POST("tell_a_friend")
    Call<TellAFriendResponse> tellAFriend(@Body TellAFriendRequest request);

    ///////////////////////////////////////////////////////


    /////////////////////////Wallet///////////////////////////////

    @GET("passenger/get_specific_card")
    Call<GetSpecificCardResponse> getSpecificCard(@Query("token") String token,
                                                  @Query("card_number") String cardNumber);

    @GET("passenger/get_wallet_balance")
    Call<GetWalletBalanceResponse> getWalletBalance(@Query("token") String token);

    @GET("card")
    Call<TransactionsResponse> getTransactions(@Query("token") String token);

    @POST("card")
    Call<AddCardResponse> AddACard(@Query("token") String token,
                                   @Body AddACardRequest request);

    @POST("passenger/card?")
    Call<AddCardResponse> addCard(@Query("token") String token, @Body AddCardRequest request);

    @DELETE("passenger/card/{id}")
    Call<StatusResponse> deleteACard(@Path("id")String id,
                                     @Query("token") String token);

    @POST("passenger/topup_wallet")
    Call<StatusResponse> topUpByCard(@Query("token") String token,
                                     @Body TopUpByCardRequest request);


    @GET("passenger/cards")
    Call<CardsListResponse> getCards(@Query("token") String token);


    @GET("passenger/cards")
    Call<CardsListResponse> cards(@Query("token") String token, @Query("page") String page);

    //////////////////////////////////////////////////////////////

    ///////////////////////Face OCR//////////////////////////////

    //////////////////////////////////////////////////////////////


    /////////////////////Third paty Apis////////////////////////////////////


    @GET("https://maps.googleapis.com/maps/api/directions/json")
    Call<DirectionResults> fetchRoutePath(@Query("origin") String origin,
                                          @Query("destination") String destination,
                                          @Query("waypoints") String waypoints,
                                          @Query("mode") String mode,
                                          @Query("key") String key);

    @GET("https://maps.googleapis.com/maps/api/distancematrix/json")
    Call<DistanceResponse> getDistanceInfo(
            @QueryMap Map<String, String> parameters
    );


    @GET("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
    Call<RestaurantResponse> fetchRestaurants(@Query("location") String location,
                                              @Query("radius") String radius,
                                              @Query("type") String type,
                                              @Query("key") String key);

    /////////////////////////////////////////////////////////////



    @POST("schedule_test/scan?")
    Call<ScheduleTestLatestResponse> schedule_test_scan(@Query("token") String token, @Body ScheduleScanResult request);

    @GET("oauth/refresh_token")
    Call<RefreshTokenResponse> refresh_token(@Query("token") String token);

    @GET("schedule_test/latest")
    Call<ScheduleTestLatestResponse> schedule_test_latest(@Query("token") String token);

    @GET("passenger/transactions?")
    Call<TransactionsResponse> transactions(@Query("token") String token,
                                            @Query("type") String type);

    @GET("pages/2")
    Call<StaticPageResponse> aboutBID();

    @GET("pages/15")
    Call<StaticPageResponse> termsAndConditions();

    @GET("pages/7")
    Call<StaticPageResponse> privacyPolicy();

    @GET("pages/9")
    Call<StaticPageResponse> dataSecurity();

    @GET("users/book/search/date")
    Call<CenterListResponse> getCOVIDCenters(@Query("token") String token, @Query("date") String date);

    @GET("schedule_test?")
    Call<ScheduleCovidResultResponse> getCovidScheduleList(@Query("token") String token);

    @GET("symptoms")
    Call<SymptomsResponse> getSymptoms();

    @GET("daily_test?")
    Call<DailyTestListResponse> getDailyTestList(@Query("token") String token);

    @POST("daily_test?")
    Call<DailyTestResponse> setDailyTest(@Query("token") String token , @Body DailyTestRequest dailyTestRequest);


    @GET("documents?")
    Call<IdDocuments> getSavedDocuments(@Query("token") String token);



    @Multipart
    @POST
    Call<DocumentUploadResponseModel> scanDocument(@Url String url, @Query("apikey") String apikey, @Part MultipartBody.Part image);


    @Multipart
    @POST
    Call<CheckFaceResponse> checkFace(@Url String url, @Query("apikey") String apikey, @Query("bidno") String bidNo,@Part MultipartBody.Part image);

    @POST("attachments?class=Document")
    Call<ImageUploadResponse> uploadDocument(@Query("token") String token, @Body RequestBody requestBody);

    @PUT("document?")
    Call<StatusResponse> addDocument(@Query("token") String token, @Body AddDocumentRequest addDocumentRequest);

    @GET("diseases")
    Call<DiseaseResponse> getDiseasesType();
}


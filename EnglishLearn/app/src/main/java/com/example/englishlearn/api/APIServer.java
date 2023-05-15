package com.example.englishlearn.api;

import com.example.englishlearn.models.Achievementofuser;
import com.example.englishlearn.models.Answer;
import com.example.englishlearn.models.AnswerOfStory;
import com.example.englishlearn.models.Level;
import com.example.englishlearn.models.Question;
import com.example.englishlearn.models.QuestionOfStory;
import com.example.englishlearn.models.QuestionOfTest;
import com.example.englishlearn.models.Story;
import com.example.englishlearn.models.TheTestOfUsers;
import com.example.englishlearn.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIServer {
    Gson gson = new GsonBuilder()
            .setLenient()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    APIServer api = new Retrofit.Builder()
            .baseUrl(APIConst.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(APIServer.class);

    //Check isLogin
    @FormUrlEncoded
    @POST(APIConst.loginHandler)
    Call<User> loginHandler(@Field(APIConst.USER_NAME) String user,
                            @Field(APIConst.PASS_WORD) String pass);

    //Regiter user
    @FormUrlEncoded
    @POST(APIConst.registerhandler)
    Call<String> registerHandler(@Field(APIConst.USER_NAME) String user,
                                 @Field(APIConst.NAME) String name,
                                 @Field(APIConst.PASS_WORD) String pass);

    //Get User logined
    @FormUrlEncoded
    @POST(APIConst.getInfoUser)
    Call<User> getInforUser(@Field(APIConst.USER_NAME) String user);

    //Update Avatar
    @Multipart
    @POST(APIConst.uploadAvatar)
    Call<String> uploadAvatar(@Part(APIConst.USER_NAME) RequestBody user,
                              @Part MultipartBody.Part avatar);

    //get Level study
    @GET(APIConst.getLevelStudy)
    Call<List<Level>> getLevel();

    //get Part in Level with idlevel
    @FormUrlEncoded
    @POST(APIConst.getPartInLevel)
    Call<List<com.example.englishlearn.models.Part>> getPartinLevel(@Field(APIConst.ID_LEVEL) int idlevel);

    //get list questions in part seleted
    @FormUrlEncoded
    @POST(APIConst.getQuestionsinPart)
    Call<List<Question>> getListQuestions(@Field(APIConst.ID_PART) int idpart);

    //get list answers in part seleted
    @FormUrlEncoded
    @POST(APIConst.getAnswersinPart)
    Call<List<Answer>> getListAnswers(@Field(APIConst.ID_PART) int idpart);

    // get Achievement
    @FormUrlEncoded
    @POST(APIConst.getAchievement)
    Call<Achievementofuser> getAchievement(@Field(APIConst.USER_NAME) String username);

    //get all story
    @GET(APIConst.getStory)
    Call<List<Story>> getStory();

    //get all current Question of story
    @FormUrlEncoded
    @POST(APIConst.getQuestionOfStory)
    Call<List<QuestionOfStory>> getQuestionOfStory(@Field(APIConst.ID_STORY) int idStory);

    //get all current Answer of story
    @FormUrlEncoded
    @POST(APIConst.getAnswerOfStory)
    Call<List<AnswerOfStory>> getAnswerOfStory(@Field(APIConst.ID_STORY) int idStory);

    // update profile
    @Multipart
    @POST(APIConst.updateProfile)
    Call<String> updateProfile(@Part(APIConst.USER_NAME) RequestBody user,
                               @Part(APIConst.NAME) RequestBody name,
                               @Part MultipartBody.Part avatar);

    // update profile
    @Multipart
    @POST(APIConst.updateProfile)
    Call<String> updateProfile(@Part(APIConst.USER_NAME) RequestBody user,
                               @Part(APIConst.NAME) RequestBody name);

    // update password
    @FormUrlEncoded
    @POST(APIConst.updatePassword)
    Call<String> updatepassword(@Field(APIConst.USER_NAME) String username,
                                @Field(APIConst.PASS_WORD) String password);

    // update achievement of user
    @FormUrlEncoded
    @POST(APIConst.updateAchievement)
    Call<String> updateAchievementOfUser(@Field(APIConst.USER_NAME) String username,
                                         @Field(APIConst.POINT_OF_DAY) int point,
                                         @Field(APIConst.CHAIN_OF_USER) int chain);

    @FormUrlEncoded
    @POST(APIConst.updateAchievement)
    Call<String> updateAchievementOfUser(@Field(APIConst.USER_NAME) String username,
                                         @Field(APIConst.POINT_OF_DAY) int point,
                                         @Field(APIConst.CHAIN_OF_USER) int chain,
                                         @Field(APIConst.POINT_OF_WEEK) int pointofweek);

    // search the test
    @FormUrlEncoded
    @POST(APIConst.searchTheTest)
    Call<List<TheTestOfUsers>> searchTest(@Field(APIConst.SEARCH_TEXT) String txtsearch);

    // insert data the test
    @FormUrlEncoded
    @POST(APIConst.insertDataTheTest)
    Call<String> insertDataInTheTest(@Field(APIConst.ID) String id,
                                     @Field(APIConst.NAME) String name,
                                     @Field(APIConst.USER) String user,
                                     @Field(APIConst.DATE) String date);

    @Multipart
    @POST(APIConst.insertDataTheTest)
    Call<String> insertDataInTheTest(@Part(APIConst.ID) RequestBody id,
                                     @Part(APIConst.NAME) RequestBody name,
                                     @Part(APIConst.USER) RequestBody user,
                                     @Part(APIConst.DATE) RequestBody date,
                                     @Part MultipartBody.Part avatar);

    // insert question of the test
    @FormUrlEncoded
    @POST(APIConst.insertQuestionOfTest)
    Call<String> insertAllQuestionOfTest(@Field(APIConst.ID_TEXT) String idtest,
                                         @Field(APIConst.CONTENT) String content,
                                         @Field(APIConst.ANSWER_1) String answer1,
                                         @Field(APIConst.ANSWER_2) String answer2,
                                         @Field(APIConst.ANSWER_3) String answer3,
                                         @Field(APIConst.ANSWER_4) String answer4,
                                         @Field(APIConst.GOOD_ANSWER) String goodanswer);

    // delete question of the test errorr
    @FormUrlEncoded
    @POST(APIConst.deleteQuestionOfTestErrorr)
    Call<String> deleteAllQuestionOfTestErrorr(@Field(APIConst.ID_TEXT) String idtest);

    // delete the test created
    @FormUrlEncoded
    @POST(APIConst.deleteTestCreated)
    Call<String> deleteTestCreated(@Field(APIConst.ID_TEXT) String idtest);

    // get question of the selected
    @FormUrlEncoded
    @POST(APIConst.getQuestionOfTest)
    Call<List<QuestionOfTest>> getQuestionOfTestSelected(@Field(APIConst.ID_TEXT) String idtest);
}

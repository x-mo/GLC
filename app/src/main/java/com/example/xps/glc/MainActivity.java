package com.example.xps.glc;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public class MainActivity extends AppCompatActivity {
    EditText postId;
    EditText accessToken;
    TextView result, total, notG;
    Button butt;

    private interface Service {

        /*@GET("{endPoint}")
        void getUsers(@Path("endpoint") String endPoint, Callback<List<data>> callback);*/

        /*@GET("/{userId}/likes/{postId}/")
        UserData verifyUser(@Path("{userId}") String userId, @Path("{postId}") String postId, @Query("limit") String limit, @Query("access_token") String access_token);*/

        //@Headers("Content-Type: application/json")
        @GET("/{userId}/likes/{pageId}/")
        void verifyUserAsync(@Path("userId") String userId, @Path("pageId") String pageId, @Query("limit") String limit, @Query("access_token") String access_token, Callback<UserData> callback);

        @GET("/{userId}/likes/{pageId}/")
        UserData verifyUser(@Path("userId") String userId, @Path("pageId") String pageId, @Query("limit") String limit, @Query("access_token") String access_token);

        @GET("/{postId}/likes/")
        void getUsersAsync(@Path("postId") String postId, @Query("access_token") String access_token, @Query("limit") String limit, Callback<UserData> callback);
        /*@GET("{}")
        void verifyUser(@Path("") String XYZ, );*/

    }

    public class UserData {
        public List<Data> data;
        public int geniune = 0;
        public int not = 0;
    }

    public class Data {
        public String name;
        public String id;
        public String created_time;
    }

    String baseURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RestAdapter RA1 = new RestAdapter.Builder().setEndpoint("https://graph.facebook.com").setLogLevel(RestAdapter.LogLevel.FULL).build();
        postId = (EditText) findViewById(R.id.post_id);
        accessToken = (EditText) findViewById(R.id.page_access_token);
        result = (TextView) findViewById(R.id.result);
        total = (TextView) findViewById(R.id.total);
        notG = (TextView) findViewById(R.id.not);
        butt = (Button) findViewById(R.id.butt);
        baseURL = "https://graph.facebook.com/";
        butt.setOnClickListener(new View.OnClickListener() {


            Service service = RA1.create(Service.class);

            String userID = /*"1546089391"*/ "100001143923761";
            String pageID = "677483845685464";
            String postID = "710614879039027";
            String page_access_token = "CAACEdEose0cBALC6qlg3FR1htOsgDvRDOC8LQZAIL50Y9l6a88985Lf89hyB2PXf5jTZBZA988IGmjwMFwbZCoEqZClUGy0xGtiG3t40YGRcbZAlLNZAnkGSuuFqdutH27ahpGpkCIjGYStmjDq68OApjHBKkZAZAiiajes7XXoe4ZCG8dH6Le2T5THhwvLkvzCBn6f57n9wwBGQZDZD";
            String person_access_token = "CAAUelZCdingIBAM9PxV3kaiccflumMx1ZA46BNefQLsqU9x3mY1X19NDhZC597nIHqCTZBafcb2Y3dCZBiM07unYA1fh9KnZC7L2xg6lkPVFlOAjF0mVPsH9PlkFw35wbZBV9YYzVZBspDFfwDOIIL8PzGU12fw3WFrfuXZCJgeSmF4gZAz7Fs82ZCvu5yav7uJBbBbNa3J9ZB6bsyMl9CHIyzTa";
        /*service.verifyUserAsync(*//*"100001143923761"*//* userID, pageID, "1000", page_access_token, new Callback<UserData>() {
            @Override
            public void success(UserData userData, Response response) {
                if (userData.data.size() == 0)
                    Toast.makeText(MainActivity.this, "This user doesn't like TOMATO.", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(MainActivity.this, userData.data.get(0).created_time, Toast.LENGTH_LONG).show();


            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });*/

            UserData tmp;
            String limit = "1000";

            @Override
            public void onClick(View v) {
                if (!(postId.getText().toString().equals("")) && !(accessToken.getText().toString().equals(""))) {
                    postID = postId.getText().toString();
                    page_access_token = accessToken.getText().toString();
                    service.getUsersAsync(postID, page_access_token, limit, new Callback<UserData>() {
                        @Override
                        public void success(UserData userData, Response response) {
                            //Toast.makeText(MainActivity.this, String.valueOf(userData.data.size()), Toast.LENGTH_LONG).show();

                            //result.setText("");
                            result.setText("Geniune: ");
                            total.setText("Total: ");
                            notG.setText("Not Geniune: ");
                            for (int i = 0; i < userData.data.size(); i++) {

                        /*    result.append(" - " + userData.data.get(i).name);
                            userData.geniune++;*/
                            /*service.verifyUserAsync(userID, pageID, limit, page_access_token, new Callback<UserData>() {
                                @Override
                                public void success(UserData userData, Response response) {
                                    if (userData.data.size() != 0) userData.geniune++;
                                }

                                @Override
                                public void failure(RetrofitError error) {

                                }
                            });*/
//if(userData.data.size()==0) break;
                                tmp = service.verifyUser(userData.data.get(i).id, pageID, limit, page_access_token);
                                if (!tmp.data.isEmpty()) {
                                    userData.geniune++;
                                    Log.e("Yaay", "MMkay");
                                    tmp = null;
                                } else {
                                    userData.not++;
                                    Log.e("Naay", "Oops");
                                }
                            }
                            //Toast.makeText(MainActivity.this, "Total: " + String.valueOf(userData.geniune + userData.not), Toast.LENGTH_LONG).show();
                            result.append(String.valueOf(userData.geniune));
                            total.append(String.valueOf(userData.geniune + userData.not));
                            notG.append(String.valueOf(userData.not));

                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(MainActivity.this, "Update Page Token, and Check Post ID.", Toast.LENGTH_LONG).show();
                        }
                    });

                } else {

                    Toast.makeText(MainActivity.this, "Fill both Post ID & Access Token", Toast.LENGTH_LONG).show();
                }
            }

        });


    }
}

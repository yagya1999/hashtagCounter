package com.example.hashtagcounter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseInstallation;

import java.util.ArrayList;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class MainActivity extends AppCompatActivity {
    EditText tag, since, untill;
    TextView display;
    Button btnDisplay;


static int a = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);


        ParseInstallation.getCurrentInstallation().saveInBackground();

        System.out.println( "no of tweets");

        setContentView(R.layout.activity_main);

        tag = findViewById(R.id.tag);
        since = findViewById(R.id.since);
        untill = findViewById(R.id.untill);

        display = findViewById(R.id.display);
        btnDisplay = findViewById(R.id.btnDisplay);

        btnDisplay.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              if (tag.getText().toString().isEmpty() || since.getText().toString().isEmpty() || untill.getText().toString().isEmpty()) {
                                                  Toast.makeText(MainActivity.this, "empty", Toast.LENGTH_SHORT).show();
                                              } else {
                                                  String tagg = tag.getText().toString();
                                                  String untilll = untill.getText().toString();
                                                  String sincee = since.getText().toString();

                                                  Longrun lr = new Longrun();
                                                  lr.execute(tagg);
                                                 display.setText("tag you have entered : " +tagg + "\n" +"from date : " + untilll + "\n" + "since date :" + sincee + "\n" + "total no of tweets so far : " + a);


                                              }


                                          }
                                      }

        );



        }



    private static class Longrun extends AsyncTask<String, String, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
String taggg = params[0];

            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey("GsXVeQeFX9NdCXdgeNOXJPiRP")
                    .setOAuthConsumerSecret("sznedrxq8WPZRIZS4MSGCp4R4hFQWe1FuLCNUKtKr2fdzNrYga")
                    .setOAuthAccessToken("714135485635960832-Ns8SsVOlBfkg1cxFhHVYQv89pvGGt2P")
                    .setOAuthAccessTokenSecret("5fZPVOl4dnqV8RMOGPXo3siLY0DLFRIlMWEF8ZinAJN6h");
            Twitter twitter = new TwitterFactory(cb.build()).getInstance();
                Query query = new Query(taggg);
                int numberOfTweets = 5000;
                long lastID = Long.MAX_VALUE;
                ArrayList<twitter4j.Status> tweets = new ArrayList<>();
                while (tweets.size() < numberOfTweets) {
                    query.setCount(Math.min(numberOfTweets - tweets.size(), 500));
                    try {
                        QueryResult result = twitter.search(query);
                        tweets.addAll(result.getTweets());
                        System.out.println("Gathered " + tweets.size() + " tweets" + "\n");
                        a = tweets.size();

                        for (twitter4j.Status t : tweets)
                            if (t.getId() < lastID)
                                lastID = t.getId();

                    } catch (TwitterException te) {
                        System.out.println("Couldn't connect: " + te);
                    }

                    query.setMaxId(lastID - 1);
                }

                return a;
            }
        }


        protected void onPostExecute(Integer result) {

            System.out.println(result);
// update the UI after background processes completes
        }






}


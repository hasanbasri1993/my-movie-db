package com.daarululuumlido.mymoviedb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.daarululuumlido.mymoviedb.BuildConfig;
import com.daarululuumlido.mymoviedb.R;
import com.daarululuumlido.mymoviedb.model.MovieListModel;
import com.daarululuumlido.mymoviedb.notification.MovieDailyReceiver;
import com.daarululuumlido.mymoviedb.notification.MovieUpcomingReceiver;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class SettingsPrefActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static class MainPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        SwitchPreference switchReminder, switchToday;

        MovieDailyReceiver movieDailyReceiver = new MovieDailyReceiver();
        MovieUpcomingReceiver movieUpcomingReceiver = new MovieUpcomingReceiver();

        List<MovieListModel> movieListModels;
        List<MovieListModel> sameMovieListModels;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_main);


            movieListModels = new ArrayList<>();
            sameMovieListModels = new ArrayList<>();

            switchReminder = (SwitchPreference) findPreference(getString(R.string.key_today_reminder));
            switchReminder.setOnPreferenceChangeListener(this);
            switchToday = (SwitchPreference) findPreference(getString(R.string.key_release_reminder));
            switchToday.setOnPreferenceChangeListener(this);

            Preference myPref = findPreference(getString(R.string.key_lang_setting));
            myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));

                    return true;
                }
            });
        }



        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {

            String key = preference.getKey();
            boolean b = (boolean) newValue;

            if (key.equals(getString(R.string.key_today_reminder))) {
                if (b) {
                    movieDailyReceiver.setAlarm(getActivity());
                } else {
                    movieDailyReceiver.cancelAlarm(getActivity());
                }
            } else {
                if (b) {
                    setReleaseAlarm();
                } else {
                    movieUpcomingReceiver.cancelAlarm(getActivity());
                }
            }
            return true;

        }

        private void setReleaseAlarm() {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = new Date();
            final String now = dateFormat.format(date);

            AsyncHttpClient client = new AsyncHttpClient();
            String url = BuildConfig.BASE_URL + "movie/upcoming?api_key=" + BuildConfig.TMDB_API_KEY;
            client.get(url, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String hasil = new String(responseBody);
                        JSONObject responseObject = new JSONObject(hasil);
                        JSONArray list = responseObject.getJSONArray("results");
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject movies = list.getJSONObject(i);
                            MovieListModel movieListModel = new MovieListModel(movies);
                            if (movieListModel.getReleaseDate().equals(now)) {
                                sameMovieListModels.add(movieListModel);
                            }
                        }
                        movieUpcomingReceiver.setAlarm(getActivity(), sameMovieListModels);
                        Log.d("LOG", "onSuccess: Selesai.....");

                    } catch (Exception e) {
                        Log.d("LOG", "onSuccess: Gagal.....");
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("LOG", "onFailure: Gagal.....");
                }
            });

        }

    }
}


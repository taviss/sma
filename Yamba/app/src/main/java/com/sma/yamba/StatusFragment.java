package com.sma.yamba;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;


public class StatusFragment extends Fragment {
    private static final String TAG = StatusFragment.class.getName();

    private EditText statusUpdate;
    private Button tweetButton;
    private TextView charCountView;

    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_status, container, false);

        statusUpdate = (EditText) view.findViewById(R.id.edit_status);
        tweetButton = (Button) view.findViewById(R.id.button_tweet);
        charCountView = (TextView) view.findViewById(R.id.char_count_text);

        statusUpdate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int count = 140 - statusUpdate.length();
                charCountView.setText(" ");
                charCountView.setText(Integer.toString(count));

                if(count < 10)
                    charCountView.setTextColor(RED);
                else
                    charCountView.setTextColor(GREEN);
            }
        });

        tweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postText = statusUpdate.getText().toString();
                Log.d(TAG, "Tweet " + postText);

                new PostTask().execute(postText);
            }
        });

        return view;
    }

    private final class PostTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

            String username = preferences.getString("username", "");
            String password = preferences.getString("password", "");

            if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                getActivity().startActivity(new Intent(getActivity(), SettingsActivity.class));
                return "Username or password not set";
            }

            YambaClient yambaClient = new YambaClient(username, password);
            try {
                yambaClient.postStatus(params[0]);
                return "Update posted!";
            } catch(YambaClientException e) {
                return "Failed";
            }
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Toast.makeText(StatusFragment.this.getActivity(), result, Toast.LENGTH_LONG).show();
        }
    }
}

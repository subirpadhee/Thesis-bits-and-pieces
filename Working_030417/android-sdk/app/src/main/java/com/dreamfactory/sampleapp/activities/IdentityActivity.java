package com.dreamfactory.sampleapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamfactory.sampleapp.R;
import com.dreamfactory.sampleapp.api.DreamFactoryAPI;
import com.dreamfactory.sampleapp.api.services.ApiInterface;
import com.dreamfactory.sampleapp.models.ResourceI;
import com.dreamfactory.sampleapp.models.ResponseDF;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by subir on 3/4/17.
 */

public class IdentityActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity);

        final TextView tvName1 = (TextView)findViewById(R.id.tvName1);
        final TextView tvName2 = (TextView)findViewById(R.id.tvName2);

        Button btnPress = (Button)findViewById(R.id.btnMain);

        btnPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Clicked!", Toast.LENGTH_SHORT).show();

                tvName1.setText("TEST");
                tvName2.setText("TEST again");

                //final ApiInterface apiService = DFClient.getClient().create(ApiInterface.class);
                final ApiInterface apiService = DreamFactoryAPI.getInstance().getService(ApiInterface.class);

                Call<ResponseDF> call = apiService.getDataDF();//, sessionToken);
                call.enqueue(new Callback<ResponseDF>() {
                    @Override
                    public void onResponse(Call<ResponseDF>call, Response<ResponseDF> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Response Received", Toast.LENGTH_SHORT).show();
                            List<ResourceI> names = response.body().getResource();
                            String name1 = names.get(0).getName();
                            String name2 = names.get(1).getName();

                            Log.d(TAG,"NAME1: "+ name1);
                            Log.d(TAG,"NAME2: "+ name2);

                            tvName1.setText(name1);
                            tvName2.setText(name2);
                        } else {
                            Toast.makeText(getApplicationContext(), "Response Failed", Toast.LENGTH_SHORT).show();
                            Log.d(TAG,"Response Failed with Code- " + response.code());
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseDF> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Response Failure", Toast.LENGTH_SHORT).show();
                        // Log error here since request failed
                        Log.e(TAG, t.toString());
                        Log.d(TAG,"Response Failure");
                    }
                });
            }
        });
    }

}

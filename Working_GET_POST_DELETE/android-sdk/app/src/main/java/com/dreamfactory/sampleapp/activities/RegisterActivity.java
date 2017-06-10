package com.dreamfactory.sampleapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dreamfactory.sampleapp.R;
import com.dreamfactory.sampleapp.api.DreamFactoryAPI;
import com.dreamfactory.sampleapp.api.services.ApiInterface;
import com.dreamfactory.sampleapp.models.ResourceI;
import com.dreamfactory.sampleapp.models.ResponseDF;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by subir on 3/4/17.
 */

public class RegisterActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText etDevHan = (EditText)findViewById(R.id.etDevHan);
        final EditText etDevTyp = (EditText)findViewById(R.id.etDevTyp);
        Button btnRegister = (Button)findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ApiInterface apiServicePost = DreamFactoryAPI.getInstance().getService(ApiInterface.class);

                ResourceI datum = new ResourceI();;
                String valHan = etDevHan.getText().toString();
                String valDevTyp = etDevTyp.getText().toString();

                datum.setName(valHan);
                datum.setType(valDevTyp);
                //datum.setStatus(true);

                List<ResourceI> datalst = new ArrayList<ResourceI>(1);
                datalst.add(0, datum);

                ResponseDF toPost = new ResponseDF();
                toPost.setResource(datalst);

                Call<ResponseDF> call = apiServicePost.postDataDF(toPost);
                call.enqueue(new Callback<ResponseDF>() {
                    @Override
                    public void onResponse(Call<ResponseDF> call, Response<ResponseDF> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "POSTED", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "ERROR: " + response.code(), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Failed with code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseDF> call, Throwable t) {
                        //Toast.makeText(getApplicationContext(),"POST FAILED", Toast.LENGTH_SHORT).show();
                    }
                });
                showIdentityActivity();
            }
        });
    }//onCreate ends

    private void showIdentityActivity(){
        final Intent intent = new Intent(this, IdentityActivity.class);
        startActivity(intent);
    }







}//class ends

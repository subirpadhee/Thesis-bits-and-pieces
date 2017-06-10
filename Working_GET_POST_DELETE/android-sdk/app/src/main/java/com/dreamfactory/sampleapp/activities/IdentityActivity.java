package com.dreamfactory.sampleapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dreamfactory.sampleapp.R;
import com.dreamfactory.sampleapp.api.DreamFactoryAPI;
import com.dreamfactory.sampleapp.api.services.ApiInterface;
import com.dreamfactory.sampleapp.models.ResourceI;
import com.dreamfactory.sampleapp.models.ResponseDF;
import com.dreamfactory.sampleapp.models.ResponseDelete;

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

        //final TextView tvName1 = (TextView)findViewById(R.id.tvName1);
        //final TextView tvName2 = (TextView)findViewById(R.id.tvName2);

        Button btnPress = (Button)findViewById(R.id.btnGet);
        Button btnPost = (Button)findViewById(R.id.btnPost);
        final EditText etID = (EditText)findViewById(R.id.etID);
        Button btnDel = (Button)findViewById(R.id.btnDel);
        final ListView lvGet = (ListView)findViewById(R.id.lvGet);


        btnPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //Toast.makeText(getApplicationContext(), "Clicked!", Toast.LENGTH_SHORT).show();

                //final ApiInterface apiService = DFClient.getClient().create(ApiInterface.class);
                final ApiInterface apiService = DreamFactoryAPI.getInstance().getService(ApiInterface.class);

                Call<ResponseDF> call = apiService.getDataDF();//, sessionToken);
                call.enqueue(new Callback<ResponseDF>() {
                    @Override
                    public void onResponse(Call<ResponseDF>call, Response<ResponseDF> response) {
                        if (response.isSuccessful()) {
                            //Toast.makeText(getApplicationContext(), "Response Received", Toast.LENGTH_SHORT).show();
                            final List<ResourceI> devices = response.body().getResource();
                            String[] arrayResp = new String[devices.size()];

                            for(int i=0; i<devices.size();i++){
                                arrayResp[i] = devices.get(i).getName() + "(" + devices.get(i).getType() + ")";
                            }

                            ArrayAdapter adapterArray = new ArrayAdapter(IdentityActivity.this, android.R.layout.simple_list_item_1, arrayResp);
                            lvGet.setAdapter(adapterArray);

                            lvGet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String name = devices.get(i).getName();
                                    String type = devices.get(i).getType();
                                    Toast.makeText(getApplicationContext(), ""+ name + "-" + type ,Toast.LENGTH_LONG).show();
                                }
                            });

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

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ApiInterface apiServiceDelete = DreamFactoryAPI.getInstance().getService(ApiInterface.class);
                int ID = Integer.parseInt(etID.getText().toString());

                Call<ResponseDelete> call = apiServiceDelete.deleteRecord(ID);
                call.enqueue(new Callback<ResponseDelete>() {
                    @Override
                    public void onResponse(Call<ResponseDelete> call, Response<ResponseDelete> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "DELETED", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "ERROR: " + response.code(), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Failed with code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseDelete> call, Throwable t) {
                        //Toast.makeText(getApplicationContext(),"POST FAILED", Toast.LENGTH_SHORT).show();
                    }
                });


            }//onClick ends here
        });//btnDel ends here

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {/*
                final ApiInterface apiServicePost = DreamFactoryAPI.getInstance().getService(ApiInterface.class);

                String val = etName.getText().toString();
                etName.getText().clear();


                ResourceI datum = new ResourceI();
                //datum.setId(3);
                datum.setName(val);
                datum.setType("Default");

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

                */


                showRegisterActivity();
            }
        });
    }

    private void showRegisterActivity(){
        final Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}

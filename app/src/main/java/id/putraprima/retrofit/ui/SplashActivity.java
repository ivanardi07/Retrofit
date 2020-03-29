package id.putraprima.retrofit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.AppVersion;
import id.putraprima.retrofit.api.models.Session;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    TextView lblAppName, lblAppTittle, lblAppVersion;
    private Session session;
    private View view;

    public Session getSession(){
        return session;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        view = findViewById(android.R.id.content).getRootView();
        session = new Session(this);
        session = getSession();
        setupLayout();
        if (checkInternetConnection()) {
            checkAppVersion();
        }
        setAppInfo();
    }

    private void setupLayout() {
        lblAppName = findViewById(R.id.lblAppName);
        lblAppTittle = findViewById(R.id.lblAppTittle);
        lblAppVersion = findViewById(R.id.lblAppVersion);
        //Sembunyikan lblAppName dan lblAppVersion pada saat awal dibuka
        lblAppVersion.setVisibility(View.INVISIBLE);
        lblAppName.setVisibility(View.INVISIBLE);
    }

    private boolean checkInternetConnection() {
        //TODO : 1. Implementasikan proses pengecekan koneksi internet, berikan informasi ke user jika tidak terdapat koneksi internet
        boolean connectStatus;
        ConnectivityManager ConnectionManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()==true){
            connectStatus = true;
        } else {
            connectStatus = false;
            setResponse(view, "Tidak ada koneksi Internet");
        }
        return connectStatus;
//        return true;
    }


    private void setAppInfo() {
        //TODO : 5. Implementasikan proses setting app info, app info pada fungsi ini diambil dari shared preferences
        //lblAppVersion dan lblAppName dimunculkan kembali dengan data dari shared preferences
        //lblAppVersion.setVisibility(View.INVISIBLE);
        //lblAppName.setVisibility(View.INVISIBLE);
        lblAppVersion.setVisibility(View.VISIBLE);
        lblAppName.setVisibility(View.VISIBLE);

        lblAppName.setText(session.getApp());
        lblAppVersion.setText(session.getVersion());
    }

    private void checkAppVersion() {
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<AppVersion> call = service.getAppVersion();
        call.enqueue(new Callback<AppVersion>() {
            @Override
            public void onResponse(Call<AppVersion> call, Response<AppVersion> response) {
                Toast.makeText(SplashActivity.this, response.body().getApp(), Toast.LENGTH_SHORT).show();
                //Todo : 2. Implementasikan Proses Simpan Data Yang didapat dari Server ke SharedPreferences
                session.setApp(response.body().getApp());
                session.setVersion(response.body().getVersion());

                //Todo : 3. Implementasikan Proses Pindah Ke MainActivity Jika Proses getAppVersion() sukses
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }

            @Override
            public void onFailure(Call<AppVersion> call, Throwable t) {
                Toast.makeText(SplashActivity.this, "Gagal Koneksi Ke Server", Toast.LENGTH_SHORT).show();
                //Todo : 4. Implementasikan Cara Notifikasi Ke user jika terjadi kegagalan koneksi ke server silahkan googling cara yang lain selain menggunakan TOAST
                setResponse(view, "Koneksi ke Server Gagal");
            }
        });
    }

    public void setResponse(View view, String message){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }
}

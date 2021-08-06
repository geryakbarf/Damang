package unikom.gery.damang.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import unikom.gery.damang.R;
import unikom.gery.damang.adapter.BeritaAdapter;
import unikom.gery.damang.api.Api;
import unikom.gery.damang.api.BaseApi;
import unikom.gery.damang.response.Article;
import unikom.gery.damang.response.News;

public class ArticleActivity extends AppCompatActivity {

    private ImageView btnBack;
    private RecyclerView rvBerita;
    private ArrayList<Article> list;
    private BeritaAdapter beritaAdapter;
    private ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        //Change statusbar color
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        //
        setContentView(R.layout.activity_article);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Sedang memuat...");
        progressDialog.setCancelable(false);
        rvBerita = findViewById(R.id.rvBerita);
        btnBack = findViewById(R.id.btnBack);
        rvBerita.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvBerita.setHasFixedSize(true);
        loadBerita();

        btnBack.setOnClickListener(view -> {
            finish();
        });
    }

    private void loadBerita() {
        progressDialog.show();
        Api api = BaseApi.getRetrofit("https://newsapi.org/v2/").create(Api.class);
        Call<News> response = api.getArtikelBerita("id", "health", "02c679d51abf4f51b390841dab64b436");
        response.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                ArrayList<Article> articleArrayList = (ArrayList<Article>) response.body().getArticles();
                beritaAdapter = new BeritaAdapter(articleArrayList, getApplicationContext());
                rvBerita.setAdapter(beritaAdapter);
                beritaAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Terjadi kesalahan saat memuat berita", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}
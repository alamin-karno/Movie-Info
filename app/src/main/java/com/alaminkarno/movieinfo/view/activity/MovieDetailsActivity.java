package com.alaminkarno.movieinfo.view.activity;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alaminkarno.movieinfo.R;
import com.alaminkarno.movieinfo.models.MovieModel;
import com.alaminkarno.movieinfo.view.utils.Credential;
import com.bumptech.glide.Glide;

public class MovieDetailsActivity extends AppCompatActivity {

    private ImageView imageViewDetails;
    private TextView titleDetails,overviewDetails,reviewDetails,ratingValueDetails;
    private RatingBar ratingBarDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        initialize();

        getDataFromIntent();
    }

    private void getDataFromIntent() {

        if(getIntent().hasExtra("movie")){
            MovieModel movieModel = getIntent().getParcelableExtra("movie");

            Log.d("tag","Intent: "+movieModel.getId());

            Glide.with(this)
                    .load(Credential.IMAGE_URL+movieModel.getPoster_path())
                    .into(imageViewDetails);

            titleDetails.setText(movieModel.getTitle());
            ratingBarDetails.setRating(movieModel.getVote_average()/2);
            ratingValueDetails.setText(String.valueOf(movieModel.getVote_average()/2));
            overviewDetails.setText(movieModel.getOverview());
            reviewDetails.setText("("+movieModel.getVote_count()+" Reviews)");
        }
    }

    private void initialize() {

        imageViewDetails = findViewById(R.id.imageViewDetails);
        titleDetails = findViewById(R.id.titleDetails);
        overviewDetails = findViewById(R.id.overviewDetails);
        ratingBarDetails = findViewById(R.id.ratingBarDetails);
        reviewDetails = findViewById(R.id.reviewDetails);
        ratingValueDetails = findViewById(R.id.ratingValueDetails);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            overviewDetails.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }
    }
}
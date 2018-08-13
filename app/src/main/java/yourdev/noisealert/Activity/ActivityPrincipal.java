package yourdev.noisealert.Activity;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import yourdev.noisealert.Class.CarouselItens;
import yourdev.noisealert.R;

import in.goodiebag.carouselpicker.CarouselPicker;


public class ActivityPrincipal extends AppCompatActivity {

    CarouselPicker carouselPicker1;

    private ImageView progressBar;
    ClipDrawable clipDrawable;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_nova);

        initializeUi();

        CarouselPicker.CarouselViewAdapter textAdapter = new CarouselItens().createCarousel(getApplicationContext());

        carouselPicker1.setAdapter(textAdapter);
        //textAdapter
        setLevel(6000);

    }

    private void initializeUi(){

        progressBar = findViewById(R.id.activity_principal_seek_bar_on);
        clipDrawable = (ClipDrawable) progressBar.getDrawable();
        carouselPicker1 = (CarouselPicker)findViewById(R.id.carouselPicker1);

    }

    private void setLevel(int level){
        clipDrawable.setLevel(level);
    }


    private int getLevel(){
        return clipDrawable.getLevel();
    }


}
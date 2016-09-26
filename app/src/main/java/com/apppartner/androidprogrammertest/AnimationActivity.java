package com.apppartner.androidprogrammertest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class AnimationActivity extends ActionBarActivity implements View.OnTouchListener
{
    ImageView anim_Icon;
    float dX, originalX;
    float dY, originalY;
    int lastAction;
    Animation anim_rotate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        anim_Icon = (ImageView) findViewById(R.id.app_partner_icon);

        anim_rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Animation");

        anim_Icon.setOnTouchListener(this);

        originalX= anim_Icon.getX();
        originalY= anim_Icon.getY();

    }

    public void startFading(View view){

        Animation StoryAnimation = AnimationUtils.loadAnimation(this, R.anim.fade);
        anim_Icon.startAnimation(StoryAnimation);

    }

    public void positionReset(View view){
        anim_Icon.setX(originalX);
        anim_Icon.setY(originalY);
    }



    @Override
    public boolean onTouch(View view, MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                lastAction = MotionEvent.ACTION_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:
                view.setY(event.getRawY() + dY);
                view.setX(event.getRawX() + dX);
                lastAction = MotionEvent.ACTION_MOVE;
                break;

            case MotionEvent.ACTION_UP:
                if (lastAction == MotionEvent.ACTION_DOWN)
                        anim_Icon.startAnimation(anim_rotate);
                break;

            default:
                return false;
        }
        return true;
    }



    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:

                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

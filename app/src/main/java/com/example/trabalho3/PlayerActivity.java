package com.example.trabalho3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    Button btnPlay, btnNext, btnPrev, btnFforward, btnFrewind;
    TextView txtNomeMusica, txtSstart, txtSstop;
    SeekBar seekMusica;
    ImageView imageView;

    String sNome;
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> minhasMusicas;
    Thread updateSeekBar;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        getSupportActionBar().setTitle("Tocando Agora");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        btnPlay = findViewById(R.id.btnPlay);
        btnFforward = findViewById(R.id.btnFforward);
        btnFrewind = findViewById(R.id.btnFrewind);
        txtNomeMusica = findViewById(R.id.txtNomeMusica);
        txtSstart = findViewById(R.id.txtsStart);
        txtSstop = findViewById(R.id.txtSstop);
        seekMusica = findViewById(R.id.seekBar);
        imageView = findViewById(R.id.imageView);

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        minhasMusicas = (ArrayList) bundle.getParcelableArrayList("musicas");
        position = bundle.getInt("pos", 0);
        txtNomeMusica.setSelected(true);
        Uri uri = Uri.parse(minhasMusicas.get(position).toString());
        sNome = minhasMusicas.get(position).getName();
        txtNomeMusica.setText(sNome);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();

        updateSeekBar = new Thread() {
            @Override
            public void run() {
                int duracaoTotal = mediaPlayer.getDuration();
                int posicaoAtual = 0;
                while (posicaoAtual < duracaoTotal) {
                    try {
                        sleep(500);
                        posicaoAtual = mediaPlayer.getCurrentPosition();
                        seekMusica.setProgress(posicaoAtual);
                    }catch (InterruptedException | IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        seekMusica.setMax(mediaPlayer.getDuration());
        updateSeekBar.start();
        seekMusica.getProgressDrawable().setColorFilter(getResources().getColor(R.color.corPrimaria), PorterDuff.Mode.MULTIPLY);
        seekMusica.getThumb().setColorFilter(getResources().getColor(R.color.corPrimaria), PorterDuff.Mode.SRC_IN);

        seekMusica.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        String tempoFim = criarTempo(mediaPlayer.getDuration());
        txtSstop.setText(tempoFim);

        final Handler handler = new Handler();
        final int delay = 1000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String tempoAtual = criarTempo(mediaPlayer.getCurrentPosition());
                txtSstart.setText(tempoAtual);
                handler.postDelayed(this, delay);
            }
        }, delay);

        btnPlay.setOnClickListener(view -> {
            if (mediaPlayer.isPlaying()) {
                btnPlay.setBackgroundResource(R.drawable.ic_play);
                mediaPlayer.pause();
            }else {
                btnPlay.setBackgroundResource(R.drawable.ic_pause);
                mediaPlayer.start();
            }
        });

        mediaPlayer.setOnCompletionListener(mediaPlayer -> btnNext.performClick());

        btnNext.setOnClickListener(view -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position+1)%minhasMusicas.size());
            Uri u = Uri.parse(minhasMusicas.get(position).toString());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
            sNome = minhasMusicas.get(position).getName();
            txtNomeMusica.setText(sNome);
            String tempoFim2 = criarTempo(mediaPlayer.getDuration());
            txtSstop.setText(tempoFim2);
            mediaPlayer.start();
            btnPlay.setBackgroundResource(R.drawable.ic_pause);
            startAnimacao(imageView);
        });

        btnPrev.setOnClickListener(view -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position-1) < 0)?(minhasMusicas.size()-1):(position-1);
            Uri u = Uri.parse(minhasMusicas.get(position).toString());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
            sNome = minhasMusicas.get(position).getName();
            txtNomeMusica.setText(sNome);
            String tempoFim3 = criarTempo(mediaPlayer.getDuration());
            txtSstop.setText(tempoFim3);
            mediaPlayer.start();
            btnPlay.setBackgroundResource(R.drawable.ic_pause);
            startAnimacao(imageView);
        });

        btnFforward.setOnClickListener(view -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+20000);
            }
        });

        btnFrewind.setOnClickListener(view -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-20000);
            }
        });
    }

    public void startAnimacao (View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        animator.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator);
        animatorSet.start();
    }

    public String criarTempo(int duracao) {
        String tempo = "";
        int min = duracao/1000/60;
        int sec = duracao/1000%60;

        tempo += min +":";

        if (sec < 10){
            tempo += "0";
        }

        tempo += sec;
        return tempo;
    }
}
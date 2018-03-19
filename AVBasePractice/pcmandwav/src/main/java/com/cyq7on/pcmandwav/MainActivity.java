package com.cyq7on.pcmandwav;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static android.media.AudioTrack.MODE_STREAM;

public class MainActivity extends AppCompatActivity {
    private File audioFile;
    private boolean isRecord = true;
    private AudioTrack player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AndPermission.with(this)
                .permission(Permission.RECORD_AUDIO, Permission.READ_EXTERNAL_STORAGE)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {

                    }
                }).onDenied(new Action() {
            @Override
            public void onAction(List<String> permissions) {
                // TODO what to do
                toast(R.string.msg_deny);
            }
        }).start();

    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnStart:
                toast("开始录音");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        startRecord();
                    }
                }).start();
                break;
            case R.id.btnEnd:
                stopRecord();
                break;
            case R.id.btnPlay:
                playPCM();
                break;
            case R.id.btnStopPlay:
                stopPlay();
                break;
            default:
                break;
        }
    }

    private void startRecord() {
        audioFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/test.pcm");
        if (audioFile.exists()) {
            audioFile.delete();
        }
        try {
            audioFile.createNewFile();
            OutputStream os = new FileOutputStream(audioFile);
            int sampleRateInHz = 44100;
            int channelConfig = AudioFormat.CHANNEL_IN_MONO;
            int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
            int minBufferSize = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
            AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRateInHz,
                    channelConfig, audioFormat, minBufferSize);
            audioRecord.startRecording();
            byte bytes[] = new byte[minBufferSize];
            while (isRecord) {
                int length = audioRecord.read(bytes, 0, minBufferSize);
                if(length > 0){
                    os.write(bytes);
                }
            }
            if (os != null) {
                os.close();
            }
            audioRecord.stop();
            audioRecord.release();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecord() {
        isRecord = false;
        toast("停止录音");
    }

    private void playPCM() {
        audioFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/test.pcm");
        if (audioFile.exists()) {
            int length = (int) audioFile.length();
            byte bytes[] = new byte[length];
            int sampleRateInHz = 44100;
            if (Build.VERSION.SDK_INT >= 23) {
                player = new AudioTrack.Builder().
                        setAudioFormat(new AudioFormat.Builder()
                                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                                .setSampleRate(sampleRateInHz)
                                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                                .build())
                        .setBufferSizeInBytes(length)
                        .build();
            } else {
                if(player == null){
                    player = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRateInHz,
                            AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                            length, MODE_STREAM);
                }
            }
            try {
                InputStream is = new FileInputStream(audioFile);
/*//                BufferedInputStream bis = new BufferedInputStream(is);
                DataInputStream dis = new DataInputStream(is);
                int i = 0;
                while (dis.available() > 0) {
                    bytes[i] = dis.readByte();
                    i++;
                }
                dis.close();*/

                int read;
                while ((read = is.read(bytes)) > 0) {
                    player.write(bytes, 0, read);
                }
                is.close();

                player.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            toast("音频文件不存在");
        }

    }

    private void stopPlay() {
        if(player != null){
            player.stop();
        }
    }


    protected void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void toast(@StringRes int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

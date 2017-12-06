package cn.yokey.nsg;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import java.io.IOException;
import java.util.Vector;
import cn.yokey.util.ActivityUtil;
import cn.yokey.util.TextUtil;
import cn.yokey.zxing.camera.CameraManager;
import cn.yokey.zxing.decoding.CaptureActivityHandler;
import cn.yokey.zxing.decoding.InactivityTimer;
import cn.yokey.zxing.view.ViewfinderView;

public class ScanActivity extends Activity implements SurfaceHolder.Callback {

    private static final float BEEP_VOLUME = 0.10f;
    private static final long VIBRATE_DURATION = 200L;

    public static Activity mActivity;

    private TextView titleTextView;
    private ImageView leftImageView;
    private ViewfinderView viewfinderView;

    private String charString;
    private boolean vibrateBoolean;
    private boolean playBeepBoolean;
    private MediaPlayer mMediaPlayer;
    private boolean hasSurfaceBoolean;
    private CaptureActivityHandler mHandler;
    private InactivityTimer mInactivityTimer;
    private Vector<BarcodeFormat> formatsVector;

    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    private void createControl() {

        mActivity = this;

        //实例化
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        leftImageView = (ImageView) findViewById(R.id.leftImageView);

        //赋值
        titleTextView.setText("扫一扫");

        hasSurfaceBoolean = false;
        mInactivityTimer = new InactivityTimer(mActivity);
        viewfinderView = (ViewfinderView) findViewById(R.id.mainViewfinderView);

        //子程序
        CameraManager.init(mActivity);

    }

    private void createEvent() {

        leftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.finish(mActivity);
            }
        });

        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void initBeepSound() {
        if (playBeepBoolean && mMediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mMediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mMediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mMediaPlayer.prepare();
            } catch (IOException e) {
                mMediaPlayer = null;
            }
        }
    }

    public void handleDecode(Result res) {
        mInactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String result = res.getText();
        if (!TextUtil.isEmpty(result)) {
            //网址
            if (TextUtil.isUrlAddress(result)) {
                if (result.contains("goods_id")) {
                    String id = result.substring(result.lastIndexOf("=") + 1, result.length());
                    Intent intent = new Intent(mActivity, GoodsDetailActivity.class);
                    intent.putExtra("id", id);
                    ActivityUtil.start(mActivity, intent);
                }
            }
        }

    }

    private void playBeepSoundAndVibrate() {
        if (playBeepBoolean && mMediaPlayer != null) {
            mMediaPlayer.start();
        }
        if (vibrateBoolean) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    public Handler getHandler() {
        return mHandler;
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return;
        }
        if (mHandler == null) {
            mHandler = new CaptureActivityHandler(this, formatsVector, charString);
        }
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mHandler != null) {
            mHandler.quitSynchronously();
            mHandler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.mainSurfaceView);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurfaceBoolean) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        formatsVector = null;
        charString = null;

        playBeepBoolean = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeepBoolean = false;
        }
        initBeepSound();
        vibrateBoolean = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        onCreate(new Bundle());
    }

    @Override
    protected void onDestroy() {
        mInactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        createControl();
        createEvent();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurfaceBoolean) {
            hasSurfaceBoolean = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurfaceBoolean = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

}
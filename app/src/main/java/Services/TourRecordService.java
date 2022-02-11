package Services;

import android.app.Service;
import android.content.Intent;

import androidx.annotation.Nullable;

import android.os.Handler;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

public class TourRecordService extends Service {

    public static final String TIMER_UPDATED = "timerUpdated";
    public static final String TIME_EXTRA = "timeExtra";

    Handler handler = new Handler();

    private Timer timer = new Timer();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        double time = intent.getDoubleExtra(TIME_EXTRA, 0.0);
        timer.scheduleAtFixedRate(new TimeTask(time), 0, 1000);
        //return super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    class TimeTask extends TimerTask{
        private double time;

        public TimeTask(double time){
           this.time = time;
        }

        @Override
        public void run(){
            Intent intent = new Intent(TIMER_UPDATED);
            time++;
            intent.putExtra(TIME_EXTRA, time);
            sendBroadcast(intent);
        }
    }
}

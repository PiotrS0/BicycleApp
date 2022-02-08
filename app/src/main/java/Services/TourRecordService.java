package Services;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import android.os.Handler;

public class TourRecordService extends IntentService {
    /**
     * @param name
     * @deprecated
     */

    Handler handler = new Handler();

    public TourRecordService() {

        super("RecordService");
    }

    public TourRecordService(String name) {

        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {


    }
}

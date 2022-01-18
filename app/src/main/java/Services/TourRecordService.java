package Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.IntentSender;

import androidx.annotation.Nullable;

public class TourRecordService extends IntentService {
    /**
     * @param name
     * @deprecated
     */
    public TourRecordService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}

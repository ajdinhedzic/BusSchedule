package ajdi.hedzic.com.busschedule;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class MainActivity extends ActionBarActivity {

    private LinearLayout hawkeyeParkSchedule;
    private LinearLayout mercySchedule;
    private LinearLayout grandSchedule;
    private LinearLayout locustSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hawkeyeParkSchedule = (LinearLayout) findViewById(R.id.hawkeyeParkColumn);
        mercySchedule = (LinearLayout) findViewById(R.id.mercyColumn);
        grandSchedule = (LinearLayout) findViewById(R.id.grandColumn);
        locustSchedule = (LinearLayout) findViewById(R.id.locustColumn);
        new FetchSchedule().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Document downloadUrl(){
        Document htmlPage = null;
        try {
            htmlPage = Jsoup.connect("https://www.ridedart.com/routes/express/98-ankeny").get();
        } catch (Exception e) {
            Log.d("ERROR", "Exception thrown while retrieving schedule");
        }
        return htmlPage;
    }

    private class FetchSchedule extends AsyncTask<Void, Void, Document> {

        @Override
        protected Document doInBackground(Void... params) {
            return downloadUrl();
        }

        @Override
        protected void onPostExecute(Document doc) {
            Elements hawkeyeParkTimes = doc.getElementsByAttributeValue("data-stop-name", "Hawkeye Park & Ride");
            createViewWithTimes(hawkeyeParkTimes, hawkeyeParkSchedule);

            Elements mercyTimes = doc.getElementsByAttributeValue("data-stop-name", "Mercy North Park & Ride");
            createViewWithTimes(mercyTimes, mercySchedule);

            Elements grandTimes = doc.getElementsByAttributeValue("data-stop-name", "Grand ave & 7th St");
            createViewWithTimes(grandTimes, grandSchedule);

            Elements locustTimes = doc.getElementsByAttributeValue("data-stop-name", "Locust ave & 7th St");
            createViewWithTimes(locustTimes, locustSchedule);
        }

        private void createViewWithTimes(Elements stopTimes, LinearLayout stopToAddTimes) {
            for (Element time : stopTimes) {
                TextView textView = new TextView(getApplicationContext());
                textView.setText(time.text());
                stopToAddTimes.addView(textView);
            }
        }
    }

}

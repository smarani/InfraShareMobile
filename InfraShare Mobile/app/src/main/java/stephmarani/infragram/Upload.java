package stephmarani.infragram;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by stephmarani on 3/15/16.
 */
public class Upload extends Activity {
    private static String filePathOriginal;
    private static String filePathInfragram;
    private static String urlString;
    private static String DateTaken;
    private static String LocationDescription;
    private static String PlantDescription;
    private static String GeneralNotes;
    private static String Latitude;
    private static String Longitude;
    private static String NdviScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);
        filePathOriginal = getIntent().getExtras().getString("FilePathOriginal");
        filePathInfragram = getIntent().getExtras().getString("FilePathInfragram");
        urlString = "http://infrashare-mobile.herokuapp.com/Map/upload/";

        DateTaken = getIntent().getExtras().getString("DateTaken");
        Latitude = getIntent().getExtras().getString("Latitude");
        Longitude = getIntent().getExtras().getString("Longitude");
        NdviScore = getIntent().getExtras().getString("NdviScore");

        try {
            Log.i("Infragram", "MediaScannerConnection#scanFile");
            MediaScannerConnection.scanFile(this, new String[]{filePathInfragram}, null, null);
        } catch (final Exception e) {
            Log.e("Infragram", "handleUpdateMedia:", e);
        }

        Button buttonUpload = (Button) findViewById(R.id.UploadButtonFinal);
        buttonUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                TextView location = (TextView) findViewById(R.id.PlantLocation);
                LocationDescription = location.getText().toString();
                TextView plant = (TextView) findViewById(R.id.PlantDescription);
                PlantDescription = plant.getText().toString();
                TextView general = (TextView) findViewById(R.id.AdditionalNotes);
                GeneralNotes = general.getText().toString();


                try{
                    new MultipartUtility().execute(filePathInfragram, filePathOriginal, DateTaken, LocationDescription, PlantDescription,
                                  GeneralNotes, Latitude, Longitude, NdviScore);
                }
                catch (Exception e){

                }
                Intent in1 = new Intent(arg0.getContext(), ShowMap.class);
                startActivity(in1);
            }
        });

    }

}



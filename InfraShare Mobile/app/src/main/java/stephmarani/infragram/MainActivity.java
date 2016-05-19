package stephmarani.infragram;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends Activity {


    private static Bitmap original_image;
    private static Bitmap infragramImage;
    private static double score;
    private static ImageView imageView;
    private static TextView textView;
    private static Button buttonLoadImage;
    private static Button buttonInfragram;
    private static Button buttonUpload;
    private ProgressBar progressBar1;
    private static String filePathOriginal = "";
    private static String filePathInfragram = "";
    private static String latitude = "";
    private static String longitude = "";
    private static String dateTaken = "";
    private static String ndviScore = "";
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        byte[] byteArray = getIntent().getByteArrayExtra("OriginalImage");
        filePathOriginal = getIntent().getExtras().getString("FilePath");
        latitude = getIntent().getExtras().getString("Latitude");
        longitude = getIntent().getExtras().getString("Longitude");
        context = MainActivity.this;
        try{
            ExifInterface ef = new ExifInterface(filePathOriginal);

            dateTaken = ef.getAttribute(ef.TAG_DATETIME);

        }
        catch(IOException e){

        }
        original_image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        new Infragram.BackgroundInfragram().execute(original_image);
        imageView = (ImageView) findViewById(R.id.imgView);
        textView = (TextView) findViewById(R.id.scoreHolder);
        buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);
        buttonInfragram = (Button) findViewById(R.id.buttonReset);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        buttonUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent in1 = new Intent(MainActivity.this, Upload.class);
                in1.putExtra("FilePathOriginal", filePathOriginal);
                in1.putExtra("FilePathInfragram", filePathInfragram);
                in1.putExtra("Latitude", latitude);
                in1.putExtra("Longitude", longitude);
                in1.putExtra("DateTaken", dateTaken);
                in1.putExtra("NdviScore", ndviScore);
                startActivity(in1);

            }
        });

    }

    public void setProgressButton(int progress){
        progressBar1.setProgress(progress);
    }



    public void setPicture(Infragram.InfragramResult result){
        infragramImage = result.modifiedBitmap;
        new UploadNewBitmap().execute(infragramImage);
        score = result.score;
        //ndviScore = Double.toString(score);
        ndviScore = String.format("%.5g%n", score);

        imageView.setImageBitmap(infragramImage);


        textView.setText(ndviScore);

        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                imageView.setImageBitmap(infragramImage);
            }
        });

        buttonInfragram.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                imageView.setImageBitmap(original_image);
            }
        });

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

    private static final File getCaptureFile(final String type, final String ext, final String name) {
        final File dir = new File(Environment.getExternalStoragePublicDirectory(type), "InfraShareMobile");
        Log.d("Infragram", "path=" + dir.toString());
        dir.mkdirs();
        if (dir.canWrite()) {
            return new File(dir, name + "_modified" + ext);
        }
        return null;
    }

    private static class UploadNewBitmap extends AsyncTask<Bitmap, Void, Void> {
        protected Void doInBackground(Bitmap... original) {
            FileOutputStream out = null;
            try {
                String justName = filePathOriginal.substring(filePathOriginal.lastIndexOf("/")+1, filePathOriginal.indexOf("."));
                File newFile = getCaptureFile(Environment.DIRECTORY_DCIM, ".jpg", justName);
                filePathInfragram = newFile.getPath();
                out = new FileOutputStream(newFile);
                original[0].compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored
                Log.d("Infragram", "saving is successful, file name is:" + newFile.getPath());

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        protected void onProgressUpdate() {

        }

        protected void onPostExecute(Void v) {
            Log.d("Infragram", "saving is successful and over, file name is:" + filePathInfragram);

        }
    }

}



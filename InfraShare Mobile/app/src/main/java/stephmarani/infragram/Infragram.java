package stephmarani.infragram;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;

/**
 * Created by stephmarani on 2/14/16.
 */
public class Infragram  {
    private static double averageScore;
    private static int n;

    public static InfragramResult infragram(Bitmap original){

        return new InfragramResult(original, ndvi(original), getScore());

    }

    public static class BackgroundInfragram extends AsyncTask<Bitmap, Integer, InfragramResult> {
        protected InfragramResult doInBackground(Bitmap... original) {
            return infragram(original[0]);
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(InfragramResult result) {
            MainActivity setPic = new MainActivity();
            setPic.setPicture(result);
        }
    }


    private static double getScore(){
        return averageScore /n;
    }

    public static class InfragramResult {
        public final Bitmap originalBitmap;
        public final Bitmap modifiedBitmap;
        public final double score;


        public InfragramResult(Bitmap originalBitmap, Bitmap modifiedBitmap, double score) {
            this.originalBitmap = originalBitmap;
            this.modifiedBitmap = modifiedBitmap;
            this.score = score;
        }
    }

    //Calculate the ndvi score of each of the pixels and set the new colors
    private static Bitmap ndvi(Bitmap original){
        averageScore = 0.0;
        n = original.getWidth() * original.getHeight();
        int img_color;
        double color_value;
        double[] ref;
        int r,g,b,c;

        Bitmap img = original.copy(Bitmap.Config.ARGB_8888, true);

        for (int i = 0; i < original.getWidth(); i++){
            for (int j = 0; j < original.getHeight(); j++){
                img_color = original.getPixel(i, j);
                double divBottom = (Color.red(img_color) + Color.blue(img_color));
                if (divBottom != 0) {
                    color_value = (float) (Color.red(img_color) - Color.blue(img_color)) / (Color.red(img_color) + Color.blue(img_color));
                }
                else {
                    color_value = (float) 0;
                }

                if (color_value >= -1 && color_value <= 1) {
                    averageScore =  averageScore + color_value;
                }

                ref = colormap(normalize(color_value, -1, 1));
                r = (int)ref[0];
                g = (int)ref[1];
                b = (int)ref[2];
                c = Color.rgb(r,g,b);

                img.setPixel(i, j, c);
            }
        }
        return img;
    }

    //return the new pixel color based on the ndvi score
    private static double[] colormap(double x){
        double segment0[] = {0,0.5, 0.75};
        double[][] segment1 = {{0,0,255},{0,150,0},{255,255,0}};
        double[][] segment2 = {{38,195,195},{255,255,0},{255,50,50}};

        double[] y0 = {0.0, 0.0, 0.0};
        double[] y1 = {0.0, 0.0, 0.0};
        double x0 = segment0[0];
        double x1 = 1;

        if (x < x0){
            return y0;
        }

        for (int i = 0; i < segment0.length; i++){
            double xstart = segment0[i];
            y0 = segment1[i];
            y1 = segment2[i];
            x0 = xstart;

            if (i == segment0.length - 1){
                x1 = 1;
                break;
            }
            x1 = segment0[i+1];
            if (xstart <= x && x < x1){
                break;
            }
        }
        double[] result = new double[y0.length];
        for (int i = 0; i < y0.length; i++) {
            result[i] = (x-x0) / (x1-x0) * (y1[i] - y0[i]) + y0[i];
        }

        return result;
    }


    private static double normalize(double num, int min, int max){
        return (num - min) / (max - min);
    }
}

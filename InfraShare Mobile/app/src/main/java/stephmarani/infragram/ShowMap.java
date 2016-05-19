package stephmarani.infragram;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

/**
 * Created by stephmarani on 3/27/16.
 */
public class ShowMap extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_map);

        WebView wb = (WebView) findViewById(R.id.webView1);


        wb.loadUrl("http://infrashare-mobile.herokuapp.com/Map");

        Button buttonLoadImage = (Button) findViewById(R.id.buttonReturn);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent in1 = new Intent(ShowMap.this, HomePage.class);
                startActivity(in1);
            }
        });
    }
}

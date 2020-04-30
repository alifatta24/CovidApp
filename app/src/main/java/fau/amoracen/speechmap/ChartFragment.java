package fau.amoracen.speechmap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;

public class ChartFragment extends Fragment {

    //variables
    WebView webView;
    String webHtml1 = "Chartjs-Cases.html";
    String Country = "World";
    ArrayList<String> datadate = new ArrayList<String>();
    ArrayList<String> datanum = new ArrayList<String>();

    public ChartFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.chart_layout, container, false);

        //call chart
        webView = (WebView) v.findViewById(R.id.webview1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new JavaScriptInterface(getActivity()), "Android");
        webView.loadUrl("file:///android_asset/Chartjs-Cases.html");


        getCovid19Data(Country);

        return v;
    }

    //use JSON to show results
    protected void DisplayCovidResults( JSONObject data ) {
        System.out.println(data);
        try {
            JSONObject CovidCasesTimeline=data.getJSONObject("timeline");
            System.out.println(CovidCasesTimeline);
            JSONObject CovidCases=CovidCasesTimeline.getJSONObject("cases");
            System.out.println(CovidCases);
            Iterator<String> iterator = CovidCases.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = CovidCases.getString(key);
                datadate.add("'"+key+"'");
                datanum.add(value);
            }
            //System.out.println(datadate);
            //System.out.println(datanum);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //use API to call info
    public void getCovid19Data(String Country) {
        String urlQuery;
        String CountryQuery;

        if (Country.isEmpty()) {
            CountryQuery = "World";
        } else {
            CountryQuery = Country;
        }

        final String url_request = "https://corona.lmao.ninja/v2/historical/";
        urlQuery = url_request + CountryQuery;

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        System.out.println(urlQuery);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, urlQuery, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        DisplayCovidResults(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }


}

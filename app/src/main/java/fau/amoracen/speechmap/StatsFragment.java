package fau.amoracen.speechmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StatsFragment extends Fragment {
    //variables
    ProgressBar progressBar;
    CountryAdapter CountryAdapter;
    RecyclerView CountryList;

    private static final String TAG = StatsFragment.class.getSimpleName();
    List<CountryInfo> covidCountries;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.stats_layout, container, false);
        setHasOptionsMenu(true);

        // app layout
        CountryList = root.findViewById(R.id.CountryList);
        progressBar = root.findViewById(R.id.progress_circular_country);
        CountryList.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(CountryList.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.line_divider));
        CountryList.addItemDecoration(dividerItemDecoration);
        covidCountries = new ArrayList<>();
        getDataFromServerSortTotalCases();
        return root;
    }

    private void showRecyclerView() {
        CountryAdapter = new CountryAdapter(covidCountries, getActivity());
        CountryList.setAdapter(CountryAdapter);

        ItemClick.addTo(CountryList).setOnItemClickListener(new ItemClick.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
            }
        });
    }

    //call covid API
    private void getDataFromServerSortTotalCases() {
        String url = "https://corona.lmao.ninja/v2/countries";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                if (response != null) {
                    Log.e(TAG, "onResponse: " + response);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            JSONObject countryInfo = data.getJSONObject("countryInfo");
                            covidCountries.add(new CountryInfo(
                                    data.getString("country"), data.getInt("cases"),
                                    data.getString("todayCases"), data.getString("deaths"),
                                    data.getString("todayDeaths"), data.getString("recovered"),
                                    data.getString("active"), data.getString("critical"),
                                    countryInfo.getString("flag")
                            ));
                        }
                        Collections.sort(covidCountries, new Comparator<CountryInfo>() {
                            @Override
                            public int compare(CountryInfo o1, CountryInfo o2) {
                                if (o1.getmCases() > o2.getmCases()) {
                                    return -1;
                                } else {
                                    return 1;
                                }
                            }
                        });

                        getActivity().setTitle(jsonArray.length() + " countries");
                        showRecyclerView();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Log.e(TAG, "onResponse: " + error);
                    }
                });
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }


}

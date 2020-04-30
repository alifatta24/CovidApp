package fau.amoracen.speechmap;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class GoogleMapFragment extends Fragment  {

    TextView textView;
    TextView textView2;

    public GoogleMapFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.news_layout, container, false);

        textView = v.findViewById(R.id.textViewLink);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        textView = v.findViewById(R.id.textViewLink2);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        return v;
    }

}

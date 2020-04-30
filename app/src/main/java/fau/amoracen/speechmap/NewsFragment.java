package fau.amoracen.speechmap;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NewsFragment extends Fragment  {

    TextView textView;

    public NewsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.news_layout, container, false);

        //links to each url
        textView = v.findViewById(R.id.textViewLink);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        textView = v.findViewById(R.id.textViewLink2);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        textView = v.findViewById(R.id.textViewLink3);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        textView = v.findViewById(R.id.textViewLink4);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        textView = v.findViewById(R.id.textViewLink5);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        textView = v.findViewById(R.id.textViewLink6);
        textView.setMovementMethod(LinkMovementMethod.getInstance());




        return v;
    }

}

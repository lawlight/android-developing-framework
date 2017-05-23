package hz.com.androidlib.index;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import hz.com.androidlib.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndexDemoFragment extends Fragment {

    String text;

    @Bind(R.id.textView)
    TextView textView;

    public IndexDemoFragment() {
        // Required empty public constructor
    }

    public static IndexDemoFragment newInstance(String text){
        IndexDemoFragment fragment = new IndexDemoFragment();
        fragment.text = text;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index, container, false);
        ButterKnife.bind(this, view);
        textView.setText(text);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

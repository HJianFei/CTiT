package com.hjf.ctit.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hjf.ctit.R;
import com.hjf.ctit.entity.Connection;
import com.hjf.ctit.entity.Connections;

public class OtherFragment extends Fragment {

    private ListView lv_msg;
    String clientHandle = null;
    ArrayAdapter<Spanned> arrayAdapter = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
       clientHandle = arguments.getString("clientHandle");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        lv_msg = (ListView) view.findViewById(R.id.lv_msg);
        Connection connection = Connections.getInstance(getActivity()).getConnection(clientHandle);
        Spanned[] history = connection.history();
        arrayAdapter = new ArrayAdapter<Spanned>(getActivity(), R.layout.list_view_text_view);
        arrayAdapter.addAll(history);
        lv_msg.setAdapter(arrayAdapter);
    }

    public void refresh() {
        if (arrayAdapter != null) {
            arrayAdapter.clear();
            arrayAdapter.addAll(Connections.getInstance(getActivity()).getConnection(clientHandle).history());
            arrayAdapter.notifyDataSetChanged();
        }

    }
}

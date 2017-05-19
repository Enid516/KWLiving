package com.enid.kwliving.server.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.enid.kwliving.R;

/**
 * Created by Enid on 2017/5/17.
 */

public class DeskFragment extends Fragment implements View.OnClickListener {

    private TextView tvPoint;
    private Button btnInit;
    private DeskFragmentListener deskFragmentListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_desk, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //init view
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 4));
        tvPoint = (TextView) view.findViewById(R.id.tvPoint);
        btnInit = (Button) view.findViewById(R.id.btnInit);
        btnInit.setOnClickListener(this);

    }

    public void pointMsg(String msg) {
        tvPoint.setText(msg);
    }

    public void setListener(DeskFragmentListener listener) {
        this.deskFragmentListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (null != deskFragmentListener)
            deskFragmentListener.clickInit();
    }

    public interface DeskFragmentListener {
        void clickInit();
    }
}

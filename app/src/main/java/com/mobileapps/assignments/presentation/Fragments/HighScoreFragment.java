package com.mobileapps.assignments.presentation.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobileapps.assignments.R;
import com.mobileapps.assignments.adapters.ScoreRecyclerViewAdapter;
import com.paz.prefy_lib.Prefy;

import java.util.ArrayList;

public class HighScoreFragment extends Fragment {

    public HighScoreFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_high_score, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.high_score_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new ScoreRecyclerViewAdapter(container.getContext(),
                Prefy.getInstance().getArrayList(
                        container.getContext().getString(R.string.scores_key),
                        new ArrayList<>())));

        return view;
    }
}
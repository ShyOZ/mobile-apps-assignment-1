package com.mobileapps.assignments.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobileapps.assignments.callbacks.ScoreCallback;
import com.mobileapps.assignments.data.Score;
import com.mobileapps.assignments.databinding.FragmentHighScoreItemBinding;

import java.util.List;

public class ScoreRecyclerViewAdapter extends RecyclerView.Adapter<ScoreRecyclerViewAdapter.HighScoreViewHolder> {
    private final List<Score> scores;
    private ScoreCallback scoreCallback;

    public ScoreRecyclerViewAdapter(Context context, List<Score> scores) {
        this.scores = scores;
    }

    public ScoreRecyclerViewAdapter setScoreCallback(ScoreCallback scoreCallback) {
        this.scoreCallback = scoreCallback;
        return this;
    }

    @NonNull
    @Override
    public HighScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HighScoreViewHolder(FragmentHighScoreItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final HighScoreViewHolder holder, int position) {
        Log.d("ScoreRecyclerViewAdapter", "onBindViewHolder: " + position);
        Score score = scores.get(position);
        holder.playerNameLabel.setText(score.getPlayerName());
        holder.scoreLabel.setText(String.valueOf(score.getScore()));
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    public class HighScoreViewHolder extends RecyclerView.ViewHolder {
        public final TextView playerNameLabel;
        public final TextView scoreLabel;

        public HighScoreViewHolder(FragmentHighScoreItemBinding binding) {
            super(binding.getRoot());
            playerNameLabel = binding.playerNameLabel;
            scoreLabel = binding.scoreLabel;
        }
    }
}
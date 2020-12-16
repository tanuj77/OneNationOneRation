package org.vsr.onenationoneration.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.vsr.onenationoneration.R;
import org.vsr.onenationoneration.apiCall.bin.RCEMembersDataList;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class RERecViewCustomAdapter extends RecyclerView.Adapter<RERecViewCustomAdapter.MyViewHolder> {

    private List<RCEMembersDataList> moviesList;
    String savedTXNIDFromRegActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMemberCode, tvMemberName, tvRelation, tvUID, tvkycStatus;
        public ImageView ivEye, ivVerified;


        public MyViewHolder(View view) {
            super(view);
            tvMemberCode = view.findViewById(R.id.tv_re_membercode);
            tvMemberName = view.findViewById(R.id.tv_re_membername);
            tvRelation = view.findViewById(R.id.tv_re_relation);
            tvUID = view.findViewById(R.id.tv_re_uid);
            tvkycStatus = view.findViewById(R.id.tv_re_kycstatus);
        }
    }


    public RERecViewCustomAdapter(List<RCEMembersDataList> moviesList, String savedTXNIDFromRegActivity) {
        this.moviesList = moviesList;
        this.savedTXNIDFromRegActivity = savedTXNIDFromRegActivity;
    }


    @Override
    public RERecViewCustomAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rationentitlement_list_row, parent, false);

        return new RERecViewCustomAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RERecViewCustomAdapter.MyViewHolder holder, int position) {
        if (position % 2 == 0) {
            holder.itemView.setBackgroundResource(R.color.White);
        } else {
            holder.itemView.setBackgroundResource(R.color.colorFadedWhite);
        }

        final RCEMembersDataList detailMovie = moviesList.get(position);
        holder.tvMemberCode.setText(detailMovie.getMemberCode());
        holder.tvMemberName.setText(detailMovie.getMemberName());
        holder.tvRelation.setText(detailMovie.getRelation());
        String uidNo = detailMovie.getuIDNo();
        if (uidNo.trim().equalsIgnoreCase("") || uidNo.trim() == "null") {
            holder.tvUID.setText("NA");
        } else {
            holder.tvUID.setText(uidNo);
        }

        holder.tvkycStatus.setText(detailMovie.geteKYCStatus());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
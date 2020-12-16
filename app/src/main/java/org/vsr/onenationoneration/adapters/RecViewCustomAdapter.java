package org.vsr.onenationoneration.adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import org.vsr.onenationoneration.FamilyDetailsActivity;
import org.vsr.onenationoneration.FullDetail;
import org.vsr.onenationoneration.R;
import org.vsr.onenationoneration.apiCall.bin.FamilyDetails_RCDetails_RationCardMembersData;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecViewCustomAdapter extends RecyclerView.Adapter<RecViewCustomAdapter.MyViewHolder> {
    Context context;
    private List<FamilyDetails_RCDetails_RationCardMembersData> moviesList;
    String eyeStatus = "hide";
    String strRationCardNo,savedTXNIDFromRegActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvRelation, tvPHRCNo, tvAadharNo, tvVerifyORNotVerify, tvShowDetails, tvAgeGender, tvHOF, tvHOFN;
        public ImageView ivEye, ivVerified;
        CircleImageView iv_profile;

        public MyViewHolder(View view) {
            super(view);
            iv_profile = view.findViewById(R.id.iv_fd_profile_image);
            tvName = view.findViewById(R.id.tv_name);
            tvRelation = view.findViewById(R.id.tv_relation);
            tvPHRCNo = view.findViewById(R.id.tv_phrcno);
            tvAadharNo = view.findViewById(R.id.tv_aadharno);
            tvVerifyORNotVerify = view.findViewById(R.id.tv_verifyornotverify);
            tvShowDetails = view.findViewById(R.id.tv_showdetails);
            tvHOF = view.findViewById(R.id.tv_hof);
            tvHOFN = view.findViewById(R.id.tv_hofn);
            tvAgeGender = view.findViewById(R.id.tv_ageandgender);
            ivEye = view.findViewById(R.id.iv_eye);
            ivVerified = view.findViewById(R.id.iv_verifiednotverified);

        }
    }


    public RecViewCustomAdapter(Context context, List<FamilyDetails_RCDetails_RationCardMembersData> moviesList,String strRationCardNo,String savedTXNIDFromRegActivity) {
        this.context = context;
        this.moviesList = moviesList;
        this.strRationCardNo=strRationCardNo;
        this.savedTXNIDFromRegActivity=savedTXNIDFromRegActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.familydetail_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (position % 2 == 0) {
            holder.itemView.setBackgroundResource(R.color.colorFadedWhite);
        } else {
            holder.itemView.setBackgroundResource(R.color.White);
        }

        final FamilyDetails_RCDetails_RationCardMembersData detailMovie = moviesList.get(position);

        final String strUserImage = detailMovie.getBase64ImageData();
        if (strUserImage.equalsIgnoreCase("")) {

        } else {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] imageBytes = baos.toByteArray();
            String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            imageBytes = Base64.decode(strUserImage, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.iv_profile.setImageBitmap(decodedImage);
        }
        if (detailMovie.getMember_Name_In_Aadhar_EN()!=""){
            holder.tvName.setText(detailMovie.getMember_Name_In_Aadhar_EN());
        }else{
            holder.tvName.setText(detailMovie.getMember_Name_EN());
        }

        holder.tvRelation.setText(detailMovie.getRelation());

        holder.tvPHRCNo.setText(detailMovie.getMemberId());
        if (detailMovie.getuIDAINo().equalsIgnoreCase("0") || detailMovie.getuIDAINo().equalsIgnoreCase("")) {
            holder.tvAadharNo.setText("NA");
            holder.ivEye.setBackgroundResource(R.drawable.ic_eye_off_24dp);
            holder.ivEye.setVisibility(View.INVISIBLE);
            eyeStatus = "hide";
        } else {
            holder.ivEye.setVisibility(View.VISIBLE);
            holder.ivEye.setBackgroundResource(R.drawable.ic_eye_off_24dp);
            eyeStatus = "hide";
            String strAadhar = detailMovie.getuIDAINo();
            String newster = strAadhar.substring(8, 12);
            holder.tvAadharNo.setText("XXXXXXXX" + newster);

        }

        holder.ivEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eyeStatus.equalsIgnoreCase("hide")) {
                    holder.tvAadharNo.setText(detailMovie.getuIDAINo());
                    holder.ivEye.setBackgroundResource(R.drawable.ic_eye_icon_24dp);
                    eyeStatus = "show";
                } else {
                    String strAadhar = detailMovie.getuIDAINo();
                    String newster = strAadhar.substring(8, 12);
                    holder.tvAadharNo.setText("XXXXXXXX" + newster);
                    holder.ivEye.setBackgroundResource(R.drawable.ic_eye_off_24dp);
                    eyeStatus = "hide";
                }

            }
        });
        holder.tvAgeGender.setText(detailMovie.getGender() + "/" + detailMovie.getAgeInYears());
        if (detailMovie.getIsHOF().equalsIgnoreCase("Y") || detailMovie.getIsHOF().equalsIgnoreCase("y")) {
            holder.tvHOF.setVisibility(View.VISIBLE);
        } else {
            holder.tvHOF.setVisibility(View.INVISIBLE);
        }

        if (detailMovie.getIsNFSA().equalsIgnoreCase("Y") || detailMovie.getIsNFSA().equalsIgnoreCase("y")) {
            holder.tvHOFN.setVisibility(View.VISIBLE);
        } else {
            holder.tvHOFN.setVisibility(View.INVISIBLE);
        }


        if (detailMovie.getIsAadharVerified().equalsIgnoreCase("1") || detailMovie.getIsAadharVerified().equals(1)) {
            holder.tvVerifyORNotVerify.setText("Verified");
            holder.ivVerified.setImageResource(R.drawable.ic_lens_green_24dp);
        } else {
            holder.tvVerifyORNotVerify.setText("Not Verified");
            holder.ivVerified.setImageResource(R.drawable.ic_lens_red_24dp);
        }
        holder.tvShowDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), FullDetail.class);
                Bundle bundle = new Bundle();
                bundle.putString("RationCard", strRationCardNo);
                bundle.putString("MemberID", holder.tvPHRCNo.getText().toString());
                bundle.putString("TXNID", savedTXNIDFromRegActivity);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                context.getApplicationContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
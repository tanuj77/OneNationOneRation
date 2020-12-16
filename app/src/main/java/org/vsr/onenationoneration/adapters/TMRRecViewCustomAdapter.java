package org.vsr.onenationoneration.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.vsr.onenationoneration.GrievanceFullDetails;
import org.vsr.onenationoneration.PdfDocumentAdapter2;
import org.vsr.onenationoneration.R;
import org.vsr.onenationoneration.apiCall.bin.RCGrievanceDetailsIssuesDataList;
import org.vsr.onenationoneration.apiCall.bin.RCGrievanceListDataList;
import org.vsr.onenationoneration.apiCall.bin.TMRReceivedDetails;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;

public class TMRRecViewCustomAdapter extends RecyclerView.Adapter<TMRRecViewCustomAdapter.MyViewHolder> {

    private List<TMRReceivedDetails> moviesList;
    private List<RCGrievanceListDataList> rcGrievanceListDataListList;
    private List<RCGrievanceDetailsIssuesDataList> rcGrievanceDetailsIssuesDataListList;
    Context context;
    String strRationCardNo, savedTXNIDFromRegActivity, flag;
    Bitmap bitmap;
    String destination;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCommodityNametxt, tvExtraAllocationtxt, tvPreviousMonthBalancetxt, tvAllocatedQtytxt, tvTotalQtySendToFPStxt, tvPercQtySendToFPStxt, tvAllocationOrderNoTypetxt, tvText8, tvText9, tvText10, tvText11, tvText12, tvText13, tvText14, tvText15, tvText16, tvText17, tvText18, tvText19, tvText20, tvText21, tvText22;
        public TextView tvCommodityNamedots, tvExtraAllocationdots, tvPreviousMonthBalancedots, tvAllocatedQtydots, tvTotalQtySendToFPSdots, tvPercQtySendToFPSdots, tvAllocationOrderNoTypedots;
        public TextView tvCommodityCode, tvCommodityName, tvUnit, tvExtraAllocation, tvPreviousMonthBalance, tvAllocatedQty, tvTotalQtySendToFPS, tvPercQtySendToFPS, tvAllocationOrderNoType, tvValue8, tvValue9, tvValue10, tvValue11, tvValue12, tvValue13, tvValue14, tvValue15, tvValue16, tvValue17, tvValue18, tvValue19, tvValue20, tvValue21, tvValue22;
        LinearLayout llAllocationOrderNoTypetxt, llPercQtySendToFPStxt, llTotalQtySendToFPStxt, ll8, ll9, ll10, ll11, ll12, ll13, ll14, ll15, ll16, ll17, ll18, ll19, ll20, ll21, ll22;
        Button btnGrievancesDetails;

        public MyViewHolder(View view, int viewType) {
            super(view);
            llTotalQtySendToFPStxt = view.findViewById(R.id.ll_tmr_tqstfps);
            llPercQtySendToFPStxt = view.findViewById(R.id.ll_tmr_pqstfps);
            llAllocationOrderNoTypetxt = view.findViewById(R.id.ll_tmr_aont);
            tvCommodityNametxt = view.findViewById(R.id.tv_tmr_cn);
            tvExtraAllocationtxt = view.findViewById(R.id.tv_tmr_ea);
            tvPreviousMonthBalancetxt = view.findViewById(R.id.tv_tmr_pmb);
            tvAllocatedQtytxt = view.findViewById(R.id.tv_tmr_aq);
            tvTotalQtySendToFPStxt = view.findViewById(R.id.tv_tmr_tqstfps);
            tvPercQtySendToFPStxt = view.findViewById(R.id.tv_tmr_pqstfps);
            tvAllocationOrderNoTypetxt = view.findViewById(R.id.tv_tmr_aont);
            tvCommodityNamedots = view.findViewById(R.id.tv_tmr_cndots);
            tvExtraAllocationdots = view.findViewById(R.id.tv_tmr_eadots);
            tvPreviousMonthBalancedots = view.findViewById(R.id.tv_tmr_pmbdots);
            tvAllocatedQtydots = view.findViewById(R.id.tv_tmr_aqdots);
            tvTotalQtySendToFPSdots = view.findViewById(R.id.tv_tmr_tqstfpsdots);
            tvPercQtySendToFPSdots = view.findViewById(R.id.tv_tmr_pqstfpsdots);
            // tvAllocationOrderNoTypedots = view.findViewById(R.id.tv_tmr_aontdots);
            tvCommodityName = view.findViewById(R.id.tv_tmr_commodityname);
            tvExtraAllocation = view.findViewById(R.id.tv_tmr_extraallocationqty);
            tvPreviousMonthBalance = view.findViewById(R.id.tv_tmr_premonthbalance);
            tvAllocatedQty = view.findViewById(R.id.tv_tmr_allocatedqty);
            tvTotalQtySendToFPS = view.findViewById(R.id.tv_tmr_totalqtysendtofps);
            tvPercQtySendToFPS = view.findViewById(R.id.tv_tmr_percqtysendtofps);
            tvAllocationOrderNoType = view.findViewById(R.id.tv_tmr_allocationordernotype);
            btnGrievancesDetails = view.findViewById(R.id.btn_detials);
        }
    }


    public TMRRecViewCustomAdapter(List<TMRReceivedDetails> moviesList) {
        flag = "TrackMyRation";
        this.moviesList = moviesList;
    }

    public TMRRecViewCustomAdapter(Context grievancecontext, List<RCGrievanceListDataList> rcGrievanceListDataListList, String strRationCardNo, String savedTXNIDFromRegActivity) {
        flag = "GrievanceList";
        this.context = grievancecontext;
        this.rcGrievanceListDataListList = rcGrievanceListDataListList;
        this.strRationCardNo = strRationCardNo;
        this.savedTXNIDFromRegActivity = savedTXNIDFromRegActivity;
    }

    public TMRRecViewCustomAdapter(Context grievanceFullcontext, List<RCGrievanceDetailsIssuesDataList> rcGrievanceDetailsIssuesDataListList) {
        flag = "GrievanceFullList";
        this.context = grievanceFullcontext;
        this.rcGrievanceDetailsIssuesDataListList = rcGrievanceDetailsIssuesDataListList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tmr_list_row, parent, false);
        return new MyViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(final TMRRecViewCustomAdapter.MyViewHolder holder, int position) {
        if (position % 2 == 0) {
            holder.itemView.setBackgroundResource(R.color.colorWhite);
        } else {
            holder.itemView.setBackgroundResource(R.color.colorFadedWhite);
        }

        if (flag.equalsIgnoreCase("TrackMyRation") || flag == "TrackMyRation") {

            final TMRReceivedDetails detailMovie = moviesList.get(position);
            holder.tvCommodityNametxt.setText("Commodity Name");
            holder.tvExtraAllocationtxt.setText("Extra Allocation");
            holder.tvPreviousMonthBalancetxt.setText("Previous Month's Balance");
            holder.tvAllocatedQtytxt.setText("Allocated Qty.");
            holder.tvTotalQtySendToFPStxt.setText("Total Qty. Sent to FPS");
            holder.tvTotalQtySendToFPStxt.setTypeface(null, Typeface.BOLD);
            holder.tvPercQtySendToFPStxt.setText("%Qty. sent to FPS");
            holder.tvAllocationOrderNoTypetxt.setText("Allocation Order No./Type");
            holder.tvCommodityName.setText(detailMovie.getCommName() + "(" + detailMovie.getUnit() + ")");
            holder.tvExtraAllocation.setText(detailMovie.getExtraAllocQty());
            holder.tvPreviousMonthBalance.setText(detailMovie.getOpeningBalance());
            holder.tvAllocatedQty.setText(detailMovie.getAllocQty());
            holder.tvTotalQtySendToFPS.setText(detailMovie.getTotalTCqty());
            holder.tvTotalQtySendToFPS.setTypeface(null, Typeface.BOLD);
            holder.tvPercQtySendToFPS.setText(detailMovie.getPercentageReceived());
            String currentString = detailMovie.getAllocationOrderNumber();
            String[] separated = currentString.split(",");
            String allocationOrderNo = separated[0];
            String allocationOrderType = separated[1];
            holder.tvAllocationOrderNoType.setText(allocationOrderNo + "\n" + allocationOrderType);
            holder.btnGrievancesDetails.setVisibility(View.GONE);
        } else if (flag.equalsIgnoreCase("GrievanceList") || flag == "GrievanceList") {
            holder.llTotalQtySendToFPStxt.setVisibility(View.GONE);
            holder.llPercQtySendToFPStxt.setVisibility(View.GONE);
            holder.llAllocationOrderNoTypetxt.setVisibility(View.GONE);

            holder.tvCommodityNametxt.setText("Issue Code");
            holder.tvExtraAllocationtxt.setText("Issue Date");
            holder.tvPreviousMonthBalancetxt.setText("Issue Status");
            holder.tvAllocatedQtytxt.setText("Grievance Type Name");
            holder.tvTotalQtySendToFPStxt.setVisibility(View.GONE);
            holder.tvPercQtySendToFPStxt.setVisibility(View.GONE);
            holder.tvAllocationOrderNoTypetxt.setVisibility(View.GONE);

            holder.tvTotalQtySendToFPSdots.setVisibility(View.GONE);
            holder.tvPercQtySendToFPSdots.setVisibility(View.GONE);
            //  holder.tvAllocationOrderNoTypedots.setVisibility(View.GONE);

            final RCGrievanceListDataList rcGrievanceListDataList = rcGrievanceListDataListList.get(position);
            holder.tvCommodityName.setText(rcGrievanceListDataList.getIssueCode());
            holder.tvExtraAllocation.setText(rcGrievanceListDataList.getIssueDate());
            holder.tvPreviousMonthBalance.setText(rcGrievanceListDataList.getIssueStatus());
            holder.tvAllocatedQty.setText(rcGrievanceListDataList.getGrievanceTypeName());

            holder.btnGrievancesDetails.setText("Show Details");
            holder.btnGrievancesDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Paper.init(context);
                    Paper.book().write("GrievancesListActivity_Adapter_IssueCode", holder.tvCommodityName.getText().toString());
                    Intent intent = new Intent(context.getApplicationContext(), GrievanceFullDetails.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("RationCard", strRationCardNo);
//                    bundle.putString("IssueCode", holder.tvCommodityName.getText().toString());
//                    bundle.putString("TXNID", savedTXNIDFromRegActivity);
//                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    context.getApplicationContext().startActivity(intent);
                }
            });
        } else if (flag.equalsIgnoreCase("GrievanceFullList") || flag == "GrievanceFullList") {
            holder.llTotalQtySendToFPStxt.setVisibility(View.GONE);//5
            holder.llPercQtySendToFPStxt.setVisibility(View.GONE);//6
            holder.btnGrievancesDetails.setVisibility(View.GONE);

            holder.tvCommodityNametxt.setText("Status Changed To");//1
            holder.tvExtraAllocationtxt.setText("Responded By");//2
            holder.tvPreviousMonthBalancetxt.setText("Responded on Date");//3
            holder.tvAllocatedQtytxt.setText("Attachements");//4
            holder.tvAllocationOrderNoTypetxt.setText("Discription");//7


            final RCGrievanceDetailsIssuesDataList rcGrievanceDetailsIssuesDataList = rcGrievanceDetailsIssuesDataListList.get(position);
            holder.tvCommodityName.setText(rcGrievanceDetailsIssuesDataList.getIssueStatus());
            holder.tvExtraAllocation.setText(rcGrievanceDetailsIssuesDataList.getFeedBy());
            holder.tvPreviousMonthBalance.setText(rcGrievanceDetailsIssuesDataList.getResponseDate());
            int intFileSize = rcGrievanceDetailsIssuesDataList.getRcGrievanceDetailsAttachementDataLists().size();
            if (intFileSize == 0) {
                holder.tvAllocatedQty.setText("");//Attached pdf file
            } else {
                holder.tvAllocatedQty.setText(rcGrievanceDetailsIssuesDataList.getRcGrievanceDetailsAttachementDataLists().get(0).getAttachmentName());//Attached pdf file

                final String strBase64Data = rcGrievanceDetailsIssuesDataList.getRcGrievanceDetailsAttachementDataLists().get(0).getBase64Data();
                final String strFileName = rcGrievanceDetailsIssuesDataList.getRcGrievanceDetailsAttachementDataLists().get(0).getAttachmentName();
                final String strFileType = rcGrievanceDetailsIssuesDataList.getRcGrievanceDetailsAttachementDataLists().get(0).getAttachmentFileType();
                holder.tvAllocatedQty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         createPdf(context, strBase64Data, strFileName, strFileType);
                    }
                });
            }

            holder.tvAllocationOrderNoType.setText(rcGrievanceDetailsIssuesDataList.getIssueDescription());

        }
    }

    @Override
    public int getItemCount() {
        if (flag.equalsIgnoreCase("GrievanceList") || flag == "GrievanceList") {
            return rcGrievanceListDataListList.size();
        } else if (flag.equalsIgnoreCase("GrievanceFullList") || flag == "GrievanceFullList") {
            return rcGrievanceDetailsIssuesDataListList.size();
        } else {
            return moviesList.size();
        }
    }

    public void storetoPdfandOpen(Context context, String strpdfbase64Data, String fileName, String fileType) {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] imageBytes = baos.toByteArray();
            imageBytes = Base64.decode(strpdfbase64Data, Base64.DEFAULT);

            String destination = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
            String strfileName = "11-nov" + fileName;
            destination += strfileName;
            File file = new File(destination);
            Log.i("SRS_filepathfirst", destination);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(imageBytes);
            Toast.makeText(context, "SRS_done" + destination, Toast.LENGTH_LONG).show();
            //   fos.flush();
            fos.close();


            File file1 = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "SRS_11nov" + fileName);
            Log.i("SRS_filepathSecond", "" + file);
            FileOutputStream fileOutputStream = new FileOutputStream(file1);
            fileOutputStream.write(imageBytes);
            fileOutputStream.close();

//            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
//            Uri uriData = Uri.fromFile(file);
//            pdfIntent.setDataAndType(uriData, fileType);
//            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
////            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//            context.startActivity(pdfIntent);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void createPdf(Context context, String strpdfbase64Data, String fileName, String fileType) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(strpdfbase64Data, Base64.DEFAULT);
      //  Bitmap bitmapDecodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

//        bitmap = Bitmap.createBitmap(bitmapDecodedImage.getWidth(), bitmapDecodedImage.getHeight(), Bitmap.Config.ARGB_8888);
//
//        PdfDocument document = new PdfDocument();
//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
//        PdfDocument.Page page = document.startPage(pageInfo);
//        Canvas canvas = page.getCanvas();
//        Paint paint = new Paint();
//        canvas.drawPaint(paint);
//        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
//        paint.setColor(Color.BLUE);
//        canvas.drawBitmap(bitmap, 0, 0, null);
//        document.finishPage(page);

        try {
            destination = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
            String strfileName = "SRS_13novfilename.pdf";
            destination += strfileName;
            final Uri uri = Uri.parse("file://" + destination);
            File mypath2 = new File(destination);
            FileOutputStream fileOutputStream =new FileOutputStream(mypath2);
            fileOutputStream.write(imageBytes);
            fileOutputStream.close();
//            document.writeTo(new FileOutputStream(mypath2));
//            document.close();
            Log.i("SRSS", "" + mypath2);
        } catch (IOException e) {
            e.printStackTrace();
            // Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

//        PrintManager printManager = (PrintManager) context.getSystemService(Context.PRINT_SERVICE);
//        try {
//            PrintDocumentAdapter printAdapter = new PdfDocumentAdapter2(context, destination);
//
//            printManager.print("Document", printAdapter, new PrintAttributes.Builder().build());
//        } catch (Exception e) {
//            Log.e("dsagsfd", "" + e);
//        }
    }
}

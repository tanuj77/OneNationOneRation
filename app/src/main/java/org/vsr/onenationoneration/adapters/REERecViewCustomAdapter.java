package org.vsr.onenationoneration.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.vsr.onenationoneration.R;
import org.vsr.onenationoneration.apiCall.bin.RCEEntitlementsDataList;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;

public class REERecViewCustomAdapter extends RecyclerView.Adapter<REERecViewCustomAdapter.MyViewHolder> {
    private List<RCEEntitlementsDataList> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCommodity, tvUnit, tvEntitledQuantity, tvEconomyPrice, tvCentralIssue, tvStateSubsidy, tvOtherExpenses, tvSalesPrice, tvAmountToBePaid, tvTotalSubsidy, tvAllocationOrderNo;

        public MyViewHolder(View view, int viewType) {
            super(view);
            tvCommodity = view.findViewById(R.id.tv_re_commodity);
            //tvUnit = (TextView) view.findViewById(R.id.tv_re_unit);
            tvEntitledQuantity = view.findViewById(R.id.tv_re_entitledquantity);
            tvEconomyPrice = view.findViewById(R.id.tv_re_economypriceperunit);
            tvCentralIssue = view.findViewById(R.id.tv_re_centralissueprice);
            tvStateSubsidy = view.findViewById(R.id.tv_re_statesubsidy);
            tvOtherExpenses = view.findViewById(R.id.tv_re_otherexpenses);
            tvSalesPrice = view.findViewById(R.id.tv_re_salesprice);
            tvAmountToBePaid = view.findViewById(R.id.tv_re_amounttobepaid);
            tvTotalSubsidy = view.findViewById(R.id.tv_re_totalsubsidy);
            tvAllocationOrderNo = view.findViewById(R.id.tv_re_allocationorderno);
        }
    }


    public REERecViewCustomAdapter(List<RCEEntitlementsDataList> moviesList) {
        this.moviesList = moviesList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rationentitlement_list_row2, parent, false);
        return new MyViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(final REERecViewCustomAdapter.MyViewHolder holder, int position) {
        if (position % 2 == 0) {
            holder.itemView.setBackgroundResource(R.color.colorFadedWhite);
        } else {
            holder.itemView.setBackgroundResource(R.color.White);
        }

        final RCEEntitlementsDataList detailMovie = moviesList.get(position);
        holder.tvCommodity.setText(detailMovie.getCommName() + "(" + detailMovie.getUnit() + ")");
        //holder.tvUnit.setText(detailMovie.getUnit());
        holder.tvEntitledQuantity.setText(detailMovie.getEntlQty());
        holder.tvEconomyPrice.setText(detailMovie.getEp());
        holder.tvCentralIssue.setText(detailMovie.getCip());
        holder.tvStateSubsidy.setText(detailMovie.getSs());
        holder.tvOtherExpenses.setText(detailMovie.getOe());
        holder.tvSalesPrice.setText(detailMovie.getRatePerUnit());
        holder.tvAmountToBePaid.setText(detailMovie.getTotalAmt());
        holder.tvTotalSubsidy.setText(detailMovie.getTotalSubsidy());
        String currentString = detailMovie.getAllocationOrderNumber();
        String[] separated = currentString.split(",");
        String allocationOrderNo = separated[0];
        String allocationOrderType = separated[1];
        holder.tvAllocationOrderNo.setText(allocationOrderNo + "\n" + allocationOrderType);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

}

package hk.edu.cuhk.ie.iems5722.group28.MainUI.dining;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hk.edu.cuhk.ie.iems5722.group28.ChatUI.ChatActivity;
import hk.edu.cuhk.ie.iems5722.group28.PostActivity;
import hk.edu.cuhk.ie.iems5722.group28.R;

public class CanteensAdapter extends RecyclerView.Adapter<CanteensAdapter.CanteensVH> {

    List<Canteens> canteensList;

    public CanteensAdapter(List<Canteens> canteensList) {
        this.canteensList = canteensList;
    }

    @NonNull
    @Override
    public CanteensVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType,parent,false);
        return new CanteensVH(view);
    }

    @Override
    public int getItemViewType(int position) {

        return R.layout.item_canteen;
    }

    @Override
    public void onBindViewHolder(@NonNull CanteensVH holder, int position) {
        Canteens canteens = canteensList.get(position);
        holder.codeNameTv.setText(canteens.getHeader());
        holder.versionsTv.setText(canteens.getSubHeaderLeft());
        holder.apiLevelTv.setText(canteens.getSubHeaderRight());
        holder.descriptionTv.setText(canteens.getDescription());
        holder.openStatusTv.setText(canteens.getOpeningStatus());
        if (canteens.getOpeningStatus().equals("Closed")){
            holder.openStatusTv.setTextColor(Color.parseColor("#8B0000"));
            holder.btnChat.setVisibility(View.GONE);
        }

        boolean isExpandable = canteensList.get(position).isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);

        holder.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext().getApplicationContext(), PostActivity.class);
                intent.putExtra("CanteenName",canteens.getHeader());
                view.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return canteensList.size();
    }

    public class CanteensVH extends RecyclerView.ViewHolder {

        TextView codeNameTv, versionsTv, apiLevelTv, descriptionTv,openStatusTv;
        LinearLayout linearLayout;
        RelativeLayout expandableLayout;
        Button btnChat;


        public CanteensVH(@NonNull View itemView) {
            super(itemView);
            codeNameTv = itemView.findViewById(R.id.Header);
            versionsTv = itemView.findViewById(R.id.subheader_left);
            apiLevelTv = itemView.findViewById(R.id.subheader_right);
            descriptionTv = itemView.findViewById(R.id.description);
            openStatusTv = itemView.findViewById(R.id.opening_status);

            linearLayout = itemView.findViewById(R.id.linear_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            btnChat = itemView.findViewById(R.id.button_chat);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Canteens canteens = canteensList.get(getAdapterPosition());
                    canteens.setExpandable(!canteens.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });


        }
    }
}

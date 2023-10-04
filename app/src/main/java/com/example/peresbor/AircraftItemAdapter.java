package com.example.peresbor;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AircraftItemAdapter extends RecyclerView.Adapter<AircraftItemAdapter.ViewHolder> {
    private List<AircraftItem> aircraftItems;

    public AircraftItemAdapter(List<AircraftItem> aircraftItems) {
        this.aircraftItems = aircraftItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_aircraft, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AircraftItem aircraftItem = aircraftItems.get(position);
        holder.itemNameTextView.setText(aircraftItem.getItemName());
        holder.itemLocationTextView.setText(aircraftItem.getItemLocation());

        holder.endDateTextView.setText(aircraftItem.getEndDate());

        holder.boolCheck.setText(aircraftItem.isOnBoardStr());
        // Устанавливаем значение ID
        holder.itemIdTextView.setText(String.valueOf(aircraftItem.getId()));
    }


    @Override
    public int getItemCount() {
        return aircraftItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemNameTextView;
        public TextView itemLocationTextView;
        public TextView endDateTextView;
        public TextView itemIdTextView;
        public TextView boolCheck;
        public ViewHolder(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            itemLocationTextView = itemView.findViewById(R.id.itemLocationTextView);
            endDateTextView = itemView.findViewById(R.id.endDateTextView);
            boolCheck=itemView.findViewById(R.id.boolCheck);
            itemIdTextView=itemView.findViewById(R.id.itemIdTextView);
        }
    }
}

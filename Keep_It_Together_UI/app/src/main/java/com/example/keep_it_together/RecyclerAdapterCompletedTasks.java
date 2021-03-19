package com.example.keep_it_together;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class RecyclerAdapterCompletedTasks extends RecyclerView.Adapter<RecyclerAdapterCompletedTasks.ViewHolder>{
    private TaskListData[] localDataSet;

    public RecyclerAdapterCompletedTasks(TaskListData[] dataSet){
        localDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_row_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TaskListData data = localDataSet[position];
        holder.textView1.setText(data.getName());
        holder.textView2.setText(data.getTask());
        holder.textView3.setText(data.getDate());
    }

    @Override
    public int getItemCount() {
        return localDataSet.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView1;
        private final TextView textView2;
        private final TextView textView3;

        public ViewHolder(View itemView) {
            super(itemView);
            textView1 = (TextView) itemView.findViewById(R.id.recyclerCol1);
            textView2 = (TextView) itemView.findViewById(R.id.recyclerCol2);
            textView3 = (TextView) itemView.findViewById(R.id.recyclerCol3);
        }

    }
}

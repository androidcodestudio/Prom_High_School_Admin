package com.androidcodestudio.promhsadmin.AdminClassFiveSet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcodestudio.promhsadmin.R;

import java.util.List;


public class AdminClassFiveSetAdapter extends RecyclerView.Adapter<AdminClassFiveSetAdapter.AdminClassFiveSet_viewHolder>{
    private Context context;
    private List<AdminClassFiveSetPojo> adminClassFiveSetPojoList;

    public AdminClassFiveSetAdapter() {
    }

    public AdminClassFiveSetAdapter(Context context, List<AdminClassFiveSetPojo> adminClassFiveSetPojoList) {
        this.context = context;
        this.adminClassFiveSetPojoList = adminClassFiveSetPojoList;
    }

    @NonNull
    @Override
    public AdminClassFiveSet_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.set_item,parent,false);
        return new AdminClassFiveSet_viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminClassFiveSet_viewHolder holder, int position) {
        int _set = adminClassFiveSetPojoList.get(position).getSet();

        holder.setMockTest(_set);
    }

    @Override
    public int getItemCount() {
        return adminClassFiveSetPojoList.size();
    }

    public class AdminClassFiveSet_viewHolder extends RecyclerView.ViewHolder {

        private TextView _mockTestTitle;

        public AdminClassFiveSet_viewHolder(@NonNull View itemView) {
            super(itemView);
            _mockTestTitle = itemView.findViewById(R.id.set_number);
        }

        private void setMockTest(int set){
            _mockTestTitle.setText(String.valueOf(set));
        }
    }
}

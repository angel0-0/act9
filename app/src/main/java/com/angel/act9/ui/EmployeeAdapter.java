package com.angel.act9.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angel.act9.R;
import com.angel.act9.model.Employee;

import java.util.List;
import java.util.Locale;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.VH> {

    public interface OnItemClick {
        void onEdit(Employee employee);
        void onDelete(Employee employee);
    }

    private final List<Employee> employeeList;
    private final OnItemClick listener;

    public EmployeeAdapter(List<Employee> employeeList, OnItemClick listener) {
        this.employeeList = employeeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_employee, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Employee employee = employeeList.get(position);
        holder.bind(employee, listener);
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public void setEmployees(List<Employee> employees) {
        // This method is not used in the current MainActivity flow, but can be kept for flexibility
        // To use it, you would need to change how the list is updated in MainActivity
    }

    static class VH extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvPosition;
        private final TextView tvSalary;

        VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPosition = itemView.findViewById(R.id.tvPosition);
            tvSalary = itemView.findViewById(R.id.tvSalary);
        }

        void bind(final Employee employee, final OnItemClick listener) {
            tvName.setText(employee.getName());
            tvPosition.setText(employee.getPosition());
            tvSalary.setText(String.format(Locale.US, "$%.2f", employee.getSalary()));

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEdit(employee);
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onDelete(employee);
                }
                return true;
            });
        }
    }
}

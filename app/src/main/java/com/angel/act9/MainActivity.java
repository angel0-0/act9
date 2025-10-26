package com.angel.act9;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.angel.act9.data.EmployeeDao;
import com.angel.act9.model.Employee;
import com.angel.act9.ui.EmployeeAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements EmployeeAdapter.OnItemClick {

    private EmployeeDao employeeDao;
    private EmployeeAdapter adapter;
    private List<Employee> employeeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        employeeDao = new EmployeeDao(this);

        RecyclerView rvEmployees = findViewById(R.id.rvEmployees);
        rvEmployees.setLayoutManager(new LinearLayoutManager(this));

        employeeList = employeeDao.getAll();
        adapter = new EmployeeAdapter(employeeList, this);
        rvEmployees.setAdapter(adapter);

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> showEmployeeDialog(null));
    }

    @Override
    public void onEdit(Employee employee) {
        showEmployeeDialog(employee);
    }

    @Override
    public void onDelete(Employee employee) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Empleado")
                .setMessage("¿Estás seguro de que quieres eliminar a " + employee.getName() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    int result = employeeDao.delete(employee.getId());
                    if (result > 0) {
                        Toast.makeText(this, "Empleado eliminado", Toast.LENGTH_SHORT).show();
                        updateEmployeeList();
                    } else {
                        Toast.makeText(this, "Error al eliminar empleado", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void showEmployeeDialog(final Employee employee) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_employee, null);
        builder.setView(dialogView);

        final EditText etName = dialogView.findViewById(R.id.etName);
        final EditText etPosition = dialogView.findViewById(R.id.etPosition);
        final EditText etSalary = dialogView.findViewById(R.id.etSalary);

        // Si es una edición, rellenar los campos
        if (employee != null) {
            builder.setTitle("Editar Empleado");
            etName.setText(employee.getName());
            etPosition.setText(employee.getPosition());
            etSalary.setText(String.valueOf(employee.getSalary()));
        } else {
            builder.setTitle("Añadir Empleado");
        }

        builder.setPositiveButton(employee != null ? "Guardar Cambios" : "Añadir", (dialog, which) -> {
            // El botón se configurará más abajo para evitar cierre automático
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        final AlertDialog dialog = builder.create();
        dialog.show();

        // Sobrescribir el listener del botón positivo para validar
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String position = etPosition.getText().toString().trim();
            String salaryStr = etSalary.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                etName.setError("El nombre es obligatorio");
                return;
            }

            double salary = 0.0;
            if (!TextUtils.isEmpty(salaryStr)) {
                try {
                    salary = Double.parseDouble(salaryStr);
                } catch (NumberFormatException e) {
                    etSalary.setError("Salario inválido");
                    return;
                }
            }

            if (employee != null) { // Actualizar
                employee.setName(name);
                employee.setPosition(position);
                employee.setSalary(salary);
                int result = employeeDao.update(employee);
                if (result > 0) {
                    Toast.makeText(this, "Empleado actualizado", Toast.LENGTH_SHORT).show();
                    updateEmployeeList();
                    dialog.dismiss();
                } else {
                    Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            } else { // Insertar
                Employee newEmployee = new Employee(name, position, salary);
                long id = employeeDao.insert(newEmployee);
                if (id != -1) {
                    Toast.makeText(this, "Empleado añadido", Toast.LENGTH_SHORT).show();
                    updateEmployeeList();
                    dialog.dismiss();
                } else {
                    Toast.makeText(this, "Error al añadir", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateEmployeeList() {
        employeeList.clear();
        employeeList.addAll(employeeDao.getAll());
        adapter.notifyDataSetChanged();
    }
}

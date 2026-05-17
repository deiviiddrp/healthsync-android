package com.healthsync.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.healthsync.android.databinding.ActivityRegisterBinding;
import com.healthsync.android.model.*;
import com.healthsync.android.network.RetrofitClient;
import com.healthsync.android.util.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sessionManager = new SessionManager(this);

        binding.btnRegister.setOnClickListener(v -> attemptRegister());
        binding.tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void attemptRegister() {
        String nombre = binding.etNombre.getText().toString().trim();
        String apellidos = binding.etApellidos.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString();

        if (nombre.isEmpty() || apellidos.isEmpty() ||
                email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 8) {
            binding.tilPassword.setError("Mínimo 8 caracteres");
            return;
        }

        setLoadingState(true);

        RetrofitClient.getInstance(sessionManager).getApiService()
                .register(new RegisterRequest(nombre, apellidos, email, password))
                .enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call,
                                           Response<UserResponse> response) {
                        setLoadingState(false);
                        if (response.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this,
                                    "¡Cuenta creada! Inicia sesión",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(
                                    RegisterActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this,
                                    "Error al crear cuenta. El email puede estar en uso.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        setLoadingState(false);
                        Toast.makeText(RegisterActivity.this,
                                "Error de conexión: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void setLoadingState(boolean loading) {
        binding.btnRegister.setEnabled(!loading);
        binding.progressBar.setVisibility(
                loading ? View.VISIBLE : View.GONE);
    }
}
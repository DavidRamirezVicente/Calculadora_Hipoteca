package com.example.calculator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;



public class FirstFragment extends Fragment{
    private EditText preu_inmoble;
    private EditText estalvis;
    private EditText plac_anys;
    private EditText euribor;
    private EditText diferencial;
    private TextView total;
    private TextView mes;


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(R.layout.fragment_first, container, false);

        preu_inmoble = view.findViewById(R.id.preu_inmoble);
        estalvis = view.findViewById(R.id.estalvis);
        plac_anys = view.findViewById(R.id.plac_anys);
        euribor = view.findViewById(R.id.euribor);
        diferencial = view.findViewById(R.id.diferencial);
        total = view.findViewById(R.id.total);
        mes = view.findViewById(R.id.mes);
        Button calcular = view.findViewById(R.id.calcular);
        Spinner tipoInteresSpinner = view.findViewById(R.id.tipo_interes_spinner);

        tipoInteresSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    euribor.setVisibility(View.VISIBLE);
                } else {
                    euribor.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });


        preu_inmoble.setText("120000");
        estalvis.setText("2000");
        plac_anys.setText("30");
        euribor.setText("0.163");
        diferencial.setText("2.5");

        preu_inmoble.addTextChangedListener(textWatcher);
        estalvis.addTextChangedListener(textWatcher);
        plac_anys.addTextChangedListener(textWatcher);
        euribor.addTextChangedListener(textWatcher);
        diferencial.addTextChangedListener(textWatcher);


        calcular.setOnClickListener(v -> calcularHipoteca());

        if (savedInstanceState != null) {
            total.setText(savedInstanceState.getString("total"));
            mes.setText(savedInstanceState.getString("mes"));
        }


        return view;

    }
    private final TextWatcher textWatcher = new TextWatcher() {


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            calcularHipoteca();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }


    };


    @SuppressLint("DefaultLocale")
    private void calcularHipoteca() {
        try {
            double preuValue = getFloatFromEditText(preu_inmoble);
            double estalvisValue = getFloatFromEditText(estalvis);
            int placValue = Integer.parseInt(plac_anys.getText().toString());
            double euriborValue = getFloatFromEditText(euribor);
            double diferencialValue = getFloatFromEditText(diferencial);

            if (estalvisValue >= preuValue) {
                Toast.makeText(getContext(), "Estalvis debe ser menor que Preu Inmoble", Toast.LENGTH_SHORT).show();
                return;
            }

            if (placValue < 15 || placValue > 50) {
                Toast.makeText(getContext(), "Plazo en años debe estar entre 15 y 50", Toast.LENGTH_SHORT).show();
                return;
            }

            double interesMensual;
            if (euribor.getVisibility() == View.VISIBLE) {
                interesMensual = (euriborValue + diferencialValue) / 12;
            } else {
                interesMensual = diferencialValue / 12;
            }

            double capital = preuValue - estalvisValue;
            int meses = placValue * 12;

            double pagoMensual = (capital * (interesMensual)) / (100 * (1 - Math.pow(1 + interesMensual / 100, -meses)));
            double costeTotal = pagoMensual * meses;

            total.setText(String.format("Total: %.2f €", costeTotal));
            mes.setText(String.format("Mes: %.2f €", pagoMensual));
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Error en los datos ingresados", Toast.LENGTH_SHORT).show();
        }
    }

    private double getFloatFromEditText(EditText editText) {
        return Double.parseDouble(editText.getText().toString());
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("total", total.getText().toString());
        outState.putString("mes", mes.getText().toString());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        com.example.calculator.databinding.FragmentFirstBinding binding = null;
    }

}

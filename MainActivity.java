package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class MainActivity extends Activity {

    private EditText etImie;
    private EditText etNazwisko;
    private EditText etDlugosc;
    private Spinner spStanowisko;

    private CheckBox cbLitery;
    private CheckBox cbCyfry;
    private CheckBox cbSpecjalne;

    private TextView tvHaslo;

    private String haslo = "";

    private final SecureRandom random = new SecureRandom();

    private final String maleLitery =
            "abcdefghijklmnopqrstuvwxyz";
    private final String wielkieLitery =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String cyfry =
            "0123456789";
    private final String znakiSpecjalne =
            "!@#$%^&*()_+-=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etImie = findViewById(R.id.etImie);
        etNazwisko = findViewById(R.id.etNazwisko);
        etDlugosc = findViewById(R.id.etDlugosc);
        spStanowisko = findViewById(R.id.spStanowisko);

        cbLitery = findViewById(R.id.cbLitery);
        cbCyfry = findViewById(R.id.cbCyfry);
        cbSpecjalne = findViewById(R.id.cbSpecjalne);

        tvHaslo = findViewById(R.id.tvHaslo);

        Button btnGeneruj = findViewById(R.id.btnGeneruj);
        Button btnZatwierdz = findViewById(R.id.btnZatwierdz);

        String[] stanowiska = {
                "Kierownik",
                "Starszy programista",
                "Młodszy programista",
                "Tester"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                stanowiska
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spStanowisko.setAdapter(adapter);

        btnGeneruj.setOnClickListener(v -> generujHaslo());

        btnZatwierdz.setOnClickListener(v -> zatwierdzDane());
    }

    private void generujHaslo() {
        String tekstDlugosci = etDlugosc.getText().toString().trim();

        int dlugosc;

        try {
            dlugosc = Integer.parseInt(tekstDlugosci);
        } catch (NumberFormatException e) {
            Toast.makeText(
                    this,
                    "Podaj prawidłową długość hasła",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (dlugosc < 1 || dlugosc > 256) {
            Toast.makeText(
                    this,
                    "Długość hasła musi wynosić od 1 do 256",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        String zestaw = maleLitery;
        List<Character> znaki = new ArrayList<>();

        if (cbLitery.isChecked()) {
            zestaw += wielkieLitery;
            znaki.add(losujZnak(wielkieLitery));
        }

        if (cbCyfry.isChecked()) {
            zestaw += cyfry;
            znaki.add(losujZnak(cyfry));
        }

        if (cbSpecjalne.isChecked()) {
            zestaw += znakiSpecjalne;
            znaki.add(losujZnak(znakiSpecjalne));
        }

        if (znaki.size() > dlugosc) {
            Toast.makeText(
                    this,
                    "Hasło jest za krótkie dla wybranych opcji",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        while (znaki.size() < dlugosc) {
            znaki.add(losujZnak(zestaw));
        }

        Collections.shuffle(znaki, random);

        StringBuilder wynik = new StringBuilder();

        for (char znak : znaki) {
            wynik.append(znak);
        }

        haslo = wynik.toString();

        tvHaslo.setText("Wygenerowane hasło: " + haslo);

        Toast.makeText(
                this,
                "Hasło zostało wygenerowane",
                Toast.LENGTH_SHORT
        ).show();
    }

    private char losujZnak(String zestaw) {
        int indeks = random.nextInt(zestaw.length());
        return zestaw.charAt(indeks);
    }

    private void zatwierdzDane() {
        String imie = etImie.getText().toString().trim();
        String nazwisko = etNazwisko.getText().toString().trim();
        String stanowisko = spStanowisko.getSelectedItem().toString();

        if (haslo.isEmpty()) {
            Toast.makeText(
                    this,
                    "Najpierw wygeneruj hasło",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        String komunikat =
                "Imię: " + imie +
                        "\nNazwisko: " + nazwisko +
                        "\nStanowisko: " + stanowisko +
                        "\nHasło: " + haslo;

        new android.app.AlertDialog.Builder(this)
                .setTitle("Dane pracownika")
                .setMessage(komunikat)
                .setPositiveButton("OK", null)
                .show();
    }
}

package alura.com.telalogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.util.Calendar;

import alura.com.modelo.Sala;

import static android.app.ProgressDialog.show;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class ActivityTelaSala extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_sala);

        Intent it = getIntent();
        Sala parametro = (Sala) it.getSerializableExtra("sala");
        TextView nome = (TextView) findViewById(R.id.tv_nome_sala);
        nome.setText(parametro.getNome());

        TextView pessoasSentadas = (TextView) findViewById(R.id.tv_quantidadePessoasSentadas);
        pessoasSentadas.setText(String.valueOf(parametro.getQuantidadePessoasSentadas()));

        TextView areaDaSala = (TextView) findViewById(R.id.tv_areaTotalDaSala);
        areaDaSala.setText(String.valueOf(parametro.getAreaDaSala()));

        TextView possuiArcondicionado = (TextView) findViewById(R.id.tv_possuiArcondicionado);
        if (parametro.isPossuiMultimidia() == TRUE) {
            possuiArcondicionado.setText("Sim");
        } else if (parametro.isPossuiMultimidia() == FALSE) {
            possuiArcondicionado.setText("Não");
        } else {
            possuiArcondicionado.setText("Não definido");
        }
        TextView possuiMultimidia = (TextView) findViewById(R.id.tv_possuiMultimidia);
        if (parametro.isPossuiMultimidia() == TRUE) {
            possuiMultimidia.setText("Sim");
        } else if (parametro.isPossuiMultimidia() == FALSE) {
            possuiMultimidia.setText("Não");
        } else {
            possuiMultimidia.setText("Não definido");
        }


        Button botaoVoltar = (Button) findViewById(R.id.btn_voltar);
        botaoVoltar.setOnClickListener((new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(ActivityTelaSala.this, ActivityTelaPrincipal.class);
                startActivity(it);
            }
        }));

        FloatingActionButton fabSelecionarDia = (FloatingActionButton) findViewById(R.id.fab_adicionar_reserva);
        fabSelecionarDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");

            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        TextView textView = (TextView) findViewById(R.id.tv_dia_selecionado);
        textView.setText(currentDateString);
    }
}

package alura.com.telalogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import alura.com.modelo.Reserva;
import alura.com.modelo.Sala;
import alura.com.services.ReservaService;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class ActivityTelaSala extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    Sala sala = new Sala();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_sala);
        List<String> listaDeReservas = new ArrayList<>();

        Intent it = getIntent();
        sala = (Sala) it.getSerializableExtra("sala");
        TextView nome = (TextView) findViewById(R.id.tv_nome_sala);
        nome.setText(sala.getNome());

        TextView pessoasSentadas = (TextView) findViewById(R.id.tv_quantidadePessoasSentadas);
        pessoasSentadas.setText(String.valueOf(sala.getQuantidadePessoasSentadas()));

        TextView areaDaSala = (TextView) findViewById(R.id.tv_areaTotalDaSala);
        areaDaSala.setText(String.valueOf(sala.getAreaDaSala()));


        TextView possuiArcondicionado = (TextView) findViewById(R.id.tv_possuiArcondicionado);
        if (sala.isPossuiMultimidia() == TRUE) {
            possuiArcondicionado.setText("Sim");
        } else if (sala.isPossuiMultimidia() == FALSE) {
            possuiArcondicionado.setText("Não");
        } else {
            possuiArcondicionado.setText("Não definido");
        }
        TextView possuiMultimidia = (TextView) findViewById(R.id.tv_possuiMultimidia);
        if (sala.isPossuiMultimidia()) {
            possuiMultimidia.setText("Sim");
        } else if (sala.isPossuiMultimidia() == FALSE) {
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
        try {
            String reservasReturn = new ReservaService().execute(Integer.toString(sala.getId())).get();
            JSONArray jsonArray = new JSONArray(reservasReturn);

            if (reservasReturn.length() > 1) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    if (obj.has("idSala") && obj.has("idUsuario") && obj.has("dataHoraInicio") && obj.has("dataHoraFim")) {
                        int idSala = obj.getInt("idSala");
                        int idUsuario = obj.getInt("idUsuario");
                        String descricao = obj.getString("descricao");
                        String dataHoraInicio = obj.getString("dataHoraInicio");
                        String dataHoraFim = obj.getString("dataHoraFim");


                        Reserva novaReserva = new Reserva();
                        novaReserva.setIdSala(idSala);
                        novaReserva.setDescricao(descricao);
                        novaReserva.setIdUsuario(idUsuario);
                        novaReserva.setDataHoraInicio(dataHoraInicio);
                        novaReserva.setDataHoraFim(dataHoraFim);
                        listaDeReservas.add(String.valueOf(novaReserva.getIdUsuario()));
                        listaDeReservas.add(String.valueOf(novaReserva.getDataHoraInicio()));
                        listaDeReservas.add(String.valueOf(novaReserva.getDataHoraFim()));
                        listaDeReservas.add(String.valueOf(novaReserva.getDescricao()));
                    }
                }
                ListView listview_descricoes = findViewById(R.id.listview_descricoes);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ActivityTelaSala.this, android.R.layout.simple_list_item_1, listaDeReservas);
                listview_descricoes.setAdapter(adapter);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.listview_lista_de_salas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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


package alura.com.telalogin;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
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
        System.out.println(sala.getId());
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
                Intent it = new Intent(ActivityTelaSala.this, ActivityReservarSala.class);
                it.putExtra("idSala", sala.getId());
                startActivity(it);

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
                        String dataEHoraInicio = novaReserva.getDataHoraInicio();
                        String dataEHoraFim = novaReserva.getDataHoraFim();
                        String anoDiaMes = "";
                        String horarioInicio = "";
                        String horarioFim = "";
                        if (dataEHoraInicio.contains("T")) {
                            String[] data = dataEHoraInicio.split("T");
                            if (data.length > 0) {
                                anoDiaMes = data[0];
                            }
                        }


                        if (dataEHoraInicio.contains("Z")) {
                            String[] horarioNaoParciado = dataEHoraInicio.split("Z");
                            if (horarioNaoParciado.length > 0) {
                                String dataEHoraRow = horarioNaoParciado[0];
                                int posicaoDoisPontos = dataEHoraRow.indexOf(":");
                                horarioInicio = dataEHoraRow.substring(posicaoDoisPontos + 1);
                            }
                        }

                        if (dataEHoraFim.contains("Z")) {
                            String[] horarioNaoParciado = dataEHoraFim.split("Z");
                            if (horarioNaoParciado.length > 0) {
                                String dataEHoraRow = horarioNaoParciado[0];
                                int posicaoDoisPontos = dataEHoraRow.indexOf(":");
                                horarioFim = dataEHoraRow.substring(posicaoDoisPontos + 1);
                            }
                        }


                        listaDeReservas.add(anoDiaMes + "        -        " + horarioInicio + "        -        " + horarioFim);
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

        ListView listview = findViewById(R.id.listview_descricoes);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ActivityTelaSala.this, "abrir dialog", Toast.LENGTH_SHORT).show();
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {
                Toast.makeText(ActivityTelaSala.this, "abrir dialog para deletar ", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}


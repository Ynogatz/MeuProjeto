package alura.com.telalogin;

import android.app.AlertDialog;
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
    Reserva reserva = new Reserva();
    List<Reserva> listaDeObjetosReserva = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_sala);
        final List<String> listaDeReservas = new ArrayList<>();

        Intent it = getIntent();

        sala = (Sala) it.getSerializableExtra("sala");

        TextView nome = findViewById(R.id.tv_nome_sala);
        nome.setText(sala.getNome());

        TextView pessoasSentadas = findViewById(R.id.tv_quantidadePessoasSentadas);
        pessoasSentadas.setText(String.valueOf(sala.getQuantidadePessoasSentadas()));

        TextView areaDaSala = findViewById(R.id.tv_areaTotalDaSala);
        areaDaSala.setText(String.valueOf(sala.getAreaDaSala()));

        TextView possuiArcondicionado = findViewById(R.id.tv_possuiArcondicionado);
        if (sala.isPossuiMultimidia() == TRUE) {
            possuiArcondicionado.setText("Sim");
        } else if (sala.isPossuiMultimidia() == FALSE) {
            possuiArcondicionado.setText("Não");
        } else {
            possuiArcondicionado.setText("Não definido");
        }
        TextView possuiMultimidia = findViewById(R.id.tv_possuiMultimidia);
        if (sala.isPossuiMultimidia()) {
            possuiMultimidia.setText("Sim");
        } else if (sala.isPossuiMultimidia() == FALSE) {
            possuiMultimidia.setText("Não");
        } else {
            possuiMultimidia.setText("Não definido");
        }


        Button botaoVoltar = findViewById(R.id.btn_voltar);
        botaoVoltar.setOnClickListener((new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(ActivityTelaSala.this, ActivityTelaPrincipal.class);
                startActivity(it);
            }
        }));

        FloatingActionButton fabSelecionarDia = findViewById(R.id.fab_adicionar_reserva);
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
                    if (obj.has("idSala") && obj.has("idUsuario") && obj.has("dataHoraInicio") && obj.has("dataHoraFim") && obj.has ("nomeOrganizador")) {
                        int idSala = obj.getInt("idSala");
                        int idUsuario = obj.getInt("idUsuario");
                        String descricao = obj.getString("descricao");
                        String dataHoraInicio = obj.getString("dataHoraInicio");
                        String dataHoraFim = obj.getString("dataHoraFim");
                        String nomeOrganizador = obj.getString("nomeOrganizador");


                        Reserva novaReserva = new Reserva();
                        novaReserva.setIdSala(idSala);
                        novaReserva.setDescricao(descricao);
                        novaReserva.setIdUsuario(idUsuario);
                        novaReserva.setDataHoraInicio(dataHoraInicio);
                        novaReserva.setDataHoraFim(dataHoraFim);
                        novaReserva.setNomeOrganizador(nomeOrganizador);
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
                                horarioInicio = dataEHoraRow.split("T")[1];
                            }
                        }

                        if (dataEHoraFim.contains("Z")) {
                            String[] horarioNaoParciado = dataEHoraFim.split("Z");
                            if (horarioNaoParciado.length > 0) {
                                String dataEHoraRow = horarioNaoParciado[0];
                                horarioFim = dataEHoraRow.split("T")[1];
                            }
                        }
                        listaDeReservas.add(anoDiaMes + "    -    " + horarioInicio + "    -    " + horarioFim);
                        listaDeObjetosReserva.add(novaReserva);
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
        if (1 == 1) {
            listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                               int index, long arg3) {
                    AlertDialog.Builder mensagem = new AlertDialog.Builder(ActivityTelaSala.this);
                    mensagem.setTitle("onlongclick");
                    mensagem.setMessage("texto");
                    mensagem.setNegativeButton("Não", null);
                    mensagem.setPositiveButton("Sim", null);
                    mensagem.show();
                    return true;
                }
            });
        }
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(ActivityTelaSala.this, ActivityTelaInfoReserva.class);
                it.putExtra("reservas", listaDeObjetosReserva.get(position));
                startActivity(it);
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}


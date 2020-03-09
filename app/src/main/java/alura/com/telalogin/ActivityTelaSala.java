package alura.com.telalogin;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import alura.com.modelo.Reserva;
import alura.com.modelo.Sala;
import alura.com.services.CancelarReservaService;
import alura.com.services.ReservaService;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class ActivityTelaSala extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Sala sala = new Sala();
    int idUsuario;
    List<Reserva> listaDeObjetosReserva = new ArrayList<>();
    ListView listview_descricoes;
    SharedPreferences prefs;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_sala);
        final List<String> listaDeReservas = new ArrayList<>();
        prefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                    if (obj.has("idSala") && obj.has("idUsuario") && obj.has("dataHoraInicio") && obj.has("dataHoraFim") && obj.has("nomeOrganizador")) {
                        int id = obj.getInt("id");
                        int idSala = obj.getInt("idSala");
                        idUsuario = obj.getInt("idUsuario");
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
                        novaReserva.setId(id);
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
                listview_descricoes = findViewById(R.id.listview_descricoes);
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
                    final int i = index;
                    System.out.println("usuario id " + idUsuario);
                    int userId = Integer.parseInt(prefs.getString("userId", null));

                    if (listaDeObjetosReserva.get(index).getIdUsuario() == userId) {
                        AlertDialog.Builder mensagem = new AlertDialog.Builder(ActivityTelaSala.this);
                        mensagem.setTitle("Cancelar reserva");
                        mensagem.setMessage("Tem certeza que deseja cancelar esta reserva?");
                        mensagem.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(ActivityTelaSala.this, "Reserva não cancelada", Toast.LENGTH_SHORT).show();
                            }
                        });
                        mensagem.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    String cancelarReservaReturn = new CancelarReservaService().execute(String.valueOf(listaDeObjetosReserva.get(i).getId())).get();
                                    if (cancelarReservaReturn.equals("A reserva foi cancelada com sucesso")) {
                                        Toast.makeText(ActivityTelaSala.this, "A reserva foi cancelada com sucesso", Toast.LENGTH_SHORT).show();
                                        listaDeReservas.remove(i);
                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(ActivityTelaSala.this, android.R.layout.simple_list_item_1, listaDeReservas);
                                        listview_descricoes.setAdapter(adapter);
                                    } else {
                                        Toast.makeText(ActivityTelaSala.this, "O cancelamento da reserva não foi concluído", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        mensagem.show();
                    } else {
                        Toast.makeText(ActivityTelaSala.this, "Não é possivel cancelar uma reserva que não foi criada por você", Toast.LENGTH_SHORT).show();
                    }
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
        swipeRefreshLayout = findViewById(R.id.swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(ActivityTelaSala.this, "Atualizando...", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }

    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}


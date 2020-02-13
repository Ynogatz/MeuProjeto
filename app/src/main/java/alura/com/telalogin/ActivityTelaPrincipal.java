package alura.com.telalogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import alura.com.services.SalaService;
import alura.com.modelo.Sala;

public class ActivityTelaPrincipal extends AppCompatActivity {
    SharedPreferences prefs;
    List<Sala> listaDeSalas = new ArrayList<>();
    List<String> listaDeNomes = new ArrayList<>();
    List<Integer> listaDeId = new ArrayList<>();
    List<Integer> listaQuantidadePessoasSentadas = new ArrayList<>();
    List<Boolean> listaPossuiArcon = new ArrayList<>();
    List<Boolean> listaPossuiMultimidia = new ArrayList<>();
    List<Double> listaAreaDaSala = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_principal);
        prefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        Button botaoLogout = (Button) findViewById(R.id.btnLogout);
        botaoLogout.setOnClickListener((new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("userEmail");
                editor.remove("userName");
                editor.remove("userId");
                editor.remove("userIdOrganizacao");
                editor.remove("userNomeEmpresa");
                editor.remove("userTipoEmpresa");
                editor.commit();
                Intent it = new Intent(ActivityTelaPrincipal.this, ActivityTelaInicial.class);
                startActivity(it);
            }
        }));
        ListView listview = findViewById(R.id.listview_lista_salas);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(ActivityTelaPrincipal.this, ActivityTelaMarcarReuniao.class);
                it.putExtra("sala", listaDeSalas.get(position));
//              it.putExtra("id", listaDeSalas.get(position).getId());

                startActivity(it);
            }
        });

        try {
            prefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
            String salasReturn = new SalaService().execute(prefs.getString("userIdOrganizacao", null)).get();
            System.out.println(salasReturn);
            JSONArray jsonArray = new JSONArray(salasReturn);

            if (salasReturn.length() > 2) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    if (obj.has("id") && obj.has("nome") && obj.has("quantidadePessoasSentadas") && obj.has("possuiArcon") && obj.has("possuiMultimidia") && obj.has("areaDaSala")) {
                        int id = obj.getInt("id");
                        String nome = obj.getString("nome");
                        int quantidadePessoasSentadas = obj.getInt("quantidadePessoasSentadas");
                        boolean possuiArcon = obj.getBoolean("possuiArcon");
                        boolean possuiMultimidia = obj.getBoolean("possuiMultimidia");
                        double areaDaSala = obj.getDouble("areaDaSala");
                        Sala novaSala = new Sala();
                        novaSala.setId(id);
                        novaSala.setNome(nome);
                        novaSala.setQuantidadePessoasSentadas(quantidadePessoasSentadas);
                        novaSala.setPossuiMultimidia(possuiMultimidia);
                        novaSala.setPossuiArcon(possuiArcon);
                        novaSala.setAreaDaSala(areaDaSala);
                        listaDeSalas.add(novaSala);
                        listaDeNomes.add(novaSala.getNome());
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ActivityTelaPrincipal.this, android.R.layout.simple_list_item_1, listaDeNomes);
                listview.setAdapter(adapter);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.listview_lista_de_salas);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}








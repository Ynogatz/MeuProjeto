package alura.com.telalogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
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

import alura.com.modelo.Sala;
import alura.com.services.SalaService;

public class ActivityTelaPrincipal extends AppCompatActivity {
    SharedPreferences prefs;
    List<Sala> listaDeSalas = new ArrayList<>();
    List<String> listaDeNomes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_principal);
        prefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        ListView listview = findViewById(R.id.listview_lista_salas);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(ActivityTelaPrincipal.this, ActivityTelaSala.class);
                it.putExtra("sala", listaDeSalas.get(position));
                startActivity(it);
            }
        });

        try {
            prefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
            String salasReturn = new SalaService().execute(prefs.getString("userIdOrganizacao", null)).get();
            JSONArray jsonArray = new JSONArray(salasReturn);

            if (salasReturn.length() > 1) {
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
    
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("userEmail");
            editor.remove("userName");
            editor.remove("userId");
            editor.remove("userIdOrganizacao");
            editor.remove("userNomeEmpresa");
            editor.remove("userTipoEmpresa");
            editor.commit();
            Intent it = new Intent(ActivityTelaPrincipal.this, ActivityTelaLogin.class);
            startActivity(it);
        }
        return super.onOptionsItemSelected(item);
    }
}








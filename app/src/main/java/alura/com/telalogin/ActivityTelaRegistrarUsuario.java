package alura.com.telalogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import alura.com.services.RegistrarService;
import alura.com.services.OrganizacaoService;
import alura.com.modelo.Organizacao;

public class ActivityTelaRegistrarUsuario extends AppCompatActivity {
    List<Organizacao> listaDeOrganizacoes = new ArrayList();
    List<String> listaDeNomesOrganizacoes = new ArrayList<>();
    int idOrganizacaoSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_registrar_usuario);
        final EditText entradaNome = findViewById(R.id.etEntradaNomeUsuario);
        final EditText entradaEmail = findViewById(R.id.etEntradaemail);
        final EditText entradaSenha = findViewById(R.id.etEntradasenha);
        final Button botaoFinalizarCadastro = findViewById(R.id.btnFinalizarCadastro);
        final Spinner spinner = findViewById(R.id.spnEmpresa);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idOrganizacaoSelecionada = listaDeOrganizacoes.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        botaoFinalizarCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeStr = entradaNome.getText().toString().trim();
                String emailStr = entradaEmail.getText().toString().trim();
                String senhaStr = entradaSenha.getText().toString().trim();

                if (TextUtils.isEmpty(nomeStr))
                    entradaNome.setError("o campo nome é obrigatorio");
                else if (TextUtils.isEmpty(emailStr))
                    entradaEmail.setError("o campo email é obrigatorio");
                else if (TextUtils.isEmpty(senhaStr))
                    entradaSenha.setError("o campo senha é obrigatorio");
                else {
                JSONObject usuarioJson = new JSONObject();

                try {
                    usuarioJson.put("email", emailStr);
                    usuarioJson.put("nome", nomeStr);
                    usuarioJson.put("senha", senhaStr);
                    usuarioJson.put("idOrganizacao", idOrganizacaoSelecionada);

                    String userCod = new String(Base64.encodeToString(usuarioJson.toString().getBytes("UTF-8"), Base64.NO_WRAP));

                    Toast.makeText(ActivityTelaRegistrarUsuario.this, new RegistrarService().execute(userCod).get(), Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(ActivityTelaRegistrarUsuario.this, ActivityTelaLogin.class);
                    startActivity(it);

                } catch (
                        JSONException e) {
                    e.printStackTrace();
                } catch (
                        UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (
                        Exception e) {
                }
                    finish();
            }}
        });
        entradaEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String emailAfterTextChange = entradaEmail.getText().toString();
                    if (emailAfterTextChange.contains("@")) {
                        String[] emailCompleto = emailAfterTextChange.split("@");
                        if (emailCompleto.length > 0) {
                            String dominio = emailCompleto[1];
                            if (dominio.contains(".")) {
                                try {
                                    String listaOrganizacao = new OrganizacaoService().execute(dominio).get();
                                    System.out.println("dominio: " + dominio);
                                    System.out.println("organizacoes retornadas " + listaOrganizacao);

                                    if (listaOrganizacao.length() == 0) {
                                        Toast.makeText(ActivityTelaRegistrarUsuario.this, "O dominio do email informado nao faz parte de nenhuma organizacao", Toast.LENGTH_SHORT).show();
                                    } else {
                                        parseOrganizacoesArray(listaOrganizacao, spinner);
                                    }
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                    }
                }
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    public void parseOrganizacoesArray(String organizacoesString, Spinner spinner) {
        try {
            JSONArray jsonArray = new JSONArray(organizacoesString);
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);

                    if (obj.has("id") && obj.has("nome") && obj.has("tipoOrganizacao")) {

                        int id = obj.getInt("id");
                        String nome = obj.getString("nome");
                        String tipoOrganizacao = obj.getString("tipoOrganizacao");
                        Organizacao novaOrganizacao = new Organizacao();
                        novaOrganizacao.setId(id);
                        novaOrganizacao.setNome(nome);
                        novaOrganizacao.setTipoOrganizacao(tipoOrganizacao);

                        listaDeOrganizacoes.add(novaOrganizacao);
                        listaDeNomesOrganizacoes.add(novaOrganizacao.getNome() + " - " + novaOrganizacao.getTipoOrganizacao());

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(ActivityTelaRegistrarUsuario.this, android.R.layout.simple_spinner_item, listaDeNomesOrganizacoes);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                        spinner.setVisibility(View.VISIBLE);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

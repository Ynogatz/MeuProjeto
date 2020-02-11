package alura.com.telalogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
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
import alura.com.telalogin.modelo.Organizacao;

public class ActivityTelaRegistrarUsuario extends AppCompatActivity {
    List<Organizacao> listaDeOrganizacoes = new ArrayList();
    List<String> listaDeNomesOrganizacoes = new ArrayList<>();
    int idOrganizacaoSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_registrar_usuario);
        final EditText entradaNome = findViewById(R.id.etEntradaNomeUsuario);
        final EditText entradaEmail = findViewById(R.id.etEntradaemail);
        final EditText entradaSenha = findViewById(R.id.etEntradasenha);
        final Button botaoFinalizarCadastro = findViewById(R.id.btnFinalizarCadastro);
        final Spinner spinner = findViewById(R.id.spinnerEmpresa);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idOrganizacaoSelecionada = listaDeOrganizacoes.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Button botaoVoltar = findViewById(R.id.btnVoltar);
        botaoVoltar.setOnClickListener((new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(ActivityTelaRegistrarUsuario.this, ActivityTelaInicial.class);
                startActivity(it);
            }
        }));

        botaoFinalizarCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject usuarioJson = new JSONObject();

                try {
                    String nomeString = entradaNome.getText().toString();
                    String emailString = entradaEmail.getText().toString();
                    String senhaString = entradaSenha.getText().toString();
                    usuarioJson.put("email", emailString);
                    usuarioJson.put("nome", nomeString);
                    usuarioJson.put("senha", senhaString);
                    usuarioJson.put("idOrganizacao", idOrganizacaoSelecionada);

                    String novoUsuarioDecode;
                    String userCod = new String(Base64.encodeToString(usuarioJson.toString().getBytes("UTF-8"), Base64.NO_WRAP));

                    Toast.makeText(ActivityTelaRegistrarUsuario.this, new RegistrarService().execute(userCod).get(), Toast.LENGTH_SHORT).show();

                } catch (
                        JSONException e) {
                    e.printStackTrace();
                } catch (
                        UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (
                        Exception e) {

                }
                Intent it = new Intent(ActivityTelaRegistrarUsuario.this, ActivityTelaInicial.class);
                startActivity(it);
                finish();
            }
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

package alura.com.telalogin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import alura.com.modelo.Organizacao;
import alura.com.services.OrganizacaoService;
import alura.com.services.RegistrarService;

public class ActivityTelaRegistrarUsuario extends AppCompatActivity {
    List<Organizacao> listaDeOrganizacoes = new ArrayList();
    List<String> listaDeNomesOrganizacoes = new ArrayList<>();
    int idOrganizacaoSelecionada;
    TextInputLayout nomeTextInput, emailTextInput, senhaTextInput;
    String nome, senha, email;
    Button botaoFinalizarCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_registrar_usuario);
        botaoFinalizarCadastro = findViewById(R.id.btnFinalizarCadastro);
        final Spinner spinner = findViewById(R.id.spnEmpresa);
        nomeTextInput = findViewById(R.id.et_nome);
        emailTextInput = findViewById(R.id.et_email);
        senhaTextInput = findViewById(R.id.et_senha);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) {
                    idOrganizacaoSelecionada = listaDeOrganizacoes.get(position - 1).getId();
                    botaoFinalizarCadastro.setEnabled(true);
                } else {
                    botaoFinalizarCadastro.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        botaoFinalizarCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nome = nomeTextInput.getEditText().getText().toString();
                email = emailTextInput.getEditText().getText().toString().trim();
                senha = senhaTextInput.getEditText().getText().toString().trim();

                if (TextUtils.isEmpty(nome))
                    nomeTextInput.setError("o campo nome é obrigatorio");
                else if (TextUtils.isEmpty(email)) {
                    emailTextInput.setError("o campo email é obrigatorio");
                    nomeTextInput.setErrorEnabled(false);
                } else if (TextUtils.isEmpty(senha)) {
                    senhaTextInput.setError("o campo senha é obrigatorio");
                    nomeTextInput.setErrorEnabled(false);
                    emailTextInput.setErrorEnabled(false);
                } else {
                    nomeTextInput.setErrorEnabled(false);
                    emailTextInput.setErrorEnabled(false);
                    senhaTextInput.setErrorEnabled(false);

                    JSONObject usuarioJson = new JSONObject();

                    try {
                        usuarioJson.put("email", email);
                        usuarioJson.put("nome", nome);
                        usuarioJson.put("senha", senha);
                        usuarioJson.put("idOrganizacao", idOrganizacaoSelecionada);

                        String userCod = (Base64.encodeToString(usuarioJson.toString().getBytes("UTF-8"), Base64.NO_WRAP));

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
                }
            }
        });
        emailTextInput.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String emailAfterTextChange = emailTextInput.getEditText().getText().toString();
                    if (emailAfterTextChange.contains("@")) {
                        String[] emailCompleto = emailAfterTextChange.split("@");
                        if (emailCompleto.length > 0) {
                            String dominio = emailCompleto[1];
                            if (dominio.contains(".")) {
                                try {
                                    String listaOrganizacao = new OrganizacaoService().execute(dominio).get();
                                    System.out.println("dominio: " + dominio);
                                    System.out.println("organizacoes retornadas " + listaOrganizacao);
                                    if (listaOrganizacao.length() <= 2) {
                                        spinner.setVisibility(View.GONE);
                                        emailTextInput.setError("Nenhuma empresa foi encontrada com o dominio informado.");
                                        botaoFinalizarCadastro.setEnabled(false);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void parseOrganizacoesArray(String organizacoesString, Spinner spinner) {
        try {
            listaDeNomesOrganizacoes.clear();
            listaDeOrganizacoes.clear();
            JSONArray jsonArray = new JSONArray(organizacoesString);
            if (jsonArray.length() > 0) {
                listaDeNomesOrganizacoes.add("Selecione a sua organização");
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
                    }
                }
                if (listaDeOrganizacoes.size() == 1) {
                    idOrganizacaoSelecionada = listaDeOrganizacoes.get(0).getId();
                    botaoFinalizarCadastro.setEnabled(true);
                    spinner.setVisibility(View.GONE);
                } else {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ActivityTelaRegistrarUsuario.this, android.R.layout.simple_spinner_item, listaDeNomesOrganizacoes);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    emailTextInput.setErrorEnabled(false);
                    spinner.setVisibility(View.VISIBLE);
                }
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }
}

package alura.com.telalogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import alura.com.services.LoginService;

public class ActivityTelaLogin extends AppCompatActivity {
    SharedPreferences prefs;
    TextInputLayout entradaEmail, entradaSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);

        if (prefs.contains("userEmail")) {
            Intent it = new Intent(ActivityTelaLogin.this, ActivityTelaPrincipal.class);
            startActivity(it);
        }

        setContentView(R.layout.tela_login);
        final Button botaoEntrar = findViewById(R.id.btnEntrar);

        Button botaoRegistrar = (Button) findViewById(R.id.btnRegistrar);
        botaoRegistrar.setOnClickListener((new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(ActivityTelaLogin.this, ActivityTelaRegistrarUsuario.class);
                startActivity(it);
            }
        }));

        botaoEntrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    entradaEmail = findViewById(R.id.et_email);
                    entradaSenha = findViewById(R.id.et_senha);
                    String emailString = entradaEmail.getEditText().getText().toString().trim();
                    String senhaString = entradaSenha.getEditText().getText().toString().trim();

                    if (TextUtils.isEmpty(emailString))
                        entradaEmail.setError("o campo email é obrigatorio");
                    else if (TextUtils.isEmpty(senhaString))
                        entradaSenha.setError("o campo senha é obrigatorio");
                    else {
                        String loginReturn = new LoginService().execute(emailString, senhaString).get();
                        if (loginReturn.length() > 0) {

                            JSONObject usuarioJSON = new JSONObject(loginReturn);

                            if (usuarioJSON.has("email") && usuarioJSON.has("id") && usuarioJSON.has("nome") && usuarioJSON.has("idOrganizacao")) {
                                int id = usuarioJSON.getInt("id");
                                String nome = usuarioJSON.getString("nome");
                                String email = usuarioJSON.getString("email");

                                JSONObject organizacao = usuarioJSON.getJSONObject("idOrganizacao");
                                String nomeOrganizacao = organizacao.getString("nome");
                                String tipoOrganizacao = organizacao.getString("tipoOrganizacao");
                                int idOrganizacao = organizacao.getInt("id");


                                prefs = getSharedPreferences("USER_DATA",
                                        Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();

                                editor.putString("userEmail", email);
                                editor.putString("userName", nome);
                                editor.putString("userId", Integer.toString(id));
                                editor.putString("userIdOrganizacao", Integer.toString(idOrganizacao));
                                editor.putString("userNomeEmpresa", nomeOrganizacao);
                                editor.putString("userTipoEmpresa", tipoOrganizacao);
                                editor.commit();
                            }
                            Toast.makeText(ActivityTelaLogin.this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show();
                            Intent it = new Intent(ActivityTelaLogin.this, ActivityTelaPrincipal.class);
                            startActivity(it);
                        } else {
                            Toast.makeText(ActivityTelaLogin.this, "Credenciais Inválidas!", Toast.LENGTH_SHORT).show();

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

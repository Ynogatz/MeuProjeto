package alura.com.telalogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.Principal;

import alura.com.services.LoginService;

public class ActivityTelaLogin extends AppCompatActivity {
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
//        if(prefs.contains("email")){
//            Intent it = new Intent(ActivityTelaLogin.this, ActivityTelaPrincipal.class);
//            startActivity(it);
//        }
        setContentView(R.layout.tela_login);
        final Button botaoEntrar = findViewById(R.id.btnEntrar);

        Button botaoVoltar = (Button) findViewById(R.id.btnVoltar);
        botaoVoltar.setOnClickListener((new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(ActivityTelaLogin.this, ActivityTelaInicial.class);
                startActivity(it);
            }
        }));
        Button botaoheu = (Button) findViewById(R.id.heuheu);
        botaoheu.setOnClickListener((new View.OnClickListener() {
            //botao atalho pra tela de usuario logado
            public void onClick(View v) {
                Intent it = new Intent(ActivityTelaLogin.this, ActivityTelaPrincipal.class);
                startActivity(it);
            }
        }));

        botaoEntrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    final EditText entradaEmail = findViewById(R.id.etLogin);
                    final EditText entradaSenha = findViewById(R.id.etSenha);
                    String emailString = entradaEmail.getText().toString().trim();
                    String senhaString = entradaSenha.getText().toString().trim();

                    if (TextUtils.isEmpty(emailString))
                        entradaEmail.setError("o campo email é obrigatorio");
                    else if (TextUtils.isEmpty(senhaString))
                        entradaSenha.setError("o campo senha é obrigatorio");
                    else {
                        String loginReturn = new LoginService().execute(emailString, senhaString).get();
                        if (loginReturn.equals("Login efetuado com sucesso!")) {


                            prefs = getSharedPreferences("USER_DATA",
                                    Context.MODE_PRIVATE);
                            SharedPreferences.Editor ed = prefs.edit();

                            ed.putString("email", emailString);
                            ed.commit();
                            String emailRecuperado = prefs.getString("email", null);
                            System.out.println("email recuperado : " + emailRecuperado);

                            exibirMensagem(loginReturn);
                            Intent it = new Intent(ActivityTelaLogin.this, ActivityTelaPrincipal.class);
                            startActivity(it);
                        } else {
                            exibirMensagem(loginReturn);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void exibirMensagem(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();
    }
}

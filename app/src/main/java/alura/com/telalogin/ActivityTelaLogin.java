package alura.com.telalogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.Principal;

import alura.com.services.LoginService;

public class ActivityTelaLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_login);
        final Button botaoEntrar = findViewById(R.id.btnEntrar);

        Button botaoVoltar = (Button) findViewById(R.id.btnVoltar);
        botaoVoltar.setOnClickListener((new View.OnClickListener(){
            public void onClick(View v){
                Intent it = new Intent(ActivityTelaLogin.this, ActivityTelaInicial.class);
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
                    else
                        exibirMensagem(new LoginService().execute(emailString, senhaString).get());

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }/*{
            @Override
            public void onClick(View v) {
                try {
                    String emailString = entradaEmail.getText().toString().trim();
                    String senhaString = entradaSenha.getText().toString().trim();

                    exibirMensagem(new LoginService().execute(emailString, senhaString).get());
                }
            }
        }*/);
    }


    public void exibirMensagem(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();
    }
}

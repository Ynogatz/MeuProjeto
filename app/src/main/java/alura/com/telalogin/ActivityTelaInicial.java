package alura.com.telalogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityTelaInicial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_inicial);
        Button botaobotao = (Button) findViewById(R.id.button);
        botaobotao.setOnClickListener((new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(ActivityTelaInicial.this, ActivityTelaPrincipal.class);
                startActivity(it);
            }
        }));
        //comando para chamar a outra tela
        Button botaoRegistra = (Button) findViewById(R.id.btnRegistrar);
        botaoRegistra.setOnClickListener((new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(ActivityTelaInicial.this, ActivityRegistrarUsuario.class);
                startActivity(it);
            }
        }));

        Button botaoLogin = (Button) findViewById(R.id.btnLogin);
        botaoLogin.setOnClickListener((new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(ActivityTelaInicial.this, ActivityTelaLogin.class);
                startActivity(it);

            }
        }));
    }
}

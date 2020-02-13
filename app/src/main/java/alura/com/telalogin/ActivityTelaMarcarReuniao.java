package alura.com.telalogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityTelaMarcarReuniao extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_marcar_reuniao);

        Intent it = getIntent();
        String parametro = (String) it.getSerializableExtra("nome");
        TextView nome = (TextView) findViewById(R.id.tv_nome_sala);
        nome.setText(parametro);

//        int parametro2 = (int) it.getSerializableExtra("id");
//        TextView id = (TextView) findViewById(R.id.tv_id);
//        nome.setText(parametro2);

        Button botaoVoltar = (Button) findViewById(R.id.btn_voltar);
        botaoVoltar.setOnClickListener((new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(ActivityTelaMarcarReuniao.this, ActivityTelaPrincipal.class);
                startActivity(it);
            }
        }));
    }
}

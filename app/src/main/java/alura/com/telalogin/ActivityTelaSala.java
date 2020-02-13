package alura.com.telalogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import alura.com.modelo.Sala;

public class ActivityTelaSala extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_sala);

        Intent it = getIntent();
        Sala parametro = (Sala) it.getSerializableExtra("sala");
        TextView nome = (TextView) findViewById(R.id.tv_nome_sala);
        nome.setText(parametro.getNome());

        TextView pessoasSentadas = (TextView) findViewById(R.id.tv_quantidadePessoasSentadas);
        pessoasSentadas.setText(String.valueOf(parametro.getQuantidadePessoasSentadas()));

        TextView areaDaSala = (TextView) findViewById(R.id.tv_areaTotalDaSala);
        areaDaSala.setText(String.valueOf(parametro.getAreaDaSala()));

        TextView possuiArcondicionado = (TextView) findViewById(R.id.tv_possuiArcondicionado);
        possuiArcondicionado.setText(String.valueOf(parametro.isPossuiArcon()));

        TextView possuiMultimidia = (TextView) findViewById(R.id.tv_possuiMultimidia);
        possuiMultimidia.setText(String.valueOf(parametro.isPossuiMultimidia()));




        Button botaoVoltar = (Button) findViewById(R.id.btn_voltar);
        botaoVoltar.setOnClickListener((new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(ActivityTelaSala.this, ActivityTelaPrincipal.class);
                startActivity(it);
            }
        }));

        FloatingActionButton fabAdicionarReservar = (FloatingActionButton) findViewById(R.id.fab_adicionar_reserva);
        fabAdicionarReservar.setOnClickListener((new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(ActivityTelaSala.this, ActivityTelaReservarSala.class);
                startActivity(it);
            }
        }));
    }
}

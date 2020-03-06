package alura.com.telalogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import alura.com.modelo.Reserva;

public class ActivityTelaInfoReserva extends AppCompatActivity {
    Reserva reserva = new Reserva();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_info_reserva);

        Intent it = getIntent();

        reserva = (Reserva) it.getSerializableExtra("reservas");
        TextView nomeOrganizador = findViewById(R.id.tv_nome_completo);
        nomeOrganizador.setText(reserva.getNomeOrganizador());

        reserva = (Reserva) it.getSerializableExtra("reservas");
        TextView dataHoraInicio = findViewById(R.id.tv_data_hora_inicio);
        dataHoraInicio.setText(reserva.getDataHoraInicio());

        reserva = (Reserva) it.getSerializableExtra("reservas");
        TextView dataHoraFim = findViewById(R.id.tv_data_hora_fim);
        dataHoraFim.setText(reserva.getDataHoraFim());

        reserva = (Reserva) it.getSerializableExtra("reservas");
        TextView descricao = findViewById(R.id.tv_descricoes);
        descricao.setText(reserva.getDescricao());

    }
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}


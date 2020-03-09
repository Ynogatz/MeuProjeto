package alura.com.telalogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import alura.com.modelo.Reserva;

public class ActivityTelaInfoReserva extends AppCompatActivity {
    Reserva reserva = new Reserva();
    String horarioInicio, horarioFim;

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
        String horaInicioFormatada = reserva.getDataHoraInicio();
        TextView dataHoraInicio = findViewById(R.id.tv_data_hora_inicio);

        if (horaInicioFormatada.contains("Z")) {
            String[] horarioNaoParciado = horaInicioFormatada.split("Z");
            if (horarioNaoParciado.length > 0) {
                String dataEHoraRow = horarioNaoParciado[0];
                horarioInicio = dataEHoraRow.split("T")[1];
            }
        }  dataHoraInicio.setText(horarioInicio);


        reserva = (Reserva) it.getSerializableExtra("reservas");
        String horaFimFormatada = reserva.getDataHoraFim();
        TextView dataHoraFim = findViewById(R.id.tv_data_hora_fim);

        if (horaFimFormatada.contains("Z")) {
            String[] horarioNaoParciado = horaFimFormatada.split("Z");
            if (horarioNaoParciado.length > 0) {
                String dataEHoraRow = horarioNaoParciado[0];
                horarioFim = dataEHoraRow.split("T")[1];
            }
        }  dataHoraFim.setText(horarioFim);

        reserva = (Reserva) it.getSerializableExtra("reservas");
        TextView descricao = findViewById(R.id.tv_descricoes);
        descricao.setText(reserva.getDescricao());

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}


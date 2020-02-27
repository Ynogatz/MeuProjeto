package alura.com.telalogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import alura.com.modelo.Reserva;

public class ActivityTelaInfoReserva extends AppCompatActivity {
    Reserva reserva = new Reserva();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_reserva);

        Intent it = getIntent();

        reserva = (Reserva) it.getSerializableExtra("reservas");
        TextView nomeOrganizador = findViewById(R.id.tv6);
        nomeOrganizador.setText(reserva.getNomeOrganizador());

        reserva = (Reserva) it.getSerializableExtra("reservas");
        TextView dataHoraInicio = findViewById(R.id.tv1);
        dataHoraInicio.setText(reserva.getDataHoraInicio());

        reserva = (Reserva) it.getSerializableExtra("reservas");
        TextView dataHoraFim = findViewById(R.id.tv2);
        dataHoraFim.setText(reserva.getDataHoraFim());

        reserva = (Reserva) it.getSerializableExtra("reservas");
        TextView descricao = findViewById(R.id.tv3);
        descricao.setText(reserva.getDescricao());
    }
}

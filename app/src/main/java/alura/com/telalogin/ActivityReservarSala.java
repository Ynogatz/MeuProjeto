package alura.com.telalogin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class ActivityReservarSala extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_reservar_sala);
        Button dateButton = findViewById(R.id.btn_selecionar_data);
        Button timeButton = findViewById(R.id.btn_selecionar_horario);
        TextView dateTextView = findViewById(R.id.tv_visualizar_data);
        TextView timeTextView = findViewById(R.id.tv_visualizar_horario);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDateButton();
            }
        });
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleTimeButton();
            }
        });

    }

    private void handleDateButton() {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int date) {
                TextView data = findViewById(R.id.tv_visualizar_data);
                String dateString = "Ano: " + year + " Mês " + month + " Dia " + date;
                data.setText(dateString);
            }
        }, YEAR, MONTH, DATE);
        datePickerDialog.show();
    }

    private void handleTimeButton() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                TextView horario = findViewById(R.id.tv_visualizar_horario);
                String timeString = "Hora :" + hour + " Minuto " + minute;
                horario.setText(timeString);
            }
        }, HOUR, MINUTE, true);
        timePickerDialog.show();
    }

}
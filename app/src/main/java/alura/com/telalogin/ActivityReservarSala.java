package alura.com.telalogin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ActivityReservarSala extends AppCompatActivity {
    SharedPreferences prefs;
    int idSala;
    Button botaoConfirma;
    String anoMesDia, horaMinutoInicio, horaMinutoFim, descricao, dateStrInicio, dateStrFim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_reservar_sala);
        Button dateButton = findViewById(R.id.btn_selecionar_data);
        Button timeButtonInicio = findViewById(R.id.btn_selecionar_horario_inicio);
        Button timeButtonFim = findViewById(R.id.btn_selecionar_horario_fim);
        final EditText etDescricao = findViewById(R.id.et_descricao);


        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDateButton();
            }
        });
        timeButtonInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleTimeButtonInicio();
            }
        });
        timeButtonFim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleTimeButtonFim();
            }
        });
        botaoConfirma = findViewById(R.id.btn_confirma);

        botaoConfirma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idSala = getIntent().getIntExtra("idSala", 0);
                prefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                int userId = Integer.parseInt(prefs.getString("userId", null));
                if (etDescricao.getText().length() < 1) {
                    Toast.makeText(ActivityReservarSala.this, "Descricao deve ser preenchida", Toast.LENGTH_SHORT).show();
                } else {
                    descricao = etDescricao.getText().toString();
                }
                if (anoMesDia == null) {
                    Toast.makeText(ActivityReservarSala.this, "selecione a data", Toast.LENGTH_SHORT).show();
                } else if (horaMinutoInicio == null) {
                    Toast.makeText(ActivityReservarSala.this, "Selecione o horario de inicio", Toast.LENGTH_SHORT).show();
                } else if (horaMinutoFim == null) {
                    Toast.makeText(ActivityReservarSala.this, "Selecione o horario de fim", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println(anoMesDia + " - " + horaMinutoInicio + " - " + horaMinutoFim);

                    dateStrInicio = (anoMesDia + " " + horaMinutoInicio);


                    try {
                        Date dateInicio = new SimpleDateFormat("dd/MM/YYYY HH:mm").parse(dateStrInicio);
                        Long dateInicioEpoch = dateInicio.getTime();
                        System.out.println(dateInicioEpoch);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void handleDateButton() {
        Calendar calendar = Calendar.getInstance();
        final int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int date) {
                TextView data = findViewById(R.id.tv_visualizar_data);
                final String dateString = "Ano: " + year + " Mês " + (month + 1) + " Dia " + date;
                String monthStr;
                String dateStr = null;

                if (month < 10) {
                    monthStr = "0" + (month);
                } else {
                    monthStr = String.valueOf(month);
                }

                if (date < 10) {
                    dateStr = "0" + (date);
                } else {
                    dateStr = String.valueOf(date);
                }
                anoMesDia = (dateStr + "/" + monthStr + "/" + year);
                data.setText(dateString);


            }
        }, YEAR, MONTH, DATE);
        datePickerDialog.show();
    }

    private void handleTimeButtonInicio() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                final TextView horario = findViewById(R.id.tv_visualizar_horario_inicio);
                String hourStr = null;
                String minuteStr = null;
                if (hour < 10) {
                    hourStr = "0" + String.valueOf(hour);
                } else {
                    hourStr = String.valueOf(hour);
                }
                if (minute < 10) {
                    minuteStr = "0" + String.valueOf(minute);
                } else {
                    minuteStr = String.valueOf(minute);
                }
                final String timeString = "Horario Inicio :" + hour + ":" + minute;
                horaMinutoInicio = (hourStr + ":" + minuteStr);
                horario.setText(timeString);

            }
        }, HOUR, MINUTE, true);
        timePickerDialog.show();
    }

    private void handleTimeButtonFim() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                final TextView horario = findViewById(R.id.tv_visualizar_horario_fim);
                String hourStr = null;
                String minuteStr = null;
                if (hour < 10) {
                    hourStr = "0" + String.valueOf(hour);
                } else {
                    hourStr = String.valueOf(hour);
                }
                if (minute < 10) {
                    minuteStr = "0" + String.valueOf(minute);
                } else {
                    minuteStr = String.valueOf(minute);
                }
                final String timeString = "Horario Fim :" + hour + ":" + minute;
                horaMinutoFim = (hourStr + ":" + minuteStr);
                horario.setText(timeString);

            }
        }, HOUR, MINUTE, true);
        timePickerDialog.show();
    }

}
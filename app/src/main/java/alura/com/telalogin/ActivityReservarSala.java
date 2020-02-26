package alura.com.telalogin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import alura.com.services.CadastroReservaService;

public class ActivityReservarSala extends AppCompatActivity {
    SharedPreferences prefs;
    int idSala;
    Button botaoConfirma;
    String anoMesDia, horaMinutoInicio, horaMinutoFim, dateStrInicio, dateStrFim, descricao;
    Long dateInicioEpoch, dateFimEpoch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_reservar_sala);
        Button dateButton = findViewById(R.id.btn_selecionar_data);
        Button timeButtonInicio = findViewById(R.id.btn_selecionar_horario_inicio);
        Button timeButtonFim = findViewById(R.id.btn_selecionar_horario_fim);
        final EditText etDescricao = findViewById(R.id.et_descricao);
        Button botaoVoltar = findViewById(R.id.btnRegistrar);
        botaoVoltar.setOnClickListener((new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(ActivityReservarSala.this, ActivityTelaPrincipal.class);
                startActivity(it);
            }
        }));

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
                    dateStrInicio = (anoMesDia + " " + horaMinutoInicio);
                    dateStrFim = (anoMesDia + " " + horaMinutoFim);

                    try {
                        Date dateInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dateStrInicio);
                        dateInicioEpoch = dateInicio.getTime();
                        Date dateFim = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dateStrFim);
                        dateFimEpoch = dateFim.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    JSONObject reservaJson = new JSONObject();
                    try {

//                        int usuarioId = Integer.valueOf(userId);
//                        int salaId = Integer.valueOf(idSala);
//                        String descricaoStr = String.valueOf(descricao);
//                        long dataInicio = Long.valueOf(dateInicioEpoch);
//                        long dataFinalString = Long.valueOf(dateFimEpoch);


                        int usuarioId = userId;
                        int salaId = idSala;
                        String descricaoStr = descricao;


                        reservaJson.put("id_usuario", usuarioId);
                        reservaJson.put("id_sala", salaId);
                        reservaJson.put("descricao", descricaoStr);
                        reservaJson.put("data_hora_inicio", dateInicioEpoch - 10800000);
                        reservaJson.put("data_hora_fim", dateFimEpoch - 10800000);


                        String reservaCod = new String(Base64.encodeToString(reservaJson.toString().getBytes("UTF-8"), Base64.NO_WRAP));
                        Toast.makeText(ActivityReservarSala.this, new CadastroReservaService().execute(reservaCod).get(), Toast.LENGTH_SHORT).show();
                    } catch (
                            JSONException e) {
                        e.printStackTrace();
                    } catch (
                            UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (
                            Exception e) {

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
                String dateStr;
                if (month < 10) {
                    monthStr = "0" + (month+1);
                } else {
                    monthStr = String.valueOf(month+1);
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
                String hourStr;
                String minuteStr;
                if (hour < 10) {
                    hourStr = "0" + (hour);
                } else {
                    hourStr = String.valueOf(hour);
                }
                if (minute < 10) {
                    minuteStr = "0" + (minute);
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
                String hourStr;
                String minuteStr;
                if (hour < 10) {
                    hourStr = "0" + (hour);
                } else {
                    hourStr = String.valueOf(hour);
                }
                if (minute < 10) {
                    minuteStr = "0" + (minute);
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
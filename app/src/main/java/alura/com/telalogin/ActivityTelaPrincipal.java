package alura.com.telalogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import alura.com.services.SalaService;
import alura.com.telalogin.modelo.Sala;

public class ActivityTelaPrincipal extends AppCompatActivity {
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_principal);
        prefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        FloatingActionButton addReserva = (FloatingActionButton) findViewById(R.id.fabAddReserva);
        try {
            String loginReturn = new SalaService().execute("1").get();

        } catch (Exception e) {
            e.printStackTrace();
        }
        addReserva.setOnClickListener((new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(ActivityTelaPrincipal.this, ActivityTelaReservarSala.class);
                startActivity(it);
            }
        }));
        Button botaoLogout = (Button) findViewById(R.id.btnLogout);
        botaoLogout.setOnClickListener((new View.OnClickListener() {
            public void onClick(View v) {

                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("userEmail");
                editor.remove("userName");
                editor.remove("userId");
                editor.remove("userIdOrganizacao");
                editor.remove("userNomeEmpresa");
                editor.remove("userTipoEmpresa");
                editor.commit();
                Intent it = new Intent(ActivityTelaPrincipal.this, ActivityTelaInicial.class);
                startActivity(it);
            }
        }));

    }
}


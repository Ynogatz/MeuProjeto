package alura.com.telalogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

import alura.com.services.CadastroService;
import alura.com.services.ServiceDominio;

public class ActivityRegistrarUsuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_registrar_usuario);
        final EditText entradaNome = findViewById(R.id.etEntradaNomeUsuario);
        final EditText entradaEmail = findViewById(R.id.etEntradaemail);
        final EditText entradaSenha = findViewById(R.id.etEntradasenha);
        final EditText entradaConfirmaSenha = findViewById(R.id.etEntradaConfirmaSenha);
        final Button botaoFinalizarCadastro = findViewById(R.id.btnFinalizarCadastro);


        botaoFinalizarCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                JSONObject usuarioJson = new JSONObject();

                try {
                    String nomeString = entradaNome.getText().toString();
                    String emailString = entradaEmail.getText().toString();
                    String senhaString = entradaSenha.getText().toString();
                    usuarioJson.put("email", emailString);
                    usuarioJson.put("nome", nomeString);
                    usuarioJson.put("senha", senhaString);

                    String novoUsuarioDecode;
                    String userCod = new String(Base64.encodeToString(usuarioJson.toString().getBytes("UTF-8"), Base64.NO_WRAP));
                    System.out.println(usuarioJson.toString());

                    exibirMensagem(new CadastroService().execute(userCod).get());

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (Exception e) {

                }
            }
        });

        entradaEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String emailAfterTextChange = entradaEmail.getText().toString();
                    if (emailAfterTextChange.contains("@")) {
                        String[] emailCompleto = emailAfterTextChange.split("@");
                        if (emailCompleto.length > 0) {
                            String dominio = emailCompleto[1];
                            if (dominio.contains(".")) {
                                try {
                                    String listaOrganizacao = new ServiceDominio().execute(dominio).get();
                                    System.out.println("dominio: " + dominio);
                                    System.out.println("dominio retorno " + listaOrganizacao);
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                System.out.println(dominio);

                            }
                        }

                    }
                }
            }
        });
    }

    public void exibirMensagem(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();
    }
}

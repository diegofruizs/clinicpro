package app.hospital.uniandes.edu.co.clinic;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import app.hospital.uniandes.edu.co.clinic.dtos.PersonDTO;
import app.hospital.uniandes.edu.co.clinic.http.HttpRequest;
import app.hospital.uniandes.edu.co.clinic.http.HttpRequest.HttpRequestException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;



public class PacienteActivity extends AppCompatActivity {

    Button btnBuscar;
    Button btnServicios;
    EditText editTextDocumento;
    EditText editTextApellidos;
    EditText editTextNombres;
    EditText editTextDireccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente);

        btnBuscar = (Button) findViewById(R.id.btnBuscar);
        btnServicios = (Button) findViewById(R.id.btnBuscarServicios);

        editTextDocumento = (EditText) findViewById(R.id.editTextDocumento);
        editTextApellidos = (EditText) findViewById(R.id.editTextApellidos);
        editTextNombres = (EditText) findViewById(R.id.editTextNombres);
        editTextDireccion = (EditText) findViewById(R.id.editTextDireccion);
        editTextApellidos.setEnabled(false);
        editTextNombres.setEnabled(false);
        editTextDireccion.setEnabled(false);

        btnBuscar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Hola " + editTextDocumento.getText(),
                        Toast.LENGTH_LONG).show();

                int numero=1000;
                //Consumiendo el servicio REST
                String url = String.format(
                        "http://localhost:8080/hospital.logic/api/persons/1000",numero);
                new consumeRest().execute(url);

            }
        });

        btnServicios.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                editTextApellidos.setText("Ruiz Sanchez");
                Toast.makeText(getApplicationContext(), "Ir a los servicios ",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(v.getContext(), ServiciosActivity.class);
                intent.putExtra("documento", editTextDocumento.getText().toString());
                intent.putExtra("nombres", editTextNombres.getText().toString());
                startActivity(intent);
            }
        });

    }

    public static final String TAG = "co.edu.uniandes.rest.hospital.resources";

    private class consumeRest extends AsyncTask<String, Long, String> {
        protected String doInBackground(String... urls) {
            try {
                return HttpRequest.get(urls[0]).accept("application/json")
                        .body();
            } catch (HttpRequest.HttpRequestException exception) {
                return null;
            }
        }

        protected void onPostExecute(String response) {
            System.out.println("------------------------------");
            System.out.println(response);
          /*  List<PersonDTO> persons = getPersons(response);
            Toast.makeText(getApplicationContext(), "... "+persons.get(0),
                    Toast.LENGTH_LONG).show();*/
        }
    }

    private List<PersonDTO> getPersons(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<PersonDTO>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    private String prettyfyJSON(String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);
        return gson.toJson(element);
    }
}

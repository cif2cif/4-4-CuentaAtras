package es.upm.dit.adsw;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private ProgressBar progressBar;
    private TextView textProgress;
    private TextView textResultado;
    private EditText segundosEdit;
    private Button botonTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        segundosEdit = (EditText) findViewById(R.id.segundos);

        botonTask = (Button) findViewById(R.id.botonTask);
        botonTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Integer milisegundos = Integer.valueOf(segundosEdit.getText().toString());
                    progressBar.setProgress(0); // reinicio a 0
                    progressBar.setMax(milisegundos); // máximo tamaño de la barra
                    new CuentaAtrasTask().execute(milisegundos);
                }catch (Exception e) {
                    String msg = "Debe introducir un número entero";
                    Log.e(TAG, msg + e.getMessage());
                    Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                }
            }
        });


        Button botonMuestra = (Button) findViewById(R.id.boton);
        botonMuestra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Ejecuto el botón "
                        + System.currentTimeMillis(), Toast.LENGTH_SHORT).show();
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textProgress = (TextView) findViewById(R.id.textProgress);
        textResultado = (TextView) findViewById(R.id.resultado);
    }

    private class CuentaAtrasTask extends AsyncTask<Integer, String, Long> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            textProgress.setVisibility(View.VISIBLE);
            segundosEdit.setEnabled(false);
            botonTask.setEnabled(false);
            textResultado.setVisibility(View.GONE);
        }

        @Override
        protected Long doInBackground(Integer... params) {
            publishProgress("Procesando...");
            long t1 = System.nanoTime();
            try {
                int tiempo = params[0];
                for (int i = 0; i < tiempo; i++) {
                    publishProgress("Procesando ..." + i + " de " + tiempo);
                    Thread.sleep(i);
                }
                publishProgress("Terminado");

            }catch (Exception e){
                Log.e(TAG, "Error en tarea de fondo " + e.getMessage());
            }
            long t2 = System.nanoTime();
            return t2 - t1;

        }

        @Override
        protected void onProgressUpdate(String... values) {
            progressBar.incrementProgressBy(1);
            textProgress.setText(values[0]);

        }

        @Override
        protected void onPostExecute(Long aLong) {
            progressBar.setVisibility(View.GONE);
            textProgress.setVisibility(View.GONE);
            textResultado.setText("Ha tardado " + aLong);
            textResultado.setVisibility(View.VISIBLE);
            segundosEdit.setText(""); // limpio campo
            segundosEdit.setEnabled(true);
            botonTask.setEnabled(true);
        }
    }
}


package com.example.alex.domotica20;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.hardware.Camera;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    int contador = 0;
    public final static String EXTRA_MESSAGE = null;
    private ContentResolver cResolver;
    private int brillo;
    private Camera camera;
    private Window window;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((SeekBar) findViewById(R.id.seekBar)).setOnSeekBarChangeListener(this);
        cResolver = getContentResolver();
        window = getWindow();
    }


    public void EncenderOnClick(View v) {


        final Button bEncender = (Button) findViewById(R.id.botonOnOff);

        contador++;

        if (contador % 2 == 0) {

            Toast.makeText(getBaseContext(), "Apagado", Toast.LENGTH_LONG).show();
            bEncender.setText("On");
            bEncender.setBackgroundColor(Color.GREEN);

            camera.stopPreview();
            camera.release();

        } else {
            Toast.makeText(getBaseContext(), "Encendido", Toast.LENGTH_LONG).show();
            bEncender.setText("Off");
            bEncender.setBackgroundColor(Color.RED);
            Vibrator vib = (Vibrator) MainActivity.this.getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            vib.vibrate(500);
            camera = Camera.open();
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
            camera.startPreview();
        }


    }

    public void forResultOnclick(View v) {
        Intent intent = new Intent(this, DisplayMessageActivity1.class);
        startActivityForResult(intent, 1);
    }

    public void VentanaMovimiento(View v) {
        Intent intent = new Intent(this, Animaciones.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                TextView editText = (TextView) findViewById(R.id.textovuelta);
                editText.setText(data.getStringExtra("resultado"));

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void mandarMensaje(View v) {

        Intent intent = new Intent(this, DisplayMessageActivity1.class);
        TextView editText = (TextView) findViewById(R.id.edit_mensaje);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        try {
            brillo = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (i <= 20) {
            brillo = 20;
        } else {
            brillo = i;
        }
        float perc = (brillo / (float) 255) * 100;

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brillo);

        WindowManager.LayoutParams layoutpars = window.getAttributes();

        layoutpars.screenBrightness = brillo / (float) 255;

        window.setAttributes(layoutpars);
    }
}



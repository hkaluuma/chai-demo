package www.hilla.com.chai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CreatePatientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_patient);

        //make referrence to the spinners from the XML
        Spinner spinnerdisease = findViewById(R.id.spinner_disease);
        Spinner spinnercity = findViewById(R.id.spinner_city);
        //spinner data source
        String[] disease_array = {"Covid 19","Malaria","Cancer","Typhoid","Hepatitis B"};
        String[] city_array = {"Kampala","Nairobi","Dar es Salaam","Kigali","Juba","Bujumbura"};

        //Configure and array adapter to glue everything together
        ArrayAdapter<String> diseaseAdapter = new ArrayAdapter<String>(CreatePatientActivity.this,R.layout.spinner_item_design,R.id.spinnertextvew,disease_array);

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(CreatePatientActivity.this,R.layout.spinner_item_design, R.id.spinnertextvew, city_array);


        //Assigning arrayAdapter to my spinner
        spinnerdisease.setAdapter(diseaseAdapter);
        spinnercity.setAdapter(cityAdapter);



    }
}
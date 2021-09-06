package www.hilla.com.chai;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CreatePatientActivity extends AppCompatActivity {

    //global variables
    String selected_disease, selected_city, patientnames, p_password, p_age, p_phonenumber, p_email, p_comments;
    String addpatient_url = "http://192.168.43.20/chai/addpatient.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_patient);

        //make referrence to the spinners from the XML
        Spinner spinnerdisease = findViewById(R.id.spinner_disease);
        Spinner spinnercity = findViewById(R.id.spinner_city);

        //spinner data source
        final String[] disease_array = {"Covid 19","Malaria","Cancer","Typhoid","Hepatitis B"};
        final String[] city_array = {"Kampala","Nairobi","Dar es Salaam","Kigali","Juba","Bujumbura"};

        //Configure and array adapter to glue everything together
        ArrayAdapter<String> diseaseAdapter = new ArrayAdapter<String>(CreatePatientActivity.this,R.layout.spinner_item_design,R.id.spinnertextvew,disease_array);

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(CreatePatientActivity.this,R.layout.spinner_item_design, R.id.spinnertextvew, city_array);


        //Assigning arrayAdapter to my spinner
        spinnerdisease.setAdapter(diseaseAdapter);
        spinnercity.setAdapter(cityAdapter);

        //adding selecteditem listener for disease
        spinnerdisease.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_disease = disease_array[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected_disease = "nothing selected";

            }
        });

        //selected item listener for the selected city
        spinnercity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_city = city_array[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected_city = "nothing selected";
            }
        });


        Button submitbtn = findViewById(R.id.register_patient);


        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make reference from the front end or other views
                EditText edtx_patientNames = findViewById(R.id.edtx_patientNames);
                EditText edtx_password = findViewById(R.id.editext_pasword);
                EditText edtx_age = findViewById(R.id.edtx_age);
                EditText contact = findViewById(R.id.edtx_contact);
                EditText email = findViewById(R.id.edtx_email);
                EditText othercomments = findViewById(R.id.edtx_othercommments);
                //input validation
                //capture these variables from the class objects that got them from front end
                patientnames = edtx_patientNames.getText().toString();
                p_password = edtx_password.getText().toString();
                p_age = edtx_age.getText().toString();
                p_phonenumber = contact.getText().toString();
                p_email = email.getText().toString();
                p_comments = othercomments.getText().toString();


                if (patientnames.isEmpty()){
                    edtx_patientNames.setError("Patient names are required");
                } else if (p_password.isEmpty()){
                    edtx_password.setError("Password is required");
                } else if (p_age.isEmpty()){
                    edtx_age.setError("Age is missing");
                } else if (p_phonenumber.isEmpty()){
                    contact.setError("Contact is missing");
                } else if (p_email.isEmpty()){
                    email.setError("Email is required");
                } else if (p_comments.isEmpty()){
                    othercomments.setError("Comments are required");
                } else if (selected_disease.equals("nothing selected")){
                    Toast.makeText(CreatePatientActivity.this, "No disease selected", Toast.LENGTH_SHORT).show();
                } else if (selected_city.equals("nothing selected")){
                    Toast.makeText(getApplicationContext(), "No city selected", Toast.LENGTH_SHORT).show();
                } else {
                    //variable for network connection
                    boolean checknetwork = haveNetworkConnection();
                    if(checknetwork){
                        Toast.makeText(getApplicationContext(), "You are connected to internet", Toast.LENGTH_SHORT).show();
                        //execute the async class
                        new CreatePatientAsync().execute();
                    }else{
                        Toast.makeText(getApplicationContext(), "There is no internet connection.", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }


    //class async task
    class CreatePatientAsync extends AsyncTask<String, String, String>{
        //global variable in this class
        ProgressDialog progdialog;
        String responcefromphp;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //set new instance of progress dialog and specify the activity
            progdialog =  new ProgressDialog(CreatePatientActivity.this);
            //set progress dialong dispalay message
            progdialog.setMessage("Please wait ...");
            //set progess dialog not to be be cancelabe but the user
            progdialog.setCancelable(false);
            //setting the progess dialong that we don't kno how long it will take in the state waiting
            progdialog.setIndeterminate(false);
            //dipslay the dialog
            progdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            //upload data to the database
            try {
                // create a http default client - initialize the HTTp client
                //running the session function
                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(addpatient_url);

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

                nameValuePairs.add(new BasicNameValuePair("cap_disease", selected_disease));
                nameValuePairs.add(new BasicNameValuePair("cap_age", p_age));
                nameValuePairs.add(new BasicNameValuePair("cap_fullname", patientnames));
                nameValuePairs.add(new BasicNameValuePair("cap_email", p_email));
                nameValuePairs.add(new BasicNameValuePair("cap_phonenumber", p_phonenumber));
                nameValuePairs.add(new BasicNameValuePair("cap_city", selected_city));
                nameValuePairs.add(new BasicNameValuePair("cap_comments", p_comments));
                nameValuePairs.add(new BasicNameValuePair("cap_password", p_password));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                InputStream inputStream = response.getEntity().getContent();

                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream), 4096);
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
                responcefromphp = sb.toString();
                inputStream.close();

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Try Again, Unexpected Error on method doing in background", Toast.LENGTH_LONG).show();
            }

            return responcefromphp;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //dismissing the dialog
            progdialog.dismiss();
            if(responcefromphp.equals("1")){
                Toast.makeText(CreatePatientActivity.this, "Patient registered successfully.", Toast.LENGTH_SHORT).show();
                Intent intentaddpatient = new Intent(CreatePatientActivity.this, HomeActivity.class);
                startActivity(intentaddpatient);

            }else{
                Toast.makeText(CreatePatientActivity.this, "Patient registration failed.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //method to check internet availability(WiFi and MobileData)
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

}
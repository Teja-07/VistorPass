package com.example.visitorpass;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;

public class MainActivity<apicall> extends  AppCompatActivity  {
    Button imagebutton, submitbutton;
    ImageView imageview;
    public Spinner spinner;
    TextView VDate,VTime;
    String currentdate,In_Time, currentdate1;
    String Selecetd_Staff;
    TextView Names,mobilenumber,note,nomembers,visitorloction;
    public static final String KEY_User_Document1 = "doc1";
    private String Document_img1="";
    private static ProgressDialog mProgressDialog;
    private static final int TAKE_PICTURE = 102;
    private static final int PICK_IMAGE = 103;
    ArrayList<SpinnerClass> SpinnerArray = new ArrayList<>();
    ArrayList<Dataclass> Dataclass = new ArrayList<>();
    ArrayList<String> names = new ArrayList<String>();
    private static final String ROOT_URL = "https://dpsgir.swiftcampus.com//VisitorEntryAPPAPI.php?visitor=entry&Visitor_Name=abc&Contact_No=1234&Visit_Purpose_Id=1&Location=hyderabad&No_Of_Person=1&Date_Of_Visit=2021-11-17&In_Time=02:50:00&Note=Text&Created_By=Swipetouch&school_id=1";
    String URL = "https://dpsgir.swiftcampus.com/VisitorPurposeAPPAPI.php?purpose=view&School_Id=1&Client_Type=WEB";
    String URL2 = "https://dpsgir.swiftcampus.com/CurrentDateTImeAppAPI.php?datetime=view";
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_PERMISSIONS = 100;
    private static final int PICK_IMAGE_REQUEST =1 ;
    private Bitmap bitmap;
    private String filePath;
    private int requestCode;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private int resultCode;
    private ProgressBar loadingPB;
    @Nullable
    private Intent data;


    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageview = findViewById(R.id.imageView);
        Names=findViewById(R.id.Name);
        mobilenumber=findViewById(R.id.MobileNumber);
        note=findViewById(R.id.TextNote);
        nomembers=findViewById(R.id.NumberofMembers1);
        visitorloction=findViewById(R.id.Visitorloction);
        VDate=findViewById(R.id.EditDate);
        VTime=findViewById(R.id.EditTime);
        spinner=findViewById(R.id.spinner);
        imagebutton = findViewById(R.id.imagebotton);
        submitbutton= findViewById(R.id.submitbutton);
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }
        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(MainActivity.this,VisitorsStatusActivity.class);
               startActivity(intent);
                String names=Names.getText().toString();
                String monum=mobilenumber.getText().toString();
                String nomem=nomembers.getText().toString();
                String notes=note.getText().toString();
                String novisitor=visitorloction.getText().toString();
                if (names!=null)
                SendDetail(names,Selecetd_Staff,currentdate,In_Time,monum,nomem,notes,novisitor);


            }
        });

        retrieveJSON();
        onapi();

        imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });



    }


    private void selectImage() {
        final Dialog dialog1 = new Dialog(MainActivity.this, R.style.dialogstyle); // Context, this, etc.
        dialog1.setContentView(R.layout.camerapopup);
        LinearLayout camearbtn = dialog1.findViewById(R.id.llCameraId);
        LinearLayout llGalleryId = dialog1.findViewById(R.id.llGalleryId);
        camearbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, TAKE_PICTURE);
                dialog1.dismiss();

            }
        });
        llGalleryId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, PICK_IMAGE);
                dialog1.dismiss();
            }
        });
        dialog1.show();
    }



        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");

                imageview.setImageBitmap(photo);



            } else if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
                try {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = MainActivity.this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();


                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                    imageview.setImageBitmap(bitmap);

                } catch (Exception e) {
                }

            }
        }

    public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
        return Document_img1;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);

    }

    private void SendDetail(String names, String selecetd_Staff, String currentdate, String in_Time, String monum, String nomem, String notes, String novisitor) {
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST,"https://dpsgir.swiftcampus.com//VisitorEntryAPPAPI.php?", new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);

                Log.e("","upload response"+resultResponse);
//                try {
//                    JSONObject jsonObject = new JSONObject(resultResponse);
//                    String status = jsonObject.getString("status");
//                    String message = jsonObject.getString("message");
//                    if(status.equals("200")){
//                        final Dialog dialog1 = new Dialog(LeaveApplicationActivity.this,R.style.dialogstyle); // Context, this, etc.
//                        dialog1.setContentView(R.layout.leavesucess);
//                        Button yesbutton = dialog1.findViewById(R.id.yesbutton);
//                        //  Button nobutton = dialog1.findViewById(R.id.nobutton);
//                        dialog1.show();
//                        yesbutton.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                dialog1.hide();
//                                startActivity(new Intent(LeaveApplicationActivity.this,LeaveApplicationActivity.class));
//                            }
//                        });
//
//
//                    }else{
//                        final Dialog dialog2 = new Dialog(LeaveApplicationActivity.this,R.style.dialogstyle); // Context, this, etc.
//                        dialog2.setContentView(R.layout.leavefailds);
//                        Button yesbutton = dialog2.findViewById(R.id.yesbutton);
//                        dialog2.show();
//                        yesbutton.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                dialog2.hide();
//                                startActivity(new Intent(LeaveApplicationActivity.this,LeaveApplicationActivity.class));
//                            }
//                        });
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
          }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";

                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                        }
                    else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                   /* String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message + " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message + " Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> stringStringMap = new HashMap<>();
//
                stringStringMap.put("visitor", "entry");
                stringStringMap.put("Visitor_Name",names);
                stringStringMap.put("Contact_No",monum);
                stringStringMap.put("Visit_Purpose_Id",selecetd_Staff);
                stringStringMap.put("Location", novisitor);
                stringStringMap.put("No_Of_Person", nomem);
                stringStringMap.put("Date_Of_Visit", currentdate);
                stringStringMap.put("In_Time", "02:50:00");
                stringStringMap.put("Note", notes);
                stringStringMap.put("Created_By","Swipetouch");

                Log.e("", "school_id" + stringStringMap);

                return stringStringMap;

            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                try {
                    Bitmap bitmap = ((BitmapDrawable) imageview.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
                    Log.e("Image_post ", "Image_post" + bitmap);
                    if (bitmap != null)
                        params.put("image", new DataPart(System.currentTimeMillis() + "Image_event.png", AppHelper.getFileDataFromDrawable(bitmap), "image/png"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return params;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        multipartRequest.setRetryPolicy(policy);
        VolleySingleton.getInstance(MainActivity.this).addToRequestQueue(multipartRequest);


    }







    private void onapi() {


        StringRequest stringRequest = new StringRequest(URL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("", "Response found" + response);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("timedate");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject timedata = jsonArray.getJSONObject(i);
                        Dataclass dataclass = new Dataclass();
                        String EditDate = timedata.getString("date");
                         In_Time= timedata.getString("time");
                        dataclass.setDate(EditDate);
                        dataclass.setTime(In_Time);
                        VDate.setText(String.valueOf(EditDate));
                        VTime.setText(String.valueOf(In_Time));
                        SimpleDateFormat spf=new SimpleDateFormat("dd/mm/yyyy");
                        Date newDate=spf.parse(EditDate);
                        spf= new SimpleDateFormat("yyyy-mm-dd");
                        currentdate = spf.format(newDate);
                        SimpleDateFormat spf1=new SimpleDateFormat("HH:mm:ss a");
                        Date newDate1=spf1.parse(EditDate);
                        spf1= new SimpleDateFormat("HH:mm:ss");
                        currentdate1 = spf1.format(newDate1);
                        System.out.println(EditDate);
                        Dataclass.add(dataclass);

                    }

                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

    private void retrieveJSON() {
        showSimpleProgressDialog(MainActivity.this, "Loading...","Fetching Json",false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equals("200")) {

                        Log.e("", "Response is found" + response);
                        SpinnerArray = new ArrayList<>();
                        JSONArray dataArray = obj.getJSONArray("purpose");
                        for (int i = 0; i < dataArray.length(); i++) {
                            SpinnerClass SpinnerModel = new SpinnerClass();
                            JSONObject dataobj = dataArray.getJSONObject(i);
                            SpinnerModel.setPurpose_id(dataobj.getInt("purpose_id"));
                            SpinnerModel.setPurpose_name(dataobj.getString("purpose_name"));
                            String purpose_name=dataobj.getString("purpose_name");
                            names.add(purpose_name);
                        }
//                        for (int i = 0; i < SpinnerArray.size(); i++) {
//                            names.add(SpinnerArray.get(i).getPurpose_name().toString());
//                        }

                        //ArrayAdapter<String> adapter_option=new ArrayAdapter<String>(MainActivity.this,android.R.layout.activity_list_item,SpinnerArray);

                        ArrayAdapter<String> spnAdapter = new ArrayAdapter<String>(MainActivity.this,
                                R.layout.simple_spinner_dropdown,names);
                        spnAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                        spinner.setAdapter(spnAdapter);


                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                Selecetd_Staff = names.get(position);
                                Log.e("","message seleted staff id"+Selecetd_Staff);


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        spinner.setSelection(names.indexOf(Selecetd_Staff));
//                        Log.e("", "product_string" + Stri.indexOf(Selected_Subject));
//                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> adapterView) {
//
//                            }
//                        });
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Data Not Found", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            private void removeSimpleProgressDialog() {
                try {
                    if (mProgressDialog != null) {
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                            mProgressDialog = null;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }

    private static void showSimpleProgressDialog(MainActivity mainActivity, String s, String fetching_json, boolean b) {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();

        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }






}



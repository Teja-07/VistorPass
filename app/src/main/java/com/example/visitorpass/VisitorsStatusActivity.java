package com.example.visitorpass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VisitorsStatusActivity extends AppCompatActivity {
    private TextView contact_No,purpose,in_Time,visitorsnote,visitor_Name,address,no_Of_Person,date_Of_Visit,status;;
     String URL="https://dpsgir.swiftcampus.com/ViewVisitorAPPAPI.php?visitor=view&from_date=2021-10-10&Client_Type=WEB";
    public RecyclerView recyclerView;
    ArrayList<VisitorsStatusActivityUser> users=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visitorsstatusactivityrecyclerview);
        recyclerView=findViewById(R.id.recyclerView);
        contact_No=findViewById(R.id.Contact_No);
        purpose=findViewById(R.id.Purpose);
        in_Time=findViewById(R.id.In_Time);
        visitorsnote=findViewById(R.id.Note);
        visitor_Name=findViewById(R.id.Visitor_Name);
        address=findViewById(R.id.Address);
        no_Of_Person=findViewById(R.id.No_Of_Person);
        date_Of_Visit=findViewById(R.id.Date_Of_Visit);
        status=findViewById(R.id.Status);
        StringRequest request=new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");
                    String message=jsonObject.getString("message");
                    if (status.equals("200"))
                    {
                        Log.e("","Response is found"+response);
                        JSONArray jsonArray = jsonObject.getJSONArray("Visitors");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject Visitors=jsonArray.getJSONObject(i);
                            VisitorsStatusActivityUser visitorsStatusActivityUser=new VisitorsStatusActivityUser();
                            String Contact_No=Visitors.getString("Contact_No");
                            String Purpose=Visitors.getString("Purpose");
                            String In_Time=Visitors.getString("In_Time");
                            String Note=Visitors.getString("Note");
                            String Visitor_Name=Visitors.getString("Visitor_Name");
                            String Address=Visitors.getString("Address");
                            String No_Of_Person=Visitors.getString("No_Of_Person");
                            String Date_Of_Visit=Visitors.getString("Date_Of_Visit");
//                            String Status=Visitors.getString("Status");
//                            visitorsStatusActivityUser.setStatus(Status);
                            visitorsStatusActivityUser.setContact_No(Contact_No);
                            visitorsStatusActivityUser.setVisitor_Name(Visitor_Name);
                            visitorsStatusActivityUser.setAddress(Address);
                            visitorsStatusActivityUser.setDate_Of_Visit(Date_Of_Visit);
                            visitorsStatusActivityUser.setNo_Of_Person(No_Of_Person);
                            visitorsStatusActivityUser.setIn_Time(In_Time);
                            visitorsStatusActivityUser.setNote(Note);
                            visitorsStatusActivityUser.setPurpose(Purpose);
                            users.add(visitorsStatusActivityUser);
                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        VisitorsStatusActivityAdapter adapter=new VisitorsStatusActivityAdapter(VisitorsStatusActivity.this,users);
                        recyclerView.setAdapter(adapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
            }
        });
        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(request);
    }



}
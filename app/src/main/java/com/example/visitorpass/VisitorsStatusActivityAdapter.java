package com.example.visitorpass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VisitorsStatusActivityAdapter extends RecyclerView.Adapter<VisitorsStatusActivityAdapter.Viewholder> {
    LayoutInflater inflater;
    ArrayList<VisitorsStatusActivityUser> VisitorsStatusActivityUser;
    Context contextl;


    public VisitorsStatusActivityAdapter(Context ctx,ArrayList<VisitorsStatusActivityUser> VisitorsStatusActivityUser) {
        this.contextl=ctx;
        this.inflater=LayoutInflater.from(ctx);
        this.VisitorsStatusActivityUser=VisitorsStatusActivityUser;
    }
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_visitors_status, parent, false);
        return new Viewholder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        VisitorsStatusActivityUser user= VisitorsStatusActivityUser.get(position);
        holder.Note.setText(String.valueOf(user.getNote()));
        holder.Purpose.setText(String.valueOf(user.getPurpose()));
        holder.Contact_No.setText(String.valueOf(user.getContact_No()));
        holder.In_Time.setText(String.valueOf(user.getIn_Time()));
        holder.Visitor_Name.setText(String.valueOf(user.getVisitor_Name()));
        holder.Address.setText(String.valueOf(user.getAddress()));
        holder.No_Of_Person.setText(String.valueOf(user.getAddress()));
        holder.Date_Of_Visit.setText(String.valueOf(user.getDate_Of_Visit()));
        holder.Status.setText(String.valueOf(user.getStatus()));
    }

    @Override
    public int getItemCount() {
        return VisitorsStatusActivityUser.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView Contact_No,Purpose,In_Time,Note,Visitor_Name,Address,No_Of_Person,Date_Of_Visit,Status;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            Contact_No=itemView.findViewById(R.id.Contact_No);
            Purpose=itemView.findViewById(R.id.Purpose);
            In_Time=itemView.findViewById(R.id.In_Time);
            Note=itemView.findViewById(R.id.Note);
            Visitor_Name=itemView.findViewById(R.id.Visitor_Name);
            Address=itemView.findViewById(R.id.Address);
            No_Of_Person=itemView.findViewById(R.id.No_Of_Person);
            Date_Of_Visit=itemView.findViewById(R.id.Date_Of_Visit);
            Status=itemView.findViewById(R.id.Status);

        }
    }
}

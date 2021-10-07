package com.example.bancodetiempodeluxe;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment {
    private RecyclerView mNotifList;
    private DatabaseReference mNotificationsDB, mThisUser;
    private FirebaseUser mCurrentUser;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment notificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notifications, container, false);//Inflate fragment layout
        mNotifList = view.findViewById(R.id.notifRecView);
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mThisUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid().toString());
        mNotificationsDB = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid().toString()).child("notifications");
        mNotifList.setLayoutManager(new LinearLayoutManager(getActivity()));


        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

        Query query = mNotificationsDB.orderByChild("status");

        FirebaseRecyclerOptions<Notification> options = new FirebaseRecyclerOptions.Builder<Notification>().setQuery(query, Notification.class).build();
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Notification, NotifsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull NotifsViewHolder holder, int position, @NonNull Notification notification) {
                holder.setType(notification.getType());
                holder.setStatus(notification.getStatus());
                System.out.println(position);
                Log.d("TAG", "--------------");
                Log.d("TAG", "users.getType() : " + notification.getType());
                Log.d("TAG", "users.getStatus() : " + notification.getStatus());

                final String selectedNotif = getRef(position).getKey();
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(notification.getType().equals("request")) {
                            Intent intent = new Intent(getActivity(), DescriptionNotificationRequest.class);
                            intent.putExtra("JID", notification.getJob());
                            intent.putExtra("NID", selectedNotif);
                            startActivity(intent);
                        }
                    }
                });
            }

            @NonNull
            @Override
            public NotifsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list_item, parent, false);
                return new NotifsViewHolder(view);
            }
        };

        mNotifList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class NotifsViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public NotifsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setType(String type){
            TextView notifBody=(TextView) mView.findViewById(R.id.notifBody);
            ImageView notifImage = (ImageView) mView.findViewById(R.id.notifImage);
            switch(type){
                case "request":
                    notifBody.setText("¡Nueva solicitud recibida!\nDa click aquí para más información");
                    notifImage.setImageResource(R.drawable.work_black);
                    break;
                default:
                    break;
            }
        }
        public void setStatus(int status){
            CardView cardView = (CardView) mView.findViewById(R.id.notifParent);
            TextView notifBody = (TextView) mView.findViewById(R.id.notifBody);
            if (status == 0){
                cardView.setCardBackgroundColor(ContextCompat.getColor(mView.getContext(), R.color.notifblue));
                notifBody.setTypeface(Typeface.DEFAULT_BOLD);
            }else{
                cardView.setCardBackgroundColor(ContextCompat.getColor(mView.getContext(), R.color.j_hueso));
                notifBody.setTypeface(Typeface.DEFAULT);
            }
        }
    }
}
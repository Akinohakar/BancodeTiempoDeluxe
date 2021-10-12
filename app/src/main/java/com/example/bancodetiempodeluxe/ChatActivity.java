package com.example.bancodetiempodeluxe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    private String mChatUser;
    private TextView nameChat;
    private CircleImageView imageChat;
    private DatabaseReference mChatDatabase;
    private FirebaseAuth mAuth;
    private EditText messageChat;
    private ImageButton mSend;
    private String mCurrentId;
    private RecyclerView mMessagesList;
    private SwipeRefreshLayout mRefreshLayout;
    private final List<Messages> messageList= new ArrayList<>();
    private LinearLayoutManager mLinearLayout;
    private MessageAdapter mAdapter;
    private static final int TOTAL_ITEMS_TO_LOAD=10;
    private int mCurrentPage=1;
    private String lastKey="";
    //ServerValue.TIMESTAMP

    //Solution pagination
    private int itemPos=0;
    private String prevKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //FIREBASE AUTH INSTANCE
        mAuth=FirebaseAuth.getInstance();
        //GET THE CURRENT UID
        mCurrentId=mAuth.getCurrentUser().getUid();
        //GET THE UID OF THE USER SELECTED IN THE PREVIOUS VIEW IN THE RECICLER VIEW UI
        mChatUser=getIntent().getStringExtra("user_id");
        //FIND ELEMENTS OF THE CURRENT VIEW
        nameChat=findViewById(R.id.chatusername);
        imageChat=findViewById(R.id.chatuserimage);

        messageChat=findViewById(R.id.chatMessage);

        mSend=findViewById(R.id.chatsend);
        //INICIALIZE THE ADAPTER FOR THE RECICLER VIEW
        mAdapter=new MessageAdapter(messageList);
        //FIND THE RECICLER VIEW
        mMessagesList=findViewById(R.id.messagesList);
        //FIND THE SWAP ID
        mRefreshLayout=findViewById(R.id.message_swipe_layout);
        //SELECTE THE TYPE OF LAYOUT FOR THE RECYCLER VIEW
        mLinearLayout=new LinearLayoutManager(this);
        mMessagesList.setHasFixedSize(true);
        //SET THE PREVIOS INTACIATED LINEAR LAYOUT TO THE RECYCLER VIEW mMessage List
        mMessagesList.setLayoutManager(mLinearLayout);
        //SET THE ADAPTER
        mMessagesList.setAdapter(mAdapter);



        //HEADER CHAT
        mChatDatabase= FirebaseDatabase.getInstance().getReference();
        mChatDatabase.child("Users").child(mChatUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name=snapshot.child("name").getValue().toString();
                String image=snapshot.child("image").getValue().toString();
                nameChat.setText(name);
                Picasso.get().load(image).placeholder(R.drawable.exampleuser).into(imageChat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Desabilitar en caso de que no este activo el trabajo
        mChatDatabase.child("Friends").child(mCurrentId).child(mChatUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.hasChild("status")){
                    String status=snapshot.child("status").getValue().toString();
                    System.out.println(status);
                    if(status.equals("0")){
                        messageChat.setHint("Mensaje");
                        messageChat.setEnabled(true);
                        messageChat.setHintTextColor(Color.GRAY);



                    }else if(status.equals("1")){
                        messageChat.setHintTextColor(Color.RED);
                        messageChat.setHint("No puedes chatear");
                        messageChat.setEnabled(false);
                    }
                }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //CHAT FUNCIONALITY
        mChatDatabase.child("Chat").child(mCurrentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.hasChild(mChatUser)){
                    Map chatAddMap=new HashMap();
                    chatAddMap.put("seen",false);
                    chatAddMap.put("timestamp", ServerValue.TIMESTAMP);

                    Map chatUserMap=new HashMap();
                    chatUserMap.put("Chat/"+mCurrentId+"/"+mChatUser,chatAddMap);
                    chatUserMap.put("Chat/"+mChatUser+"/"+mCurrentId,chatAddMap);

                    mChatDatabase.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                            if(error!=null){
                                //En caso de que aparesca el arror
                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();

            }
        });
        loadMesssages();

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentPage++;
                itemPos=0;

                loadMoreMesssages();

            }
        });





    }

    private void loadMoreMesssages() {
        DatabaseReference messageRef=mChatDatabase.child("messages").child(mCurrentId).child(mChatUser);
        Query messageQuery=messageRef.orderByKey().endAt(lastKey).limitToLast(10);
        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Messages message=snapshot.getValue(Messages.class);


                String messagekey=snapshot.getKey();
                if(!prevKey.equals(messagekey)){
                    messageList.add(itemPos++,message);
                }else{
                    prevKey=lastKey;
                }
                if(itemPos==1){

                    lastKey=messagekey;
                }





                mAdapter.notifyDataSetChanged();

                mLinearLayout.scrollToPositionWithOffset(10,0);
                mRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadMesssages() {
        DatabaseReference messageRef=mChatDatabase.child("messages").child(mCurrentId).child(mChatUser);
        Query messageQuery=messageRef.limitToLast(mCurrentPage*TOTAL_ITEMS_TO_LOAD);//To do
        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Messages message=snapshot.getValue(Messages.class);
                itemPos++;
                if(itemPos==1){
                    String messagekey=snapshot.getKey();
                    lastKey=messagekey;
                    prevKey=messagekey;

                }
                messageList.add(message);
                mAdapter.notifyDataSetChanged();
                mMessagesList.scrollToPosition(messageList.size()-1);

                mRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendMessage() {
        String message=messageChat.getText().toString();
        if(!TextUtils.isEmpty(message)){
            String current_user_ref="messages/"+mCurrentId+"/"+mChatUser;
            String chat_user_ref="messages/"+mChatUser+"/"+mCurrentId;
            DatabaseReference user_message_push=mChatDatabase.child("messages").child(mCurrentId).child(mChatUser).push();
            String push_id=user_message_push.getKey();

            Map messageMap=new HashMap();
            messageMap.put("message",message);
            messageMap.put("seen",false);
            messageMap.put("type","text");
            messageMap.put("time",ServerValue.TIMESTAMP);
            messageMap.put("from",mCurrentId);

            Map messageuserMap=new HashMap();
            messageuserMap.put(current_user_ref+"/"+push_id,messageMap);
            messageuserMap.put(chat_user_ref+"/"+push_id,messageMap);
            messageChat.setText("");

            mChatDatabase.updateChildren(messageuserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if(error!=null){

                    }
                }
            });






        }

    }
}
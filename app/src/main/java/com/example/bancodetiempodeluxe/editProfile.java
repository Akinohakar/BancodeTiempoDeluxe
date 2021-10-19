package com.example.bancodetiempodeluxe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class editProfile extends AppCompatActivity {
    private TextInputLayout editName,editAge,editPronouns,editPhone,editAdr,editJob,editDescJob;
    private Button submitChanges;
    private DatabaseReference mStatusDatabse;
    private FirebaseUser mCurrentUser;
    private FloatingActionButton editProfilePic;
    private static final int GALLERY_PICK=1;
    private CircleImageView mDisplayImage;
    private Snackbar snackbar,snackbarImg;
    private CheckBox dayL,dayM,dayMI,dayJV,dayV,dayS,dayD;

    //Storage Firebase
    private StorageReference mImageStorage;

    //Progress
    private ProgressDialog mProgress;
    private ProgressDialog mPicProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_profile);
        //Finding Views
        dayL=findViewById(R.id.editprofile_job_monday);
        dayM=findViewById(R.id.editprofile_job_tuesday);
        dayMI=findViewById(R.id.editprofile_job_wednesday);
        dayJV=findViewById(R.id.editprofile_job_thursday);
        dayV=findViewById(R.id.editprofile_job_friday);
        dayS=findViewById(R.id.editprofile_job_saturday);
        dayD=findViewById(R.id.editprofile_job_sunday);
        editProfilePic=findViewById(R.id.fabImageChange);
        editName=findViewById(R.id.editName);
        submitChanges=findViewById(R.id.submitChangesButton);
        editAge=findViewById(R.id.editDate);
        editPronouns=findViewById(R.id.editPronombres);
        editPhone=findViewById(R.id.editNoTel);
        editAdr=findViewById(R.id.editDirect);
        editJob=findViewById(R.id.editJob);
        editDescJob=findViewById(R.id.editJobDesc);
        mDisplayImage=findViewById(R.id.profile_image);
        String name_value=getIntent().getStringExtra("name_value");
        editName.getEditText().setText(name_value);
        //Firebase Storage
        mImageStorage= FirebaseStorage.getInstance().getReference();//pointing to root
        //Firebase
        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        String current_uid=mCurrentUser.getUid();
          mStatusDatabse= FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        //get values to put it in textfields
         mStatusDatabse.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 String name=snapshot.child("name").getValue().toString();
                 String age=snapshot.child("age").getValue().toString();
                 String pronoun=snapshot.child("pronoun").getValue().toString();
                 String phone=snapshot.child("phone").getValue().toString();
                 String address=snapshot.child("address").getValue().toString();
                 String job=snapshot.child("jobtitle").getValue().toString();
                 String descjob=snapshot.child("jobdesc").getValue().toString();
                 String image=snapshot.child("image").getValue().toString();
                 String datejob=snapshot.child("datejob").getValue().toString();
                 System.out.println(datejob);

                 if(datejob.contains("L")){
                    dayL.setChecked(true);
                 }
                 if(datejob.contains("M")){
                     dayM.setChecked(true);
                 }
                 if(datejob.contains("X")){
                     dayMI.setChecked(true);
                 }
                 if(datejob.contains("J")){
                     dayJV.setChecked(true);
                 }
                 if(datejob.contains("V")){
                     dayV.setChecked(true);
                 }
                 if(datejob.contains("S")){
                     dayS.setChecked(true);
                 }
                 if(datejob.contains("D")){
                     dayD.setChecked(true);
                 }
                 editName.getEditText().setText(name);
                 editAge.getEditText().setText(age);
                 editPronouns.getEditText().setText(pronoun);
                 editPhone.getEditText().setText(phone);
                 editAdr.getEditText().setText(address);
                 editJob.getEditText().setText(job);
                 editDescJob.getEditText().setText(descjob);
                 if(!image.equals("default")) {
                     Picasso.get().load(image).placeholder(R.drawable.exampleuser).into(mDisplayImage);
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });



        submitChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgress=new ProgressDialog(editProfile.this);

                mProgress.setTitle("Guardando cambios");
                mProgress.setMessage("Porfavor espero en lo que se guarda el mensaje");
                mProgress.show();
                //String geters
                String name=editName.getEditText().getText().toString();
                String age=editAge.getEditText().getText().toString();
                String pronouns=editPronouns.getEditText().getText().toString();
                String phone=editPhone.getEditText().getText().toString();
                String adrs=editAdr.getEditText().getText().toString();
                String job=editJob.getEditText().getText().toString();
                String descjob=editDescJob.getEditText().getText().toString();

                //Contatenate into date
                StringBuilder strDate= new StringBuilder();
                if (dayL.isChecked()){
                    strDate.append("L ");
                }
                if (dayM.isChecked()){
                    strDate.append("M ");
                }
                if (dayMI.isChecked()){
                    strDate.append("X ");

                }
                if (dayJV.isChecked()){
                    strDate.append("J ");
                }
                if (dayV.isChecked()){
                    strDate.append("V ");

                }
                if (dayS.isChecked()){
                    strDate.append("S ");
                }
                if (dayD.isChecked()){
                    strDate.append("D ");

                }

                mStatusDatabse.child("datejob").setValue(strDate.toString());
                mStatusDatabse.child("address").setValue(adrs);
                mStatusDatabse.child("jobtitle").setValue(job);
                mStatusDatabse.child("phone").setValue(phone);
                mStatusDatabse.child("pronoun").setValue(pronouns);
                mStatusDatabse.child("jobdesc").setValue(descjob);
                mStatusDatabse.child("age").setValue(age);


                mStatusDatabse.child("name").setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mProgress.dismiss();
                            snackbar=Snackbar.make(view,"Datos actualizados",Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Hubo un error a cqambiar los datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

        //To pic an image form the gallery
        editProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent=new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE"),GALLERY_PICK);
                snackbarImg=Snackbar.make(view,"Imagen cambiada correctamente",Snackbar.LENGTH_SHORT);

            }
        });
    }
    //For iomage crop
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==GALLERY_PICK&&resultCode==RESULT_OK){
            Uri imageUri=data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {//is the result code has passed the request activity

            CropImage.ActivityResult result = CropImage.getActivityResult(data);//stores the result

            if (resultCode == RESULT_OK) {
                mPicProgress=new ProgressDialog(editProfile.this);
                mPicProgress.setTitle("Subiendo Imagen..");
                mPicProgress.setMessage("Porfavor espere en lo que se sube la imagen");
                mPicProgress.setCanceledOnTouchOutside(false);
                mPicProgress.show();

                Uri resultUri = result.getUri();//Gets the uri of that image
                String current_user_id=mCurrentUser.getUid();


                StorageReference filepath=mImageStorage.child("profile_images").child(current_user_id+".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful()){



                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {//to get the image url
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl;
                                    downloadUrl = uri.toString();

                                    mStatusDatabse.child("image").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {//put the image url into the user child(img)
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            mPicProgress.dismiss();
                                            snackbarImg.show();

                                        }
                                    });

                                }
                            });


                        }else{

                            Toast.makeText(editProfile.this, "Error in uploadin", Toast.LENGTH_SHORT).show();
                            mPicProgress.dismiss();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


}
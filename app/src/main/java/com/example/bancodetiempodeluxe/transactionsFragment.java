package com.example.bancodetiempodeluxe;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link transactionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class transactionsFragment extends Fragment {

    private DatabaseReference mUsersDatabase, mTrabajosContratados, mTrabajosRealizados, mActualJob, mTrabajos;
    private FirebaseAuth mAuth;
    ArrayList<TransaccionesModel> transaccionesContratadas, transaccionesRealizadas, trabajoActual;
    RecyclerView recycler;
    TextView balance;
    String username,balanceDB;

    private static final int PERMISSION_REQUEST_CODE = 200;
    Bitmap bmp, scaledbmp;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public transactionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment transactionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static transactionsFragment newInstance(String param1, String param2) {
        transactionsFragment fragment = new transactionsFragment();
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
        View view= inflater.inflate(R.layout.fragment_transactions, container, false);//Inflate fragment layout


        Button btnPDF = (Button) view.findViewById(R.id.btnDescargarPDF);

        recycler = (RecyclerView) view.findViewById(R.id.recyclerViewIDTransacciones);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 80, 80,false);

        if(!checkPermision()){
            requestPermision();
        }

        btnPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!transaccionesContratadas.isEmpty() || !transaccionesRealizadas.isEmpty()){
                    create_PDFDocument(transaccionesContratadas,transaccionesRealizadas);
                }else{
                    Toast.makeText(getActivity(),"No existen transacciones para descargar...",Toast.LENGTH_SHORT).show();
                }

            }
        });

        balance = view.findViewById(R.id.idBalance);

        mAuth = FirebaseAuth.getInstance();

        String current_id = mAuth.getCurrentUser().getUid();
        mUsersDatabase       = FirebaseDatabase.getInstance().getReference().child("Users").child(current_id);

        mUsersDatabase       = FirebaseDatabase.getInstance().getReference().child("Users").child(current_id);
        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getKey() != null){
                        if(dataSnapshot.getKey().equals("name")){
                            username = dataSnapshot.getValue(String.class);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mTrabajosContratados = FirebaseDatabase.getInstance().getReference().child("Users").child(current_id).child("Hired Jobs");
        mTrabajosRealizados  = FirebaseDatabase.getInstance().getReference().child("Users").child(current_id).child("Worked Jobs");
        mActualJob           = FirebaseDatabase.getInstance().getReference().child("Users").child(current_id).child("Actual Job");

        transaccionesContratadas = new ArrayList<TransaccionesModel>();
        transaccionesRealizadas  = new ArrayList<TransaccionesModel>();

        transaccionesContratadas = new ArrayList<>();
        transaccionesRealizadas  = new ArrayList<>();
        trabajoActual            = new ArrayList<>();



        AdapterDatosTransacciones adapter = new AdapterDatosTransacciones(transaccionesContratadas, transaccionesRealizadas);
        recycler.setAdapter(adapter);

        mActualJob.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getKey() != null){
                        String trabajoID = dataSnapshot.getKey();
                        TransaccionesModel tm = new TransaccionesModel();
                        mTrabajos = FirebaseDatabase.getInstance().getReference().child("Jobs in progress");
                        mTrabajos.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot1: snapshot.getChildren()){
                                    if(dataSnapshot1.getKey()!=null){
                                        if(dataSnapshot1.getKey().equals(trabajoID)){
                                            TransaccionesModel tm = dataSnapshot1.getValue(TransaccionesModel.class);
                                            if(tm.getIduserhire().equals(current_id)){
                                                transaccionesContratadas.add(tm);
                                                adapter.notifyDataSetChanged();
                                            }
                                            else if(tm.getIdusersupplier().equals(current_id)){
                                                transaccionesRealizadas.add(tm);
                                                adapter.notifyDataSetChanged();
                                            }
                                        }
                                    }}
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mTrabajosRealizados.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getKey() != null){
                        final boolean[] inP = {false};

                        mActualJob.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                                    if(dataSnapshot1.getKey().equals(dataSnapshot.getKey())){
                                        inP[0] = true;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        if(!inP[0]){
                            transaccionesRealizadas.add(dataSnapshot.getValue(TransaccionesModel.class));
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mTrabajosContratados.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getKey() != null){
                        final boolean[] inP = {false};

                        mActualJob.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                                    if(dataSnapshot1.getKey().equals(dataSnapshot.getKey())){
                                        inP[0] = true;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        if(!inP[0]){
                            transaccionesContratadas.add(dataSnapshot.getValue(TransaccionesModel.class));
                            adapter.notifyDataSetChanged();
                        }
                    }}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getKey().equals("balance")){
                        balanceDB = dataSnapshot.getValue(String.class);
                        balance.setText(balanceDB);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }


    @Override
    public void onStart(){
        super.onStart();
    }



    public void create_PDFDocument(ArrayList<TransaccionesModel> transaccionesContratadas, ArrayList<TransaccionesModel>transaccionesRealizadas){
        //Medidas de la pagina
        int pageHeight = 792;
        int pageWidth  = 612;

        int pageNumber = 1;
        int overViewContents = 0;

        //Positions
        int starting_y = 0;

        //Strings for PDF Header
        String clientName = username;

        String documentTitle = "Transacciones "+ clientName+".pdf";

        //Actual Date
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String actualDate = df.format(date);

        //Headers
        String hUser = "Usuario";
        String hWork = "Trabajo";
        String hDate = "Fecha";
        String hHour = "Horas";
        String hStat = "Estado";


        int separator = 63;
        int xUser = 15;
        int xWork = 5 + separator + xUser + separator;
        int xDate = separator + xWork + separator;
        int xHour = separator + xDate + separator;
        int xStat = separator + xHour + separator;
        ///////////////////////////////////////////

        //for drawing shapes we use title
        //for adding text in PDF file
        Paint paint = new Paint();
        Paint title = new Paint();
        PdfDocument.Page page;

        //Crear nuevo documento
        PdfDocument document = new PdfDocument();

        //Creando cada página

        //Create a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth,pageHeight,1).create();

        //Start a page
        page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        ///////////////////// HEADER ///////////////////////////////
        //Dibujar el logo
        canvas.drawBitmap(scaledbmp, 5,10, paint);

        title.setColor(ContextCompat.getColor(getActivity(), R.color.blackapp));

        //Parametros: texto, position from start, position from top, paint variable
        title.setTextSize(30);
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Banco de Tiempo",100,40,title);
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(10);
        canvas.drawText(actualDate,530,30,title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(10);
        canvas.drawText("Balance", 430, 50, title);
        canvas.drawText("Horas Dadas", 430, 60, title);
        canvas.drawText("Horas Recibidas", 430, 70, title);


        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        title.setTextSize(8);
        canvas.drawText(balanceDB, 500, 50, title);
        canvas.drawText(String.valueOf(transaccionesContratadas.size()), 530, 60, title);
        canvas.drawText(String.valueOf(transaccionesRealizadas.size()), 530, 70, title);

        title.setTextSize(15);
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));
        canvas.drawText("Listado de Transacciones", 100,55, title);
        title.setTextSize(10);
        canvas.drawText(clientName, 100,65, title);
        ///////////////////////////////////////////////////////////////////////////////
        starting_y = 100;

        /////////////// Trabajos Contratados ///////////////////////////////////////////
        paint.setColor(Color.rgb(150, 159, 168));
        paint.setStrokeWidth(0);
        canvas.drawRect(5,starting_y,607,starting_y += 30,paint);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setColor(ContextCompat.getColor(getActivity(), R.color.blackapp));
        title.setTextSize(20);
        canvas.drawText("Trabajos Contratados", 10, starting_y - 10, title);

        //Encabezados
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));
        title.setTextSize(15);
        starting_y +=20;

        canvas.drawText(hUser, xUser, starting_y, title);
        canvas.drawText(hWork, xWork, starting_y, title);
        canvas.drawText(hDate, xDate, starting_y, title);
        canvas.drawText(hHour, xHour, starting_y, title);
        canvas.drawText(hStat, xStat, starting_y, title);

        starting_y += 10;
        paint.setColor(Color.rgb(150, 159, 168));
        paint.setStrokeWidth(0);
        canvas.drawRect(5,starting_y,607,starting_y += 1,paint);
        starting_y += 10;

        ///////Obtener elementos de Lista
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(8);
        for(int i = 0; i< transaccionesContratadas.size(); i++){
            if(overViewContents >= 60){
                document.finishPage(page);
                pageInfo = new PdfDocument.PageInfo.Builder(pageWidth,pageHeight,++pageNumber).create();
                page = document.startPage(pageInfo);
                canvas = page.getCanvas();
                overViewContents = 0;
                starting_y = 30;
            }
            canvas.drawText(transaccionesContratadas.get(i).nameusersupplier, xUser, starting_y, title);
            canvas.drawText(transaccionesContratadas.get(i).job, xWork, starting_y, title);
            canvas.drawText(transaccionesContratadas.get(i).date, xDate, starting_y, title);
            canvas.drawText(transaccionesContratadas.get(i).hour, xHour, starting_y, title);
            canvas.drawText(transaccionesContratadas.get(i).status, xStat, starting_y, title);
            starting_y += 10;
            ++overViewContents;

        }
        /////////////////////////////////////////////////////////////////////////////////
        document.finishPage(page);
        pageInfo = new PdfDocument.PageInfo.Builder(pageWidth,pageHeight,++pageNumber).create();
        page = document.startPage(pageInfo);
        canvas = page.getCanvas();

        overViewContents = 0;
        starting_y = 30;

        /////////////// Trabajos Realizados ///////////////////////////////////////////
        paint.setColor(Color.rgb(150, 159, 168));
        paint.setStrokeWidth(0);
        canvas.drawRect(5,starting_y,607,starting_y += 30,paint);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setColor(ContextCompat.getColor(getActivity(), R.color.blackapp));
        title.setTextSize(20);
        canvas.drawText("Trabajos Realizados", 10, starting_y - 10, title);

        //Encabezados
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));
        title.setTextSize(15);
        starting_y +=20;

        canvas.drawText(hUser, xUser, starting_y, title);
        canvas.drawText(hWork, xWork, starting_y, title);
        canvas.drawText(hDate, xDate, starting_y, title);
        canvas.drawText(hHour, xHour, starting_y, title);
        canvas.drawText(hStat, xStat, starting_y, title);

        starting_y += 10;
        paint.setColor(Color.rgb(150, 159, 168));
        paint.setStrokeWidth(0);
        canvas.drawRect(5,starting_y,607,starting_y += 1,paint);
        starting_y += 10;

        ///////Obtener elementos de Lista
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(8);
        overViewContents = 0;
        for(int j = 0; j< transaccionesRealizadas.size(); j++){
            if(overViewContents >= 60){
                document.finishPage(page);
                pageInfo = new PdfDocument.PageInfo.Builder(pageWidth,pageHeight,++pageNumber).create();
                page = document.startPage(pageInfo);
                canvas = page.getCanvas();
                overViewContents = 0;
                starting_y = 30;
            }
            canvas.drawText(transaccionesRealizadas.get(j).nameuserhire, xUser, starting_y, title);
            canvas.drawText(transaccionesRealizadas.get(j).job, xWork, starting_y, title);
            canvas.drawText(transaccionesRealizadas.get(j).date, xDate, starting_y, title);
            canvas.drawText(transaccionesRealizadas.get(j).hour, xHour, starting_y, title);
            canvas.drawText(transaccionesRealizadas.get(j).status, xStat, starting_y, title);
            starting_y += 10;
            ++overViewContents;
        }
        //////////////////////////////////////////////////////////////////////////////////
        document.finishPage(page);

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),documentTitle);

        try{
            document.writeTo(new FileOutputStream(file));
            Toast.makeText(getActivity(),"PDF generado Correctamente en "+Environment.getExternalStorageDirectory().getAbsolutePath(), Toast.LENGTH_SHORT).show();
        }catch (FileNotFoundException e) {
            requestPermision();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        document.close();
    }

    private boolean checkPermision(){
        int permission1 = ContextCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getActivity(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermision(){
        ActivityCompat.requestPermissions(getActivity(),new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults){
        if(requestCode == PERMISSION_REQUEST_CODE){
            if (grantResults.length > 0){
                boolean wrtieStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage  = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if(wrtieStorage && readStorage){
                    //Toast.makeText(getActivity(), "Permiso Concedido", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),"Permiso Rechazado", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }
        }
    }

}
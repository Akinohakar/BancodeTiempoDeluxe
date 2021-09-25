package com.example.bancodetiempodeluxe;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link transactionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class transactionsFragment extends Fragment {

    ArrayList<TransaccionesModel> listDatos;
    RecyclerView recycler;

    private static final int PERMISSION_REQUEST_CODE = 200;
    Bitmap bmp, scaledbmp;
    String documentTitle = "TestPDF.pdf";

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
        //recycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        listDatos = new ArrayList<TransaccionesModel>();

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140,false);

        if(checkPermision()){
            Toast.makeText(getActivity(), "Permiso Concedido", Toast.LENGTH_SHORT).show();
        }else{
            requestPermision();
        }

        //Crear las transacciones
        listDatos.add(new TransaccionesModel("2021-09-24","01","Good Co","Música","Completado","Contrato"));
        listDatos.add(new TransaccionesModel("2021-09-23","01","Emma Clair","Remix","Completado","Contratade"));
        listDatos.add(new TransaccionesModel("2021-09-22","01","Wolfgang Lohr","Remix","Cancelado","Contrato"));
        listDatos.add(new TransaccionesModel("2021-09-21","01","Alan Aquino","Programacion","Cancelado","Contrato"));
        listDatos.add(new TransaccionesModel("2021-09-21","01","Vale Cabañas","Diseños","Completado","Contratade"));
        listDatos.add(new TransaccionesModel("2021-09-21","01","Juan VaTe","Smash","Cancelado","Contrato"));
        listDatos.add(new TransaccionesModel("2021-09-21","01","DinhoTec","Siuuuu","En Progreso","Contratade"));

        AdapterDatosTransacciones adapter = new AdapterDatosTransacciones(listDatos);
        recycler.setAdapter(adapter);

        btnPDF.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              //generatePDF_transactions();
              create_PDFDocument();
          }
        });

        return view;
    }

    public void generatePDF_transactions(){
        String msg = "Generando Reporte PDF";
        Toast toast = Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT);
        toast.show();
    }

    public void create_PDFDocument(){
        //Medidas de la pagina
        int pageHeight = 1120;
        int pageWidth  = 792;

        //for drawing shapes we use title
        //for adding text in PDF file
        Paint paint = new Paint();
        Paint title = new Paint();

        //Crear nuevo documento
        PdfDocument document = new PdfDocument();

        //Create a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth,pageHeight,1).create();

        //Start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        //Dibujar el logo
        canvas.drawBitmap(scaledbmp, 56,40, paint);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(20);

        title.setColor(ContextCompat.getColor(getActivity(), R.color.blackapp));

        //Parametros: texto, position from start, position from top, paint variable
        canvas.drawText("Banco de tiempo",209,80,title);
        canvas.drawText("Reporte Automático Generado", 209,100, title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(15);

        title.setColor(ContextCompat.getColor(getActivity(), R.color.blackapp));
        canvas.drawText("Listado de Transacciones", 209, 250, title);

        document.finishPage(page);

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),documentTitle);

        try{
            document.writeTo(new FileOutputStream(file));
            Toast.makeText(getActivity(),"PDF generado Correctamente en "+Environment.getExternalStorageDirectory().getAbsolutePath(), Toast.LENGTH_SHORT).show();
        }catch (FileNotFoundException e) {
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
                    Toast.makeText(getActivity(), "Permiso Concedido", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),"Permiso Rechazado", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }
        }
    }

}
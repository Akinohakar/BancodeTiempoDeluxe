package com.example.bancodetiempodeluxe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link transactionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class transactionsFragment extends Fragment {

    ArrayList<TransaccionesModel> listDatos;
    RecyclerView recycler;

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

        recycler = (RecyclerView) view.findViewById(R.id.recyclerViewIDTransacciones);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        listDatos = new ArrayList<TransaccionesModel>();

        //Crear las transacciones
        listDatos.add(new TransaccionesModel("2021-09-24","01","Good Co","Música","Done"));
        listDatos.add(new TransaccionesModel("2021-09-23","01","Emma Clair","Remix","Done"));
        listDatos.add(new TransaccionesModel("2021-09-22","01","Wolfgang Lohr","Remix","Cancel"));
        listDatos.add(new TransaccionesModel("2021-09-21","01","Alan Aquino","Programacion","Cancel"));
        listDatos.add(new TransaccionesModel("2021-09-21","01","Vale Cabañas","Diseños","Done"));
        listDatos.add(new TransaccionesModel("2021-09-21","01","Juan VaTe","Smash","Cancel"));
        listDatos.add(new TransaccionesModel("2021-09-21","01","DinhoTec","Siuuuu","Done"));

        AdapterDatosTransacciones adapter = new AdapterDatosTransacciones(listDatos);
        recycler.setAdapter(adapter);

        return view;
    }

    public void generatePDF_transactions(){
        String msg = "Pasar a ver detalle";
        Toast toast = Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT);
    }
}
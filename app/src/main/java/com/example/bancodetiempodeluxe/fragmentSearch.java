package com.example.bancodetiempodeluxe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragmentSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragmentSearch extends Fragment {
    private RecyclerView contactsRecView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragmentSearch() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragmentSearch.
     */
    // TODO: Rename and change types and number of parameters
    public static fragmentSearch newInstance(String param1, String param2) {
        fragmentSearch fragment = new fragmentSearch();
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
    //The important Stuff

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view= (ViewGroup) inflater.inflate(R.layout.fragment_search, container, false);//Inflate fragment layout
        super.onCreate(savedInstanceState);

        contactsRecView=view.findViewById(R.id.contactsRecView);//Se isntancia aqui

        ArrayList<Contact> contacts=new ArrayList<>();
        contacts.add(new Contact("Margot Robie","Toma@tomassitometonatiu.com","https://s1.1zoom.me/big3/471/Painting_Art_Back_view_Photographer_575380_3840x2400.jpg","Plomeria", "L M M J V S D"));
        contacts.add(new Contact("Tonita  Le Tinac Jr","Toma@tomassitometonatiu.com","https://www.google.com.mx/","Carpinteria","L M M J V S D"));
        contacts.add(new Contact("El Sinjo de cuae","Toma@tomassitometonatiu.com","https://www.google.com.mx/","Tineria","L M M J V S D"));
        contacts.add(new Contact("Juan le tonga","Toma@tomassitometonatiu.com","https://www.google.com.mx/","Programacion","L M M J V S D"));
        contacts.add(new Contact("Ximena Ximena ","Toma@tomassitometonatiu.com","https://www.google.com.mx/","Director de cine","L M M J V S D"));
        contacts.add(new Contact("Tinajero Jr","Toma@tomassitometonatiu.com","https://www.google.com.mx/","Derecho","L M M J V S D"));
        contacts.add(new Contact("Tonalinto Pretque","Toma@tomassitometonatiu.com","https://www.google.com.mx/","Asesorias","L M M J V S D"));


        //Lista de contactos

        ContactRecViewAdapter adapter=new ContactRecViewAdapter(getActivity());//se hace el adater o se intancia,el aprameto que se las pasa el constructor es el contecto de la main Activity
        adapter.setContacts(contacts);//se le pone el arraylist

        contactsRecView.setAdapter(adapter);
        //contactsRecView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));//El linear leyourt es para indicar que los itesm va a estar en forma de Linear
        contactsRecView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        return view;
    }
}
package com.example.bancodetiempodeluxe;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link configPerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class configPerfilFragment extends Fragment {
    private Switch switchviewmode;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public configPerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment configPerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static configPerfilFragment newInstance(String param1, String param2) {
        configPerfilFragment fragment = new configPerfilFragment();
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
        View view = inflater.inflate(R.layout.fragment_config_perfil, container, false);

        View.OnClickListener chngMail = new View.OnClickListener(){
          @Override
          public void onClick(View v){
              startActivity(new Intent(getActivity(), ChangeEmail.class));
          }
        };

        View.OnClickListener chngPassw = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getActivity(), ChangePassword.class));
            }
        };

        View.OnClickListener helpo = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //startActivity(new Intent(getActivity(), Help.class));
                startActivity(new Intent(getActivity(), VerificationMain.class));
            }
        };

        View.OnClickListener rport = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getActivity(), ReportProblems.class));
            }
        };

        View.OnClickListener cntactUs = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getActivity(), ContactUs.class));
            }
        };

        LinearLayout changeEmail = view.findViewById(R.id.idCambiarCorreo);
        changeEmail.setOnClickListener(chngMail);

        LinearLayout changePassw = view.findViewById(R.id.idCambiarContra);
        changePassw.setOnClickListener(chngPassw);

        LinearLayout askHelpo = view.findViewById(R.id.idHelpo);
        askHelpo.setOnClickListener(helpo);

        LinearLayout report = view.findViewById(R.id.idReportProblems);
        report.setOnClickListener(rport);

        LinearLayout contac = view.findViewById(R.id.idContactUs);
        contac.setOnClickListener(cntactUs);

        switchviewmode=view.findViewById(R.id.configperfilmodeswitch);
        switchviewmode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){


                    switchviewmode.setText("Modo claro");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


                }else{


                    switchviewmode.setText("Modo oscuro");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                }
            }
        });

        return view;
    }
}
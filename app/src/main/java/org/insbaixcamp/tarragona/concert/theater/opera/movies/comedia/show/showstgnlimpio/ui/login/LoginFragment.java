package org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.MainActivity;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.R;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.data.RealtimeDatabase;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment{

    private FragmentLoginBinding binding;
    private LoginViewModel loginViewModel;
    private View root;
    private EditText etCorreu;
    private EditText etPsw;
    private Button btLogin;
    private Button btRegistre;

    public LoginFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loginViewModel =
                new ViewModelProvider(this).get(LoginViewModel.class);

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        etCorreu = binding.etCorreu;
        etPsw = binding.etContrasenya;
        btLogin = binding.bLogin;
        btRegistre = binding.bRegistre;

        btRegistre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main)
                        .navigate(R.id.nav_registre);
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correu = etCorreu.getText().toString().trim();
                String psw = etPsw.getText().toString().trim();
                if (!correu.isEmpty() && !psw.isEmpty()) {
                    if(FirebaseAuth.getInstance().getCurrentUser().isAnonymous()) {
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(correu, psw)
                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            ((MainActivity)getActivity()).updateUI(RealtimeDatabase.getInstance().carregaDadesUsuari(user.getUid()));
                                            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main)
                                                    .navigate(R.id.nav_home);
                                        } else {
                                            Log.i("error", task.getException().toString());
                                            Toast.makeText(getActivity(), "Error al iniciar sessio! Revisa les credencials",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(getActivity(), "Tanca sessio avants!",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Error al iniciar sessio! Revisa les credencials",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
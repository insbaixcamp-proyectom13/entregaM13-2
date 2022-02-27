package org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.ui.slideshow;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.MainActivity;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.R;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.databinding.FragmentSlideshowBinding;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.ui.slideshow.SlideshowFragment;
import org.jetbrains.annotations.NotNull;

public class SlideshowFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private FragmentSlideshowBinding binding;
    private SlideshowViewModel slideshowViewModel;
    private FirebaseAuth mAuth;
    private GoogleApiClient googleApiClient;

    public static final int SING_IN_CODE = 777;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /*
        NavigationView navigationView = root.findViewById(R.id.nav_view);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);

        Log.i("Header view",""+(headerView == null));*/

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(),this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        Button login = binding.bLogin;
        SignInButton loginGoogle = binding.bLogingoogle;

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText email = binding.etCorreu;
                EditText pass = binding.etContrasenya;

                String correu = email.getText().toString();
                String contra = pass.getText().toString();

                if (!correu.isEmpty() && !contra.isEmpty()){

                    mAuth.signInWithEmailAndPassword(correu,contra).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {

                            if (task.isSuccessful()){

/*                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference = firebaseDatabase.getReference("Nom");
                                DatabaseReference databaseReference2 = firebaseDatabase.getReference("Cognom");
                                DatabaseReference messageRef = databaseReference.child("Nom");
                                DatabaseReference messageRef2 = databaseReference2.child("Cognom");*/

                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);

/*                                Fragment loginFragment = new HomeFragment();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.fragment_inici,loginFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();*/

                                Navigation.findNavController(binding.getRoot()).navigate(R.id.nav_home);

                                //Toast.makeText(getContext(), "Funca", Toast.LENGTH_SHORT).show();

/*                                try {
                                    finalize();
                                } catch (Throwable throwable) {
                                    throwable.printStackTrace();
                                }*/

                            } else {
                                updateUI(null);
                                Toast.makeText(getContext(), "No s'ha pogut iniciar sesio", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {

                    Toast.makeText(getContext(), "Omple tots el camps", Toast.LENGTH_SHORT).show();
                }

            }
        });

        loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,SING_IN_CODE);
            }
        });

        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SING_IN_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSingInResult(result);
        } else {

            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleSingInResult(GoogleSignInResult result) {

        if (result.isSuccess()){
            goMainScreen();
        } else {
            Toast.makeText(getContext(), "No s'ha pogut iniciar sesio", Toast.LENGTH_SHORT).show();
        }
    }

    private void goMainScreen() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onConnectionFailed(@NonNull @NotNull ConnectionResult connectionResult) {
        Toast.makeText(getContext(), "Error de conexio", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if(mAuth.getInstance().getCurrentUser() != null) {
            mAuth.getInstance().getCurrentUser().getUid();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        googleApiClient.stopAutoManage(getActivity());
        googleApiClient.disconnect();
    }

    private void updateUI(FirebaseUser user) {

    }

    private void reload() { }
}
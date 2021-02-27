package com.codefestfinal.codefest21;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codefestfinal.codefest21.Model.User;
import com.codefestfinal.codefest21.databinding.FragmentLogInBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;


public class LogIn extends Fragment {

    FragmentLogInBinding logInBinding;
    FragmentManager fragmentManager;

    private GoogleSignInClient mGoogleSignInClient;
    private final int RC_SIGN_IN = 101;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    private String token;

    public LogIn() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity)getActivity()).hideToolbar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        logInBinding = FragmentLogInBinding.inflate(inflater, container, false);
        return logInBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getFCMToken();
        //config firestore
        db = FirebaseFirestore.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        mAuth = FirebaseAuth.getInstance();

        logInBinding.registerHereLable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(MainActivity.this, SignUp.class);
//                startActivity(intent);
            }
        });
        logInBinding.signInButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String UserEmail = logInBinding.useremailSignIn.getText().toString();
                String UserPassword = logInBinding.passwordSignIn.getText().toString();
            }
        });

        logInBinding.googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


    }

    private void firebaseAuthWithGoogle(String idToken) {
        // [START_EXCLUDE silent]
        startProgress();
        // [END_EXCLUDE]
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //  Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            isUserRegistered(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());


                        }


                    }
                });
    }


    private void isUserRegistered(FirebaseUser account) {


        db.collection("Users").whereEqualTo("email", account.getEmail()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();

                if (documentSnapshotList.size() > 0) {
                    DocumentSnapshot documentSnapshot = documentSnapshotList.get(0);
                    String userDocId = documentSnapshot.getId();
                    db.collection("Users").document(userDocId).update("fcmToken",token).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            User user = documentSnapshot.toObject(User.class);

                            Bundle b = new Bundle();
                            b.putString("userDocId", userDocId);
                          b.putString("userName", user.getFirstName()+" "+user.getLastName());

                            FragmentManager fragmentManager=getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.addToBackStack("Home");
                            Home home=new Home();
                            home.setArguments(b);
                            fragmentTransaction.replace(R.id.mainLayout,home,"Home");
                            fragmentTransaction.commit();
                        }
                    });


//

                } else {
                    final String displayName = account.getDisplayName();
                    final String email = account.getEmail();
                    String firstName = displayName.split(" ")[0];
                    String lastName = displayName.split(" ")[1];

                    Bundle bundle = new Bundle();
                    bundle.putString("FirstName", firstName);
                    bundle.putString("LastName", lastName);
                    bundle.putString("Email", email);


                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.addToBackStack("");

                    SignUp signUp = new SignUp();
                    signUp.setArguments(bundle);
                    fragmentTransaction.replace(R.id.mainLayout, signUp, "SignUp");
                    fragmentTransaction.commit();

                }
            }
        });
        stopProgress();
    }

    void startProgress() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    void stopProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {

            try {

                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                if (task.isSuccessful()) {

                    GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
                    if (googleSignInAccount != null) {

                        firebaseAuthWithGoogle(googleSignInAccount.getIdToken());
                    }

                }


            } catch (Exception e) {

                e.printStackTrace();
            }

            // handleSignInResult(task);
        }
    }

    private void getFCMToken() {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                       token = task.getResult();


                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
package com.codefestfinal.codefest21;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.codefestfinal.codefest21.Model.User;
import com.codefestfinal.codefest21.databinding.FragmentSignUpBinding;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignUp extends Fragment {

    private static final int IMAGE_SELECT = 52;
    FragmentSignUpBinding signUpBinding;
    private final int GOOGLE_SIGN_IN = 100;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> GenderArray;
    private Uri imageUri;
    private String userNameImage;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private ProgressDialog progressDialog;


    public SignUp() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GenderArray = new ArrayList<>();
        GenderArray.add("Male");
        GenderArray.add("Female");

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setContentView(R.layout.progress_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        signUpBinding = FragmentSignUpBinding.inflate(inflater, container, false);
        return signUpBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Bundle bundle = getArguments();
        if (bundle != null) {
            String firstName = bundle.getString("FirstName");
            String lastName = bundle.getString("LastName");
            String email = bundle.getString("Email");

            signUpBinding.firstNameTextSignUp.setText(firstName);
            signUpBinding.lastNameTextSignUp.setText(lastName);
            signUpBinding.emailTextSignUp.setText(email);
            //enable false
            signUpBinding.emailTextSignUp.setEnabled(false);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, GenderArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        signUpBinding.genderSpinnerSignUp.setAdapter(adapter);

        signUpBinding.imageUploadRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectImageFile = new Intent();
                selectImageFile.setAction(Intent.ACTION_GET_CONTENT);
                selectImageFile.setType("image/*");
                startActivityForResult(selectImageFile, IMAGE_SELECT);


            }
        });
        signUpBinding.googleSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewGoogleUsers();
            }
        });

        //register Button
        signUpBinding.registerButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstName = signUpBinding.firstNameTextSignUp.getText().toString();
                String lastName = signUpBinding.lastNameTextSignUp.getText().toString();
                String email = signUpBinding.emailTextSignUp.getText().toString();
                String mobile = signUpBinding.mobileTextSignUp.getText().toString();
                String nic = signUpBinding.nicTextSignUp.getText().toString();
                String gender = signUpBinding.genderSpinnerSignUp.getSelectedItem().toString();


                if (imageUri != null) {
                    uploadImage(firstName, lastName, nic, mobile, gender, email);
//                    uploadImage(String firstName, String lastName, String nic, String mobile, String gender, String email)
                } else {
                    Toast.makeText(getContext(), "Please Upload Image", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void viewGoogleUsers() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(

                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                GOOGLE_SIGN_IN);
        // [END auth_fui_create_intent]
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_SELECT && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            try {

                ImageView im = signUpBinding.imageUploadRegister;
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                im.setImageBitmap(bitmap);
                createImageName();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == GOOGLE_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            getDataFromGoogle(resultCode, data);
        }
    }


    void createImageName() {
        Editable text = signUpBinding.nicTextSignUp.getText();
        if (text != null) {

            userNameImage = text.toString() + "." + getFileExtension(imageUri);


        } else {
            Toast.makeText(getContext(), "Please Add NIC", Toast.LENGTH_SHORT).show();
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Return file Extension
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void getDataFromGoogle(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            final String displayName = user.getDisplayName();
            final String email = user.getEmail();
            String firstName = displayName.split(" ")[0];
            String lastName = displayName.split(" ")[1];
            final String uid = user.getUid();


            signUpBinding.emailTextSignUp.setText(email);
            signUpBinding.firstNameTextSignUp.setText(firstName);
            signUpBinding.lastNameTextSignUp.setText(lastName);

            //hide component
            signUpBinding.textView11.setVisibility(View.GONE);
            signUpBinding.googleSignUpButton.setVisibility(View.GONE);

            //enable false
            signUpBinding.emailTextSignUp.setEnabled(false);


        } else {

        }
    }

    private void uploadImage(String firstName, String lastName, String nic, String mobile, String gender, String email) {

        startProgress();
        storageRef.child("Users").child(userNameImage).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {


                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUrl = task.getResult();
                            User user = new User(firstName, lastName, nic, mobile, email, gender, downloadUrl.toString(), null);

                            db.collection("Users").add(user).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();

                                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                        LogIn logIn = new LogIn();
                                        fragmentTransaction.replace(R.id.mainLayout, logIn, "LogIn");
                                        fragmentTransaction.commit();

                                        signUpBinding.firstNameTextSignUp.setText(null);
                                        signUpBinding.lastNameTextSignUp.setText(null);
                                        signUpBinding.nicTextSignUp.setText(null);
                                        signUpBinding.mobileTextSignUp.setText(null);
                                        signUpBinding.emailTextSignUp.setText(null);
                                        signUpBinding.imageUploadRegister.setImageResource(R.drawable.ic_photo);

                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                    }
                });

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
        stopProgress();
    }

    void startProgress() {

        progressDialog.show();
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    void stopProgress() {
        progressDialog.dismiss();
    }
}
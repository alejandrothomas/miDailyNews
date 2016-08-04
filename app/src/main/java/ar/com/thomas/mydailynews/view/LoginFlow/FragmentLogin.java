package ar.com.thomas.mydailynews.view.LoginFlow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.TwitterAuthProvider;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import ar.com.thomas.mydailynews.R;
import ar.com.thomas.mydailynews.view.MainActivity;
import io.fabric.sdk.android.Fabric;

public class FragmentLogin extends Fragment {

    private LoginButton fbLoginButton;
    private TwitterLoginButton twLoginButton;
    private FrameLayout frameLayout;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser user;
    private AuthCredential credential;
    private static final String TWITTER_KEY = "n17S5rk1q3FkyeAQGX3xXtchD";
    private static final String TWITTER_SECRET = "n5snTcbuv3DHDLVCmbe5NXXgTsFW2tr91qiNGa1uEKSXYXa9dn";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(getContext(), new Twitter(authConfig));

        View view = inflater.inflate(R.layout.fragment_login, container, false);


        callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    Log.d("status","SIGNED IN" + user.getEmail() + " " + user.getUid() + " " + user.getDisplayName() + " " + user.getPhotoUrl());
                }else {
                    Log.d("status", "NOT LOGGED");
                }
            }
        };

        frameLayout = (FrameLayout)view.findViewById(R.id.bgFrameLayoutLogin);
        fbLoginButton = (LoginButton) view.findViewById(R.id.fb_login_button);
        twLoginButton = (TwitterLoginButton) view.findViewById(R.id.twitter_login_button);

        fbLoginButton.setReadPermissions("email", "public_profile");
        fbLoginButton.setFragment(this);

        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                ((MainActivity)getContext()).setLoginButtonColor(R.drawable.com_facebook_button_icon_blue);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });

        twLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                TwitterSession session = result.data;
                ((MainActivity)getContext()).setLoginButtonColor(R.drawable.tw__composer_logo_blue);
                handleTwitterSession(session);

            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });



        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            callbackManager.onActivityResult(requestCode,resultCode,data);
            twLoginButton.onActivityResult(requestCode,resultCode,data);

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthStateListener);
    }

    private void handleFacebookAccessToken(AccessToken token){
        credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()){
                    ((MainActivity)getActivity()).setSnackbar(getString(R.string.loginFail));
                }else{
                    ((MainActivity)getActivity()).setSnackbar("Welcome " + user.getDisplayName() + " :)");
                    getFragmentManager().popBackStack();
                }
            }
        });
    }

    private void handleTwitterSession(TwitterSession session) {

        credential = TwitterAuthProvider.getCredential(session.getAuthToken().token,session.getAuthToken().secret);

        mAuth.signInWithCredential(credential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            ((MainActivity)getActivity()).setSnackbar(getString(R.string.loginFail));

                        }else{
                            ((MainActivity)getActivity()).setSnackbar("Welcome " + user.getDisplayName() + " :)");
                            getFragmentManager().popBackStack();
                        }

                    }
                });
    }

}

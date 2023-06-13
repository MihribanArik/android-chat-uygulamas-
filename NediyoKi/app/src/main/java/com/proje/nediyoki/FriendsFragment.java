package com.proje.nediyoki;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FriendsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendsFragment newInstance(String param1, String param2) {
        FriendsFragment fragment = new FriendsFragment();
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

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String uid;

    DatabaseReference mDatabase;
    DatabaseReference usersRef;
    DatabaseReference friendsRef;

    RecyclerView recyclerView;
    ArrayList<String> friends_uid;
    ArrayList<User> users;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        recyclerView = view.findViewById(R.id.rcwFriends);
        friends_uid = new ArrayList<String>();
        users = new ArrayList<User>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        uid = firebaseUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        usersRef = mDatabase.child("users");
        friendsRef = mDatabase.child("friends");

        friendsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friends_uid.clear();
                for (DataSnapshot item: snapshot.getChildren()) {
                    if (item.child("uidFirst").getValue().toString().equals(uid)){
                        friends_uid.add(item.child("uidSecond").getValue().toString());
                    }
                    if (item.child("uidSecond").getValue().toString().equals(uid)){
                        friends_uid.add(item.child("uidFirst").getValue().toString());
                    }
                }
                FriendAdapter friendAdapter = new FriendAdapter(users, getContext());
                recyclerView.setAdapter(friendAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Bir hata oluştu", Toast.LENGTH_SHORT).show();
            }
        });

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot user: snapshot.getChildren()) {
                    for(String i: friends_uid){
                        if (user.child("uid").getValue().toString().equals(i)){
                            users.add(new User(user.child("uid").getValue().toString(),
                                    user.child("userName").getValue().toString(),
                                    user.child("email").getValue().toString(),
                                    user.child("description").getValue().toString()));
                            break;
                        }
                    }
                }
                FriendAdapter friendAdapter = new FriendAdapter(users, getContext());
                recyclerView.setAdapter(friendAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Bir hata oluştu.", Toast.LENGTH_SHORT).show();
            }
        });

        //users.add(new User("dfd","dfdf","dfd","dfdfd"));
        FriendAdapter friendAdapter = new FriendAdapter(users, getContext());
        recyclerView.setAdapter(friendAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}
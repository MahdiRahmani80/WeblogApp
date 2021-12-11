package com.mahdirahmani8.weblogapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.mahdirahmani8.weblogapp.Adopter.GetTempBlogAdapter;
import com.mahdirahmani8.weblogapp.DataBase.DataBase;
import com.mahdirahmani8.weblogapp.R;
import com.mahdirahmani8.weblogapp.write_blog;

import java.util.Objects;

public class CreateFragment extends Fragment {

    View view;
    RelativeLayout next, rel_create;
    TextView title, disc;
    RecyclerView rec_archive;
    ImageView img_archive, back;
    TextInputEditText etSubject;
    boolean isArchiveShow = false;
    GetTempBlogAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.create_blog_fragment, container, false);
        cast();
        OnClick();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        img_archive.setOnClickListener(v -> {

            isArchiveShow = !isArchiveShow;
            title.setVisibility(View.GONE);
            disc.setVisibility(View.GONE);
            rel_create.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);
            rec_archive.setVisibility(View.VISIBLE);
            rec_archive.setHasFixedSize(true);
            rec_archive.setLayoutManager(new LinearLayoutManager(getContext()));

            DataBase dataBase = new DataBase(getContext());
            adapter = new GetTempBlogAdapter(getContext(), dataBase.getTempBlog(dataBase.getId()));
            rec_archive.setAdapter(adapter);


        });

        back.setOnClickListener(v -> {
            isArchiveShow = !isArchiveShow;
            title.setVisibility(View.VISIBLE);
            disc.setVisibility(View.VISIBLE);
            rel_create.setVisibility(View.VISIBLE);
            rec_archive.setVisibility(View.GONE);
            back.setVisibility(View.GONE);

        });

    }

    private void OnClick() {
        next.setOnClickListener(v -> {
            if (
                    etSubject != null &&
                            Objects.requireNonNull(etSubject.getText()).toString().hashCode() != 0 &&
                            etSubject.getText().toString().length() > 2 &&
                            etSubject.getText().toString().length() <= 36
            ) {
                Intent intent = new Intent(getActivity(), write_blog.class);
                intent.putExtra("SUB", Objects.requireNonNull(etSubject.getText()).toString().trim());
                startActivity(intent);

            } else {
                assert etSubject != null;
                if (etSubject.getText().toString().length() > 36) {
                    Toast.makeText(getContext(),"this subject is too long!",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "this subject is too short", Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    private void cast() {
        next = view.findViewById(R.id.next_page);
        etSubject = view.findViewById(R.id.et_subject);
        disc = view.findViewById(R.id.txt_disc);
        title = view.findViewById(R.id.txt_create);
        img_archive = view.findViewById(R.id.img_archive);
        rel_create = view.findViewById(R.id.REL_create);
        rec_archive = view.findViewById(R.id.rec_create);
        back = view.findViewById(R.id.back);
    }
}

package com.example.cleaningapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.lifecycle.Observer;

import java.io.FileOutputStream;

import android.Manifest;
import android.accounts.Account;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cleaningapplication.Model.LogActivity;
import com.example.cleaningapplication.ViewHolder.LogActivityViewHolder;
import com.example.cleaningapplication.menu.CProfiles;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class clientLogActivity extends AppCompatActivity {
    RecyclerView myitemList;
    private FirebaseUser User;
    private FirebaseAuth Auth;
    private ProgressDialog progressDialog;
    private ImageView back;
    FloatingActionButton fab;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_log);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating your file...");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("is showing data");

        back = (ImageView) findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(clientLogActivity.this, CProfiles.class);
                startActivity(intent);
            }
        });
        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();

        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);

        myitemList = (RecyclerView) findViewById(R.id.listofLog);
        myitemList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(clientLogActivity.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myitemList.setLayoutManager(linearLayoutManager);


        fab = findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference ProductsRef = FirebaseDatabase.getInstance().getReference().child("Log");


        FirebaseRecyclerOptions<LogActivity> options = new FirebaseRecyclerOptions.Builder<LogActivity>()
                .setQuery(ProductsRef.orderByChild("Users").equalTo(User.getUid()), LogActivity.class).build();

        FirebaseRecyclerAdapter<LogActivity, LogActivityViewHolder> adapter =
                new FirebaseRecyclerAdapter<LogActivity, LogActivityViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull LogActivityViewHolder logActivityViewHolder, int i, @NonNull LogActivity logActivity) {
                        logActivityViewHolder.date.setText(logActivity.getDate());
                        logActivityViewHolder.action.setText(logActivity.getAction());
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                try {
                                    List<List<String>> data = new ArrayList<>();
                                    for (int s = 0; s < myitemList.getChildCount(); s++) {

                                        View itemView = myitemList.getChildAt(s);
                                        TextView dates = itemView.findViewById(R.id.date_log);
                                        TextView actions = itemView.findViewById(R.id.action_log);

                                        List<String> row = new ArrayList<>();
                                        row.add(dates.getText().toString());
                                        row.add(actions.getText().toString());
                                        data.add(row);
                                    }

// Step 3: Use Apache POI to create an Excel workbook and write the data to it
                                    Workbook workbook = new XSSFWorkbook();
                                    Sheet sheet = workbook.createSheet("Action Log");

                                    for (int i = 0; i < data.size(); i++) {
                                        Row row = sheet.createRow(i);
                                        List<String> rowData = data.get(i);
                                        for (int j = 0; j < rowData.size(); j++) {
                                            Cell cell = row.createCell(j);
                                            cell.setCellValue(rowData.get(j));
                                        }
                                    }
// Step 4: Save the Excel file to a location that the user can access
                                    String strDate = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss", Locale.getDefault()).format(new Date());
                                    File root = new File(Environment
                                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "FileExcel");
                                    if (!root.exists())
                                        root.mkdirs();
                                    File path = new File(root, "/" + strDate + ".xlsx");
                                    FileOutputStream outputStream = new FileOutputStream(path);

                                    workbook.write(outputStream);
                                    outputStream.close();
                                    Toast.makeText(clientLogActivity.this, "Data successfully exported!", Toast.LENGTH_SHORT).show();

                                    progressDialog.dismiss();
                                    Toast.makeText(clientLogActivity.this, "You can access your file by going to Internal storage/Documents/FileExcel.", Toast.LENGTH_LONG).show();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        });


                    }

                    @NonNull
                    @Override
                    public LogActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_log_layout, parent, false);
                        LogActivityViewHolder holder = new LogActivityViewHolder(view);
                        return holder;
                    }
                };
        myitemList.setAdapter(adapter);
        adapter.startListening();

    }



    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(clientLogActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(clientLogActivity.this, new String[]{permission}, requestCode);
        } else {
            Toast.makeText(clientLogActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(clientLogActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(clientLogActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /**/


}
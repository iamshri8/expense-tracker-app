package com.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddTransactionActivity extends AppCompatActivity {

    enum TransactionType {
        INCOME("income"),
        EXPENSE("expense");

        private final String transType;

        TransactionType(String transactionType) {
            this.transType = transactionType;
        }

        public String getTransactionType() {
            return transType;
        }
    }

    TransactionType transactionType = TransactionType.EXPENSE;

    private RadioGroup transactionTypeRG;

    private EditText amount;
    private EditText note;
    private Button addTransactionBtn;

    private ProgressDialog dialog;

    private FirebaseFirestore fireStore;

    private FirebaseAuth auth;

    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        fireStore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        dialog = new ProgressDialog(this);

        saveTransactionToDB();


    }

    private void saveTransactionToDB() {
        amount = findViewById(R.id.amount_add_transaction);
        note = findViewById(R.id.note_add_transaction);
        transactionTypeRG = findViewById(R.id.radio_group_income_expense);
        addTransactionBtn = findViewById(R.id.add_transaction_button);

        transactionTypeRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedBtnId) {
                switch (checkedBtnId) {
                    case R.id.radio_btn_expense:
                        transactionType = TransactionType.EXPENSE;
                        break;
                    case R.id.radio_btn_income:
                        transactionType = TransactionType.INCOME;
                        break;
                }
            }
        });

        addTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amountVal = amount.getText().toString().trim();
                String noteVal = note.getText().toString().trim();
                String transactionTypeVal = transactionType.getTransactionType();

                if (TextUtils.isEmpty(amountVal)) {
                    amount.setError("Amount field required!");
                    return;
                }

                dialog.setMessage("Processing...");

                String id = UUID.randomUUID().toString();
                Map<String, Object> transaction = new HashMap<>();
                transaction.put("id", id);
                transaction.put("amount", amountVal);
                transaction.put("notes", noteVal);
                transaction.put("type", transactionTypeVal);

                fireStore.collection("transactions")
                        .document(auth.getUid()).collection("eachTransaction")
                        .document(id).set(transaction)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Transaction successfully registered", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), DashboardActivity.class));

                                } else {
                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Some error occurred. Please try again later!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        });


    }
}

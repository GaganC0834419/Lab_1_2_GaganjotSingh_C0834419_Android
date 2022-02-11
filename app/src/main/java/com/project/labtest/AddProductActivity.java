package com.project.labtest;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.project.labtest.db.DatabaseClient;
import com.project.labtest.db.entities.AddExpense;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddProductActivity extends BaseActivity {

    @BindView(R.id.mNameEt)
    EditText mNameEt;
    @BindView(R.id.description_et)
    EditText descriptionEt;
    @BindView(R.id.mPriceEt)
    EditText mPriceEt;
    @BindView(R.id.mLatEt)
    EditText mLatEt;
    @BindView(R.id.mLngEt)
    EditText mLngEt;
    @BindView(R.id.saveBtn)
    Button mSaveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.saveBtn)
    public void onClick() {
        if(mNameEt.getText().toString().trim().isEmpty()
        || descriptionEt.getText().toString().trim().isEmpty()
        ||mPriceEt.getText().toString().trim().isEmpty()
        ||mLatEt.getText().toString().trim().isEmpty()
        ||mLngEt.getText().toString().trim().isEmpty()){
            Toast.makeText(AddProductActivity.this, "Please enter all teh details", Toast.LENGTH_SHORT).show();
        }
        else{
            saveProduct();
        }
    }

    private void saveProduct() {
        class SaveExpense extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                AddExpense addIncome = new AddExpense(mNameEt.getText().toString().trim(),descriptionEt.getText().toString().trim()
                ,mPriceEt.getText().toString().trim(),mLatEt.getText().toString().trim(),mLngEt.getText().toString().trim());

                //adding to database
                DatabaseClient.getInstance(AddProductActivity.this).getAppDatabase()
                        .addExpenseDao()
                        .insert(addIncome);
                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(AddProductActivity.this, "Your data has been saved successfully", Toast.LENGTH_LONG).show();
                startActivity( new Intent(AddProductActivity.this,ProductsActivty.class));
                finish();
            }
        }

        SaveExpense saveExpense = new SaveExpense();
        saveExpense.execute();
    }
}
package com.project.labtest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.labtest.db.AppDatabase;
import com.project.labtest.db.DatabaseClient;
import com.project.labtest.db.entities.AddExpense;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsActivty extends BaseActivity {

    @BindView(R.id.searchEt)
    EditText mSearchEt;
    @BindView(R.id.noData_ll)
    LinearLayout mNoDataLl;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mAddProductBtn)
    FloatingActionButton mAddProductBtn;
    @BindView(R.id.mTotalTv)
    TextView mTotalTv;

    private int[] mMaterialColors;

    List<AddExpense> ledgers;
    ArrayList<AddExpense> newList;
    LedgerAdapter ledgerAdapter;

    private static final Random RANDOM = new Random();
    BottomSheetDialog dialog1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_activty);
        ButterKnife.bind(this);

        mMaterialColors = getResources().getIntArray(R.array.colors);
        mSearchEt.setText("");

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ProductsActivty.this));

        getProducts();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSearchEt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() == 0) {
                            ledgerAdapter.filterList(ledgers);
                        } else {
                            ((LedgerAdapter) mRecyclerView.getAdapter()).filter(s.toString());

                        }

                    }
                });
            }
        }, 100);

        mAddProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductsActivty.this, AddProductActivity.class));

            }
        });
    }

    private void getProducts() {

        class GetLedgerHistory extends AsyncTask<Void, Void, List<AddExpense>> {

            @Override
            protected List<AddExpense> doInBackground(Void... voids) {
                ledgers = DatabaseClient
                        .getInstance(ProductsActivty.this)
                        .getAppDatabase()
                        .addExpenseDao()
                        .getAll();
                return ledgers;
            }

            @Override
            protected void onPostExecute(List<AddExpense> ledgers) {
                super.onPostExecute(ledgers);

                mTotalTv.setText("Total Products "+ledgers.size()+"");

                newList = new ArrayList<>(ledgers.size());

                if (ledgers.size() == 0) {
                    mNoDataLl.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);


                } else {

                    mRecyclerView.setVisibility(View.VISIBLE);
                    mNoDataLl.setVisibility(View.GONE);
                    ledgerAdapter = new LedgerAdapter(ProductsActivty.this, ledgers);
                    Log.e("List", ledgers + "");
                    mRecyclerView.setAdapter(ledgerAdapter);
                }


            }
        }
        GetLedgerHistory getLedgerHistory = new GetLedgerHistory();
        getLedgerHistory.execute();
    }


    //---------------------------lEDGER lIST Adapter------------------------------------//
    public class LedgerAdapter extends RecyclerView.Adapter<LedgerAdapter.MyViewHolder> {

        Context context;
        List<AddExpense> childFeedList;


        public LedgerAdapter(Context context, List<AddExpense> childFeedList) {
            this.context = context;
            this.childFeedList = childFeedList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ledger_list_design, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final AddExpense ledger = childFeedList.get(position);
            holder.mAccountNameTv.setText(ledger.getName());
            holder.mAmountTv.setTextColor(Color.parseColor("#689F38"));
            holder.mAmountTv.setText("$ " + ledger.getPrice());


            holder.mIcon.setInitials(true);
            holder.mIcon.setShapeType(MaterialLetterIcon.Shape.CIRCLE);
            holder.mIcon.setLetter(ledger.getName().substring(0, 1));
            holder.mIcon.setLetterSize(16);
            holder.mIcon.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);


            holder.mDeleteTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    deleteSelectedRow(ledger);

                }
            });

            holder.mEditTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setEditDialog(ledger);

                }
            });

            holder.mFrontLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }

        private void setEditDialog(AddExpense ledger) {
            View view = getLayoutInflater().inflate(R.layout.demo2_layout, null);

            TextView mNameEt = view.findViewById(R.id.mNameEt);
            TextView descriptionEt = view.findViewById(R.id.description_et);
            TextView mPriceEt = view.findViewById(R.id.mPriceEt);
            TextView mLatEt = view.findViewById(R.id.mLatEt);
            TextView mLngEt = view.findViewById(R.id.mLngEt);
            Button saveBtn = view.findViewById(R.id.saveBtn);

            mNameEt.setText(ledger.getName());
            descriptionEt.setText(ledger.getDescription());
            mPriceEt.setText(ledger.getPrice());
            mLatEt.setText(ledger.getLat());
            mLngEt.setText(ledger.getLng());

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mNameEt.getText().toString().trim().isEmpty()
                            || descriptionEt.getText().toString().trim().isEmpty()
                            || mPriceEt.getText().toString().trim().isEmpty()
                            || mLatEt.getText().toString().trim().isEmpty()
                            || mLngEt.getText().toString().trim().isEmpty()) {
                        Toast.makeText(ProductsActivty.this, "Please enter all teh details", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog1.dismiss();
                        updateProduct(ledger, mNameEt.getText().toString().trim(), descriptionEt.getText().toString().trim(),
                                mPriceEt.getText().toString().trim(), mLatEt.getText().toString().trim(), mLngEt.getText().toString().trim());
                    }
                }
            });


            dialog1 = new BottomSheetDialog(ProductsActivty.this);
            dialog1.setContentView(view);
            dialog1.show();
        }

        private void updateProduct(AddExpense ledger, String name, String des, String price, String lat, String lng) {
            class SaveExpense extends AsyncTask<Void, Void, Void> {

                @Override
                protected Void doInBackground(Void... voids) {

                    ledger.setName(name);
                    ledger.setDescription(des);
                    ledger.setPrice(price);
                    ledger.setLat(lat);
                    ledger.setLng(lng);

                    //adding to database
                    DatabaseClient.getInstance(ProductsActivty.this).getAppDatabase()
                            .addExpenseDao()
                            .update(ledger);
                    return null;

                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    Toast.makeText(ProductsActivty.this, "Your data has been updated successfully", Toast.LENGTH_LONG).show();
                    getProducts();
                }
            }

            SaveExpense saveExpense = new SaveExpense();
            saveExpense.execute();
        }


        @Override
        public int getItemCount() {
            return childFeedList.size();
        }

        public void filterList(List<AddExpense> filteredList) {
            this.childFeedList = filteredList;
            notifyDataSetChanged();
        }

        public void filter(String text) {
            List<AddExpense> filteredList = new ArrayList<>();
            for (AddExpense item : childFeedList) {
                Log.e("Item", item.getName());
                if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
            this.childFeedList = filteredList;
            notifyDataSetChanged();
            ledgerAdapter.filterList(filteredList);
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView mAccountNameTv;
            TextView mAmountTv;
            TextView mDeleteTv;
            TextView mEditTv;
            MaterialLetterIcon mIcon;
            View mFrontLayout;


            public MyViewHolder(View itemView) {
                super(itemView);

                mAccountNameTv = (TextView) itemView.findViewById(R.id.accountName_tv);
                mAmountTv = (TextView) itemView.findViewById(R.id.amount_tv);
                mDeleteTv = (TextView) itemView.findViewById(R.id.delete_tv);
                mEditTv = (TextView) itemView.findViewById(R.id.edit_tv);
                mIcon = (MaterialLetterIcon) itemView.findViewById(R.id.imageIcon);
                mFrontLayout = (FrameLayout) itemView.findViewById(R.id.frontLayout);


            }
        }
    }

    private void deleteSelectedRow(AddExpense model) {
        class DeleteSelectedRow extends AsyncTask<Void, Void, Void> {

            boolean isSuccess = false;

            @Override
            protected Void doInBackground(Void... voids) {

                final AppDatabase appDatabase = DatabaseClient.getInstance(ProductsActivty.this).getAppDatabase();
                appDatabase.runInTransaction(new Runnable() {
                    @Override
                    public void run() {
                        appDatabase.addExpenseDao().delete(model);

                    }
                });


                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
               getProducts();
            }
        }

        DeleteSelectedRow deleteSelectedRow = new DeleteSelectedRow();
        deleteSelectedRow.execute();
    }
}
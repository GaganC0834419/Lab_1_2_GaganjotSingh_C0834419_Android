package com.project.labtest;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.project.labtest.db.DatabaseClient;
import com.project.labtest.db.entities.AddExpense;

import java.util.ArrayList;
import java.util.List;

public class MainApplication extends Application {

    List<AddExpense> list=new ArrayList<>();
    SharedPreference sharedPreference;


    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreference=new SharedPreference(MainApplication.this);

        list.add(new AddExpense("Product1","Description 1","10","51.4638","0.6500"));
        list.add(new AddExpense("Product2","Description 2","20","51.4638","0.6500"));
        list.add(new AddExpense("Product3","Description 3","30","51.4638","0.6500"));
        list.add(new AddExpense("Product4","Description 4","40","51.4638","0.6500"));
        list.add(new AddExpense("Product5","Description 5","50","51.4638","0.6500"));
        list.add(new AddExpense("Product6","Description 6","60","51.4638","0.6500"));
        list.add(new AddExpense("Product7","Description 7","70","51.4638","0.6500"));
        list.add(new AddExpense("Product8","Description 8","80","51.4638","0.6500"));
        list.add(new AddExpense("Product9","Description 9","90","51.4638","0.6500"));
        list.add(new AddExpense("Product10","Description 10","10","51.4638","0.6500"));

        if(sharedPreference.isFirstTime()==false){
            saveProduct();
        }




    }

    private void saveProduct() {
        sharedPreference.setFirstTime();
        class SaveExpense extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                for(int i=0;i<list.size();i++){
                    AddExpense addIncome = new AddExpense(list.get(i).getName(),list.get(i).getDescription(),list.get(i).getPrice(),
                            list.get(i).getLat(),list.get(i).getLng());

                    //adding to database
                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                            .addExpenseDao()
                            .insert(addIncome);
                }

                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Your data has been saved successfully", Toast.LENGTH_LONG).show();
            }
        }

        SaveExpense saveExpense = new SaveExpense();
        saveExpense.execute();
    }
}

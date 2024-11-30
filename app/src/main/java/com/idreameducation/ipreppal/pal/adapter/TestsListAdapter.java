package com.idreameducation.ipreppal.pal.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.model.TestModel;

import java.util.List;

public class TestsListAdapter extends RecyclerView.Adapter<TestsListAdapter.MyViewHolder> {

    public List<TestModel> testsList;
    private String language;
    String marksText;
    public TestsListAdapter(List<TestModel> testsList,String language) {
        this.testsList = testsList;
        this.language = language;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tests_item_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String name = testsList.get(position).getName();

        if (name=="डायग्नोस्टिक परीक्षण"||name=="Diagnostic Test")
        {
            if (language.equalsIgnoreCase("hindi"))
            {
                holder.name.setText("डायग्नोस्टिक परीक्षण");
                marksText = "अंक: ";
            }else {
                holder.name.setText("Diagnostic Test");
                marksText = "Marks: ";
            }

        }else if (name=="परीक्षा"||name=="Test")
        {
            if (language.equalsIgnoreCase("hindi"))
            {
                holder.name.setText("अंतिम परीक्षा");
                marksText = "अंक: ";
            }else {
                holder.name.setText("Final Test");
                marksText = "Score : ";
            }
        }

        if(testsList.get(position).getScore().equals("0")){
            holder.score.setVisibility(View.GONE);
        }else{
            holder.score.setVisibility(View.VISIBLE);
            holder.score.setText(marksText+testsList.get(position).getScore());
        }
    }

    @Override
    public int getItemCount() {
        return testsList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, score;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.textViewTestName);
            score = view.findViewById(R.id.textViewTestScore);

        }

    }

}

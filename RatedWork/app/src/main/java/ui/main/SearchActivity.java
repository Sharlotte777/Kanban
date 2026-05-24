package ui.main;

import android.os.Bundle;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ratedwork.R;

public class SearchActivity extends AppCompatActivity {
    private MainViewModel viewModel;
    private TicketAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        RecyclerView resultsRecycler = findViewById(R.id.searchResultsRecycler);
        resultsRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TicketAdapter(viewModel);
        resultsRecycler.setAdapter(adapter);

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Выполняется при нажатии Enter
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void performSearch(String query) {
        if (query == null || query.trim().isEmpty()) {
            adapter.setTickets(new java.util.ArrayList<>());
        } else {
            viewModel.searchTickets(query).observe(this, tickets -> {
                adapter.setTickets(tickets);
            });
        }
    }
}
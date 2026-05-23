package ui.main;

import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ratedwork.R;
import models.User;
import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> users = new ArrayList<>();
    private MainViewModel viewModel;
    private Handler clickHandler = new Handler();
    private int clickCount = 0;
    private Runnable clickRunnable;

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public void setViewModel(MainViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @NonNull @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.name.setText(user.getName());
        holder.chatId.setText(user.getTelegramChatId());

        holder.itemView.setOnClickListener(v -> {
            clickCount++;
            if (clickRunnable != null) clickHandler.removeCallbacks(clickRunnable);
            clickRunnable = () -> {
                if (clickCount == 1) {
                    Intent intent = new Intent(v.getContext(), UserEditActivity.class);
                    intent.putExtra("user_id", user.getId());
                    intent.putExtra("user", user);
                    v.getContext().startActivity(intent);
                } else if (clickCount == 2) {
                    User copy = new User();
                    copy.setId(java.util.UUID.randomUUID().toString());
                    copy.setName(user.getName() + " (copy)");
                    copy.setTelegramChatId(user.getTelegramChatId());
                    viewModel.insertUser(copy);
                } else if (clickCount >= 3) {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Delete user")
                            .setMessage("Delete \"" + user.getName() + "\"?")
                            .setPositiveButton("Delete", (dialog, which) -> viewModel.deleteUser(user))
                            .setNegativeButton("Cancel", null)
                            .show();
                }
                clickCount = 0;
                clickRunnable = null;
            };
            clickHandler.postDelayed(clickRunnable, 300);
        });
    }

    @Override
    public int getItemCount() { return users.size(); }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name, chatId;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.userName);
            chatId = itemView.findViewById(R.id.userTelegramId);
        }
    }
}
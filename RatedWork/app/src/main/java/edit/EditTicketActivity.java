package edit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import models.Ticket;
import ui.main.MainViewModel;
import utils.FileHelper;
import com.example.ratedwork.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EditTicketActivity extends AppCompatActivity {
    private EditText titleEdit, descriptionEdit, tagsEdit, assigneeEdit, linkEdit;
    private Button saveButton, attachButton;
    private ChipGroup attachmentsGroup;
    private MainViewModel viewModel;
    private Ticket existingTicket;
    private List<String> attachmentUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ticket);

        titleEdit = findViewById(R.id.editTitle);
        descriptionEdit = findViewById(R.id.editDescription);
        tagsEdit = findViewById(R.id.editTags);
        assigneeEdit = findViewById(R.id.editAssignee);
        linkEdit = findViewById(R.id.editLink);
        saveButton = findViewById(R.id.buttonSave);
        attachButton = findViewById(R.id.buttonAttach);
        attachmentsGroup = findViewById(R.id.attachmentsGroup);

        String ticketId = getIntent().getStringExtra("ticket_id");
        if (ticketId != null) {
            existingTicket = (Ticket) getIntent().getSerializableExtra("ticket");
            if (existingTicket != null) {
                titleEdit.setText(existingTicket.getTitle());
                descriptionEdit.setText(existingTicket.getDescription());
                tagsEdit.setText(existingTicket.getTags());
                assigneeEdit.setText(existingTicket.getAssigneeId());
                linkEdit.setText(existingTicket.getExternalLink());
                if (existingTicket.getAttachments() != null && !existingTicket.getAttachments().isEmpty()) {
                    String[] urls = existingTicket.getAttachments().split(";");
                    for (String url : urls) {
                        attachmentUrls.add(url);
                        addChipForAttachment(url);
                    }
                }
            }
        }

        attachButton.setOnClickListener(v -> openFileChooser());
        saveButton.setOnClickListener(v -> saveTicket());
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            FileHelper.uploadFile(this, fileUri, new FileHelper.OnFileUploadedListener() {
                @Override
                public void onSuccess(String downloadUrl) {
                    attachmentUrls.add(downloadUrl);
                    addChipForAttachment(downloadUrl);
                }
                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(EditTicketActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void addChipForAttachment(String url) {
        Chip chip = new Chip(this);
        chip.setText(url.substring(url.lastIndexOf('/') + 1));
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(v -> {
            attachmentUrls.remove(url);
            attachmentsGroup.removeView(chip);
        });
        attachmentsGroup.addView(chip);
    }

    private void saveTicket() {
        String title = titleEdit.getText().toString().trim();
        if (title.isEmpty()) {
            titleEdit.setError("Required");
            return;
        }
        Ticket ticket = existingTicket != null ? existingTicket : new Ticket();
        if (existingTicket == null) {
            ticket.setId(UUID.randomUUID().toString());
            ticket.setCreatedAt(System.currentTimeMillis());
        }
        ticket.setTitle(title);
        ticket.setDescription(descriptionEdit.getText().toString());
        ticket.setTags(tagsEdit.getText().toString());
        ticket.setAssigneeId(assigneeEdit.getText().toString());
        ticket.setExternalLink(linkEdit.getText().toString());
        ticket.setAttachments(android.text.TextUtils.join(";", attachmentUrls));
        ticket.setUpdatedAt(System.currentTimeMillis());

        if (existingTicket == null) {
            // Новый тикет – нужно задать boardId и статус по умолчанию
            ticket.setBoardId(getIntent().getStringExtra("board_id"));
            ticket.setStatus("todo");
            ticket.setPosition(0);
            viewModel.insertTicket(ticket);
        } else {
            viewModel.updateTicket(ticket);
        }
        finish();
    }
}
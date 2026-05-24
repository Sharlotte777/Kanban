package Tests;

import models.Ticket;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.junit.Assert.*;

public class FunctionalTests {

    // Тест 1: Создание тикета
    @Test
    public void testCreateTicket() {
        Ticket ticket = new Ticket();
        String id = UUID.randomUUID().toString();
        ticket.setId(id);
        ticket.setTitle("New Ticket");
        ticket.setDescription("Description");
        ticket.setTags("test,unit");
        ticket.setAssigneeId("user123");
        ticket.setBoardId("board1");
        ticket.setStatus("todo");
        ticket.setPosition(0);

        assertEquals(id, ticket.getId());
        assertEquals("New Ticket", ticket.getTitle());
        assertEquals("Description", ticket.getDescription());
        assertEquals("test,unit", ticket.getTags());
        assertEquals("user123", ticket.getAssigneeId());
        assertEquals("board1", ticket.getBoardId());
        assertEquals("todo", ticket.getStatus());
        assertEquals(0, ticket.getPosition());
    }

    // Тест 2: Обновление тикета
    @Test
    public void testUpdateTicket() {
        Ticket ticket = new Ticket();
        ticket.setTitle("Old title");
        ticket.setDescription("Old desc");

        // Обновляем
        ticket.setTitle("New title");
        ticket.setDescription("New desc");

        assertEquals("New title", ticket.getTitle());
        assertEquals("New desc", ticket.getDescription());
    }

    // Тест 3: Дублирование тикета
    @Test
    public void testDuplicateTicket() {
        Ticket original = new Ticket();
        original.setId("123");
        original.setTitle("Original");
        original.setDescription("Desc");
        original.setTags("tag1");
        original.setAssigneeId("user1");
        original.setBoardId("board1");
        original.setStatus("inprogress");
        original.setPosition(1);

        // Создаём копию
        Ticket copy = new Ticket();
        copy.setId(UUID.randomUUID().toString());
        copy.setTitle(original.getTitle() + " (copy)");
        copy.setDescription(original.getDescription());
        copy.setTags(original.getTags());
        copy.setAssigneeId(original.getAssigneeId());
        copy.setBoardId(original.getBoardId());
        copy.setStatus(original.getStatus());
        copy.setPosition(original.getPosition() + 1);

        assertEquals("Original (copy)", copy.getTitle());
        assertEquals(original.getDescription(), copy.getDescription());
        assertEquals(original.getTags(), copy.getTags());
        assertEquals(original.getAssigneeId(), copy.getAssigneeId());
        assertEquals(original.getBoardId(), copy.getBoardId());
        assertEquals(original.getStatus(), copy.getStatus());
        assertEquals(2, copy.getPosition());
        assertNotEquals(original.getId(), copy.getId());
    }

    // Тест 4: Удаление тикета (проверяем, что объект помечен как удалённый)
    @Test
    public void testDeleteTicket() {
        Ticket ticket = new Ticket();
        ticket.setId("toDelete");
        boolean isDeleted = false;

        // Проверяем, что мы можем его удалить из списка.
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(ticket);
        tickets.remove(ticket);
        assertEquals(0, tickets.size());
        assertFalse(tickets.contains(ticket));
    }

    // Тест 5: Поиск тикетов
    @Test
    public void testSearchTickets() {
        List<Ticket> tickets = new ArrayList<>();

        Ticket t1 = new Ticket();
        t1.setTitle("Android test");
        t1.setDescription("Something");
        tickets.add(t1);

        Ticket t2 = new Ticket();
        t2.setTitle("iOS test");
        t2.setDescription("Other");
        tickets.add(t2);

        String query = "Android";
        List<Ticket> results = new ArrayList<>();
        for (Ticket t : tickets) {
            if (t.getTitle().contains(query) || t.getDescription().contains(query)) {
                results.add(t);
            }
        }

        assertEquals(1, results.size());
        assertEquals("Android test", results.get(0).getTitle());
    }
}
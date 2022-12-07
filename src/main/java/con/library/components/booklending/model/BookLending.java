package con.library.components.booklending.model;

import con.library.utils.InstantUtils;

import java.time.Instant;

public class BookLending {
    private Long id;
    private Long bookItemId;
    private Long userId;
    private LendingStatus status;
    private Instant createdAt;
    private Instant dueAt;
    private Instant returnAt;

    public BookLending() {
    }

    public BookLending(long id, long bookItemId, long userId, LendingStatus status, Instant createdAt, Instant dueAt, Instant returnAt) {
        this.id = id;
        this.bookItemId = bookItemId;
        this.userId = userId;
        this.status = status;
        this.createdAt = createdAt;
        this.dueAt = dueAt;
        this.returnAt = returnAt;
    }


    public static BookLending parse(String record) {
        String[] fields = record.split(",");
        long id = Long.parseLong(fields[0]);
        long bookItemId = Long.parseLong(fields[1]);
        long userId = Long.parseLong(fields[2]);
        LendingStatus status = LendingStatus.parseLendingStatus(fields[3]);
        Instant createdAt = InstantUtils.parseInstant(fields[4]);
        Instant dueAt = InstantUtils.parseInstant(fields[5]);
        Instant returnAt = InstantUtils.parseInstant(fields[6]);
        return new BookLending(id, bookItemId, userId, status, createdAt, dueAt, returnAt);
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookItemId() {
        return bookItemId;
    }

    public void setBookItemId(Long bookItemId) {
        this.bookItemId = bookItemId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public LendingStatus getStatus() {
        return status;
    }

    public void setStatus(LendingStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getDueAt() {
        return dueAt;
    }

    public void setDueAt(Instant dueAt) {
        this.dueAt = dueAt;
    }

    public Instant getReturnAt() {
        return returnAt;
    }

    public void setReturnAt(Instant returnAt) {
        this.returnAt = returnAt;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s",
                id,
                bookItemId,
                userId,
                status,
                createdAt,
                dueAt,
                returnAt);
    }
}

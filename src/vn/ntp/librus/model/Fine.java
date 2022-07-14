package vn.ntp.librus.model;

import vn.ntp.librus.utils.InstantUtils;

import java.time.Instant;

public class Fine {
    private Long id;
    private Long bookItemId;
    private Long userId;
    private Instant createdAt;
    private Double fineAmount;

    public Fine(long id, long bookItemId, long userId, Instant createdAt, Double fineAmount) {
        this.id = id;
        this.bookItemId = bookItemId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.fineAmount = fineAmount;
    }

    public static Fine parse(String record) {
        String[] fields = record.split(",");
        long id = Long.parseLong(fields[0]);
        long bookItemId = Long.parseLong(fields[1]);
        long userId = Long.parseLong(fields[2]);
        Instant createdAt = InstantUtils.parseInstant(fields[3]);
        Double fineAmount = Double.parseDouble(fields[4]);
        return new Fine(id, bookItemId, userId, createdAt, fineAmount);
    }

    public Long getId() {
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Double getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(Double fineAmount) {
        this.fineAmount = fineAmount;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s",
                id,
                bookItemId,
                userId,
                fineAmount,
                createdAt);
    }
}

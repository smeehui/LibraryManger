package com.library.components.fine.models;

import com.library.utils.InstantUtils;

import java.time.Instant;

public class Fine {
    private Long id;
    private Long bookItemId;
    private Long userId;
    private Instant createdAt;
    private Instant paidAt;
    private Double fineAmount;
    private FineStatus status;

    public void setPaidAt() {
        this.paidAt = Instant.now();
    }
    public void setPaidAt(Instant paidAt) {
        this.paidAt = paidAt;
    }

    public Instant getPaidAt() {
        return paidAt;
    }

    public Fine(long bookItemId, long userId, Double fineAmount) {
        this.id = System.currentTimeMillis();
        this.bookItemId = bookItemId;
        this.userId = userId;
        this.createdAt = Instant.now();
        this.fineAmount = fineAmount;
        this.status = FineStatus.UNPAID;
    }

    public Fine(long id, long bookItemId, long userId, Instant createdAt, Double fineAmount) {
        this.id = id;
        this.bookItemId = bookItemId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.fineAmount = fineAmount;
        this.status = FineStatus.UNPAID;
    }

    public FineStatus getStatus() {
        return status;
    }

    public void setStatus(FineStatus status) {
        this.status = status;
    }

    public Fine() {
        this.id = System.currentTimeMillis();
    }

    public static Fine parse(String record) {
        String[] fields = record.split(",");
        //1670919901,1657613272,1657613920,1150000.0,2022-12-13T08:25:01.169088Z,UNPAID
        long id = Long.parseLong(fields[0]);
        long bookItemId = Long.parseLong(fields[1]);
        long userId = Long.parseLong(fields[2]);
        Instant createdAt = InstantUtils.parseInstant(fields[4]);
        Double fineAmount = Double.parseDouble(fields[3]);
        FineStatus status = FineStatus.valueOf(fields[5]);
        Instant paidAt = InstantUtils.parseInstant(fields[6]);
        Fine newFine = new Fine(id, bookItemId, userId, createdAt, fineAmount);
        newFine.setStatus(status);
        newFine.setPaidAt(paidAt);
        return newFine;
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
        return String.format("%s,%s,%s,%s,%s,%s,%s",
                id,
                bookItemId,
                userId,
                fineAmount,
                createdAt,
                status,paidAt);
    }
}

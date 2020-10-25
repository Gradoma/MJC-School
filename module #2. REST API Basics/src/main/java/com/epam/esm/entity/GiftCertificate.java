package com.epam.esm.entity;

import java.time.ZonedDateTime;

public class GiftCertificate {
    private Tag name;
    private String description;
    private double price;
    private ZonedDateTime createTime;
    private ZonedDateTime LastUpdateDate;
    private int duration;

    public GiftCertificate(){}

    public Tag getName() {
        return name;
    }

    public void setName(Tag name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public ZonedDateTime getLastUpdateDate() {
        return LastUpdateDate;
    }

    public void setLastUpdateDate(ZonedDateTime lastUpdateDate) {
        LastUpdateDate = lastUpdateDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GiftCertificate that = (GiftCertificate) o;

        if (Double.compare(that.price, price) != 0) return false;
        if (duration != that.duration) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        return LastUpdateDate != null ? LastUpdateDate.equals(that.LastUpdateDate) : that.LastUpdateDate == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name != null ? name.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (LastUpdateDate != null ? LastUpdateDate.hashCode() : 0);
        result = 31 * result + duration;
        return result;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "name=" + name +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", createTime=" + createTime +
                ", LastUpdateDate=" + LastUpdateDate +
                ", duration=" + duration +
                '}';
    }
}

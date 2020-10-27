package com.epam.esm.entity;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class GiftCertificate {
    private long id;
    private String name;
    private List<Tag> tagList = new ArrayList<>();
    private String description;
    private Double price;
    private ZonedDateTime createDate;
    private ZonedDateTime LastUpdateDate;
    private Duration duration;

    public GiftCertificate(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Tag> getTagList() {
        List<Tag> listReturn = new ArrayList<>();
        for(Tag currentTag : tagList){
            listReturn.add(new Tag(currentTag));
        }
        return listReturn;
    }

    public void setTagList(List<Tag> list) {
        for(Tag currentTag : list){
            tagList.add(new Tag(currentTag));
        }
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

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public ZonedDateTime getLastUpdateDate() {
        return LastUpdateDate;
    }

    public void setLastUpdateDate(ZonedDateTime lastUpdateDate) {
        LastUpdateDate = lastUpdateDate;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GiftCertificate that = (GiftCertificate) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (tagList != null ? !tagList.equals(that.tagList) : that.tagList != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (LastUpdateDate != null ? !LastUpdateDate.equals(that.LastUpdateDate) : that.LastUpdateDate != null)
            return false;
        return duration != null ? duration.equals(that.duration) : that.duration == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (tagList != null ? tagList.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (LastUpdateDate != null ? LastUpdateDate.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "id=" + id +
                "name=" + name +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", createTime=" + createDate +
                ", LastUpdateDate=" + LastUpdateDate +
                ", duration=" + duration +
                '}';
    }
}

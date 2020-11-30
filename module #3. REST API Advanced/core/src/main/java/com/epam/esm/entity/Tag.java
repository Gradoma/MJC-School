package com.epam.esm.entity;

import com.epam.esm.dao.column.TagTableConst;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = TagTableConst.TABLE_TAG)
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "findByCertificateId",
                query = "SELECT tag.id, tag.Name FROM tag " +
                        "JOIN tag_certificate ON tag_certificate.tag_id = tag.id " +
                        "JOIN giftcertificate ON giftcertificate.id = tag_certificate.certificate_id " +
                        "WHERE giftcertificate.id = :id",
                resultClass = Tag.class
        )
})
public class Tag{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = TagTableConst.NAME, length = 20, unique = true)
    private String name;

    public Tag(){}
    public Tag(Tag originTag){
        this.id = originTag.getId();
        this.name = originTag.getName();
    }

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if (id != tag.id) return false;
        return name != null ? name.equals(tag.name) : tag.name == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "id=" + id +
                ", name=" + name +
                "}";
    }
}

package com.epam.esm.entity;

import com.epam.esm.dao.column.TagTableConst;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
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

    public Tag(Tag originTag){
        this.id = originTag.getId();
        this.name = originTag.getName();
    }
}

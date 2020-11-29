package com.epam.esm.entity;

import com.epam.esm.dao.column.GiftCertificateTableConst;
import com.epam.esm.dao.column.TagToCertificateTableConst;
import com.epam.esm.entity.util.CertificateTagsUtil;
import lombok.*;

import javax.persistence.*;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = GiftCertificateTableConst.TABLE_CERTIFICATE)
public class GiftCertificate {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Getter
    @Setter
    @Column(name = GiftCertificateTableConst.NAME, length = 40)
    private String name;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = TagToCertificateTableConst.TABLE_TAG_CERT,
            joinColumns = @JoinColumn(name = TagToCertificateTableConst.CERTIFICATE_ID),
    inverseJoinColumns = @JoinColumn(name = TagToCertificateTableConst.TAG_ID))
    private Set<Tag> tagSet = new HashSet<>();
    @Getter
    @Setter
    @Column(name = GiftCertificateTableConst.DESCRIPTION)
    private String description;
    @Getter
    @Setter
    @Column(name = GiftCertificateTableConst.PRICE)
    private Double price;
    @Getter
    @Setter
    @Column(name = GiftCertificateTableConst.CREATE_DATE)
    private ZonedDateTime createDate;
    @Getter
    @Setter
    @Column(name = GiftCertificateTableConst.LAST_UPDATE_DATE)
    private ZonedDateTime lastUpdateDate;
    @Getter
    @Setter
    @Column(name = GiftCertificateTableConst.DURATION)
    private Duration duration;

    public Set<Tag> getTagSet() {
        return CertificateTagsUtil.getTagList(tagSet);
    }

    public void setTagSet(Set<Tag> tagSet) {
        CertificateTagsUtil.setTagsToCertificate(this, tagSet);
    }

    public void addTag(Tag tag){
        tagSet.add(new Tag(tag));
    }
}

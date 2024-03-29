package com.epam.esm.entity;

import com.epam.esm.dao.audit.AuditListener;
import com.epam.esm.dao.column.GiftCertificateTableConst;
import com.epam.esm.dao.column.TagToCertificateTableConst;
import com.epam.esm.dao.util.DurationConverter;
import com.epam.esm.entity.util.CertificateTagsUtil;
import lombok.*;

import javax.persistence.*;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@EntityListeners(AuditListener.class)
@Entity
@Table(name = GiftCertificateTableConst.TABLE_CERTIFICATE)
public class GiftCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = GiftCertificateTableConst.NAME, length = 40)
    private String name;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = TagToCertificateTableConst.TABLE_TAG_CERT,
            joinColumns = @JoinColumn(name = TagToCertificateTableConst.CERTIFICATE_ID),
    inverseJoinColumns = @JoinColumn(name = TagToCertificateTableConst.TAG_ID))
    private Set<Tag> tagSet = new HashSet<>();

    @Column(name = GiftCertificateTableConst.DESCRIPTION)
    private String description;

    @Column(name = GiftCertificateTableConst.PRICE)
    private Double price;

    @Column(name = GiftCertificateTableConst.CREATE_DATE)
    private ZonedDateTime createDate;

    @Column(name = GiftCertificateTableConst.LAST_UPDATE_DATE)
    private ZonedDateTime lastUpdateDate;

    @Column(name = GiftCertificateTableConst.DURATION)
    @Convert(converter = DurationConverter.class)
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

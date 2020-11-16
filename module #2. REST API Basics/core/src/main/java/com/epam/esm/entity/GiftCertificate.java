package com.epam.esm.entity;

import com.epam.esm.entity.util.CertificateTagsUtil;
import lombok.*;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class GiftCertificate {
    @Getter
    @Setter
    private long id;
    @Getter
    @Setter
    private String name;
    private Set<Tag> tagSet = new HashSet<>();
    @Getter
    @Setter
    private String description;
    @Getter
    @Setter
    private Double price;
    @Getter
    @Setter
    private ZonedDateTime createDate;
    @Getter
    @Setter
    private ZonedDateTime lastUpdateDate;
    @Getter
    @Setter
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

package com.epam.esm.entity.util;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import java.util.HashSet;
import java.util.Set;

public class CertificateTagsUtil {
    public static Set<Tag> getTagList(Set<Tag> tagSet){
        Set<Tag> returnSet = new HashSet<>();
        tagSet.forEach(tag -> returnSet.add(new Tag(tag)));
        return returnSet;
    }

    public static void setTagsToCertificate(GiftCertificate certificate, Set<Tag> tagSet){
        tagSet.forEach(tag -> certificate.addTag(new Tag(tag)));
    }
}

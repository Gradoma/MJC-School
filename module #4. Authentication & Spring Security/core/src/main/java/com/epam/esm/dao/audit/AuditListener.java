package com.epam.esm.dao.audit;

import com.epam.esm.entity.GiftCertificate;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class AuditListener {

    @PrePersist
    private void beforeSave(Object object) {
        if(object instanceof GiftCertificate){
            GiftCertificate certificate = (GiftCertificate) object;
            certificate.setCreateDate(ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC));
            certificate.setLastUpdateDate(certificate.getCreateDate());
        }
    }

    @PreUpdate
    private void beforeUpdate(Object object) {
        if(object instanceof GiftCertificate){
            GiftCertificate certificate = (GiftCertificate) object;
            certificate.setLastUpdateDate(ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC));
        }
    }
}

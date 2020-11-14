package com.epam.esm.service.comparator;

import com.epam.esm.dto.GiftCertificateDto;

import java.util.Comparator;

public class NameComparator implements Comparator<GiftCertificateDto> {

    @Override
    public int compare(GiftCertificateDto o1, GiftCertificateDto o2) {
        return o1.getName().compareTo(o2.getName());
    }
}

package com.epam.esm;

import com.epam.esm.config.SpringConfig;
import com.epam.esm.dao.impl.TagDaoImpl;
import com.epam.esm.entity.Tag;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        TagDaoImpl tagDao = context.getBean(TagDaoImpl.class);
        for(Tag currentTag : tagDao.findAll()){
            System.out.println("tag : " + currentTag);
        }
    }
}

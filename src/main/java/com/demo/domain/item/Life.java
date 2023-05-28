package com.demo.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Life")
@Getter
@Setter
public class Life extends Item {
    private String style;
}
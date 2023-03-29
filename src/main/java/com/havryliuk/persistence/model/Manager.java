package com.havryliuk.persistence.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;

@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class Manager extends User {


}

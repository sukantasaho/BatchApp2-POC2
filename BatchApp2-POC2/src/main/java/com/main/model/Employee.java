package com.main.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Employee{

	private Integer empno;
	private String ename;
	private String eaddr;
	private Float salary;
	private Float grossSalary;
	private Float netSalary;
	
}

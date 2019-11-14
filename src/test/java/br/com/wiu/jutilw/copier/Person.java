package br.com.wiu.jutilw.copier;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Person {

	private long id; 
	private String name;
	private double weight;

	private People people;

	@NewInstanceOf
	public Person helperBuild() {
		return Person.builder().build();
	}
}

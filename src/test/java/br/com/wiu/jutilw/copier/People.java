package br.com.wiu.jutilw.copier;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class People {

	private int group;
	@ReferenceThisObject
	private List<Person> listPerson;
	private Double randomAmount; 
}

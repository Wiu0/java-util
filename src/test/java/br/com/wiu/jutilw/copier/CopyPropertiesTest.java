package br.com.wiu.jutilw.copier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Arrays;

import org.junit.Test;

public class CopyPropertiesTest {

	@Test
	public void copiedPersonToPersonWithFieldsId12NameWiuWeight01Sucess() {
		Person p0 = Person.
				builder().
				id(12). 
				name("wiu").
				weight(0.1).build();
		Person p1 = Person.builder().build();

		CopyProperties.copyAll(p0, p1);
		assertEquals(p0, p1);
	}

	@Test
	public void copiedPeopleToPeople() {
		Person p0 = Person.builder().
				id(12). 
				name("wiu").
				weight(0.1).build();
		Person p1 = Person.builder().
				id(32). 
				name("will").
				weight(546).build();

		People pp0 = People.
				builder().
				group(11).
				randomAmount(34.9).
				listPerson(Arrays.asList(new Person[] {p0, p1})).build();


		People pp1 = People.builder().build();

		System.out.println("Before:\nsource: " + pp0 + "\ntarget: " + pp1);
		CopyProperties.copyAll(pp0, pp1);
		System.out.println("After:\nsource: " + pp0 + "\ntarget: " + pp1);
		assertEquals(pp0, pp1.getListPerson().get(0).getPeople());
	}

	
	@Test
	public void copiedObjectNoSameObject() {
		Person p0 = Person.builder().
				id(12). 
				name("wiu").
				weight(0.1).build();
		
		String s = "Object";

		System.out.println("Before:\nsource: " + p0 + "\ntarget: " + s);
		CopyProperties.copyAll(p0, s);
		System.out.println("After:\nsource: " + p0 + "\ntarget: " + s);
		assertNotEquals(p0, s);
	}
}
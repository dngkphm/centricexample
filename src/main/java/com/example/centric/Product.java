package com.example.centric;

import javax.persistence.*;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name="Products")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable=false)
	private String id;
	private String name;
	private String description;
	private String brand;
	private ArrayList<String> tags;
	private String category;
	private String created_at;
	
	private static String counter="0"; //+1 per genID
	
	public Product(){}
	
	public Product(String id, String name, String desc, String brand, String category, String date, ArrayList<String> tags) {
		this.name = name;
		this.description = desc;
		this.brand = brand;
		this.tags = tags;
		this.category = category;
		
		//auto gen ID and Time
		if (date==null || date == "")
			date = makeDtf();
		this.created_at = date;
		if (id == null || id.equals(""))
			id = makeProdID(date);
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String pid) {
		this.id = pid;
	}
	
	//generates a new hash id w/global counter and seed
	public String makeProdID(String seed)
	{
		int tempCount = Integer.parseInt(counter);
		String genSeed = counter+seed;
		counter = Integer.toString(tempCount+1);
		return Integer.toString(genSeed.hashCode());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	
	public String makeDtf()
	{
		LocalDateTime moment = LocalDateTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
		return moment.format(dtf);
	}
	
	public ArrayList<String> getTags() {
		return tags;
	}

	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
	
	public String toString()
	{
		return "Product("+this.id+")"+this.name+":"+this.description+this.brand+this.category+"["+/*this.tags.toString()+*/"]"+this.created_at;
	}
}

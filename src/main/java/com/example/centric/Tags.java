package com.example.centric;

import javax.persistence.*;
import java.util.List;
/**
 * 
 *  Unused class bc it's design wise it's unnecessary for another controller just for tags
 *  when we can just have a list type in the table
@Entity
@Table(name="Tags")
public class Tags{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="tid")
	private int tid;
	@ManyToOne
	@JoinColumn(name="Product.tags")
	@Column(name="pid")
	private Product p;
	@Column(name="tag")
	private String value;
	public Tags() {}
	
	public Tags(int tid, Product pid, String tag) {
		this.tid= tid;
		this.p = pid;
		this.value = tag;
	}
	
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public Product getPid() {
		return this.p;
	}
	public void setPid(Product pid) {
		this.p = pid;
	}
	public String getTag() {
		return value;
	}
	public void setTag(String value) {
		this.value = value;
	}
	
	
}
**/

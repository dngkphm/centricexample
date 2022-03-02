package com.example.centric;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
//for debugging crash requests
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import net.minidev.json.JSONObject;

import java.sql.*;

@CrossOrigin(origins = "https://localhost:8080")
@RestController
@RequestMapping("/v1/products")
public class ProductController{
	@Autowired
	ProductRepo prodRepo;
	String sqlURL = "jdbc:h2:mem:Products";
	String adminName = "centric";
	String adminPass = "";
	
	boolean cacheTrue = true;
	String prevCat = "";
	ArrayList<Product> cachedData = new ArrayList<Product>();
	
	/***GET***/
	@GetMapping("")
	public ResponseEntity<ArrayList<Product> > getProducts(){
		/**TODO
		 * Add user search query
		 * **/
		try {
			ArrayList<Product> items = fetchProductfromDB("products",null,null,null,"");
			if(items.isEmpty()) 
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			return new ResponseEntity<>(items,HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@GetMapping(value="category/{type}")
	public ResponseEntity<ArrayList<Product> > getProductsbyCategory(
			@PathVariable(value="type") String category,
			@RequestParam(value="page",defaultValue = "0") Integer pageNo,
			@RequestParam(value="size",defaultValue = "5") Integer pageSize){
		/**TODO
		 * Get all items under category (findByID(...))
		 * get all items where category = "category"
		 * limit results by pageSize, splice by pageNo
		 * 
		 * EXTRA: cache data by pageSize
		 * **/
		//handle odd requests with defaults
		if (pageSize <= 0) pageSize = 5;
		if (pageNo < 0) pageNo = 0;
		//check cached request if similar
		if (this.cacheTrue && this.prevCat.equals(category)) {
			this.prevCat = category;
			return new ResponseEntity<>(spliceArray(this.cachedData,pageNo,pageSize), HttpStatus.OK);
		}
		//retrieve data
		ArrayList<Product> items = new ArrayList<>();
		try {
			ArrayList<String> cols = new ArrayList<>();
			ArrayList<String> match = new ArrayList<>();
			cols.add("category");
			match.add(category);
			String order = "ORDER BY "+"created_at"+" ASC";
			items = fetchProductfromDB("products",null,cols,match,order);
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//cache data if enabled
		if(this.cacheTrue)
			this.cachedData = items;
		
		if(items.size() <= 0)
			return new ResponseEntity<>(items, HttpStatus.NO_CONTENT);
		return new ResponseEntity<>(spliceArray(items,pageNo,pageSize),HttpStatus.OK);
	}
	
	/***PUT***/
	@PutMapping("")
	public ResponseEntity<Product> addProduct(@RequestBody JSONObject fItem){
		/**TO-DO:
		 * parse JSON string
		 * package Product
		 * save Product
		**/
		String total = "";
		String name = fItem.getAsString("name")==null ? "" : fItem.getAsString("name");
		String desc = fItem.getAsString("description")==null ? "" : fItem.getAsString("description");
		String brand = fItem.getAsString("brand")==null ? "" : fItem.getAsString("brand");
		String category = fItem.getAsString("category")==null ? "" : fItem.getAsString("category");
		String tagAsStr = fItem.getAsString("tags")==null ? "[]" : fItem.getAsString("tags");
		String[] tagsAsList = tagAsStr.substring(1,tagAsStr.length()-1).split(",");
		ArrayList<String> ptags = new ArrayList<>();
		for(int i = 0; i<tagsAsList.length; i++) {
			ptags.add(tagsAsList[i]);
		}
		Product item = new Product("",name,desc,brand,category,"",ptags);
		//save item manually bc >:(
		try {
			Connection conn = DriverManager.getConnection(this.sqlURL,this.adminName,this.adminPass);
			String query = "insert into Products(id,name,description,brand,category,created_at) Values('"+
					item.getId()+"','"+name+"','"+desc+"','"+brand+"','"+category+"','"+item.getCreated_at()+"')";
			PreparedStatement prep = conn.prepareStatement(query);
			prep.executeUpdate();
			for(int i = 0; i<ptags.size(); i++){
				query = "insert into Tags(pid,tag) Values('"+item.getId()+"','"+ptags.get(i)+"')";
				prep =  conn.prepareStatement(query);
				prep.executeUpdate();
			}
			conn.close();
		}catch(Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(item,HttpStatus.CREATED);
	}
	
	//connects to the database with the specified table name, column names, and matching attributes
	//modified for Product
	public ArrayList<Product> fetchProductfromDB(String table, ArrayList<String> attributes,
			ArrayList<String> columns, ArrayList<String> match,
			String order) throws Exception
	{
		ArrayList<Product> items = new ArrayList<>();
		//parse table columns for display
		String queryAttr = "*";
		if (attributes != null && attributes.size() > 0) {
			queryAttr = attributes.toString();
			queryAttr = queryAttr.substring(1,queryAttr.length()-2); //remove brackets
		}
		//parse conditionals
		String queryWhere = ""; //WHERE {column,*} = {match,*}
		if (columns != null && match != null && columns.size() > 0 && match.size() > 0) {
			int colSize = columns.size();
			if (colSize>0 && colSize == match.size()) {
				int index = 0;
				queryWhere = "WHERE ";
				while(index < colSize){
					queryWhere += columns.get(index)+"='"+match.get(index)+"'";
					queryWhere = (index + 1 < colSize) ? queryWhere + "," : queryWhere + "";
					index += 1;
				}
			}
			else throw new SQLException(); //mismatching columns = "match"
		}
		
		//Fetch Data
		Connection conn = DriverManager.getConnection(this.sqlURL,this.adminName,this.adminPass);
		String query = String.format("SELECT %s FROM %s %s %s",queryAttr,table,queryWhere,order);
		PreparedStatement prep = conn.prepareStatement(query);
		ResultSet rs = prep.executeQuery();
		//Package into Product	
		while(rs.next()) {
			String pid = rs.getString("id");
			String pname = rs.getString("name");
			String pbrand = rs.getString("brand");
			String pdesc = rs.getString("description");
			String ptime = rs.getString("created_at");
			String pcat = rs.getString("category");
			ArrayList<String> ptags = new ArrayList<String>();
			//Collect tags
			String queryTags = "SELECT TAG FROM tags WHERE PID = '"+pid+"'";
			PreparedStatement prepTags =  conn.prepareStatement(queryTags);
			ResultSet rsTags = prepTags.executeQuery();
			while(rsTags.next())
				ptags.add(rsTags.getString("TAG"));
			Product item = new Product(pid,pname,pdesc,pbrand,pcat,ptime,ptags);

			items.add(item);
		}
		conn.close();
		return items;
	}
	
	//splices up array
	public ArrayList<Product> spliceArray(ArrayList<Product> list, int pageNum, int numElem)
	{
		int itemSize = list.size();
		int start = numElem*pageNum;
		int end = start + numElem;
		//out of bounds: return empty list
		if (start >= itemSize)
			return new ArrayList<>();
		// within bounds but too few items: return list[start:list.size()]
		if (end > itemSize)
			end = itemSize;
		//within bounds and enough items: return list[start:end]
		return new ArrayList<Product>(list.subList(start, end));
	}
	
	/* JPA IMPLEMENTATION */
	/*
	 * We avoid this implementation bc:
	 * 1) JPARepository.findById({id}) fetches directly from products table without joining TAGS
	 * 2) Implementing Tags as a 2nd table causes a non-hex error when storing arrays 
	 *
	ArrayList<Product> items = new ArrayList<>();
	Optional<Product> shortlist = this.prodRepo.findById(category);
	if(!shortlist.isPresent())
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	//items.add(shortlist.get());
	/*
	Pageable page = PageRequest.of(pageNo, pageSize, Sort.by(Order.asc("created_at")));
	Page<Product> pageresults = this.prodRepo.findAll(page);
	if(pageresults.getTotalElements() <= 0)
		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NOT_FOUND);
	return new ResponseEntity<>((ArrayList<Product>)pageresults.getContent(),HttpStatus.OK);
	*/
}

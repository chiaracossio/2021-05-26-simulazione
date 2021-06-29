package it.polito.tdp.yelp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.yelp.model.Adiacenza;
import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Review;
import it.polito.tdp.yelp.model.User;

public class YelpDao {

	public void getAllBusiness(Map<String, Business> idMap){
		String sql = "SELECT * FROM Business";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				if(!idMap.containsKey(res.getString("business_id"))) {

					Business business = new Business(res.getString("business_id"), 
							res.getString("full_address"),
							res.getString("active"),
							res.getString("categories"),
							res.getString("city"),
							res.getInt("review_count"),
							res.getString("business_name"),
							res.getString("neighborhoods"),
							res.getDouble("latitude"),
							res.getDouble("longitude"),
							res.getString("state"),
							res.getDouble("stars"));
					idMap.put(res.getString("business_id"), business);
				}
			}
			res.close();
			st.close();
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Review> getAllReviews(){
		String sql = "SELECT * FROM Reviews";
		List<Review> result = new ArrayList<Review>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Review review = new Review(res.getString("review_id"), 
						res.getString("business_id"),
						res.getString("user_id"),
						res.getDouble("stars"),
						res.getDate("review_date").toLocalDate(),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("review_text"));
				result.add(review);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<User> getAllUsers(){
		String sql = "SELECT * FROM Users";
		List<User> result = new ArrayList<User>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				User user = new User(res.getString("user_id"),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("name"),
						res.getDouble("average_stars"),
						res.getInt("review_count"));
				
				result.add(user);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getAllCitta(){
		String sql = "SELECT DISTINCT city "
				+ "FROM business";
		
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				
				result.add(res.getString("city"));
				
			}
			
			res.close();
			st.close();
			conn.close();
			return result;
			
		} 
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Business> getAllVertici(Map<String, Business> idMap, String b, Integer anno){
		String sql = "SELECT DISTINCT business.business_id "
				+ "FROM business, reviews "
				+ "WHERE business.city = ? AND YEAR(review_date) = ? AND "
				+ "business.business_id = reviews.business_id";
		
		List<Business> result = new ArrayList<Business>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setString(1, b);
			st.setInt(2, anno);
			
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				if(idMap.containsKey(res.getString("business_id"))) {

					result.add(idMap.get(res.getString("business_id")));
					
				}
				
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Adiacenza> getAllArchi(Map<String, Business> idMap, String b, Integer anno){
		
		String sql = "SELECT b1.business_id, b2.business_id, AVG(r1.stars) peso1, AVG(r2.stars) AS peso2 "
				+ "FROM business b1, reviews r1, business b2, reviews r2 "
				+ "WHERE b1.city = ? AND YEAR(r1.review_date) = ? AND "
				+ "b1.business_id = r1.business_id AND "
				+ "b2.city = ? AND YEAR(r2.review_date) = ? AND "
				+ "b2.business_id = r2.business_id AND "
				+ "b1.business_id < b2.business_id "
				+ "GROUP BY b1.business_id, b2.business_id "
				+ "HAVING AVG(r1.stars) <> AVG(r2.stars)";
		
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setString(1, b);
			st.setInt(2, anno);
			st.setString(3, b);
			st.setInt(4, anno);
			
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				if(idMap.containsKey(res.getString("business_id"))) {
					
					if(res.getDouble("peso1") < res.getDouble("peso2")) {
						
						result.add(new Adiacenza(idMap.get(res.getString("b1.business_id")), idMap.get(res.getString("b2.business_id")), res.getDouble("peso2") - res.getDouble("peso1")));
					}
					if(res.getDouble("peso2") < res.getDouble("peso1")) {
						
						result.add(new Adiacenza(idMap.get(res.getString("b2.business_id")), idMap.get(res.getString("b1.business_id")), res.getDouble("peso1") - res.getDouble("peso2")));
					}
					
				}
				
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
}

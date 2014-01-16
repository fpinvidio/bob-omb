package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
@Table(name = "Product_Category")
public class Category extends Model{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id_product_type")
	public Long id;
	@Required
	public String name;
	
	public static void create(Category category) {
		category.save();
	}

	public static Finder<Long, Category> find = new Finder<Long, Category>(Long.class,
			Category.class);

}

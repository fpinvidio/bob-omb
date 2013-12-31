package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
@Table(name = "Product")
public class Product extends Model {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id_product")
	public Long id;
	@Required
	public String name;
	@Required
	public String description;
	@Required
	public String laboratory;
	@Required
	public Boolean enabled;
	public Long code;
	public Long image_number;
	public Long weight;
	@ManyToOne
	@JoinColumn(name="id_product_category")
	public Category category;
	
	public static Finder<Long, Product> find = new Finder<Long, Product>(Long.class,
			Product.class);

	public static void create(Product product) {
		product.save();
	}

}

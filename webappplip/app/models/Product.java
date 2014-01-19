package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
@Table(name = "product")
public class Product extends Model {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id_product")
	public Long id;
	@Required
	public String name;
	public int weight;
	public String description;
	@Required
	public int code;
	@Required
	public String laboratory;
	@ManyToOne
	@JoinColumn(name="id_product_category")
	public Category category;
	public int image_number;
	@Required
	public short enabled;
	/*
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy="product")
	public List<PageProduct> page_products;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="id_product", referencedColumnName="id_product")
	public List<PageProduct> page_products;*/
	
	public static Finder<Long, Product> find = new Finder<Long, Product>(Long.class,
			Product.class);

	public static void create(Product product) {
		product.save();
	}

}

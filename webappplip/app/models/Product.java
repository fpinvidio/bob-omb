package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.avaje.ebean.Page;

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
	public Integer weight;
	public String description;
	@Required
	public int code;
	@Required
	public String laboratory;
	@ManyToOne
	@JoinColumn(name="id_product_category")
	public Category category;
	public Integer image_number;
	public boolean enabled;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="id_product", referencedColumnName="id_product")
	public List<Image> images;
	
	public Product() {
		super();
		this.enabled = false;
	}
	
	public Product(Long id, String name, int weight, String description,
			int code, String laboratory, Category category, int image_number,
			boolean enabled, List<Image> images) {
		super();
		this.id = id;
		this.name = name;
		this.weight = weight;
		this.description = description;
		this.code = code;
		this.laboratory = laboratory;
		this.category = category;
		this.image_number = image_number;
		this.enabled = enabled;
		this.images = images;
	}

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
	
	public static Page<Product> page(int page, int pageSize, String sortBy, String order, String filter) {
		return 
            find.where()
                .ilike("name", "%" + filter + "%")
                .orderBy(sortBy + " " + order)
                .findPagingList(pageSize)
                .setFetchAhead(false)
                .getPage(page);
    }

}

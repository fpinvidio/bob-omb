package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.libs.Json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;

@Entity
@Table(name = "page")
public class Page extends Model {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id_page")
	public Long id;
	@ManyToOne
	@JoinColumn(name = "id_order", referencedColumnName = "id_order")
	public Order order;
	public Long product_quantity;
	@OneToOne
	@JoinColumn(name = "id_page_image")
	public PageImage page_image;
	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "id_page")
	public Tray tray;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_page", referencedColumnName = "id_page")
	public List<PageProduct> page_products;
	@Required
	public Long page_number;

	public static Finder<Long, Page> find = new Finder<Long, Page>(Long.class,
			Page.class);

	public JsonNode toJson() {
		JsonNode jnode = Json.toJson(this);
		return jnode;
	}
}

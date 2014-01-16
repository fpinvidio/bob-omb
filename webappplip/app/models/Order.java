package models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "`order`")
public class Order extends Model {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id_order")
	public Long id;
	@Required
	public String code;
	@Required
	public Date insert_date;
	@Required
	public String client;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy="order")
	public List<Page> pages;
	
	public static Finder<Long, Order> find = new Finder<Long, Order>(Long.class,
			Order.class);
}

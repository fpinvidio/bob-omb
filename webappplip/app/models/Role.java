package models;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
@Table(name = "Plip_Role")
public class Role extends Model {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id_plip_role")
	public Long id;
	@Required
	@Column(name = "name")
	public String name;
	@Column(name = "description")
	public String description;
	@OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    public List<User> users;

	public static void create(Role role) {
		role.save();
	}

	public static Finder<Long, Role> find = new Finder<Long, Role>(Long.class,
			Role.class);
	
	public static Map<String,String> options() {
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(Role c: Role.find.orderBy("name").findList()) {
            options.put(c.id.toString(), c.name);
        }
        return options;
    }

}

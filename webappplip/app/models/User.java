package models;

import helpers.HashHelper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import exceptions.EmptyPasswordException;

@Entity
@Table(name = "Plip_User")
public class User extends Model {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id_plip_user")
	public int id;
	@Required
	@Column(name = "name")
	public String name;
	@Required
	@Column(name = "last_name")
	public String last_name;
	@Required
	@Column(name = "username")
	public String username;
	@Required
	@Column(name = "password")
	public String password;
	@Required
	@ManyToOne
	@JoinColumn(name="id_plip_role")
	public Role role;

	public static void create(User user) {
		try {
			user.password = HashHelper.createPassword(user.password);
			user.save();
		} catch (EmptyPasswordException e) {
			e.printStackTrace();
		}
	}
	
	public static Finder<Long, User> find = new Finder<Long, User>(Long.class,
			User.class);
	
	public static User authenticate(String username) {
		return User.find.where().eq("username", username).findUnique();
	}

}

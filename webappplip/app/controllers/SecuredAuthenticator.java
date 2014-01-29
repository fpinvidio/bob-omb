package controllers;

import models.User;
import play.mvc.Http.Context;
import play.mvc.Http.Request;
import play.mvc.Result;
import play.mvc.Security;

public class SecuredAuthenticator extends Security.Authenticator {
	@Override
	public String getUsername(Context ctx) {
		return ctx.session().get("username");
	}

	@Override
	public Result onUnauthorized(Context ctx) {
		return redirect(routes.ApplicationController.login());
	}

	public static boolean isAdmin(Request request) {
		User user = User.find.where().eq("username", request.username())
				.findUnique();
		return (user != null && user.isAdmin());
	}
}

package com.m2i.poec.twittergreen.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import com.m2i.poec.twittergreen.POJO.DisplayedTweet;
import com.m2i.poec.twittergreen.entity.Retweet;
import com.m2i.poec.twittergreen.entity.Tweet;
import com.m2i.poec.twittergreen.entity.User;
import com.m2i.poec.twittergreen.service.TweeterService;

@Named
@ViewScoped
public class ProfilePageBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private String userName;

	private static final Logger LOGGER = Logger.getLogger(ProfilePageBean.class.getName());

	@Inject
    private SessionBean sessionBean;

	@Inject
	private TweeterService tweeterService;

	private String content ="";

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<DisplayedTweet> getDisplayedTweets() {

		try{
			User author = tweeterService.getUser(userName);
			List<Tweet> tweets = author.getTweets();
			List<Retweet> retweets = author.getRetweets();
			
			List<DisplayedTweet> displayedTweets = DisplayedTweet.createList(tweets, retweets);
			
			return displayedTweets;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.setResponseStatus(HttpServletResponse.SC_NOT_FOUND);

            try {
				externalContext.dispatch("404.xhtml");
			} catch (IOException e1) {
				e1.printStackTrace();
			}

            facesContext.responseComplete();
			return null;
		}
	}

	public String createTweet() {
		try {
			LOGGER.info(content);
			tweeterService.createTweet(sessionBean.getUser(), content);
			return "Profil?username=" + sessionBean.getUser().getUsername() + "&faces-redirect=true";
		} catch(EJBException ex) {

			// TODO décortiquer l'exception, voir ou est l'erreur, préparer un message d'erreur adéquat
			LOGGER.log(Level.INFO, "oups...", ex);
			return null;
		}
	}
	
	public void createReTweet(User user, DisplayedTweet displayedtweet) {

		try {
			Tweet tweet = tweeterService.findATweet(displayedtweet.getId());
			tweeterService.reTweet(sessionBean.getUser(), tweet);

		} catch (EJBException ex) {

			// TODO dÃ©cortiquer l'exception, voir ou est l'erreur, prÃ©parer un
			// message d'erreur adÃ©quat
			LOGGER.log(Level.INFO, "oups...", ex);

		}
	}

	public String disconnect() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "Login.xhtml?faces-redirect=true";
	}
	
	public boolean isRetweetable(User user, DisplayedTweet displayedtweet){
		if (user == null) {
			return false;
		}
		Tweet tweet = tweeterService.findATweet(displayedtweet.getId());
		return tweeterService.retweetable(user, tweet);
	}

}

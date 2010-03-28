package com.elolozone.trafficwave.manager;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.elolozone.trafficwave.BaseTest;
import com.elolozone.trafficwave.manager.api.UserManager;
import com.elolozone.trafficwave.model.User;

public class UserManagerTest extends BaseTest {

	@Autowired
    protected UserManager userManager = null;
	
	@Test
	public void testConnect() {
		String idUser = "toto";
		
		User firstUser = userManager.connect(idUser);
		
		Assert.assertNotNull(firstUser);
		Assert.assertEquals(firstUser.getId(), idUser);
		Assert.assertTrue(firstUser.getLastIdSession() == 1);
		
		User nextConnectionUser = userManager.connect(idUser);
		
		Assert.assertNotNull(nextConnectionUser);
		Assert.assertEquals(nextConnectionUser.getId(), idUser);
		Assert.assertTrue(nextConnectionUser.getLastIdSession() == 2);
		
		Assert.assertTrue(firstUser.getLastConnectionTime().before(nextConnectionUser.getLastConnectionTime()));
	}
}

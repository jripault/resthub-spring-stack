package org.resthub.oauth2.provider.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.test.AbstractResourceDaoTest;
import org.resthub.oauth2.provider.model.Token;

/**
 * Test class for token DAO.
 */
public class TokenDaoImplTest extends AbstractResourceDaoTest<Token, TokenDao> {

	// -----------------------------------------------------------------------------------------------------------------
	// Attributes

	/**
	 * Sets the tested dao implementation
	 * 
	 * @param resourceService: the tested dao.
	 */
	@Inject
	@Named("tokenDao")
	@Override
	public void setResourceDao(TokenDao resourceDao) {
		this.resourceDao = resourceDao;
	}

	// -----------------------------------------------------------------------------------------------------------------
	// Tests

	/**
	 * Tests the creation, update and removal of a token.
	 */
	@Override
	public void testUpdate() throws Exception {
		Token token = new Token();
		token.accessToken = "XXXXXX";
		token.userId = "123456";
		
		// saves a news user in DB
		token = resourceDao.save(token);

		assertNotNull("token has not been created", token);

		assertNotNull("database id has not been generated", token.getId());

		// retrieves a user by his id
		Token retrieved = resourceDao.readByPrimaryKey(token.getId());

		assertEquals("token created has changed : finder not valid", token, retrieved);
		assertEquals("token's value was not persisted", token.accessToken, retrieved.accessToken);
		assertEquals("token's creation date was not persisted", token.createdOn, retrieved.createdOn);
		assertEquals("token's permissions was not persisted", token.permissions, retrieved.permissions);
		assertEquals("token's user id was not persisted", token.userId, retrieved.userId);

		String newValue = "YYYYYY";
		String newUserId = "654321";
		retrieved.accessToken = newValue;
		retrieved.createdOn.setTime(retrieved.createdOn.getTime()+5000);
		retrieved.userId = newUserId;

		// updates the user and checks new values
		Token updated = resourceDao.save(retrieved);

		assertEquals("token's id has changed", token.getId(), updated.getId());
		assertEquals("token's value should have changed", newValue, updated.accessToken);
		assertEquals("token's user id name should have changed", newUserId, updated.userId);

		// deletes the user
		resourceDao.delete(updated);

		assertNull("token not deleted", resourceDao.readByPrimaryKey(updated.getId()));
	} // testUpdate().
	
	/**
	 * Test token retrieval.
	 */
    @Test
	public void testFindEquals() {
		Token token = new Token();
		token.accessToken = "XXXXXX";
		token.userId = "123456";
		token = resourceDao.save(token);		
		List<Token> results = resourceDao.findEquals("userId", token.userId);

		assertNotNull("Result must not be null", results);
		assertEquals("Only one token must be returned", 1 , results.size());
		assertTrue("Returned token not equals to expected one", results.contains(token));
	} // testFindEquals().

} // class TokenDaoImplTest().

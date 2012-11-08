package de.escalon.rest.bt.dragon;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ThiefAccessTest {

	@Mock
	DragonAccess dragonAccess;

	ThiefAccess thiefAccess;

	@Before
	public void setUp() throws Exception {
		thiefAccess = new ThiefAccess();
		thiefAccess.dragonAccess = dragonAccess;
	}

	@Test
	public void testGetThiefLocation() throws Exception {

		when(dragonAccess.getHomeLocations()).thenReturn(Arrays.asList("Isengard", "Rohan"));
		assertEquals("Isengard", thiefAccess.getThiefLocation());
		assertEquals("Rohan", thiefAccess.getThiefLocation());
		assertEquals("Thief's Home", thiefAccess.getThiefLocation());
		assertEquals("Isengard", thiefAccess.getThiefLocation());
		assertEquals("Rohan", thiefAccess.getThiefLocation());
		assertEquals("Thief's Home", thiefAccess.getThiefLocation());
		assertEquals("Isengard", thiefAccess.getThiefLocation());
		assertEquals("Rohan", thiefAccess.getThiefLocation());
		assertEquals("Thief's Home", thiefAccess.getThiefLocation());

	}

}

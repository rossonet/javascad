package eu.printingin3d.javascad.models;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import eu.printingin3d.javascad.context.ColorHandlingContext;
import eu.printingin3d.javascad.context.ScadGenerationContextFactory;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundaries3dTest;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.testutils.AssertEx;
import eu.printingin3d.javascad.testutils.RandomUtils;
import eu.printingin3d.javascad.testutils.Test3dModel;
import eu.printingin3d.javascad.testutils.TestExtendable3dModel;

public class Extendable3dModelTest {
	private static final double MAX_BOUND = 15.0;
	private static final double MIN_BOUND = -10.0;

	private Abstract3dModel testSubject;
	private Abstract3dModel baseModel;

	@Before
	public void init() {
		baseModel = new Test3dModel("(empty)",
				new Boundaries3d(
						new Boundary(MIN_BOUND, MAX_BOUND), 
						new Boundary(MIN_BOUND, MAX_BOUND),
						new Boundary(MIN_BOUND, MAX_BOUND)))
			.move(RandomUtils.getRandomCoords())
			.rotate(RandomUtils.getRandomAngle());
		testSubject = new TestExtendable3dModel(baseModel);
	}
	
	@Test
	public void toScadShouldReturnWithTheSameAsTheBaseModel() {
		AssertEx.assertEqualsWithoutWhiteSpaces(baseModel.toScad(ColorHandlingContext.DEFAULT).getScad(), 
				testSubject.toScad(ColorHandlingContext.DEFAULT).getScad());
	}

	@Test
	public void operationOnTheExtendableModelShouldNotAffectTheBaseModel() {
		testSubject.move(RandomUtils.getRandomCoords());
		
		Assert.assertNotEquals(baseModel.toScad(ColorHandlingContext.DEFAULT), 
				testSubject.toScad(ColorHandlingContext.DEFAULT));
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void cloningShouldThrowUnsupportedOperationException() {
		testSubject.cloneModel();
	}
	
	@Test
	public void boundariesShouldBeTheSameAsBaseModel() {
		Boundaries3dTest.assertBoundariesEquals(baseModel.getBoundaries(), testSubject.getBoundaries());
	}

	private static class TestSubModel extends Extendable3dModel {
		public TestSubModel() {
			this.baseModel = new Cube(100);
		}
	}
	
	@Test(expected=IllegalValueException.class)
	public void subModelShouldThrowExceptonIsThereIsNoDefaultConstructor() {
		testSubject.subModel(ScadGenerationContextFactory.DEFAULT);
	}
	
	@Test
	public void subModelShouldWorkIfThereIsDefaultConstructor() {
		Abstract3dModel test = new TestSubModel();
		Abstract3dModel subModel = test.subModel(ScadGenerationContextFactory.DEFAULT);
		
		Assert.assertEquals(test.toScad(ColorHandlingContext.DEFAULT), subModel.toScad(ColorHandlingContext.DEFAULT));
		Assert.assertNotSame(test, subModel);
	}
	
	@Test
	public void subModelShouldCopyMoves() {
		Abstract3dModel test = new TestSubModel().move(new Coords3d(10, 20, 30));
		Abstract3dModel subModel = test.subModel(ScadGenerationContextFactory.DEFAULT);
		
		Assert.assertEquals(test.toScad(ColorHandlingContext.DEFAULT), subModel.toScad(ColorHandlingContext.DEFAULT));
		Assert.assertNotSame(test, subModel);
	}
}
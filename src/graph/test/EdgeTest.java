package graph.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import graph.*;

public final class EdgeTest {
	Node<String,String> one,two,three;
	Edge<String,String> one_one,one_one_2,one_two,one_three,two_three;
	
	@Before
	public void SetUp() { // tests node constructor and initializes test nodes and edges
		one_one = new Edge<String,String>(new Node<String,String>("1"),new Node<String,String>("1"),"1");
		one_one_2 = new Edge<String,String>(new Node<String,String>("1"),new Node<String,String>("1"),"2");
		one_two = new Edge<String,String>(new Node<String,String>("1"),new Node<String,String>("2"),"2");
		one_three = new Edge<String,String>(new Node<String,String>("1"),new Node<String,String>("3"),"5");
		two_three = new Edge<String,String>(new Node<String,String>("2"),new Node<String,String>("3"),"6");

		one = new Node<String,String>("1");
		two = new Node<String,String>("2");
		three = new Node<String,String>("3");
	}
	
	@Test
	public void testReturnMethods() {
		assertEquals(one_one.getParent(),one);
		assertEquals(one_one.getChild(),one);
		assertEquals(one_one.getLabel(),"1");
		assertEquals(one_one_2.getParent(),one);
		assertEquals(one_one_2.getChild(),one);
		assertEquals(one_one_2.getLabel(),"2");
		assertEquals(one_two.getParent(),one);
		assertEquals(one_two.getChild(),two);
		assertEquals(one_two.getLabel(),"2");
		assertEquals(one_three.getParent(),one);
		assertEquals(one_three.getChild(),three);
		assertEquals(one_three.getLabel(),"5");
	}
	
	@Test
	public void testEquals() {
		assertEquals(one_one.equals(one_one),true);
		assertEquals(one_one.equals(new Edge<String,String>(new Node<String,String>("1"),new Node<String,String>("1"),"1")),true);
		assertEquals(one_one.equals(one_one_2),false);
		assertEquals(one_one.equals(one_two),false);
		assertEquals(one_one.equals(one_three),false);
		assertEquals(one_one.equals(two_three),false);
		assertEquals(one_one.equals(one),false);
	}
	
	@Test(expected = RuntimeException.class)
	public void testNullParent() {
		Edge<String,String> e = new Edge<String,String>(null,two,"3");
		assertEquals(e.getLabel(),"3");
	}
	
	@Test(expected = RuntimeException.class)
	public void testNullChild() {
		Edge<String,String> e = new Edge<String,String>(one,null,"3");
		assertEquals(e.getLabel(),"3");
	}
	
	@Test(expected = RuntimeException.class)
	public void testNullLabel() {
		Edge<String,String> e = new Edge<String,String>(new Node<String,String>("1"),new Node<String,String>("2"),null);
		assertEquals(e.getParent(),one);
	}
}
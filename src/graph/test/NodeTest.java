package graph.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;

import graph.*;

public final class NodeTest {
	Node<String,String> one,two,three;
	Edge<String,String> one_one,one_one_2,one_two,one_three;
	
	@Before
	public void SetUp() { // tests node constructor and initializes test nodes and edges
		one = new Node<String,String>("1");
		two = new Node<String,String>("2");
		three = new Node<String,String>("3");
		
		one_one = new Edge<String,String>(new Node<String,String>("1"),new Node<String,String>("1"),"1");
		one_one_2 = new Edge<String,String>(new Node<String,String>("1"),new Node<String,String>("1"),"2");
		one_two = new Edge<String,String>(new Node<String,String>("1"),new Node<String,String>("2"),"2");
		one_three = new Edge<String,String>(new Node<String,String>("1"),new Node<String,String>("3"),"5");
	}
	
	@Test
	public void testAddingEdgesToNode() {
		assertEquals(one.addEdge(one_one),true);
		assertEquals(one.addEdge(one_two),true);
		assertEquals(one.addEdge(one_three),true);
		assertEquals(one.getEdges(),Arrays.asList(one_one,one_two,one_three));
	}
	
	@Test
	public void testRemovingEdgesFromNode() {
		Node<String,String> four = new Node<String,String>("4",new ArrayList<Edge<String,String>>(Arrays.asList(one_one,one_one_2,one_two,one_three)));
		assertEquals(four.getEdges(),Arrays.asList(one_one,one_one_2,one_two,one_three));
		assertEquals(four.removeEdge(one_three),true);
		assertEquals(four.removeEdge(one_three),false);
		assertEquals(four.getEdges(),Arrays.asList(one_one,one_one_2,one_two));
		assertEquals(four.removeEdge(one_two),true);
		assertEquals(four.removeEdge(one_two),false);
		assertEquals(four.getEdges(),Arrays.asList(one_one,one_one_2));
		assertEquals(four.removeEdge(one_one_2),true);
		assertEquals(four.removeEdge(one_one_2),false);
		assertEquals(four.getEdges(),Arrays.asList(one_one));
		assertEquals(four.removeEdge(one_one),true);
		assertEquals(four.removeEdge(one_one),false);
		assertEquals(four.getEdges(),new ArrayList<Edge<String,String>>(0));
		assertEquals(four.removeEdge(one_one),false);
		assertEquals(four.removeEdge(one_one_2),false);
		assertEquals(four.removeEdge(one_two),false);
		assertEquals(four.removeEdge(one_three),false);
	}
	
	@Test
	public void testEdgeswithSameNodesButDifferentLabel() {
		assertEquals(two.getEdges(),new ArrayList<Edge<String,String>>(0));
		assertEquals(two.addEdge(one_one),true);
		assertEquals(two.addEdge(one_one),false);
		assertEquals(two.getEdges(),Arrays.asList(one_one));
		assertEquals(two.addEdge(one_one_2),true);
		assertEquals(two.addEdge(one_one_2),false);
		assertEquals(two.getEdges(),Arrays.asList(one_one,one_one_2));
		assertEquals(two.removeEdge(one_one_2),true);
		assertEquals(two.removeEdge(one_one_2),false);
		assertEquals(two.getEdges(),Arrays.asList(one_one));
		assertEquals(two.removeEdge(one_one),true);
		assertEquals(two.removeEdge(one_one),false);
		assertEquals(two.getEdges(),new ArrayList<Edge<String,String>>(0));
	}
	
	@Test
	public void testEquals() {
		assertEquals(one.equals(one),true);
		assertEquals(one.equals(new Node<String,String>("1")),true);
		assertEquals(one.equals(new Node<String,String>("1",new ArrayList<Edge<String,String>>(Arrays.asList(one_one)))),true);
		assertEquals(one.equals(two),false);
		assertEquals(one.equals(three),false);
		assertEquals(one.equals(one_one),false);
	}
	
	@Test
	public void testContainsEdge() {
		Node<String,String> four = new Node<String,String>("4",new ArrayList<Edge<String,String>>(Arrays.asList(one_one,one_one_2)));
		assertEquals(four.containsEdge(one_one),true);
		assertEquals(four.containsEdge(one_one_2),true);
		assertEquals(four.containsEdge(one_two),false);
		assertEquals(four.containsEdge(one_three),false);
	}
	
	@Test(expected = RuntimeException.class)
	public void testNullLabel() {
		Node<String,String> test = new Node<String,String>(null);
		assertEquals(test.getLabel(),null);
	}
}
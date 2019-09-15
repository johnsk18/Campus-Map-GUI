package graph.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import graph.*;

public final class GraphTest {
	Graph<String,String> zero, one, two, three;
	Edge<String,String> one_one,one_two,one_three,two_three;
	
	@Before
	public void SetUp() { // tests graph node constructor and initializes test graphs & edges
		zero = new Graph<String,String>();
		
		HashMap<String,Node<String,String>> nodes = new HashMap<String,Node<String,String>>(1);
		nodes.put("1", new Node<String,String>("1"));
		one = new Graph<String,String>(nodes);
		
		nodes.put("2", new Node<String,String>("2"));
		two = new Graph<String,String>(nodes);
		
		nodes.put("3", new Node<String,String>("3"));
		three = new Graph<String,String>(nodes);
		
		one_one = new Edge<String,String>(new Node<String,String>("1"),new Node<String,String>("1"),"1");
		one_two = new Edge<String,String>(new Node<String,String>("1"),new Node<String,String>("2"),"2");
		one_three = new Edge<String,String>(new Node<String,String>("1"),new Node<String,String>("3"),"5");
		two_three = new Edge<String,String>(new Node<String,String>("2"),new Node<String,String>("3"),"6");
	}
	
	@Test
	public void testEmptyGraph() {
		assertEquals(zero.getNode("1"),null);
		assertEquals(zero.removeNode(new Node<String,String>("1")),false);
		assertEquals(zero.addEdge(new Edge<String,String>(new Node<String,String>("1"),new Node<String,String>("2"),"1")),false);
		assertEquals(zero.removeEdge(new Edge<String,String>(new Node<String,String>("1"),new Node<String,String>("2"),"1")),false);
		assertEquals(zero.getEdges(),new ArrayList<Edge<String,String>>(0));
		assertEquals(zero.getNodes(),new ArrayList<Node<String,String>>(0));
	}
	
	@Test
	public void testGraphEdgeConstructor() {
		Graph<String,String> test1 = new Graph<String,String>(new ArrayList<Edge<String,String>>(Arrays.asList(one_one,one_two,one_three)));
		assertEquals(test1.getEdges(),Arrays.asList(one_one,one_two,one_three));
		assertEquals(test1.getNodes(),Arrays.asList(new Node<String,String>("1"),new Node<String,String>("2"),new Node<String,String>("3")));
		
		Graph<String,String> test2 = new Graph<String,String>(new ArrayList<Edge<String,String>>(Arrays.asList(one_one,one_three)));
		assertEquals(test2.getEdges(),Arrays.asList(one_one,one_three));
		assertEquals(test2.getNodes(),Arrays.asList(new Node<String,String>("1"),new Node<String,String>("3")));
		
		Graph<String,String> test3 = new Graph<String,String>(new ArrayList<Edge<String,String>>(Arrays.asList(one_one)));
		assertEquals(test3.getEdges(),Arrays.asList(one_one));
		assertEquals(test3.getNodes(),Arrays.asList(new Node<String,String>("1")));
		
		Graph<String,String> test4 = new Graph<String,String>(new ArrayList<Edge<String,String>>(0));
		assertEquals(test4.getEdges(),new ArrayList<Edge<String,String>>(0));
		assertEquals(test4.getNodes(),new ArrayList<Node<String,String>>(0));
	}
	
	@Test
	public void testGraphNodeConstructor() {
		HashMap<String,Node<String,String>> nodes = new HashMap<String,Node<String,String>>(0);
		nodes.put("1", new Node<String,String>("1", new ArrayList<Edge<String,String>>(Arrays.asList(one_one))));
		nodes.put("2", new Node<String,String>("2", new ArrayList<Edge<String,String>>(Arrays.asList(two_three))));
		nodes.put("3", new Node<String,String>("3"));
		Graph<String,String> test1 = new Graph<String,String>(nodes);
		assertEquals(test1.getNode("1").getEdges(),Arrays.asList(one_one));
		assertEquals(test1.getNode("2").getEdges(),Arrays.asList(two_three));
		assertEquals(test1.getNode("3").getEdges(),new ArrayList<Edge<String,String>>(0));
		assertEquals(test1.getNode("4"),null);
		
	}
	
	@Test
	public void testGettingNodes() {
		assertEquals(two.getNode("1"),new Node<String,String>("1"));
		assertEquals(two.getNode("1").getLabel(),"1");
		assertEquals(two.getNode("2"),new Node<String,String>("2"));
		assertEquals(two.getNode("2").getLabel(),"2");
		assertEquals(two.getNode("3"),null);
	}
	
	@Test
	public void testAddingNodes() {
		assertEquals(one.getNode("1"),new Node<String,String>("1"));
		assertEquals(one.addNode(new Node<String,String>("1")),false);
		assertEquals(one.addNode(new Node<String,String>("2")),true);
		assertEquals(one.addNode(new Node<String,String>("2")),false);
		assertEquals(one.getNode("2"),new Node<String,String>("2"));
		assertEquals(one.getNodes(),Arrays.asList(new Node<String,String>("1"),new Node<String,String>("2")));
	}
	
	@Test
	public void testRemovingNodes() {
		assertEquals(three.getNodes(),Arrays.asList(new Node<String,String>("1"),new Node<String,String>("2"),new Node<String,String>("3")));
		assertEquals(three.removeNode(new Node<String,String>("3")),true);
		assertEquals(three.removeNode(new Node<String,String>("3")),false);
		assertEquals(three.removeNode(new Node<String,String>("2")),true);
		assertEquals(three.removeNode(new Node<String,String>("2")),false);
		assertEquals(three.removeNode(new Node<String,String>("1")),true);
		assertEquals(three.removeNode(new Node<String,String>("1")),false);
		assertEquals(three.getNodes(),new ArrayList<Node<String,String>>(0));
	}
	
	@Test
	public void testAddingEdges() {
		assertEquals(zero.addEdge(one_one),false);
		assertEquals(zero.addEdge(one_two),false);
		assertEquals(zero.addEdge(one_three),false);
		assertEquals(one.addEdge(one_one),true);
		assertEquals(one.addEdge(one_two),false);
		assertEquals(one.addEdge(one_three),false);
		assertEquals(two.addEdge(one_one),true);
		assertEquals(two.addEdge(one_two),true);
		assertEquals(two.addEdge(one_three),false);
		assertEquals(three.addEdge(one_one),true);
		assertEquals(three.addEdge(one_two),true);
		assertEquals(three.addEdge(one_three),true);
		assertEquals(one.getEdges(),Arrays.asList(one_one));
		assertEquals(two.getEdges(),Arrays.asList(one_one,one_two));
		assertEquals(three.getEdges(),Arrays.asList(one_one,one_two,one_three));
	}
	
	@Test
	public void testRemovingEdges() {
		Graph<String,String> test1 = new Graph<String,String>(new ArrayList<Edge<String,String>>(Arrays.asList(one_one,one_two,one_three)));
		assertEquals(test1.getEdges(),Arrays.asList(one_one,one_two,one_three));
		assertEquals(test1.removeEdge(one_one),true);
		assertEquals(test1.removeEdge(one_one),false);
		assertEquals(test1.getEdges(),Arrays.asList(one_two,one_three));
		assertEquals(test1.removeEdge(one_two),true);
		assertEquals(test1.removeEdge(one_two),false);
		assertEquals(test1.getEdges(),Arrays.asList(one_three));
		assertEquals(test1.removeEdge(one_three),true);
		assertEquals(test1.removeEdge(one_three),false);
		assertEquals(test1.getEdges(),new ArrayList<Edge<String,String>>(0));
	}
	
	@Test(expected = RuntimeException.class)
	public void testIdenticalEdges() {
		Graph<String,String> test = new Graph<String,String>(new ArrayList<Edge<String,String>>(Arrays.asList(one_one,one_one,one_one)));
		test.addEdge(one_one);
	}
	
	@Test
	public void testNullEdges() {
		Graph<String,String> test = new Graph<String,String>((ArrayList<Edge<String,String>>) null);
		assertEquals(test.getEdges(),new ArrayList<Edge<String,String>>(0));
	}
	
	@Test
	public void testNullNodes() {
		Graph<String,String> test = new Graph<String,String>((HashMap<String,Node<String,String>>) null);
		assertEquals(test.getEdges(),new ArrayList<Edge<String,String>>(0));
	}
}
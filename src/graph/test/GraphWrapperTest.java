package graph.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import graph.*;

public final class GraphWrapperTest {
	GraphWrapper zero, one, two, three;
	
	@Before
	public void SetUp() { // tests GraphWrapper constructor
		zero = new GraphWrapper();
		one = new GraphWrapper();
		two = new GraphWrapper();
		three = new GraphWrapper();
	}
	
	@Test
	public void testEmptyGraph() {
		assertEquals(zero.listNodes().hasNext(),false);
		assertEquals(zero.listChildren("1"),null);
	}
	
	@Test
	public void testAddingAndListingNodes() {
		GraphWrapper five = new GraphWrapper();
		Iterator<String> iter = two.listNodes();
		ArrayList<String> strings = new ArrayList<String>(Arrays.asList("5","1","3","2","4"));
		assertEquals(iter.hasNext(),false);
		for (int i = 0; i < strings.size(); i++) {
			five.addNode(strings.get(i));
		}
		iter = five.listNodes();
		assertEquals(iter.hasNext(),true);
		assertEquals(iter.next(),"1");
		assertEquals(iter.hasNext(),true);
		assertEquals(iter.next(),"2");
		assertEquals(iter.hasNext(),true);
		assertEquals(iter.next(),"3");
		assertEquals(iter.hasNext(),true);
		assertEquals(iter.next(),"4");
		assertEquals(iter.hasNext(),true);
		assertEquals(iter.next(),"5");
		assertEquals(iter.hasNext(),false);
	}
	
	@Test
	public void testAddingAndListingChildren() {
		two.addNode("1");
		two.addNode("2");
		two.addNode("3");
		two.addEdge("1", "3", "1");
		two.addEdge("1", "1", "6");
		two.addEdge("1", "2", "1");
		two.addEdge("1", "3", "3");
		two.addEdge("1", "2", "2");
		two.addEdge("1", "2", "3");
		Iterator<String> iter = two.listChildren("1");
		assertEquals(two.listChildren("4"),null);
		assertEquals(two.listChildren("3").hasNext(),false);
		assertEquals(iter.hasNext(),true);
		assertEquals(iter.next(),"1(6)");
		assertEquals(iter.next(),"2(1)");
		assertEquals(iter.next(),"2(2)");
		assertEquals(iter.next(),"2(3)");
		assertEquals(iter.next(),"3(1)");
		assertEquals(iter.next(),"3(3)");
		assertEquals(iter.hasNext(),false);
		assertEquals(iter.hasNext(),false);
	}
}
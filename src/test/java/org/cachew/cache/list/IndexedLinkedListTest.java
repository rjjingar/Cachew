package org.cachew.cache.list;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class IndexedLinkedListTest {

    @Test
    public void testAddAndSize() {
        IndexedLinkedList<Integer> list = new IndexedLinkedList<>();
        list.addFirst(3);
        list.addFirst(2);
        list.addFirst(1);
        Assert.assertEquals(3, list.size());
        Assert.assertEquals(1, list.getFirst().intValue());
    }

    @Test
    public void testAddFirstAndLast() {
        IndexedLinkedList<Integer> list = new IndexedLinkedList<>();
        // list : 1, 3, 2, 0
        list.addFirst(3);
        list.addLast(2);
        list.addFirst(1);
        list.addLast(0);
        Assert.assertEquals(4, list.size());
        Assert.assertEquals(1, list.getFirst().intValue());
        Assert.assertEquals(0, list.getLast().intValue());
    }

    @Test
    public void testAddAndRemove() {
        IndexedLinkedList<Integer> list = new IndexedLinkedList<>();
        // list : 1, 3, 2, 0
        list.addFirst(3); // [3]
        list.addLast(2); // [3, 2]
        list.addFirst(1); // [1, 3, 2]
        list.addLast(0); // [1, 3, 2, 0]

        list.removeLast(); // [1, 3, 2]

        Assert.assertEquals(3, list.size());
        Assert.assertEquals(1, list.getFirst().intValue());
        Assert.assertEquals(2, list.getLast().intValue());

        list.removeFirst(); // [3, 2]
        Assert.assertEquals(2, list.size());
        Assert.assertEquals(3, list.getFirst().intValue());
        Assert.assertEquals(2, list.getLast().intValue());
    }

    @Test
    public void testFindByIndex() {
        IndexedLinkedList<Integer> list = new IndexedLinkedList<>(Arrays.asList(10, 20, 30, 40, 50));
        Assert.assertEquals(30, list.findByIndex(30).getElement().intValue());
        Assert.assertEquals(50, list.findByIndex(50).getElement().intValue());
    }

    @Test
    public void testFindByIndexAndRemove() {
        IndexedLinkedList<Integer> list = new IndexedLinkedList<>(Arrays.asList(10, 20, 30, 40, 50));
        Assert.assertEquals(20, list.findByIndex(20).getElement().intValue());
        list.removeNode(new Node<>(20));
        Assert.assertEquals(null, list.findByIndex(20));
    }

    @Test
    public void testMoveToHead() {
        IndexedLinkedList<Integer> list = new IndexedLinkedList<>(Arrays.asList(10, 20, 30, 40, 50));
        Assert.assertEquals(10, list.getFirst().intValue());
        list.moveNodeToHead(20);
        Assert.assertEquals(20, list.getFirst().intValue());
    }
}

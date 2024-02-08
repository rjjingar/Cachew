package org.cachew.cache.list;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class IndexedLinkedListTest {

    @Test
    public void testAddAndSize() {
        IndexedLinkedList<Integer> list = new IndexedLinkedList<>();
        list.addFirst(3);
        list.addFirst(2);
        list.addFirst(1);
        Assertions.assertEquals(3, list.size());
        Assertions.assertEquals(1, list.getFirst().intValue());
    }

    @Test
    public void testAddFirstAndLast() {
        IndexedLinkedList<Integer> list = new IndexedLinkedList<>();
        // list : 1, 3, 2, 0
        list.addFirst(3);
        list.addLast(2);
        list.addFirst(1);
        list.addLast(0);
        Assertions.assertEquals(4, list.size());
        Assertions.assertEquals(1, list.getFirst().intValue());
        Assertions.assertEquals(0, list.getLast().intValue());
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

        Assertions.assertEquals(3, list.size());
        Assertions.assertEquals(1, list.getFirst().intValue());
        Assertions.assertEquals(2, list.getLast().intValue());

        list.removeFirst(); // [3, 2]
        Assertions.assertEquals(2, list.size());
        Assertions.assertEquals(3, list.getFirst().intValue());
        Assertions.assertEquals(2, list.getLast().intValue());
    }

    @Test
    public void testFindByIndex() {
        IndexedLinkedList<Integer> list = new IndexedLinkedList<>(Arrays.asList(10, 20, 30, 40, 50));
        Assertions.assertEquals(30, list.findByIndex(30).getElement().intValue());
        Assertions.assertEquals(50, list.findByIndex(50).getElement().intValue());
    }

    @Test
    public void testFindByIndexAndRemove() {
        IndexedLinkedList<Integer> list = new IndexedLinkedList<>(Arrays.asList(10, 20, 30, 40, 50));
        Assertions.assertEquals(20, list.findByIndex(20).getElement().intValue());
        list.removeNode(new Node<>(20));
        Assertions.assertEquals(null, list.findByIndex(20));
    }

    @Test
    public void testMoveToHead() {
        IndexedLinkedList<Integer> list = new IndexedLinkedList<>(Arrays.asList(10, 20, 30, 40, 50));
        Assertions.assertEquals(10, list.getFirst().intValue());
        list.moveNodeToHead(20);
        Assertions.assertEquals(20, list.getFirst().intValue());
    }
}

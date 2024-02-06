package org.cachew.cache.list;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread safe linked list implementation with O(1) access to internal nodes.
 * It does not allow duplicate entries to exist.
 * @param <E> Data type stored in linked list
 */
public class IndexedLinkedList<E> {
    protected ConcurrentHashMap<E, Node<E>> nodeMap;

    protected Node<E> head;
    protected Node<E> tail;

    public IndexedLinkedList() {
        this.nodeMap = new ConcurrentHashMap<>();
    }

    public IndexedLinkedList(List<E> other) {
        this.nodeMap = new ConcurrentHashMap<>();
        for (E next : other) {
            addLast(next);
        }
    }

    /** Basic access pattern for linked list */
    public Node<E> addFirst(E element) {
        Node<E> node = findByIndex(element);
        if (node != null && node == head) {
            // existing node and is already at head
            return node;
        }

        if (node != null) {
            // existing node
            removeFromList(node);
        } else {
            // it is a new node
            node = new Node<>(element);
            addToMap(node);
        }

        node.setNext(head);
        node.setPrev(null);
        head = node;
        if (tail == null) {
            tail = node;
        }
        return node;
    }

    public Node<E> addLast(E element) {
        Node<E> node = findByIndex(element);
        if (node != null && node == tail) {
            // existing node and is already at tail
            return node;
        }
        if (node != null) {
            // existing node
            removeFromList(node);
        } else {
            // it is a new node
            node = new Node<>(element);
            addToMap(node);
        }

        node.setNext(null);
        node.setPrev(tail);
        tail = node;
        if (head == null) {
            head = node;
        }
        return node;
    }

    public Node<E> removeFirst() {
        if (head == null) {
            return null;
        }
        Node<E> node = head;
        removeNode(node);
        return node;
    }

    public Node<E> removeLast() {
        if (tail == null) {
            return null;
        }
        Node<E> node = tail;
        removeNode(node);
        return node;
    }

    public Node<E> findByIndex(final E key) {
        if (nodeMap.containsKey(key)) {
            Node<E> curr = nodeMap.get(key);
            curr.setElement(key);
            return curr;
        }
        return null;
    }
    public Node<E> moveNodeToHead(E element) {
        Node<E> node = findByIndex(element);
        if (node == null) {
            // New node
            node = new Node<>(element);
            addToMap(node);

            node.setNext(head);
            if (head != null) {
                head.setPrev(node);
                head = node;
            } else {
                head = node;
                tail = node;
            }
            return node;
        }
        // existing node
        if (node == head) {
            // if already at head
            return node;
        }
        if (node == tail) {
            // if last node and tail != head
            tail = node.getPrev();
            node.setPrev(null);
            node.setNext(head);
            head.setPrev(node);
            head = node;
            return node;
        }

        // internal node
        Node<E> prevNode = node.getPrev();
        Node<E> nextNode = node.getNext();
        if (prevNode != null) {
            prevNode.setNext(nextNode);
        }
        if (nextNode != null) {
            nextNode.setPrev(prevNode);
        }


        node.setNext(head);
        node.setPrev(null);
        head.setPrev(node);
        head = node;
        return node;
    }

    public E evictNode() {
        if (tail == null) {
            return null;
        }
        Node<E> node = tail;
        removeNode(node);
        return node.getElement();
    }

    public int size() {
        return nodeMap.size();
    }

    public E getFirst() {
        if (head != null) {
            return head.getElement();
        }
        return null;
    }

    public E getLast() {
        if (tail != null) {
            return tail.getElement();
        }
        return null;
    }

    public void removeNode(Node<E> node) {
        removeFromList(node);
        removeFromMap(node);
    }

    private void addToMap(final Node<E> node) {
        nodeMap.remove(node.getElement());
        nodeMap.put(node.getElement(), node);
    }

    private void removeFromMap(final Node<E> node) {
        nodeMap.remove(node.getElement());
    }

    private void removeFromList(final Node<E> node) {
        if (node == tail) {
            tail = node.getPrev();
            if (tail != null) {
                tail.setNext(null);
            }
        }
        if (node == head) {
            head = node.getNext();
            head.setPrev(null);
        }
        node.setPrev(null);
        node.setNext(null);
    }
}

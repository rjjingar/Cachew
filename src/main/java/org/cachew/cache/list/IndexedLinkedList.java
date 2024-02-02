package org.cachew.cache.list;

import java.util.concurrent.ConcurrentHashMap;

public class IndexedLinkedList<E> {
    private ConcurrentHashMap<E, Node<E>> nodeMap;

    private Node<E> head;
    private Node<E> tail;

    public IndexedLinkedList() {
        this.nodeMap = new ConcurrentHashMap<>();
    }

    public Node<E> findByIndex(final E key) {
        if (nodeMap.containsKey(key)) {
            nodeMap.get(key);
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

    public Node<E> addFirst(E element) {
        Node<E> node = new Node<>(element);
        node.setNext(head);
        node.setPrev(null);
        head = node;
        if (tail == null) {
            tail = node;
        }
        addToMap(node);
        return node;
    }

    public Node<E> addLast(E element) {
        Node<E> node = new Node<>(element);
        node.setNext(null);
        node.setPrev(tail);
        tail = node;
        if (head == null) {
            head = node;
        }
        addToMap(node);
        return node;
    }

    public Node<E> removeFirst() {
        if (head == null) {
            return null;
        }
        Node<E> node = head;
        head = node.getNext();
        removeNode(node);
        return node;
    }

    public Node<E> removeLast() {
        if (tail == null) {
            return null;
        }
        Node<E> node = tail;
        tail = node.getPrev();
        tail.setNext(null);
        removeNode(node);
        return node;
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

    private void removeNode(Node<E> node) {
        node.setPrev(null);
        node.setNext(null);
        removeFromMap(node);
    }

    private void addToMap(final Node<E> node) {
        nodeMap.remove(node.getElement());
        nodeMap.put(node.getElement(), node);
    }

    private void removeFromMap(final Node<E> node) {
        nodeMap.remove(node.getElement());
    }



}

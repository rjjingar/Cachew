package org.cachew.cache.list;

import lombok.Data;

@Data
public class Node<E> {
    private E element;
    private Node<E> next;
    private Node<E> prev;

    public Node(E element) {
        this.element = element;
    }

}

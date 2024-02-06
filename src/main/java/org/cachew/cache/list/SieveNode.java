package org.cachew.cache.list;

public class SieveNode<E> extends Node<E> {

    private boolean visited;
    public SieveNode(E element) {
        super(element);
        this.visited = true;
    }
}

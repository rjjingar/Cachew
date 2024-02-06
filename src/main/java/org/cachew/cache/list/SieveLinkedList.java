package org.cachew.cache.list;

public class SieveLinkedList<E> extends IndexedLinkedList<E> {
    private Node<E> hand;

    public SieveLinkedList() {
        super();
    }

    @Override
    public E evictNode() {
        Node<E> other = hand;
        if (other == null) {
            other = super.tail;
        }
        while (other.isVisited()) {
            other.setVisited(false);
            other = other.getPrev();
            if (other == null) {
                other = super.tail;
            }
        }
        hand = other.getPrev();
        super.removeNode(other);
        return other.getElement();
    }
}

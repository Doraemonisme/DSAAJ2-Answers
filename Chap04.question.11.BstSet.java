//Chap04.question.11.BstSet.java

import java.util.*;
import java.util.Collection;

/**
 * A TreeSet implementation based on Binary Search Tree.
 * <p/>
 * null is not allowed.
 *
 * @param <E>
 */
public class BstSet<E extends Comparable<? super E>> implements Set<E> {
    private int size;
    private BstNode root;

    public BstSet() {
        size = 0;
        root = null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return contains0(root, o);
    }

    @SuppressWarnings("unchecked")
    private boolean contains0(BstNode node, Object o) {
        if (o == null || node == null) return false;
        try {
            int compareResult = ((E) o).compareTo(node.data);
            if (compareResult < 0)
                return contains0(node.left, o);
            else return compareResult <= 0 || contains0(node.right, o);
        } catch (ClassCastException cce) {
            return false;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int cur = 0;
            BstNode node = root;
            BstNode prev = null;

            {
                if (node != null)
                    while (node.left != null)
                        node = node.left;
            }

            @Override
            public boolean hasNext() {
                return cur < size;
            }

            @Override
            public E next() {
                if(!hasNext())
                    throw new NoSuchElementException();
                prev=node;
                if(node.parent.left==node) {
                    node=node.parent;
                    if(node.right!=null)
                        node=node.right;
                    while (node.left!=null)
                        node=node.left;
                } else {
                    node=node.parent;
                }
                cur++;
                return node.data;
            }

            @Override
            public void remove() {
                BstSet.this.remove(prev.data);
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] oa = new Object[size];
        Iterator<E> iterator = iterator();
        for (int i = 0; i < size; i++) {
            oa[i] = iterator.next();
        }
        return oa;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        T[] r = a.length > size ? a :
                (T[]) java.lang.reflect.Array.newInstance(
                        a.getClass().getComponentType(), size);
        Iterator<E> iterator = iterator();
        for (int i = 0; i < size; i++) {
            E e = iterator.next();
            r[i] = (T) e;
        }
        return r;
    }

    @Override
    public boolean add(E e) {
        if (e == null)
            return false;
        int oldSize = size;
        root = add0(root, e);
        return size > oldSize;
    }

    private BstNode add0(BstNode node, E e) {
        assert e != null;
        if (node == null) {
            size++;
            return new BstNode(e, null, null, null);
        }

        int compareResult = e.compareTo(node.data);

        if (compareResult < 0) {
            node.left = add0(node.left, e);
            if (node.left != null)
                node.left.parent = node;
        } else if (compareResult > 0) {
            node.right = add0(node.right, e);
            if (node.right != null)
                node.right.parent = node;
        }
        return node;
    }

    @Override
    public boolean remove(Object o) {
        int oldSize = size;
        remove0(root, o);
        return size < oldSize;
    }

    @SuppressWarnings("unchecked")
    private BstNode remove0(BstNode node, Object o) {
        if (o == null || node == null) return node;

        try {
            int compareResult = ((E) o).compareTo(node.data);
            if (compareResult < 0) {
                node.left = remove0(node.left, o);
                if (node.left != null)
                    node.left.parent = node;
            } else if (compareResult > 0) {
                node.right = remove0(node.right, o);
                if (node.right != null)
                    node.right.parent = node;
            } else {
                //remove node
                size--;
                if (node.left == null && node.right == null) {
                    node = null;
                } else if (node.left == null) {
                    node.right.parent = node.parent;
                    node = node.right;
                } else if (node.right == null) {
                    node.left.parent = node.parent;
                    node = node.left;
                } else {
                    BstNode r = node.left;
                    while (r.right != null)
                        r = r.right;
                    node.left.parent = node.parent;
                    node.right.parent = r;
                    r.right = node.right;
                    node = node.left;
                }
            }
        } catch (ClassCastException cce) {
            return node;
        }
        return node;
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        if (c == null) return false;
        for (Object o : c) {
            if (!contains(o))
                return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) return false;
        int oldSize = size;
        for (Object o : c) {
            add((E) o);
        }
        return size > oldSize;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) return false;
        Iterator<E> iterator = iterator();
        for (int i = 0; i < size; i++) {
            E e = iterator.next();
            if (!c.contains(e))
                remove(e);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) return false;
        int oldSize = size;
        for (Object o : c) {
            remove(o);
        }
        return size < oldSize;
    }

    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    private class BstNode {
        E data;
        BstNode parent, left, right;

        BstNode(E d, BstNode p, BstNode l, BstNode r) {
            data = d;
            parent = p;
            left = l;
            right = r;
        }
    }
}

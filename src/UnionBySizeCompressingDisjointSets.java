package disjointsets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;

    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */

    private Map<T, Integer> dict;


    public UnionBySizeCompressingDisjointSets() {
        pointers = new ArrayList<Integer>();
        dict = new HashMap<T, Integer>();

    }

    @Override
    public void makeSet(T item) {
        if (item == null || this.dict.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        this.dict.put(item, this.pointers.size());
        this.pointers.add(-1);
    }

    @Override
    public int findSet(T item) {
        if (item == null || !this.dict.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        int rootNode = dict.get(item);
        while (pointers.get(rootNode) >= 0) {
            rootNode = pointers.get(rootNode);
        }

        int changePointers = dict.get(item);
        while (pointers.get(changePointers) >= 0) {
            int temp = pointers.get(changePointers);
            pointers.set(changePointers, rootNode);
            changePointers = temp;
        }
        return rootNode;
    }
    @Override
    public boolean union(T item1, T item2) {
        if (item1 == null || item2 == null || !dict.containsKey(item1) || !dict.containsKey(item2)) {
            throw new IllegalArgumentException();
        }

        int itemOneRoot = findSet(item1);
        int itemTwoRoot = findSet(item2);

        if (!pointers.get(itemOneRoot).equals(-1)  && (itemOneRoot == itemTwoRoot)) {
            return false;
        }

        if (pointers.get(itemOneRoot) > pointers.get(itemTwoRoot)) {
            this.pointers.set(itemTwoRoot, pointers.get(itemTwoRoot) + pointers.get(itemOneRoot));
            this.pointers.set(itemOneRoot, itemTwoRoot);

        } else {
            this.pointers.set(itemOneRoot, pointers.get(itemOneRoot) + pointers.get(itemTwoRoot));
            this.pointers.set(itemTwoRoot, itemOneRoot);
        }
        return true;
    }
}

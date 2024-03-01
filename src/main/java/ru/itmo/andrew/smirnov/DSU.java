package ru.itmo.andrew.smirnov;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DSU {
    private final List<Integer> p = new ArrayList<>();
    private final List<Integer> r = new ArrayList<>();

    public void makeSet(int x) {
        while (x >= p.size()) {
            p.add(0);
            r.add(0);
        }
        p.set(x, x);
    }

    public int find(int x) {
        if (p.get(x) == x) {
            return x;
        }
        p.set(x, find(p.get(x)));
        return p.get(x);
    }

    public void union(int x, int y) {
        x = find(x);
        y = find(y);

        if (r.get(x) < r.get(y)) {
            p.set(x, y);
        } else {
            p.set(y, x);
            if (Objects.equals(r.get(x), r.get(y))) {
                r.set(x, r.get(x) + 1);
            }
        }
    }
}

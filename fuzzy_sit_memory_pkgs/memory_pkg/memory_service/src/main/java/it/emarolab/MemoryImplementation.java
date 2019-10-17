package it.emarolab;

import it.emarolab.fuzzySIT.memoryLike.MemoryInterface;
import it.emarolab.fuzzySIT.semantic.hierarchy.SceneHierarchyVertex;

import java.util.Set;

public class MemoryImplementation extends MemoryInterface {

    @Override
    protected SceneHierarchyVertex store(String s) {
        return null;
    }

    @Override
    public SceneHierarchyVertex retrieve() {
        return null;
    }

    @Override
    protected void consolidate() {

    }

    @Override
    public Set<SceneHierarchyVertex> forget() {
        return null;
    }
}

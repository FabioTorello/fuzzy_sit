package it.emarolab.fuzzySIT.perception;

import it.emarolab.fuzzySIT.semantic.axioms.SpatialObject;

public class FeaturedSpatialObject<F> extends SpatialObject {

    private F feature;

    public FeaturedSpatialObject(FeaturedSpatialObject<F> copy) {
        super(copy);
        this.feature = copy.getFeature();
    }

    public FeaturedSpatialObject(String type, String object, double degree, F feature) {
        super(type, object, degree);
        this.feature = feature;
    }
    public FeaturedSpatialObject(String type, String object, F feature) {
        super(type, object);
        this.feature = feature;
    }

    public F getFeature() {
        return feature;
    }

    @Override
    public String toString() {
        return super.toString() + "⟨" + feature + "⟩";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FeaturedSpatialObject)) return false;
        if (!super.equals(o)) return false;

        FeaturedSpatialObject<?> that = (FeaturedSpatialObject<?>) o;

        return getFeature() != null ? getFeature().equals(that.getFeature()) : that.getFeature() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getFeature() != null ? getFeature().hashCode() : 0);
        return result;
    }
}
